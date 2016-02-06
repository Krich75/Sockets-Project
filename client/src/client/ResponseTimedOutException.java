package client;

public class ResponseTimedOutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1897420021922722692L;

	/**
	 * Default constructor of ResponseTimeOutException
	 */
	public ResponseTimedOutException() {
		super();
	}

	/**
	 * Default constructor of ResponseTimeOutException
	 * Parameter : Parameter : String message, a throwable cause and a boolean for remove
	 */
	public ResponseTimedOutException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Default constructor of ResponseTimeOutException
	 * Parameter : Parameter : String message and a throwable cause 
	 */
	public ResponseTimedOutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor of ResponseTimeOutException
	 * Parameter : Parameter : String message
	 */
	public ResponseTimedOutException(String message) {
		super(message);
	}

	/**
	 * Default constructor of ResponseTimeOutException
	 * Parameter : a throwable cause
	 */
	public ResponseTimedOutException(Throwable cause) {
		super(cause);
	}

}
