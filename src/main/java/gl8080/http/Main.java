package gl8080.http;

public class Main {
    
    public static void main(String[] args) throws Exception {
        System.out.println("start >>>");
        
        SimpleHttpServer server = new SimpleHttpServer();
        server.start();
    }
}