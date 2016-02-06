package client;

public class InvalidResponseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5447257982011059852L;

	/**
	 * Default constructor of InvalidResponseException
	 */
	public InvalidResponseException() {
		super();
	}

	/**
	 *  Constructor of InvalideResponseExcpetion
	 * Parameter : String message, a throwable cause and a boolean for remove
	 */
	public InvalidResponseException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 *  Constructor of InvalideResponseExcpetion
	 * Parameter : String message and a throwable cause
	 */
	public InvalidResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 *  Constructor of InvalideResponseExcpetion
	 * Parameter : String message
	 */
	public InvalidResponseException(String message) {
		super(message);
	}

	/**
	 *  Constructor of InvalideResponseExcpetion
	 * Parameter : a throwable cause 
	 */
	public InvalidResponseException(Throwable cause) {
		super(cause);
	}

}
