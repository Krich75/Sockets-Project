package client;

public class Response {
	
	private int number;
	private String message;
	
	/**
	 * Constructor of Response
	 * @param number
	 * @param message
	 */
	public Response(int number, String message) {
		super();
		this.number = number;
		this.message = message;
	}

	/**
	 * Getter of number
	 * @return number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Getter of message
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * responseHasMessage
	 * @return Boolean
	 */
	public boolean responseHasMessage(){
		if (message == null)
			return false;
		
		return !message.equals("");
	}
}
