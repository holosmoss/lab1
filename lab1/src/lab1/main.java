package lab1;

import java.io.BufferedReader;
import java.io.File;
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
	      //display48(wholeString);
//	      Map <Byte,Integer> frequence = new HashMap <Byte,Integer>();
//	      for(byte c : b){
//	    	  	if (frequence.containsKey(c)) {
//	    		    Integer prev = frequence.get(c);
//	    		    frequence.put(c, prev+1);
//	    		}  else {
//	    			frequence.put(c, 1);
//	    		}
//	      }
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
//	      System.out.println("Les occurences: " + frequence);
	      //List de lordre des cle de la hashmap
	      List<Character> freqSortedList = getWordInDescendingFreqOrder(frequence);
//	      for(int j =0; j<freqSortedList.size();j++ ){	    	  
//	    	  System.out.println(freqSortedList.get(j)+" = "+ byteToChar(freqSortedList.get(j) ) );
//	      }
	      
	      //construit l'arbre binaire
	      ArbreBinaire arbreBin = new ArbreBinaire(frequence, freqSortedList);
	      arbreBin.tableBinaire();
	      //System.out.println( "la liste de noeud: "+ arbreBin.printNodeList( arbreBin.getNodeList() ) );
	      
	      //compressSuperTigh représente nos byte compressé
	      ArrayList<Byte> compressSuperTight = arbreBin.doCompress(wholeString);
	      
	      
	      //TEST pour écrire notre file en binaire (string style)
	     // System.out.println(compressSuperTight);
	      PrintWriter writer = new PrintWriter("compressed.txt", "UTF-8");
	      for(int i =0; i < compressSuperTight.size(); i++){
	    	  writer.print( compressSuperTight.get(i) ); 
	      }	      
	      writer.close();
	      
//	      TODO test this method whit byte[] instead of string
//	      byte dataToWrite[] = //...
//		  FileOutputStream out = new FileOutputStream("the-file-name");
//		  out.write(dataToWrite);
//		  out.close();
	      
	      
	      //décompression
	     // arbreBin.decompress(compressSuperTight, encodingTable, wholeString.length() );
	      
	}
	
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
	 */
	public static List<Character> getWordInDescendingFreqOrder(Map<Character, Integer> wordCount) {
		
		//http://stackoverflow.com/questions/10158793/sorting-words-in-order-of-frequency-least-to-greatest
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
	    List<Character> result = new ArrayList<Character>(); //on devrait populer le résultat dans un autre map pour garder le integer
	    for (Map.Entry<Character, Integer> entry : list) {
	        result.add(entry.getKey());   	
	    	
	    }
	    	    
	    return result;
	    
	}
	
	public static char byteToChar(byte b){ 
		byte ba[] = {b};
		StringBuilder buffer = new StringBuilder();
	    buffer.append((char)ba[0]); 
        return buffer.charAt(0);
	}
	
	
}
