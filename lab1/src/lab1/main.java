package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {
	//branche aimeric
	public static void main(String[] args) throws IOException{
		
		  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	      System.out.print("Enter String : ");
	      //ouvrir le fichier positioner dans le dossier du projet avec le nom indiquer dans la console
	      String s = br.readLine();
	      System.out.print("opening :"+s);
	      File file = new File(s);
	      FileReader fr = new FileReader(file);
	      //TODO le faire sans set size
	      char [] a = new char[50];
	      fr.read(a); // reads the content to the array
	      for(char c : a)
	          System.out.print(c); //prints the characters one by one
	      fr.close();
	}
}
