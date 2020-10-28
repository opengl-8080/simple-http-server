package gl8080.http;

import static gl8080.http.Constant.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class IOUtil {
    
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    
    public static void println(OutputStream out, String line) {
        print(out, line + CRLF);
    }
    
    public static void print(OutputStream out, String line) {
        try {
            out.write(line.getBytes(UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String readLine(InputStream in) throws IOException {
        List<Byte> list = new ArrayList<>();
        
        while (true) {
            int c = in.read();
            
            if (c == -1) {
                throw new EmptyRequestException();
            }
            
            list.add((byte)c);
            
            int size = list.size();
            if (2 <= size) {
                byte cr = list.get(size - 2).byteValue();
                byte lf = list.get(size - 1).byteValue();
                
                if (cr == '\r' && lf == '\n') {
                    break;
                }
            }
        }

        // copy to byte array except tailing CRLF
        byte[] buffer = new byte[list.size() - 2]; 
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = list.get(i);
        }
        
        return new String(buffer, UTF_8);
    }
    
    public static InputStream toInputStream(String string) {
        return new ByteArrayInputStream(string.getBytes(UTF_8));
    }
    
    public static String toString(byte[] buffer) {
        return new String(buffer, UTF_8);
    }
    
    private IOUtil() {}
}
