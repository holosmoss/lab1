package lab1;

import java.util.Enumeration;
import java.util.Hashtable;

public class Decompressor {

	public String decodedText;
	private Hashtable <Byte,String>decodingBitMap = new Hashtable<Byte,String>();
	private String bitText;
	private String header="";
	private ArbreBinaire decodeTree;
	private Hashtable<String,Character> decodingTable = new Hashtable<String,Character>();
	private String lengthStr = "";
	private Integer decodedRealLength;

		
		public Decompressor(byte[] compressedData) {
			
			//garde en m�moire les valeurs re�u en param�tre
			//�a �vite de les passer en param�tre par la suite
//			this.compressedData = compressedData;
//			this.encodingTable = encodingTable;
			//TODO avoir un class decompressor qui va creer un nouvel arbre

			//cr�er une table des possiblit�s binaire d'un octet pour le d�codage
			buildDecodingBitMap();
			
			
			
			//d�code l'array de Byte en une chaine de bit (bitText)
			decodeByteToStringBit(compressedData);

			this.decodeTree = new ArbreBinaire(header);
			
			//TODO a remplir avec ce que le header vas nous donner
			//cr�er la table de d�codage (decodingTable)
			DecodingTable();
			
			
			//d�code la chaine de bit (decodedText)
			BitToChar();
			
			//retourne le text d�compress� et d�cod�, et supprime les 0 ajout�s
			//lors de l'encodage
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
			
//			
//			//la limite interne du header est  :
			int headerBorne1 = bitText.indexOf("00111010");
			//la limite finale du header  est  _
			int headerBorne2 = bitText.indexOf("01011111")+8;
			String headerBits = bitText.substring(0, headerBorne1);
			
			
			for(int i=0;i<headerBits.length();i+=8 ){
				//cration du string decoder du header
				int charCode = Integer.parseInt(headerBits.substring(i, i+8), 2);
				this.header += new Character((char)charCode).toString();
			}
			
			String lenghtBits = bitText.substring(headerBorne1+8, headerBorne2-8);
			
			for(int i=0;i<lenghtBits.length();i+=8 ){
				//cration du string decoder du header
				int charCode = Integer.parseInt(lenghtBits.substring(i, i+8), 2);
				this.lengthStr += new Character((char)charCode).toString();
			}

//			int headerBorne2 =  tempUTFString.indexOf("_", headerBorne+1);
//			String lengthStr = bitText.substring(headerBorne1, headerBorne2);
			this.decodedRealLength = Integer.valueOf(lengthStr);
			bitText = bitText.substring(headerBorne2);
			bitText = bitText.substring(0,decodedRealLength );
//			System.out.println(headerBorne2);
//			
//			
			System.out.println("++++++++");
			System.out.println(decodedRealLength);
			System.out.println("++++++++");

		}
		
		/**
		 *  Fonction qui interverti la table d'encodage CharBitValueTable
		 *  dans une autre table pour le d�codage. Soit <Character, String>
		 *  devient <String, Character>. L'id�e ici est d'avoir une table qui
		 *  retourne un caract�re lorsqu'on fait table.get(String) -> retourne character
		 *  �a sera utile pour le d�codage du string ex: 0101010100101010101000111010111
		 *  en caract�re AABBCCDDEE
		 *  
		 *  r�f�rence : 
		 * http://www.developer.com/java/other/article.php/3603066/
		 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
		 */
		public void DecodingTable(){
			//TODO  cette fonction inverse notre map pour associer char a bits
			Enumeration<Character> enumerator = decodeTree.binaryValues.keys();
			//TODO this has to be built from the new tree made of the header
			while(enumerator.hasMoreElements() ){
				Character nextKey = enumerator.nextElement();
				String nextString = decodeTree.binaryValues.get(nextKey);
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
		    }//end for loop
		  }//end buildDecodingBitMap()
}
