package gl8080.http;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ContentTypeTest {

    @Test
    public void html() {
        // exercise
        ContentType contentType = ContentType.toContentType("html");
        
        // verify
        assertThat(contentType, is(ContentType.TEXT_HTML));
    }

    @Test
    public void htm() {
        // exercise
        ContentType contentType = ContentType.toContentType("htm");
        
        // verify
        assertThat(contentType, is(ContentType.TEXT_HTML));
    }

    @Test
    public void css() {
        // exercise
        ContentType contentType = ContentType.toContentType("css");
        
        // verify
        assertThat(contentType, is(ContentType.TEXT_CSS));
    }

    @Test
    public void jpeg() {
        // exercise
        ContentType contentType = ContentType.toContentType("jpeg");
        
        // verify
        assertThat(contentType, is(ContentType.IMAGE_JPEG));
    }

}
