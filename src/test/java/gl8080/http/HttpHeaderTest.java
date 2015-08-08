package gl8080.http;

import static gl8080.http.Constant.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;

public class HttpHeaderTest {
    
    @Test
    public void method_get() throws Exception {
        // setup
        String headerText = "GET /foo/bar HTTP/1.1" + CRLF
                          + CRLF;
        
        // exercise
        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));
        
        // verify
        assertThat(header.isGetMethod(), is(true));
    }
    
    @Test
    public void path() throws Exception {
        // setup
        String headerText = "GET /foo/bar HTTP/1.1" + CRLF
                          + CRLF;
        
        // exercise
        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));
        
        // verify
        assertThat(header.getPath(), is("/foo/bar"));
    }
    
    @Test
    public void text() throws Exception {
        // setup
        String headerText =
                "POST /chunk.txt HTTP/1.1" + CRLF
              + "User-Agent: curl/7.37.1" + CRLF
              + "Host: localhost" + CRLF
              + "Accept: */*" + CRLF
              + "Transfer-Encoding: chunked" + CRLF
              + "Expect: 100-continue" + CRLF
              + CRLF;
        
        // exercise
        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));
        
        // verify
        assertThat(header.getText() + CRLF, is(headerText));
    }
    

    @Test
    public void contentLength() throws Exception {
        // setup
        String headerText = 
                "POST / HTTP/1.1" + CRLF
              + "User-Agent: curl/7.37.1" + CRLF
              + "Host: localhost" + CRLF
              + "Accept: */*" + CRLF
              + "Content-Length: 14" + CRLF
              + "Content-Type: application/x-www-form-urlencoded" + CRLF
              + CRLF;
        
        // exercise
        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));
        
        // verify
        assertThat(header.getContentLength(), is(14));
    }


    @Test
    public void chunked() throws Exception {
        // setup
        String headerText =
                "POST /chunk.txt HTTP/1.1" + CRLF
              + "User-Agent: curl/7.37.1" + CRLF
              + "Host: localhost" + CRLF
              + "Accept: */*" + CRLF
              + "Transfer-Encoding: chunked" + CRLF
              + "Expect: 100-continue" + CRLF
              + CRLF;
        
        // exercise
        HttpHeader header = new HttpHeader(IOUtil.toInputStream(headerText));
        
        // verify
        assertThat(header.isChunkedTransfer(), is(true));
    }

}
