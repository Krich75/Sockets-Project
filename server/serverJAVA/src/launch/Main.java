package launch;

import handler.CustomSSLServer;
import handler.LetterCounterHandler;
import impl.UnsecuredServer;

public class Main {
	
	public static final int SERVERS_GRACE_TIME = 6000;

	private static int portClear = 7777;
	private static int portSsl = 9999;
	
	public static void printUsage(){
		System.out.println("Usage: java -jar server.jar [-clear port] [-ssl port]");
	}
	
	public static void parseArgs(String[] args){
		try{
			for (int i=0; i<args.length; i+=2){
				if (args[i].equals("-clear")){
					portClear = Integer.parseInt(args[i+1]);
				}
				else if (args[i].equals("-ssl")){
					portSsl = Integer.parseInt(args[i+1]);
				}
				else {
					System.out.println(args[i] + " : Invalid switch");
					printUsage();
					System.exit(0);
				}
			}
		} catch (Exception e){
			printUsage();
			System.exit(0);
		}
		
	}
	
	public static void main(String args[]) throws Exception {
		
		parseArgs(args);
		
		final UnsecuredServer server = new UnsecuredServer(portClear);
		final CustomSSLServer sslServer = new CustomSSLServer(portSsl);
		
		Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
            	System.out.print("Stopping servers.. ");
            	server.stop(SERVERS_GRACE_TIME);
            	sslServer.stop(SERVERS_GRACE_TIME);
                System.out.println(" done.");
            }
        });
		
		server.start(new LetterCounterHandler(), 20);
		sslServer.start(new LetterCounterHandler(), 20);
		
		System.out.println("Unsecured server listening on port :"+ portClear);
		System.out.println("Secured server listening on port :"+ portSsl);
	}
}
