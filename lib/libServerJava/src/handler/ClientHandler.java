package handler;

import java.net.Socket;
import java.lang.Runnable;

/**
 * Allow to define a customized dialog with the client.
 * @author greg
 *
 */
public interface ClientHandler {

	/**
	 * Called on each new connection with the corresponding socket.
	 * A server has only one instance of ClientHandler for all connections,
	 * so all members of an implementation will be shared across the different threads. 
	 * @param sockClient The socket on which client is connected.
	 */
	public abstract void handleClient(Socket sockClient);
	
}
