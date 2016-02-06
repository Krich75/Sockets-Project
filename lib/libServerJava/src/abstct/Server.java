package abstct;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import excpt.InvalidStateException;
import handler.ClientHandler;

/**
 * Manage the connection mechanism.
 * @author greg
 *
 */
public abstract class Server {
	
	private ServerSocket listeningSock;
	private ExecutorService threadPool;
	private Thread listeningThread;
	private boolean running;
	private int terminationTimeout;
	private ClientHandler handler;
	
	/**
	 * Give a Runnable object calling the ClientHandler "handleClient()" method.
	 * @param clientSock The socket associated to this client.
	 * @return A java.lang.Runnable.
	 */
	private Runnable createClientHandlerRunnable(final Socket clientSock){
		return new Runnable() {
			@Override
			public void run() {
				handler.handleClient(clientSock);
			}
		};
	}
	
	/**
	 * Create the thread waiting for clients connections.
	 * @return A Thread Object.
	 */
	private Thread createListeningThread(){
		return new Thread(new Runnable() {
			@Override
			public void run() {
				
				try {
					while (running){
						Socket clientSock = listeningSock.accept();
						threadPool.submit(createClientHandlerRunnable(clientSock));
					}
				} catch (SocketException e){
					/* Thrown when listeningSock.close() is called by another thread
					 * and current thread is blocked on listeningSock.accept().
					 */
					running = false;
				}catch (IOException e) {
				} finally {
					try {
						if (!listeningSock.isClosed())
							listeningSock.close();
					} catch (IOException e) {}
				}
				
				boolean notAllConnectionTerminated = true;
				while (notAllConnectionTerminated){
					threadPool.shutdown();
					try {
						threadPool.awaitTermination(terminationTimeout, TimeUnit.MILLISECONDS);
						notAllConnectionTerminated = false;
					} catch (InterruptedException e) {
					}
				}
			}
		});
	}
	
	/**
	 * These abstract methods allow future implementations
	 * to use their own ServerSocket implementations.
	 * @param listeningPort The port on which this socket will be binded.
	 * @param backlog The size of connection queue.
	 * @param listeningAddress The interface on which this socket will be binded.
	 * @return A Socket Object.
	 * @throws IOException If a network error occur.
	 */
	protected abstract ServerSocket createListeningSocket(int listeningPort, int backlog, InetAddress listeningAddress) throws IOException;
	protected abstract ServerSocket createListeningSocket(int listeningPort, int backlog) throws IOException;
	protected abstract ServerSocket createListeningSocket(int listeningPort) throws IOException;
	
	/**
	 * Create a new Server.
	 * @param listeningPort The port on which the server will listen.
	 * @param backlog The size of the connection queue.
	 * @param listeningAddress The interface on which the server will listen.
	 * @throws IOException If a network error occur.
	 */
	public Server(int listeningPort, int backlog, InetAddress listeningAddress) throws IOException {
		this.listeningSock = createListeningSocket(listeningPort, backlog, listeningAddress);
	}
	
	/**
	 * Create a new Server listening on all interfaces.
	 * @param listeningPort The port on which the server will listen.
	 * @param listeningAddress The interface on which the server will listen.
	 * @throws IOException If a network error occur.
	 */
	public Server(int listeningPort, int backlog) throws IOException {
		this.listeningSock = this.createListeningSocket(listeningPort, backlog);
	}
	
	/**
	 * Create a new Server listening on all interfaces without specify backlog.
	 * @param listeningPort The port on which the server will listen.
	 * @param backlog The size of the connection queue.
	 * @param listeningAddress The interface on which the server will listen.
	 * @throws IOException If a network error occur.
	 */
	public Server(int listeningPort) throws IOException {
		this.listeningSock = this.createListeningSocket(listeningPort);
	}
	
	/**
	 * Make the server listening for connections.
	 * @param handler The object in charge of the dialog with the client.
	 * @param maxConnections The maximum of clients connected simultaneously (maximum of thread).
	 */
	public void start(ClientHandler handler, int maxConnections) {
		
		if (this.running){
			throw new InvalidStateException("Server already started");
		}
		
		this.threadPool = Executors.newFixedThreadPool(maxConnections);
		this.handler = handler;
		this.running = true;
		this.listeningThread = createListeningThread();
		listeningThread.start();
	}
	
	/**
	 * Make the server listening for connections
	 * without specify the maximum of simultaneous connections (default=20).
	 * @param handler The object in charge of the dialog with the client.
	 */
	public void start(ClientHandler handler) {
		this.start(handler, 20);
	}
	
	/**
	 * Make the server stop for accepting new connections,
	 * and specify a timeout beyond which the client threads will be stopped.
	 * @param terminationTimeout A timeout beyond which the client threads will be stopped.
	 * @throws InvalidStateException If the server isn't already started.
	 */
	public void stop(int terminationTimeout) throws InvalidStateException {
		
		if (!this.running){
			throw new InvalidStateException("Server is not running");
		}
		this.terminationTimeout = terminationTimeout;
		this.running = false;
		try {
			listeningSock.close();
		} catch (IOException e) {}
	}
	
	/**
	 * Make the server stop for accepting new connections,
	 * and stop client threads immediately.
	 * @throws InvalidStateException If the server isn't already started.
	 */
	public void stopListening() throws InvalidStateException {
		this.stop(0);
	}
	
	/**
	 * Wait for the server to stop properly
	 * (wait for the listening thread to terminate properly,
	 * which will wait for the client threads to terminate too).
	 */
	public void awaitTermination(){
		
		boolean unJoined = true;
		
		if (this.listeningThread == null)
			return;
		
		while (unJoined){
			try {
				this.listeningThread.join();
				unJoined = false;
			} catch (InterruptedException e) {
			}
		}
	}
}
