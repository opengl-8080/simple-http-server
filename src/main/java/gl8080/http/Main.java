package gl8080.http;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    
    public static void main(String[] args) throws Exception {
        System.out.println("start >>>");
        
        try (
            ServerSocket server = new ServerSocket(80);
            Socket socket = server.accept();
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            ) {
            
            HttpRequest request = new HttpRequest(in);

            HttpResponse response = new HttpResponse(Status.OK);
            
            HttpHeader header = request.getHeader();
            
            if (header.isGetMethod()) {
                // ★GET メソッドの場合は、パスで指定されたファイルをローカルから取得
                response.setBody(new File(".", header.getPath()));
            }
            
            response.writeTo(out);
        }
        
        System.out.println("<<< end");
    }
}