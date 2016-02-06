package exception;

/**
 * An exception describing an error in the SSL context initialization.
 * @author greg
 *
 */
public class SSLContextInitializationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4040985844712397663L;

	public SSLContextInitializationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SSLContextInitializationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public SSLContextInitializationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SSLContextInitializationException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SSLContextInitializationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
}
