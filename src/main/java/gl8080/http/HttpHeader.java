package gl8080.http;

import static gl8080.http.Constant.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpHeader {
    
    private final String headerText;
    private Map<String, String> messageHeaders = new HashMap<>();
    
    public HttpHeader(InputStream in) throws IOException {
        StringBuilder header = new StringBuilder();

        header.append(this.readRequestLine(in))
              .append(this.readMessageLine(in));
        
        this.headerText =  header.toString();
    }
    
    private String readRequestLine(InputStream in) throws IOException {
        return IOUtil.readLine(in) + CRLF;
    }
    
    private StringBuilder readMessageLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        String messageLine = IOUtil.readLine(in);
        
        while (messageLine != null && !messageLine.isEmpty()) {
            this.putMessageLine(messageLine);
            
            sb.append(messageLine + CRLF);
            messageLine = IOUtil.readLine(in);
        }
        
        return sb;
    }
    
    private void putMessageLine(String messageLine) {
        String[] tmp = messageLine.split(":");
        String key = tmp[0].trim();
        String value = tmp[1].trim();
        this.messageHeaders.put(key, value);
    }

    public String getText() {
        return this.headerText;
    }
    
    public int getContentLength() {
        return Integer.parseInt(this.messageHeaders.getOrDefault("Content-Length", "0"));
    }

    public boolean isChunkedTransfer() {
        return this.messageHeaders.getOrDefault("Transfer-Encoding", "-").equals("chunked");
    }
}
