package lab1;

import java.util.Enumeration;
import java.util.Hashtable;

public class Decompressor {

	public String decodedText;
	private Hashtable <Byte,String>decodingBitMap = new Hashtable<Byte,String>();
	private String bitText;
	private String header="";
	private Hashtable<String,Character> decodingTable = new Hashtable<String,Character>();
	private String lengthStr = "";
	private Integer decodedRealLength;
	private Hashtable <Character,String> binaryValues = new Hashtable<Character,String>();

		
		public Decompressor(byte[] compressedData) {

			//cr�er une table des possiblit�s binaire d'un octet pour le d�codage
			buildDecodingBitMap();
			
			//d�code l'array de Byte en une chaine de bit (bitText)
			//soccupe aussi du header pour la suite
			decodeByteToStringBit(compressedData);
			
			//creation de la map de binaryValues par char selon le header
			//separation de la chaine de char du header en symbole de valeur allant dans notre Hashmap : binaryValues 
			String[] nodeStrings = header.split(",!");
			for (String n : nodeStrings){
				//nodeValues[0].charAt(0) est la valeur du char  et [1] le bit string qui la remplace
				String[] nodeValues = n.split("-!");
				this.binaryValues.put(nodeValues[0].charAt(0), nodeValues[1]);
				
			}
//			
//			//cr�er la table de d�codage (decodingTable)
			DecodingTable();
//			
//			
//			//d�code la chaine de bit (decodedText)
			BitToChar();
		}
	
		/**
		 * Fonction qui d�code chaque byte du arrayList<Byte> 
		 * et construit une String de 0 et de 1
		 * 
		 * r�f�rence : 
		 * http://www.developer.com/java/other/article.php/3603066/
		 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
		 */
		public void decodeByteToStringBit(byte[] compressedBytes){
			StringBuffer strBuffer = new StringBuffer();

			//fait correspondre chaque byte du arrayList � la table binaire
			//pour faire un match-up
			for(Byte b : compressedBytes){
				byte byteTmp = b;
				strBuffer.append(this.decodingBitMap.get(byteTmp));
			}
			//on est encore en binaire
			this.bitText = strBuffer.toString();
			
			//On cherche les borne du header pour le separer du text
			//la limite interne du header est  ::
			int headerBorne1 = bitText.indexOf("0011101000111010");
			//la limite finale du header  est  __
			int headerBorne2 = bitText.indexOf("0101111101011111")+16;
			String headerBits = bitText.substring(0, headerBorne1);
			
			for(int i=0;i<headerBits.length();i+=8 ){
				//cration du string decoder du header
				int charCode = Integer.parseInt(headerBits.substring(i, i+8), 2);
				this.header += new Character((char)charCode).toString();
			}
			
			//separation pour la longueur du text original
			String lenghtBits = bitText.substring(headerBorne1+16, headerBorne2-16);

			for(int i=0;i<lenghtBits.length();i+=8 ){
				//cration du string decoder du header
				 int charCode = Integer.parseInt(lenghtBits.substring(i, i+8), 2);
				this.lengthStr += new Character((char)charCode).toString();
			}
			
			this.decodedRealLength = Integer.valueOf(lengthStr);
			//decoupage du text de bit pour garder que le message
			bitText = bitText.substring(headerBorne2);
			bitText = bitText.substring(0,decodedRealLength);

		}
		
		/**
		 *  Fonction qui interverti la table d'encodage CharBitValueTable
		 *  dans une autre table pour le d�codage. Soit <Character, String>
		 *  devient <String, Character>. L'id�e ici est d'avoir une table qui
		 *  retourne un caract�re lorsqu'on fait table.get(String) -> retourne character
		 *  �a sera utile pour le d�codage du string ex: 0101010100101010101000111010111
		 *  en caract�re AABBCCDDEE
		 *  
		 *  r�f�rence (copy pasted): 
		 * http://www.developer.com/java/other/article.php/3603066/
		 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
		 */
		public void DecodingTable(){
			Enumeration<Character> enumerator = binaryValues.keys();
			while(enumerator.hasMoreElements() ){
				Character nextKey = enumerator.nextElement();
				String nextString = binaryValues.get(nextKey);
				decodingTable.put(nextString,nextKey);
			}
		}
		
		/**
		 * On utilise deux buffer de string, un pour construire l'output 
		 * qui sera notre text fully decompress�, et l'autre comme tampon.
		 * On ajoute dans le buffer tampon le caract�re en bit � la postion
		 * de l'iterateur de la boucle for puis on v�rifie si ce dernier
		 * ce retrouve dans la table de d�codage, si oui, on l'ajoute
		 * � l'output
		 * 
		 * r�f�rence : 
		 * http://www.developer.com/java/other/article.php/3603066/
		 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
		 */
		public void BitToChar(){
			
			StringBuffer output = new StringBuffer();
			StringBuffer strBuffer = new StringBuffer();

			for(int i = 0; i < this.bitText.length(); i++){
				
				strBuffer.append( this.bitText.charAt(i) );
				
				if(decodingTable.containsKey( strBuffer.toString() ) ){
			    	output.append( decodingTable.get( strBuffer.toString() ) );
			    	strBuffer = new StringBuffer();
				}
			}
			 
			this.decodedText = output.toString();
		}
		/**
		 * Fonction qui cr�er une table binaire de tous les possibilit�s
		 * binaire qu'un octet peux prendre. On s'en servira pour faire un match-up
		 * lors de la decompression. 
		 * 
		 * 
		 * r�f�rence (copy pasted): 
		 * http://www.developer.com/java/other/article.php/3603066/
		 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
		 */
		private void buildDecodingBitMap(){
		    for(int cnt = 0; cnt <= 255;cnt++){
		      StringBuffer workingBuf = new StringBuffer();
		      if((cnt & 128) > 0){workingBuf.append("1");
		        }else {workingBuf.append("0");};
		      if((cnt & 64) > 0){workingBuf.append("1");
		        }else {workingBuf.append("0");};
		      if((cnt & 32) > 0){workingBuf.append("1");
		        }else {workingBuf.append("0");};
		      if((cnt & 16) > 0){workingBuf.append("1");
		        }else {workingBuf.append("0");};
		      if((cnt & 8) > 0){workingBuf.append("1");
		        }else {workingBuf.append("0");};
		      if((cnt & 4) > 0){workingBuf.append("1");
		        }else {workingBuf.append("0");};
		      if((cnt & 2) > 0){workingBuf.append("1");
		        }else {workingBuf.append("0");};
		      if((cnt & 1) > 0){workingBuf.append("1");
		        }else {workingBuf.append("0");};
		      decodingBitMap.put((byte)(cnt),workingBuf.
		                                               toString());
		    }
		  }
}
