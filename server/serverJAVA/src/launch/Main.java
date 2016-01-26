package launch;

import java.net.ServerSocket;
import java.net.Socket;

import server.MultiThreadServer;

public class Main {
	
	public static final int portServerJava = 7777;

	public static void main(String args[]) throws Exception {
		
		int numberClient =0;
		
		ServerSocket serverSocket = new ServerSocket(6666);
		
		System.out.println("Listening on port :"+ portServerJava);
		
		while (true) {
			numberClient++;
			Socket socket = serverSocket.accept();
			System.out.println("New client connected : client"+numberClient);
			new Thread(new MultiThreadServer(socket)).start();
		}
	}
}
