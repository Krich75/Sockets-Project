package impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.net.ssl.SSLServerSocketFactory;

import abstct.Server;
import handler.ClientHandler;

/**
 * Implementation of Server using SSLServerSocket.
 * @author greg
 *
 */
public class SSLServer extends Server {

	public SSLServer(int listeningPort, int backlog, InetAddress listeningAddress) throws IOException {
		super(listeningPort, backlog, listeningAddress);
	}
	
	public SSLServer(int listeningPort, int backlog) throws IOException {
		super(listeningPort, backlog);
	}
	
	public SSLServer(int listeningPort) throws IOException {
		super(listeningPort);
	}
	
	@Override
	protected ServerSocket createListeningSocket(int listeningPort, int backlog, InetAddress listeningAddress) throws IOException {
		return SSLServerSocketFactory.getDefault().createServerSocket(listeningPort, backlog, listeningAddress);
	}

	@Override
	protected ServerSocket createListeningSocket(int listeningPort, int backlog) throws IOException {
		return SSLServerSocketFactory.getDefault().createServerSocket(listeningPort, backlog);
	}

	@Override
	protected ServerSocket createListeningSocket(int listeningPort) throws IOException {
		return SSLServerSocketFactory.getDefault().createServerSocket(listeningPort);
	}

}
