package launch;

import handler.LetterCounterHandler;
import impl.SSLServer;
import impl.UnsecuredServer;

public class Main {
	
	public static void main(String args[]) throws Exception {
		
		UnsecuredServer server = new UnsecuredServer(CONFIG.portServer);
		SSLServer sslServer = new SSLServer(CONFIG.portServerSSL);
		
		Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
            	System.out.println("Stopping servers..");
            	server.stopListening();
            	sslServer.stopListening();
            	System.out.print("Waiting for all connections to end..");
                server.awaitTermination();
                sslServer.awaitTermination();
                System.out.println(" done.");
            }
        });
		
		server.startListening(new LetterCounterHandler(), 20);
		sslServer.startListening(new LetterCounterHandler(), 20);
		System.out.println("Unsecured server listening on port :"+ CONFIG.portServerJava);
		System.out.println("Secured server listening on port :"+ CONFIG.portServerSSL);
	}
}
