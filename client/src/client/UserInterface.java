package client;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class UserInterface {
	
	public UserInterface(){
		
	}
	
	public int displayChoice(){
		Scanner choice = new Scanner(System.in);
		int choiceNumber;
		do{
			System.out.println("Veuillez choisir une option (saisissez 1-2-3-4, ou 0 pour quitter) :");
			System.out.println("-- 1) Compter le nombre de voyelles.");
			System.out.println("-- 2) Compter le nombre de consonne.");
			System.out.println("-- 3) Compter le nombre de lettre.");
			System.out.println("-- 4) Donner la valeur d'une phrase.");
			choiceNumber = choice.nextInt();
			System.out.println(choiceNumber);
		}while(choiceNumber!=0 && choiceNumber!=1 && choiceNumber!=2 && choiceNumber!=3 && choiceNumber!=4);
		return choiceNumber;
	}
	
	public String getLine(){
		String inputStr = "";
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir une ligne");
		try{
			inputStr = sc.nextLine();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		finally {
			sc.close();
		}
		System.out.println("Vous avez saisis : "+inputStr);
		return inputStr;
	}
	
	
	public void display(String text){
		System.out.println(text);
	}

}
