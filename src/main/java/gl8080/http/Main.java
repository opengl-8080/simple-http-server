package gl8080.http;

public class Main {
    
    public static void main(String[] args) throws Exception {
        System.out.println("start >>>");

	int port = 80;
	if (args.length == 1) {
	    port = Integer.valueOf(args[0]);
	}
        
        SimpleHttpServer server = new SimpleHttpServer(port);
        server.start();
    }
}
