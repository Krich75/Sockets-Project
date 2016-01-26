package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

	static final int portServerC = 9999;
	static final int portServerJava = 6666;

	private UserInterface userInterface;
	private Communication communication;
	private HttpRequest httpRequest;

	/**
	 * Constructor to Client : affect and create usefull communication 
	 * @param userInterface
	 * @param communication
	 */
	public Client(UserInterface userInterface, Communication communication) {
		this.userInterface = userInterface;
		this.communication = communication;
		this.httpRequest = new HttpRequest(new Communication());
	}

	/**
	 * Methode to execute client action and send request to different server
	 */
	public void execute(){
		try{
			InetAddress hostName = InetAddress.getLocalHost();
			String line = userInterface.getLine();
			switch (userInterface.displayChoice()){
			  case 0:
				  System.exit(0);
			  case 1:
				  communication.initSocket(hostName,portServerC);
				  communication.send("voy "+line+"\r\n");
				  userInterface.display(communication.receive()+" voyelles \n");
				  break;
			  case 2:
				  communication.initSocket(hostName,portServerC);
				  communication.send("con "+line+"\r\n");
				  userInterface.display(communication.receive()+" consonnes \n");
				  break;
			  case 3:
				  communication.initSocket(hostName,portServerJava);
				  communication.send("car "+line+"\r\n");
				  httpRequest.postRequest("nbc", line);
				  userInterface.display(communication.receive()+" caractères (sans espace) \n");
				  break;
			  case 4:
				  communication.initSocket(hostName,portServerJava);
				  communication.send("val "+line+"\r\n");
				  httpRequest.getRequest("val", line);
				  userInterface.display("Valeur de la phrase :"+ communication.receive()+"\n");
				  break;      
			  default:
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		finally {
			try {
				communication.close();
				httpRequest.close();
			} catch (IOException e) {
				System.out.println("Erreur de fermeture des communications");
			}
		}	
	}

}
