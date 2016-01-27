package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MultiThreadServer implements Runnable {

	public static final int portServerJava = 6666;

	private Socket socket;
	private BufferedReader in;
	private PrintWriter out;

	/**
	 * Constructor to MultiThreadServer
	 * 
	 * @param socket
	 *            : the current socket use for this Thread
	 */
	public MultiThreadServer(Socket socket) {
		this.socket = socket;
		initCommunication();
	}

	/**
	 * Code to run a new Thread
	 */
	public void run() {
		try {
			String text = in.readLine();
			String tmp = text.substring(4);
			tmp = tmp.replaceAll(" ", "").toLowerCase();
			if (text.startsWith("car")) {
				out.println(tmp.length());
			} 
			else if (text.startsWith("val")) {
				out.println(value(tmp));
			} 
			else if (text.startsWith("voy")) {
				out.println(voyelle(tmp));
			}
			else if (text.startsWith("con")) {
				out.println(tmp.length()-voyelle(tmp));
			}
			else{
				out.println("Nothing");
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		close();
	}
	
	/**
	 * Method to count number of wovel
	 * @param tmp the current string 
	 * @return the number wovel
	 */
	private int voyelle(String tmp){
		char ch;
		int nb=0;
		for (int i = 0; i < tmp.length(); i++) {
            ch = tmp.charAt(i);
            if (isVoyelle(ch)) {
                nb++;
            }
        }
		return nb;
	}
	
	/**
	 * Method to count value of sentence
	 * @param tmp
	 * @return this value
	 */
	private int value(String tmp){
		int value = 0;
		for (int i = 0; i < tmp.length(); i++) {
			if ((int) tmp.charAt(i) > 96 && (int) tmp.charAt(i) < 127) {
				value += (int) tmp.charAt(i) - 96;
			}
		}
		return value;
	}
	

	/**
	 * Method to initialise different element to communicate
	 */
	private void initCommunication() {
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isVoyelle(char ch) {
        if (ch == 'a' || ch == 'e' || ch == 'i'|| ch == 'o'|| ch == 'u'  || ch == 'y') {
            return true;
        }
        return false;
    }
	/**
	 * Method to close all element to communicate
	 */
	private void close() {
		try {
			socket.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}