package impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import abstct.Server;
import handler.ClientHandler;

/**
 * Implementation of Server using standard ServerSocket.
 * @author greg
 *
 */
public class UnsecuredServer extends Server {

	public UnsecuredServer(int listeningPort, int backlog, InetAddress listeningAddress) throws IOException {
		super(listeningPort, backlog, listeningAddress);
	}
	
	public UnsecuredServer(int listeningPort, int backlog) throws IOException {
		super(listeningPort, backlog);
	}
	
	public UnsecuredServer(int listeningPort) throws IOException {
		super(listeningPort);
	}

	@Override
	protected ServerSocket createListeningSocket(int listeningPort, int backlog, InetAddress listeningAddress) throws IOException {
		return new ServerSocket(listeningPort, backlog, listeningAddress);
	}

	@Override
	protected ServerSocket createListeningSocket(int listeningPort, int backlog) throws IOException {
		return new ServerSocket(listeningPort, backlog);
	}

	@Override
	protected ServerSocket createListeningSocket(int listeningPort) throws IOException {
		return new ServerSocket(listeningPort);
	}

}
