package lab1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main {
	//branche aimeric
	public static void main(String[] args) throws IOException{
		  BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		  //System.out.print("Enter String : ");
	      //ouvrir le fichier positioner dans le dossier du projet avec le nom indiquer dans la console
	      //String s = br.readLine();
	      File file = new File("test.txt");
	      Path path = file.toPath();
	      byte [] b = Files.readAllBytes(path);
	      Map <Byte,Integer> frequence = new HashMap <Byte,Integer>();
	      for(byte c : b){
	    	  	if (frequence.containsKey(c)) {
	    		    Integer prev = frequence.get(c);
	    		    frequence.put(c, prev+1);
	    		}  else {
	    			frequence.put(c, 1);
	    		}
	      }
	      System.out.print(frequence);
	      System.out.print(getWordInDescendingFreqOrder(frequence));
	}
	static List<Byte> getWordInDescendingFreqOrder(Map<Byte, Integer> wordCount) {
		//http://stackoverflow.com/questions/10158793/sorting-words-in-order-of-frequency-least-to-greatest
	    // Convert map to list of <String,Integer> entries
	    List<Map.Entry<Byte, Integer>> list = 
	        new ArrayList<Map.Entry<Byte, Integer>>(wordCount.entrySet());

	    // Sort list by integer values
	    Collections.sort(list, new Comparator<Map.Entry<Byte, Integer>>() {
	        public int compare(Map.Entry<Byte, Integer> o1, Map.Entry<Byte, Integer> o2) {
	            // compare o2 to o1, instead of o1 to o2, to get descending freq. order
	            return (o2.getValue()).compareTo(o1.getValue());
	        }
	    });

	    // Populate the result into a list
	    List<Byte> result = new ArrayList<Byte>();
	    for (Map.Entry<Byte, Integer> entry : list) {
	        result.add(entry.getKey());
	    }
	    return result;
	}
}
