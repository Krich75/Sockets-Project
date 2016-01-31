package abstct;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import handler.ClientHandler;

public abstract class Server extends Thread {
	
	private ServerSocket listeningSock;
	private ExecutorService threadPool;
	private boolean running;
	private int terminationTimeout;
	private TimeUnit timeUnit;
	private ClientHandler handler;
	
	private void acceptConnection() {
		try {
			Socket clientSock = listeningSock.accept();
			
			this.threadPool.submit(new Runnable() {
				@Override
				public void run() {
					handler.handleClient(clientSock);
				}
			});
		} catch (SocketException e){
			/* Thrown when listeningSock.close() is called by another thread
			 * and current thread is blocked on listeningSock.accept().
			 */
			this.running = false;
		}catch (IOException e) {
			this.running = false;
		}
	}
	
	protected abstract ServerSocket createListeningSocket(int listeningPort, int backlog, InetAddress listeningAddress) throws IOException;
	protected abstract ServerSocket createListeningSocket(int listeningPort, int backlog) throws IOException;
	protected abstract ServerSocket createListeningSocket(int listeningPort) throws IOException;
	
	public Server(int listeningPort, int backlog, InetAddress listeningAddress) throws IOException {
		this.listeningSock = createListeningSocket(listeningPort, backlog, listeningAddress);
	}
	
	public Server(int listeningPort, int backlog) throws IOException {
		this.listeningSock = this.createListeningSocket(listeningPort, backlog);
	}
	
	public Server(int listeningPort) throws IOException {
		this.listeningSock = this.createListeningSocket(listeningPort);
	}
	
	public void startListening(ClientHandler handler, int maxConnections) {
		this.threadPool = Executors.newFixedThreadPool(maxConnections);
		this.handler = handler;
		this.running = true;
		this.start();
	}
	
	public void startListening(ClientHandler handler) {
		this.startListening(handler, 20);
	}
	
	public void stopListening(int terminationTimeout, TimeUnit timeUnit){
		this.terminationTimeout = terminationTimeout;
		this.timeUnit = timeUnit;
		try {
			listeningSock.close();
		} catch (IOException e) {}
	}
	
	public void stopListening(){
		this.stopListening(5, TimeUnit.SECONDS);
	}
	
	public void awaitTermination(){
		
		boolean unJoined = true;
		
		while (unJoined){
			try {
				this.join();
				unJoined = false;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void run() {
		
		boolean notAllConnectionTerminated = true;
		
		while (running){
			this.acceptConnection();
		}
		
		while (notAllConnectionTerminated){
			threadPool.shutdown();
			try {
				threadPool.awaitTermination(terminationTimeout, timeUnit);
				notAllConnectionTerminated = false;
			} catch (InterruptedException e) {
			}
		}
	}
}
