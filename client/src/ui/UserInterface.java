package ui;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserInterface {
	
	public static final String OPTIONS = 
			"1-2-3-4-5-6, 7 pour afficher l'aide, ou 0 pour quitter\r\n"
			+"Veuillez choisir une option : ";
	
	public static final String HELP =
			"-- 1) Demander le nombre de voyelles d'une phrase.\r\n"
			+"-- 2) Demander le nombre de consonne d'une phrase.\r\n"
			+"-- 3) Demander le nombre de lettre d'une phrase.\r\n"
			+"-- 4) Demander la valeur d'une phrase d'une phrase.\r\n"
			+"-- 5) Demander le nombre de voyelles d'une phrase (Connexion sécurisé).\r\n"
			+"-- 6) Demander le nombre de consones d'une phrase (Connexion sécurisé).\r\n"
			+"-- 7) Afficher ce message.";
	
	public static final String SERVER_OPTION = 
			"A qu'elle serveur voulez-vous vous connecter ? \r\n " +
			"1 -> Serveur C \r\n " +
			"2 -> Serveur Java";
	
	public static final String MISTAKE = "Merci de saisir un choix valide.";
	
	public static final String ASK_FOR_SENTENCE = "Merci de saisir une phrase : ";
	
	private Scanner scanner;
	
	/**
	 * Method for display options and read user choice
	 * @return the choice or  -1
	 */
	private int displayOptionsAndReadChoice(){
		System.out.print(OPTIONS);
		try{
			return scanner.nextInt();
		} catch (InputMismatchException e){
			scanner.nextLine();
			return -1;
		}
	}
	
	private int displayOptionsAndReadSeverChoice(){
		System.out.print(SERVER_OPTION);
		try{
			return scanner.nextInt();
		} catch (InputMismatchException e){
			scanner.nextLine();
			return -1;
		}
	}
	/**
	 * Constructor of UserInterface
	 * @param input
	 */
	public UserInterface(InputStream input){
		scanner = new Scanner(input);
	}
	
	/**
	 * Method for display many choice to user
	 * @return selected choice of the user
	 */
	public int displayChoice(){
		int choice = -1;
		
		choice = displayOptionsAndReadChoice();
		
		while(choice < 0 || choice > 6) {
			if (choice == 7)
				System.out.println(HELP);
			else
				System.out.println(MISTAKE);
			choice = displayOptionsAndReadChoice();
		}
		
		return choice;
	}
	
	/**
	 * Method for get the user entries line
	 * @return this line
	 */
	public String getSentence(){
		System.out.print(ASK_FOR_SENTENCE);
		
		String line = "";
		while (line.equals("")) {
			try {
				line = scanner.nextLine();
			} catch (NoSuchElementException e){} 
		}
		
		return line;
	}
	
	/**
	 * Method for display a message
	 * @param text
	 */
	public void display(String text){
		System.out.println(text);
	}

}
