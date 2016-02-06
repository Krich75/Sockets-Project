package client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;

import builder.CustomSSLSocketFactoryBuilder;
import exception.SSLContextInitializationException;
import launch.ServerConfig;

public class Client {

	public static int portServer;
	
	public static final int RESPONSE_TIMEOUT = 50000000;
	
	public static final int MAX_RESPONSE_SIZE = 1024;
	
	public static final String UTF_8 = "UTF-8";
	
	public static final String HTTP_GET_REQUEST_FMT = 
			"GET /%s%s HTTP/1.1\r\n" +
			"Host: %s\r\n" +
			"Connection: close\r\n\r\n";
	
	public static final String HTTP_POST_REQUEST_FMT = 
			"POST /%s HTTP/1.1\r\n" +
			"Host: %s\r\n" +
			"Content-Type: %s\r\n" +
			"Content-Length: %s\r\n" +
			"Connection: close\r\n" +
			"\r\n" +
			"%s";
	
	public static final String PLAIN_TEXT_REQUEST_FMT =
			"%s %s\r\n";
	
	private ServerConfig clearConfig;
	private ServerConfig httpConfig;
	private ServerConfig sslConfig;
	
	private SSLSocketFactory sslSocketFactory;
	
	private String buildGetRequest(String hostname, String path, String query){
		return String.format(HTTP_GET_REQUEST_FMT, path, query, hostname);
	}
	
	private String buildPostRequest(String hostname, String path, String contentType, String content){
		
		return String.format(HTTP_POST_REQUEST_FMT, 
				path, hostname, contentType, new Integer(content.length()).toString(), content);
	}
	
	private String buildPlainTextRequest(String method, String sentence){
		
		return String.format(PLAIN_TEXT_REQUEST_FMT, method, sentence); 
	}
	
	private Response parseHTTPResponse(String response) throws InvalidResponseException {
		
		String[] parts = response.split("\r\n\r\n");
		
		if (parts.length < 2){
			throw new InvalidResponseException();
		}
		
		return parseResponse(parts[1]);
	}
	
	private Response parseResponse(String response) 
			throws InvalidResponseException{
		if (response.endsWith("\r\n"))
			response = response.substring(0, response.length()-2);
		
		String[] parts = response.split(" ", 2);
		
		try{
			return new Response(Integer.parseInt(parts[0]), parts.length == 2  ? parts[1] : "");
		} catch (NumberFormatException e){
			throw new InvalidResponseException();
		}
	}
	
	private Socket createSocket(String host, int port) 
			throws UnknownHostException, IOException{
		return new Socket(host, port);
	}
	
	private Socket createSSLSocket(String host, int port) 
			throws UnknownHostException, SSLHandshakeException, IOException{
		return sslSocketFactory.createSocket(host, port);
	}

	private void sendRequest(String request, Socket sock) 
			throws IOException{
		OutputStreamWriter output = new OutputStreamWriter(sock.getOutputStream());
		output.write(request.toString());
		output.flush();
	}
	
	private String readResponse(Socket sock) 
			throws IOException, ResponseTimedOutException, InvalidResponseException{
		InputStreamReader input = new InputStreamReader(sock.getInputStream());
		char[] data = new char[MAX_RESPONSE_SIZE];
		
		sock.setSoTimeout(RESPONSE_TIMEOUT);
		try{
			int n = input.read(data,  0, data.length);
			
			if (n < 1)
				throw new InvalidResponseException();
			return new String(data, 0, n);
		} catch (SocketTimeoutException e){
			throw new ResponseTimedOutException();
		} finally {
			sock.close();
		}
	}
	
	public Client(ServerConfig clearConfig, ServerConfig httpConfig, ServerConfig sslConfig){
		this.clearConfig = clearConfig;
		this.httpConfig = httpConfig;
		this.sslConfig = sslConfig;
		
		try {
			this.sslSocketFactory = CustomSSLSocketFactoryBuilder
							.getInstance(null, null, getClass().getClassLoader().getResourceAsStream("truststore.jts"), "uselesspassword")
							.getSSLSocketFactory();
		} catch (SSLContextInitializationException e) {
			System.out.println("Impossible de charger les certificats de confiance (mode sécurisé). Le programme doit quitter.");
			System.exit(0);
		}
	}
	
	public Response getVowelNumber(String sentence) 
			throws IOException, InvalidResponseException, ResponseTimedOutException {
		Socket sock = createSocket(this.clearConfig.getHostname(), this.clearConfig.getPort());
		
		sendRequest(buildPlainTextRequest("voy", sentence), sock);
		
		return parseResponse(readResponse(sock));
	}
	
	public Response getConsonantNumber(String sentence) 
			throws UnknownHostException, IOException, InvalidResponseException, ResponseTimedOutException {
		Socket sock = createSocket(this.clearConfig.getHostname(), this.clearConfig.getPort());
		
		sendRequest(buildPlainTextRequest("con", sentence), sock);
		
		return parseResponse(readResponse(sock));	
	}
	
	public Response getLetterNumber(String sentence) 
			throws UnknownHostException, IOException, InvalidResponseException, ResponseTimedOutException {
		
		Socket sock = createSocket(this.httpConfig.getHostname(), this.httpConfig.getPort());
		
		sendRequest(
				buildGetRequest(
						this.httpConfig.getHostname(),
						this.httpConfig.getPath(),
						String.format("?action=nbc&text=%s", URLEncoder.encode(sentence, UTF_8))),
				sock);
		
		return parseHTTPResponse(readResponse(sock));
	}

	public Response getSentenceValue(String sentence) 
			throws UnknownHostException, IOException, InvalidResponseException, ResponseTimedOutException {
		Socket sock = createSocket(this.httpConfig.getHostname(), this.httpConfig.getPort());
		
		sendRequest(
				buildPostRequest(
						this.httpConfig.getHostname(),
						this.httpConfig.getPath(),
						"application/x-www-form-urlencoded",
						String.format("action=val&text=%s", URLEncoder.encode(sentence, UTF_8))),
				sock);
		
		return parseHTTPResponse(readResponse(sock));
	}
	
	public Response getVowelNumberSecured(String sentence) 
			throws UnknownHostException, SSLHandshakeException, IOException, InvalidResponseException, ResponseTimedOutException {
		Socket sock = createSSLSocket(this.sslConfig.getHostname(), this.sslConfig.getPort());
		
		sendRequest(buildPlainTextRequest("voy", sentence), sock);
		
		return parseResponse(readResponse(sock));
	}
	
	public Response getConsonantNumberSecured(String sentence) 
			throws UnknownHostException, SSLHandshakeException, IOException, InvalidResponseException, ResponseTimedOutException {
		Socket sock = createSSLSocket(this.sslConfig.getHostname(), this.sslConfig.getPort());
		
		sendRequest(buildPlainTextRequest("con", sentence), sock);
		
		return parseResponse(readResponse(sock));	
	}
}
