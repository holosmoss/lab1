package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class main {
	//branche aimeric
	public static void main(String[] args) throws IOException{
		
		  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	      System.out.print("Enter String : ");
	      //ouvrir le fichier positioner dans le dossier du projet avec le nom indiquer dans la console
	      String s = br.readLine();
	      File file = new File(s);
	      Path path = file.toPath();
	      byte [] b = Files.readAllBytes(path);
//	      FileReader fr = new FileReader(file);
//	      //TODO le faire sans set size
//	      char [] a = new char[50];
//	      fr.read(b); // reads the content to the array
	      for(byte c : b)
	          System.out.println(c); //prints the characters one by one
//	      fr.close();
	}
}
