package handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class LetterCounterHandler implements ClientHandler {

	public final static String VOYELLES = "aeiouy";
	public final static String CONSONES = "bcdfghjklmnpqrstvwxz";
	
	public int isConsone(String s){
		return CONSONES.contains(s) ? 1 : 0;
	}
	
	public int isVoyelle(String s){
		return VOYELLES.contains(s) ? 1 : 0;
	}
	
	public int compterConsones(String message){
		
		int result = 0;
		
		for (int i=0; i<message.length(); i++){
			result += isConsone(message.substring(i, i+1));
		}
		
		return result;
	}
	
	public int compterVoyelles(String message){
		
		int result = 0;
		
		for (int i=0; i<message.length(); i++){
			result += isVoyelle(message.substring(i, i+1));
		}
		
		return result;
	}

	@Override
	public void handleClient(Socket socketClient) {
		
		BufferedReader input;
		BufferedWriter output;
		
		System.out.println("Un client s'est connecté.");
		
		try {
			socketClient.setSoTimeout(5000);
			input = new BufferedReader(new InputStreamReader(socketClient.getInputStream(), "UTF-8"));
			output = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
			
			try {
				System.out.println("En attente de la requête du client..");
				String request = input.readLine();
				
				if (request.startsWith("voy")){
					output.write(compterVoyelles(request.substring(4))+ "\r\n");
				} else if (request.startsWith("con")) {
					output.write(compterConsones(request.substring(4))+ "\r\n");
				} else {
					output.write("-1 Protocol error\r\n");
				}
			} catch (SocketTimeoutException e){
				output.write("-1 Connection timed out\r\n");
			}
			output.flush();
			socketClient.close();
		} catch (IOException e) {
			if (!socketClient.isClosed()) {
				try {
					socketClient.close();
				} catch (IOException e1) {}
			}
		}
		
	}

}
