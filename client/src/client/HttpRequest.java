package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import launch.CONFIG;

public class HttpRequest {

	private Communication communication;

	/**
	 * Constructor to HttpRequest
	 * This constructor initialise necessary socket to communicate with the PHP server
	 * @param communication : a no initialise connection
	 */
	public HttpRequest(Communication communication) {
		this.communication = communication;
		try {
			communication.initSocket(InetAddress.getByName(CONFIG.hostName), CONFIG.portServerPhp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Methode to send a POST request
	 * @param action : the action ("nbc" -> number of caractère, "val" -> value of text)
	 * @param text : the text to send
	 */
	public String postRequest(String action, String text) {
		String line="";
		String result ="";
		String param = prepareParameters(action, text);
		String postRequest = "POST " +CONFIG.path + " HTTP/1.1\r\n";
		postRequest+="Host: "+CONFIG.hostName+"\r\n";
		postRequest+="Content-Type: application/x-www-form-urlencoded\r\n";
		postRequest+="Content-Length: "+ param.length() + "\r\n\r\n";
		postRequest+=param;	
		communication.send(postRequest);
		while((line=communication.receive())!= null){
		    result=line; // get the last string to request response
		}
		return result;
	}
	
	/**
	 * Methode to send a GET request
	 * @param action : the action ("nbc" -> number of caractère, "val" -> value of text)
	 * @param text : the text to send
	 */
	public String getRequest(String action, String text) {
		String line="";
		String result="";
		String param = prepareParameters(action, text);
		String getRequest = "GET "+CONFIG.path+"?"+ param+" HTTP/1.1\r\n";
		getRequest+="Host: "+CONFIG.hostName+" \r\n\r\n";

		communication.send(getRequest);
		while((line=communication.receive())!= null){  
			result=line; // get the last string to request response
		}
		return result;
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
