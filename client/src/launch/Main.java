package launch;

import client.Client;
import client.Communication;
import client.UserInterface;


public class Main {

	public static void main(String[] args) {
		Client client = new Client(new UserInterface(), new Communication());
		client.execute();
		System.exit(0);	
	}
}
