package client;

import java.io.IOException;
import java.net.InetAddress;

import launch.CONFIG;


public class Client {

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
			int choice = userInterface.displayChoice();
			String line = userInterface.getLine();
			String result="";
			
			switch (choice){
			  case 0:
				  System.exit(0);
			  case 1: // Number vowel
				  communication.initSocket(hostName,CONFIG.portServer);
				  communication.send("voy "+line+"\r\n");
				  userInterface.display(communication.receive()+" voyelles \n");
				  break;
			  case 2: // Number consonne
				  communication.initSocket(hostName,CONFIG.portServer);
				  communication.send("con "+line+"\r\n");
				  userInterface.display(communication.receive()+" consonnes \n");
				  break;
			  case 3: // Number letter
				  result = httpRequest.postRequest("nbc", line);
				  userInterface.display("--Server PHP-- : "+ result+" caractères (sans espace) \n");
				  break;
			  case 4: // value of sentences
				  result = httpRequest.getRequest("val", line);
				  userInterface.display("--Server PHP-- : Valeur de la phrase :"+ result +"\n");
				  break;      
			  case 5: // vowel ssl
				  communication.initSocketSSL(hostName,CONFIG.portServerSSL);
				  communication.send("voy "+line+"\r\n");
				  userInterface.display(communication.receive()+" voyelles \n");
				  break;
			  case 6: // Consonne ssl
				  communication.initSocketSSL(hostName,CONFIG.portServerSSL);
				  communication.send("con "+line+"\r\n");
				  userInterface.display(communication.receive()+" consonnes \n");
				  break; 
			  default:
			}
			
			communication.close();
			httpRequest.close();
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}

}
