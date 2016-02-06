package launch;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import client.Client;
import client.InvalidResponseException;
import client.Response;
import client.ResponseTimedOutException;
import ui.UserInterface;


public class Main {
	
	private static ServerConfig clearConfig = new ServerConfig("127.0.0.1", 7777, null);
	private static ServerConfig httpConfig = new ServerConfig("127.0.0.1", 8888, "index.php");
	private static ServerConfig sslConfig = new ServerConfig("127.0.0.1", 9999, null);
	
	private static void printUsage(){
		System.out.println("Usage : java -jar client.jar [-clear hostname[:port]] [-http hostname[:port][/path]] [-ssl hostname[:port]]");
	}
	
	private static ServerConfig parseServerInfo(String arg, String defaultHostname, int defaultPort, String defaultPath){
		
		String hostname = defaultHostname;
		int port = defaultPort;
		String path = defaultPath;
		
		String[] parts = arg.split(":");
		
		if (parts.length > 1){
			
			hostname = parts[0];
			
			String[] parts2 = parts[1].split("/", 2);
			
			try {
				port = Integer.parseInt(parts2[0]);
			} catch (NumberFormatException e){
				throw new RuntimeException(parts2[0] + " : invalid port number");
			}
			
			if (parts2.length > 1){
				path = parts2[1];
			}
		} else {
			String[] parts2 = parts[0].split("/", 2);
			
			hostname = parts2[0];
			
			if (parts2.length > 1){
				path = parts2[1];
			}
		}
		
		return new ServerConfig(hostname, port, path);
	}
	
	private static void parseArgs(String[] args){
		try{
			for (int i=0; i<args.length; i+=2){
				if (args[i].equals("-clear")){
					clearConfig = parseServerInfo(args[i+1], clearConfig.getHostname(), clearConfig.getPort(), clearConfig.getPath());
				}
				else if (args[i].equals("-http")){
					httpConfig = parseServerInfo(args[i+1], httpConfig.getHostname(), httpConfig.getPort(), httpConfig.getPath());
				}
				else if (args[i].equals("-ssl")){
					clearConfig = parseServerInfo(args[i+1], sslConfig.getHostname(), sslConfig.getPort(), sslConfig.getPath());
				}
				else {
					System.out.println(args[i] + " : Invalid switch.");
					printUsage();
					System.exit(1);
				}
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
			printUsage();
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		
		parseArgs(args);
		
		UserInterface userInterface = new UserInterface(System.in);
		Client client = new Client(clearConfig, httpConfig, sslConfig);
			
		int choice = -1;
		
		while (choice != 0) {
			
			choice = userInterface.displayChoice();
			
			Response response;
			
			try {
				switch (choice){
					case 1: // Number vowel
						response = client.getVowelNumber(userInterface.getSentence());
					break;
					case 2: // Number consonne
						response = client.getConsonantNumber(userInterface.getSentence());
					break;
					case 3: // Number letter
						response = client.getLetterNumber(userInterface.getSentence());
					break; 
					case 4: // value of sentences
						response = client.getSentenceValue(userInterface.getSentence());
					break;  
					case 5: // vowel ssl
						response = client.getVowelNumberSecured(userInterface.getSentence());
					break;  
					case 6: // Consonne ssl
						response = client.getConsonantNumberSecured(userInterface.getSentence());
					break;
					default:
						continue;
				}
				
				if (response.getNumber() == -1){
					userInterface.display("Le server à renvoyé un erreur" +
							(response.responseHasMessage() ? " : " +response.getMessage() + "." : "."));
				} else {
					userInterface.display("Le serveur à renvoyé : " + response.getNumber() + 
							(response.responseHasMessage() ? " " + response.getMessage() : ""));
				}
				
			} catch (UnknownHostException e) {
				userInterface.display(e.getMessage());
				System.exit(1);
			} catch (SSLHandshakeException e) {
				System.out.println("Impossible d'établir une connexion sécurisée avec le serveur.");
			} catch (IOException e) {
				userInterface.display("Une erreur réseau s'est produite.");
			} catch (InvalidResponseException e) {
				userInterface.display("Le serveur à renvoyé une réponse invalide.");
			} catch (ResponseTimedOutException e) {
				userInterface.display("Le serveur à été trop long à répondre.");
			} catch (Exception e) {
				userInterface.display("Une erreur imprévue s'est produit. Le programme doit quitter.");
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}
}
