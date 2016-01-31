package handler;

import java.net.Socket;
import java.lang.Runnable;

public interface ClientHandler {

	public abstract void handleClient(Socket sockClient);
	
}
