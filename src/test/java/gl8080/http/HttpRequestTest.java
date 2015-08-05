package gl8080.http;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class HttpRequestTest {
    
    private static final String CRLF = HttpRequest.CRLF;
    
    @Test
    public void chunk() throws Exception {
        // setup
        String header =
            "POST /chunk.txt HTTP/1.1" + CRLF
          + "User-Agent: curl/7.37.1" + CRLF
          + "Host: localhost" + CRLF
          + "Accept: */*" + CRLF
          + "Transfer-Encoding: chunked" + CRLF
          + "Expect: 100-continue" + CRLF;
        
        String body =
            "5" + CRLF
          + "start" + CRLF
          + "c" + CRLF
          + "123456789" + CRLF
          + "0" + CRLF
          + "3" + CRLF
          + "end" + CRLF
          + "0" + CRLF
          + CRLF;

        String httpRequestMessage = header + CRLF + body;

        InputStream in = new ByteArrayInputStream(httpRequestMessage.getBytes());
        HttpRequest request = new HttpRequest(in);

        // exercise
        String headerText = request.getHeaderText();
        String bodyText = request.getBodyText();
        
        // verify
        assertThat(headerText, is(header));
        assertThat(bodyText, is("start123456789" + CRLF + "0end"));
    }
    
    @Test
    public void contentLength() {
        // setup
        String header = 
            "POST / HTTP/1.1" + CRLF
          + "User-Agent: curl/7.37.1" + CRLF
          + "Host: localhost" + CRLF
          + "Accept: */*" + CRLF
          + "Content-Length: 14" + CRLF
          + "Content-Type: application/x-www-form-urlencoded" + CRLF;
        
        String body = "Message Body!!";
        
        String httpRequestMessage = header + CRLF + body;
        
        InputStream in = new ByteArrayInputStream(httpRequestMessage.getBytes());
        HttpRequest request = new HttpRequest(in);
        
        // exercise
        String headerText = request.getHeaderText();
        String bodyText = request.getBodyText();
        
        // verify
        assertThat(headerText, is(header));
        assertThat(bodyText, is(body));
    }
    
    @Test
    public void contentLength_日本語() {
        // setup
        String header = 
            "POST / HTTP/1.1" + CRLF
          + "User-Agent: curl/7.37.1" + CRLF
          + "Host: localhost" + CRLF
          + "Accept: */*" + CRLF
          + "Content-Length: 15" + CRLF
          + "Content-Type: application/x-www-form-urlencoded" + CRLF;
        
        String body = "あいうえお";
        
        String httpRequestMessage = header + CRLF + body;
        
        InputStream in = new ByteArrayInputStream(httpRequestMessage.getBytes());
        HttpRequest request = new HttpRequest(in);
        
        // exercise
        String headerText = request.getHeaderText();
        String bodyText = request.getBodyText();
        
        // verify
        assertThat(headerText, is(header));
        assertThat(bodyText, is(body));
    }
}
