package gl8080.http;

import static gl8080.http.Constant.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class HttpResponseTest {
    
    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();
    
    @Test
    public void file_html() throws Exception {
        // setup
        File tmpFile = new File(tmpDir.getRoot(), "test.html");
        Files.write(tmpFile.toPath(), "hello!!".getBytes("UTF-8"), StandardOpenOption.CREATE);

        HttpResponse response = new HttpResponse(Status.OK);
        response.setBody(tmpFile);
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // exercise
        response.writeTo(out);
        
        // verify
        String actual = out.toString("UTF-8");
        
        assertThat(actual, is(
            "HTTP/1.1 200 OK" + CRLF +
            "Content-Type: text/html" + CRLF +
            CRLF +
            "hello!!"
        ));
    }

    @Test
    public void basic() throws Exception {
        // setup
        HttpResponse response = new HttpResponse(Status.OK);
        response.addHeader("Content-Type", ContentType.TEXT_HTML);
        response.setBody("<h1>Hello World!!</h1>");
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        // exercise
        response.writeTo(out);
        
        // verify
        String actual = out.toString("UTF-8");
        assertThat(actual, is(
            "HTTP/1.1 200 OK" + CRLF +
            "Content-Type: text/html" + CRLF +
            CRLF +
            "<h1>Hello World!!</h1>"
        ));
    }

}
