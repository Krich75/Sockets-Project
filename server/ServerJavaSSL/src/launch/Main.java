package launch;

import java.net.ServerSocket;
import java.net.Socket;

import server.MultiThreadServer;

public class Main {
	

	public static void main(String args[]) throws Exception {
		
		int numberClient =0;
		
		ServerSocket serverSocket = new ServerSocket(CONFIG.portServerSSL);
		
		System.out.println("Listening on port :"+ CONFIG.portServerSSL);
		
		while (true) {
			numberClient++;
			Socket socket = serverSocket.accept();
			System.out.println("New client connected : client"+numberClient);
			new Thread(new MultiThreadServer(socket)).start();
		}
	}
}
