package excpt;

/**
 * An exception describing an invalid state of a Server.
 * @author greg
 *
 */
public class InvalidStateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1651500290544771021L;

	public InvalidStateException() {
		super();
	}

	public InvalidStateException(String message) {
		super(message);
	}

}
