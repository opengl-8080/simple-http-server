package gl8080.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    private int port;
    private ExecutorService service = Executors.newCachedThreadPool();

    public SimpleHttpServer(int port) {
	this.port = port;
    }
    
    public void start() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                this.serverProcess(server);
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
    private void serverProcess(ServerSocket server) throws IOException {
        Socket socket = server.accept();
        
        this.service.execute(() -> {
            try (
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                ) {
                
                HttpRequest request = new HttpRequest(in);
                
                HttpHeader header = request.getHeader();
                
                if (header.isGetMethod()) {
                    File file = new File(".", header.getPath());
                    
                    if (file.exists() && file.isFile()) {
                        this.respondLocalFile(file, out);
                    } else {
                        this.respondNotFoundError(out);
                    }
                } else {
                    this.respondOk(out);
                }
            } catch (EmptyRequestException e) {
                // ignore
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void respondNotFoundError(OutputStream out) throws IOException {
        HttpResponse response = new HttpResponse(Status.NOT_FOUND);
        response.addHeader("Content-Type", ContentType.TEXT_PLAIN);
        response.setBody("404 Not Found");
        response.writeTo(out);
    }
    
    private void respondLocalFile(File file, OutputStream out) throws IOException {
        HttpResponse response = new HttpResponse(Status.OK);
        response.setBody(file);
        response.writeTo(out);
    }

    private void respondOk(OutputStream out) throws IOException {
        HttpResponse response = new HttpResponse(Status.OK);
        response.writeTo(out);
    }
}
