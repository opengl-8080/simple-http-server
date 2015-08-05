package gl8080.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HttpRequest {
    
    public static final String CRLF = "\r\n";
    
    private final String headerText;
    private final String bodyText;
    
    public HttpRequest(InputStream input) {
        try {
            this.headerText = this.readHeader(input);
            this.bodyText = this.readBody(input);
            
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    private String readHeader(InputStream in) throws IOException {
        String line = this.readLine(in);
        StringBuilder header = new StringBuilder();
        
        while (line != null && !line.isEmpty()) {
            header.append(line + CRLF);
            line = this.readLine(in);
        }
        
        return header.toString();
    }
    
    private String readBody(InputStream in) throws IOException {
        if (this.isChunkedTransfer()) {
            return this.readBodyByChunkedTransfer(in);
        } else {
            return this.readBodyByContentLength(in);
        }
    }
    
    private boolean isChunkedTransfer() {
        return Stream.of(this.headerText.split(CRLF))
                     .filter(headerLine -> headerLine.startsWith("Transfer-Encoding"))
                     .map(transferEncoding -> transferEncoding.split(":")[1].trim())
                     .anyMatch(s -> "chunked".equals(s));
    }
    
    private String readBodyByChunkedTransfer(InputStream in) throws IOException {
        StringBuilder body = new StringBuilder();
        
        int chunkSize = Integer.parseInt(this.readLine(in), 16);
        
        while (chunkSize != 0) {
            byte[] buffer = new byte[chunkSize];
            in.read(buffer);
            
            body.append(new String(buffer, "UTF-8"));
            
            this.readLine(in); // chunk-body の末尾にある CRLF を読み飛ばす
            chunkSize = Integer.parseInt(this.readLine(in), 16);
        }
        
        return body.toString();
    }
    
    private String readLine(InputStream in) throws IOException {
        List<Byte> list = new ArrayList<>();
        
        while (true) {
            byte b = (byte)in.read();
            
            list.add(b);
            
            int size = list.size();
            if (2 <= size) {
                char cr = (char)list.get(size - 2).byteValue();
                char lf = (char)list.get(size - 1).byteValue();
                
                if (cr == '\r' && lf == '\n') {
                    break;
                }
            }
        }
        
        byte[] buffer = new byte[list.size() - 2]; // CRLF の分減らす
        for (int i = 0; i < list.size() - 2; i++) {
            buffer[i] = list.get(i);
        }
        
        return new String(buffer, "UTF-8");
    }
    
    private String readBodyByContentLength(InputStream in) throws IOException {
        final int contentLength = this.getContentLength();
        
        if (contentLength <= 0) {
            return null;
        }
        
        byte[] buffer = new byte[contentLength];
        in.read(buffer);
        
        return new String(buffer, "UTF-8");
    }
    
    private int getContentLength() {
        return Stream.of(this.headerText.split(CRLF))
                     .filter(headerLine -> headerLine.startsWith("Content-Length"))
                     .map(contentLengthHeader -> contentLengthHeader.split(":")[1].trim())
                     .mapToInt(Integer::parseInt)
                     .findFirst().orElse(0);
    }
    
    public String getHeaderText() {
        return this.headerText;
    }

    public String getBodyText() {
        return this.bodyText;
    }
    
}
