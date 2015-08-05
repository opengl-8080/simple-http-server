package gl8080.http;

import static gl8080.http.Constant.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    
    public static void main(String[] args) throws Exception {
        System.out.println("start >>>");
        
        try (
            ServerSocket server = new ServerSocket(80);
            Socket socket = server.accept();
            InputStream in = socket.getInputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ) {
            
            HttpRequest request = new HttpRequest(in);
            
            System.out.println(request.getHeaderText());
            System.out.println(request.getBodyText());
            
            bw.write("HTTP/1.1 200 OK" + CRLF);
        }
        
        System.out.println("<<< end");
    }
    
    public static void hoge(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        
        String line = in.readLine();
        StringBuilder header = new StringBuilder();
        
        while (line != null) {
            header.append(line + "\n");
            System.out.println(line);
            line = in.readLine();
        }
        
        System.out.println(header);
    }
}
