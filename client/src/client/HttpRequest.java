package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;

public class HttpRequest {

	public static final int portServerPhp = 80;

	private Communication communication;
	private String path = "/Projet-socket";
	private String hostName = "hina/~picharles/";

	/**
	 * Constructor to HttpRequest
	 * This constructor initialise necessary socket to communicate with the PHP server
	 * @param communication : a no initialise connection
	 */
	public HttpRequest(Communication communication) {
		this.communication = communication;
		try {
			communication.initSocket(InetAddress.getByName(hostName), portServerPhp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode to send a POST request
	 * @param action : the action ("nbc" -> number of caractère, "val" -> value of text)
	 * @param text : the text to send
	 */
	public void postRequest(String action, String text) {
		String param = prepareParameters(action, text);
		communication.send("POST " + path + " HTTP/1.0\r\n");
		communication.send("Content-Length: " + param.length() + "\r\n");
		communication.send("Content-Type: application/x-www-form-urlencoded\r\n");
		communication.send("\r\n");
		communication.send(param);
	}

	/**
	 * Methode to send a GET request
	 * @param action : the action ("nbc" -> number of caractère, "val" -> value of text)
	 * @param text : the text to send
	 */
	public void getRequest(String action, String text) {
		communication.send("GET " + prepareParameters(action, text));
		communication.send("Host: \r\n\r\n");
	}
	
	/**
	 * Methode to encode parameters in UTF-8 
	 * @param action
	 * @param text
	 * @return new String encoded
	 */
	private String prepareParameters(String action, String text){
		String params ="";
		try {
			params = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode(action, "UTF-8");
			params += "&" + URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return params;
	}
	
	/***
	 * Methode to close communication with PHP server
	 */
	public void close(){
		try {
			communication.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}


}
