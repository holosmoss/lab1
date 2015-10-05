package lab1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main {
	
	public static void main(String[] args) throws IOException{
		
		  long startTime;
		  long endTime;
		  String time;
		
		  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		  System.out.println("Entrer le nom du fichier avec son extension : ");
		  String filename = br.readLine();
		  System.out.println("tapez 1 pour compresser, 2 pour d�compresser:  ");
	      String s = br.readLine();
	      if(s.equals("1")){
	    	  //1 est la commande pour compresser le document
		      System.out.println("---Compress---------------------------------------------------------");
		      startTime = System.currentTimeMillis();
		      File file = new File(filename);
		      Path path = file.toPath();
		      byte [] b = Files.readAllBytes(path);
		      String wholeString = new String(b, StandardCharsets.UTF_8 );
		      //hashmap des frequences
		      Map <Character,Integer> frequence = new HashMap <Character,Integer>();
		      for(int i = 1;i < wholeString.length();i++){
		    	  //TODO le ficier encod� en utf-8 a un ? au debut so ont skip le premier char
		    	  char c = wholeString.charAt(i);
		    	  if (frequence.containsKey(c)) {
		    		    Integer prev = frequence.get(c);
		    		    frequence.put(c, prev+1);
		    		}  else {
		    			frequence.put(c, 1);
		    		}
		      }
		      //List de lordre descandant des cle de la hashmap
		      List<Character> freqSortedList = getWordInDescendingFreqOrder(frequence);
		      
		      //construit l'arbre binaire
		      ArbreBinaire arbreBin = new ArbreBinaire(frequence, freqSortedList);
		      arbreBin.tableBinaire();
		      
		      //compressSuperTigh repr�sente nos byte compress� qu<ont met dans le document
		      byte[] compressSuperTight = arbreBin.doCompress(wholeString);	
		      File compressedFile = new File("compressed.txt");
		      java.nio.file.Files.write(compressedFile.toPath(), compressSuperTight);
		      endTime = System.currentTimeMillis();
			  time = String.valueOf(endTime-startTime);			
			  System.out.println("temps1= "+ time + "ms" );
		      System.out.println("complete");
		      

		      
	      }else if(s.equals("2")){
	    	 System.out.println("---Decompress---------------------------------------------------------");
	    	 startTime = System.currentTimeMillis();
	    	 File decompressibleFile = new File(filename);
		     byte [] compressedBytes = Files.readAllBytes(decompressibleFile.toPath());
		     //Creation de lobj decompressor qui transforme le stringCompressed en la version originale
		     Decompressor decompressor = new Decompressor(compressedBytes);
		     File decompressedFile = new File("expanded.txt");
		     //sort le text decompresser pour le sauvegarder dans un nouveau .txt
		     java.nio.file.Files.write(decompressedFile.toPath(), decompressor.decodedText.getBytes());
		     endTime = System.currentTimeMillis();
			 time = String.valueOf(endTime-startTime);			
			 System.out.println("temps2= "+ time + "ms" );
		     System.out.println("complete");
	      }

	     
	}

	
	/**
	 * 
	 * @param wordCount une Map<Byte, Integer> contenant des byte et son nombres d'occurence
	 * @return List<Byte> une liste de Byte tri� en ordre d�croissant du nombre d'occurence
	 * 
	 * r�f�rence:
	 * http://stackoverflow.com/questions/10158793/sorting-words-in-order-of-frequency-least-to-greatest
	 */
	public static List<Character> getWordInDescendingFreqOrder(Map<Character, Integer> wordCount) {
				
	    // Convert map to list of <String,Integer> entries
	    List<Map.Entry<Character, Integer>> list = 
	        new ArrayList< Map.Entry<Character, Integer> >( wordCount.entrySet() );

	    // Sort list by integer values
	    Collections.sort(list, new Comparator<Map.Entry<Character, Integer>>() {
	        public int compare(Map.Entry<Character, Integer> o1, Map.Entry<Character, Integer> o2) {
	            // compare o2 to o1, instead of o1 to o2, to get descending freq. order
	            return (o2.getValue()).compareTo(o1.getValue());
	        }
	    });

	    // Populate the result into a list
	    List<Character> result = new ArrayList<Character>();
	    for (Map.Entry<Character, Integer> entry : list) {
	        result.add(entry.getKey());   	
	    	
	    }
	    return result;
	}
	
}
