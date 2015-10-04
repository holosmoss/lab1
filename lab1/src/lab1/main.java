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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main {
	
	public static void main(String[] args) throws IOException{
		
		  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		  //System.out.print("Enter String : ");
	      //ouvrir le fichier positioner dans le dossier du projet avec le nom indiquer dans la console
	      //String s = br.readLine();
	      File file = new File("test.txt");
	      Path path = file.toPath();
	      byte [] b = Files.readAllBytes(path);
	      String wholeString = new String(b, StandardCharsets.UTF_8 );
	      //VIEWING
	      display48(wholeString);
	      System.out.println("--displayRawDataAsBits--------------------------------------------------------");
	      displayRawDataAsBits(wholeString);

	      //hashmap des frequences
	      Map <Character,Integer> frequence = new HashMap <Character,Integer>();
	      for(int i = 0;i < wholeString.length();i++){
	    	  char c = wholeString.charAt(i);
	    	  if (frequence.containsKey(c)) {
	    		    Integer prev = frequence.get(c);
	    		    frequence.put(c, prev+1);
	    		}  else {
	    			frequence.put(c, 1);
	    		}
	      }
//	      
	      //List de lordre des cle de la hashmap
	      List<Character> freqSortedList = getWordInDescendingFreqOrder(frequence);
//	      
	      
	      //construit l'arbre binaire
	      ArbreBinaire arbreBin = new ArbreBinaire(frequence, freqSortedList);
	      arbreBin.tableBinaire();
	      
	      
	      //compressSuperTigh représente nos byte compressé
	      byte[] compressSuperTight = arbreBin.doCompress(wholeString);	      
	      java.nio.file.Files.write(new File("file.txt").toPath(), compressSuperTight);
	      
	      
	      
	      //décompression
	     // arbreBin.decompress(compressSuperTight, encodingTable, wholeString.length() );
	      
	}

	
	/**
	 * Petit fonction pour afficher de très grande string sur plusieurs ligne
	 * référence : 
	 * http://www.developer.com/java/other/article.php/3603066/
	 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 */
	public static void display48(String data){
		 for(int cnt = 0;cnt < data.length();cnt += 48){
		   if((cnt + 48) < data.length()){
		     //Display 48 characters.
		     System.out.println(data.substring(cnt,cnt+48));
		   }else{
		     //Display the final line, which may be short.
		     System.out.println(data.substring(cnt));
		   }//end else
		 }//end for loop
		}
	
	/**
	 * 
	 * @param wordCount une Map<Byte, Integer> contenant des byte et son nombres d'occurence
	 * @return List<Byte> une liste de Byte trié en ordre décroissant du nombre d'occurence
	 * 
	 * référence:
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
	

	 //-----------------------------------------------------//
	  
	  //This method displays a message string as a series of
	  // characters each having a value of 1 or 0.
	 private static void displayRawDataAsBits(String rawData){
	    for(int cnt = 0,charCnt = 0;cnt < rawData.length();
	                                          cnt++,charCnt++){
	      char theCharacter = rawData.charAt(cnt);
	      String binaryString = Integer.toBinaryString(
	                                             theCharacter);
	      //Append leading zeros as necessary to show eight
	      // bits per character.
	      while(binaryString.length() < 8){
	        binaryString = "0" + binaryString;
	      }//end while loop
	      if(charCnt%6 == 0){
	        //Display 48 bits per line.
	        charCnt = 0;
	        System.out.println();//new line
	      }//end if
	      System.out.print(binaryString);
	    }//end for loop
	    System.out.println();
	  }//end displayRawDataAsBits
	  //-----------------------------------------------------//
	
}
