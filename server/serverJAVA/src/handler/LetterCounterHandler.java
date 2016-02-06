package handler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class LetterCounterHandler implements ClientHandler {

	public final static String VOYELLES = "aeiouy";
	public final static String CONSONES = "bcdfghjklmnpqrstvwxz";
	public final static int REQUEST_TIMEOUT = 5000;
	public final static int MAX_REQUEST_SIZE = 1024;
	
	public int isConsone(String s){
		return CONSONES.contains(s) ? 1 : 0;
	}
	
	/**
	 * Method for know if a letter is a voyelle
	 * @param s
	 * @return 1 for yes and 0 for no
	 */
	public int isVoyelle(String s){
		return VOYELLES.contains(s) ? 1 : 0;
	}
	
	/**
	 * Method for count consonnes of a messages
	 * @param message
	 * @return number
	 */
	public int compterConsones(String message){
		int result = 0;
		for (int i=0; i<message.length(); i++){
			result += isConsone(message.substring(i, i+1));
		}
		return result;
	}
	
	/**
	 * Method for count wovel in a message
	 * @param message
	 * @return number
	 */
	public int compterVoyelles(String message){
		int result = 0;
		for (int i=0; i<message.length(); i++){
			result += isVoyelle(message.substring(i, i+1));
		}
		return result;
	}

	@Override
	public void handleClient(Socket socketClient) {
		
		InputStreamReader input;
		BufferedWriter output;
		
		System.out.println("Un client s'est connecté.");
		
		try {
			socketClient.setSoTimeout(REQUEST_TIMEOUT);
			input  = new InputStreamReader(socketClient.getInputStream());
			output = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
			
			try {
				System.out.println("En attente de la requête du client..");
				
				char[] data = new char[MAX_REQUEST_SIZE];
				int n = input.read(data,  0, data.length);
				
				if (n > 0) {
					String request = new String(data, 0, n);
					
					if (request.startsWith("voy")){
						output.write(compterVoyelles(request.substring(4))+ "\r\n");
					} else if (request.startsWith("con")) {
						output.write(compterConsones(request.substring(4))+ "\r\n");
					} else {
						output.write("-1 Protocol error\r\n");
					}
				}
			} catch (SocketTimeoutException e){
				output.write("-1 Connection timed out\r\n");
			}
			output.flush();
		} catch (IOException e) {}
		finally {
			try {
				socketClient.close();
			} catch (IOException e1) {}
		}
		
	}

}
