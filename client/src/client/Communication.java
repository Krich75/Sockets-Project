package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Communication {
		
	private Socket socket;
	private PrintStream output;
	private Reader reader;
	private BufferedReader ret;
	
	/**
	 * Constructor to communication
	 */
	public Communication(){ 
		
	}
	
	/**
	 * Methode to initialise Socket and element to communicate
	 * @param hostName
	 * @param port
	 */
	public void initSocket(InetAddress hostName, int port){
		try {
			socket = new Socket(hostName, port);
			output = new PrintStream(socket.getOutputStream());
			reader = new InputStreamReader(socket.getInputStream());
			ret = new BufferedReader(reader);
		} catch (UnknownHostException e) {
			System.out.println("Hote inconnu");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to close all element to communicate
	 * @throws IOException
	 */
	public void close() throws IOException{
		this.output.close();
		this.reader.close();
		this.socket.close();
	}
	
	/**
	 * Method to send a message
	 * @param sendText
	 */
	public void send(String sendText){
		this.output.println(sendText);
	}
	
	/**
	 * Method to get the receive message
	 * @return
	 * @throws IOException
	 */
	public String receive() throws IOException{
		return this.ret.readLine();
		
	}
}
