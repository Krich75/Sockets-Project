package handler;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

import javax.net.ssl.SSLServerSocketFactory;

import builder.CustomSSLSocketFactoryBuilder;
import exception.SSLContextInitializationException;
import impl.SSLServer;

public class CustomSSLServer extends SSLServer {

	private SSLServerSocketFactory sslServerSocketFactory = null;
	
	private SSLServerSocketFactory createSSLServerSocketFactory(){
		try {
			return CustomSSLSocketFactoryBuilder
					.getInstance(getClass().getClassLoader().getResourceAsStream("keystore.jks"), "uselesspassword", null, null)
					.getSSLServerSocketFactory();
		} catch (SSLContextInitializationException e) {
			System.err.println("Impossible to load SSLContext. Program cannot continue");
			System.exit(1);
		}
		return null;
	}
	
	public CustomSSLServer(int listeningPort) throws IOException {
		super(listeningPort);
	}
	
	@Override
	protected ServerSocket createListeningSocket(int listeningPort) throws IOException {
		
		if (sslServerSocketFactory == null){
			sslServerSocketFactory = createSSLServerSocketFactory();
		}
		
		return sslServerSocketFactory.createServerSocket(listeningPort);
	}

}
