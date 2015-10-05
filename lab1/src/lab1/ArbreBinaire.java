package lab1;

import java.util.*;



public class ArbreBinaire {
	
	private TreeSet<Node> nodeList;
	private Node root;
	private Hashtable<String,Byte> tableBinaire = new Hashtable<String,Byte>();
	private ArrayList<Byte> compressedData = new ArrayList<Byte>();
	private String bitText;
	private String decodedText;
	private String header = "";
	public Hashtable <Character,String> binaryValues = new Hashtable<Character,String>();
	private Hashtable<String,Character> decodingTable = new Hashtable<String,Character>();
	private Hashtable <Byte,String>decodingBitMap = new Hashtable<Byte,String>();
	private int outputLength;
	/**
	 * Constructeur qui créer l'abre binaire pour l'encodage
	 * 
	 * @param frequence hashMap contenant les bytes et leur fréquence
	 * @param freqSortedList arrayList contenant les bytes triés
	 */
	public ArbreBinaire(Map<Character, Integer> frequence, List<Character> freqSortedList) {
		
		nodeList = new TreeSet<Node>();
		
		//créer les noeuds de chaque caractère(byte) avec leur fréquence
		for(char b : freqSortedList){
			Node n = new Node(b,frequence.get(b));
			nodeList.add(n);	
		}
		
		root = creerArbre(nodeList);
		binaryNames(root);
	}


	public String printHeader(){
		//on doit avoir suffisament d<info pour recreer larbre
		//char+freq de chq node
		//_ comme borne du header
		header+="::"+outputLength+"__";
		//return header;
		System.out.println("--->"+header);
		return binaryStringConverter(header);
	}
	private String binaryStringConverter(String txt){
		//http://stackoverflow.com/questions/917163/convert-a-string-like-testing123-to-binary-in-java
		byte[] bytes = txt.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes){
			int val = b;
			for (int i = 0; i < 8; i++){
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}
		return binary.toString();
	}
	public Node creerArbre(TreeSet<Node> nodeList2) {
		
		while(nodeList2.size() > 1){
			
			//on retire les 2 premier node 
			Node left = nodeList2.first();
			nodeList2.remove(left);
			Node right = nodeList2.first();
		    nodeList2.remove(right);		
			
			int newFreq = left.getFreqLettre() + right.getFreqLettre();
			
			//combine les deux node dans un nouveau noeuds
			Node newNode = new Node(newFreq);
			
			//on assigne les node comme enfant au noeud
			newNode.setNodeDroit(right);
			newNode.setNodeGauche(left);
			
			//est-ce vraiment necessaire ?
			left.setParent(newNode);
			right.setParent(newNode);
			
			//ajoute le nouveau noeud à la fin
			nodeList2.add(newNode);
		}
		
		//return le noeud/racine de l'arbre
		return nodeList2.first();
		
	}
	
	/**
	 * Fonction récursive qui nomme la chaine de caractère 
	 * binaryValue de chaque noeud de l'arbre binaire.
	 * la chaine correspond au chemin pour retrouver le caractère
	 * dans l'arbre exemple a = 010
	 * @param node - la node initial doit être la racine de l'arbre 
	 * 
	 */
	private void binaryNames(Node node){
			
			String parentBVal = node.binaryValue;
			//assigne la racine de l'arbre à 0
			if( node.binaryValue.equals("") ){
				node.binaryValue = "root";
				
			}
			Node kidDroit = node.getNodeDroit();
			Node kidGauche = node.getNodeGauche();
			
			if(kidDroit != null){
				kidDroit.binaryValue = parentBVal+"1";				
				
				if(kidDroit.isLeaf){
					binaryValues.put(kidDroit.getLettre(),kidDroit.binaryValue);
					header += kidDroit.getLettre()+"-!"+kidDroit.binaryValue+",!";
				}else{
					binaryNames(kidDroit);
				}
			}
			if(kidGauche != null){
				kidGauche.binaryValue = parentBVal+"0";
				if(kidGauche.isLeaf){
					binaryValues.put(kidGauche.getLettre(),kidGauche.binaryValue);
					header += kidGauche.getLettre()+"-!"+kidGauche.binaryValue+",!";
				}else{
					binaryNames(kidGauche);
				}
			}
			
		}

	/**
	 * Remplace les caractères du fichier texte dans un String par 
	 * leur valeur binaire respective dans l'arbre puis 
	 * retourne la String encodé...
	 * 
	 * référence : 
	 * http://www.developer.com/java/other/article.php/3603066/
	 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 */
	private String charToBit(Node node, String text){
		//TODO comment
		StringBuffer strBuffer = new StringBuffer();
		
	    for(int i = 0;i < text.length();i++){
	    	if(binaryValues.containsKey(text.charAt(i))){
	    		strBuffer.append(binaryValues.get(text.charAt(i)));
	    	}else{
	    		System.out.println("impossible values : "+text.charAt(i));
	    	}
	    }

		return strBuffer.toString();
	}
	
	
public byte[] doCompress(String text){
		
		String textEnBits = charToBit(this.root, text);
		System.out.println("--------textEnBits--------------------------------------------------");
		display48(textEnBits);
		
		//ajouter le header en bits au textEnbits
		//add length of orginal text in binary
		//TODO use the header to recreate the tree
		this.outputLength = textEnBits.length();
		String fullOutput = printHeader()+textEnBits;
		//String fullOutput = textEnBits;
		
		/*
		 *Agrandi la String de text maintenant en bit pour quelle
		 *soit de longueur multiple de 8
		 */
		int bitRestant = fullOutput.length()%8;
		for(int i = 0;i < (8 - bitRestant);i++){
			fullOutput += "0";
		}
		System.out.println("--------FullOutput--------------------------------------------------");
		display48(fullOutput);
		
		/*
		 * Divise la String de text maintenant en bits par échantillon de 8 bits(1octet)
		 * puis match-up l'octet avec la table binaire et ajoute le résultat
		 * dans l'arrayList de byte
		 */
		for(int j = 0;j < fullOutput.length();j += 8){
			//prend les 8 premiers caractère de la string
			String tempString  = fullOutput.substring(j,j+8);
			//cherche dans la table binaire
			byte bitsSample = this.tableBinaire.get(tempString);			  
			compressedData.add(bitsSample);			
		}
		
		byte tabByte[] = new byte[compressedData.size()];
		for(int u = 0;u < compressedData.size(); u++)
			tabByte[u] = compressedData.get(u);
		
		
		return tabByte;
	}

	/**
	 * petit fonction pour faire afficher la list de noeud...
	 * @param list - arrayList<Node>
	 * @return String 
	 */
	public String printNodeList(ArrayList<Node> list){
		
		String result = "";
		if(list != null){
			for(int i=0; i<list.size(); i++){
				result += " | lettre en byte: "+ list.get(i).getLettre() + 
						  " - freq: "+ list.get(i).getFreqLettre();
			}			
			
		}
		else{
			result = "liste vide";
		}
		return result;
	}

	public TreeSet<Node> getNodeList() {
		return nodeList;
	}
	
	
	/**
	 * Petit fonction pour afficher de très grande string sur plusieurs ligne
	 * référence : 
	 * http://www.developer.com/java/other/article.php/3603066/
	 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 */
	public void display48(String data){
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
	 * Fonction qui créer une table binaire de tous les possibilités
	 * binaire qu'un octet peux prendre. On s'en servira pour faire un match-up
	 * lors de l'encodage
	 * 
	 * référence : 
	 * http://www.developer.com/java/other/article.php/3603066/
	 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 */
	public	void tableBinaire(){

		for(int i = 0; i <= 255;i++){
			StringBuffer strBuffer = new StringBuffer();
			if((i & 128) > 0)   
				strBuffer.append("1");   
			else
				strBuffer.append("0");
			   
			if((i & 64) > 0)
				strBuffer.append("1");
			else
				strBuffer.append("0");
			   
			if((i & 32) > 0)
				strBuffer.append("1");
			else
				strBuffer.append("0");
			   
			if((i & 16) > 0)
				strBuffer.append("1");
			else
				strBuffer.append("0");
			   
			if((i & 8) > 0)
				strBuffer.append("1");
			else 
				strBuffer.append("0");
			   
			if((i & 4) > 0)
				strBuffer.append("1");
			else 
				strBuffer.append("0");
			   
			if((i & 2) > 0)
				strBuffer.append("1");
			else
				strBuffer.append("0");
			   
			if((i & 1) > 0)
				strBuffer.append("1");
			else
				strBuffer.append("0");
			   
			this.tableBinaire.put(strBuffer.toString(),(byte)(i));		   
		 }
	}
	
	// ==========================================================
	// décodage
	//
	
	/**
	 * Fonction qui décompress un arrayList<Byte>
	 * selon les données encodé de l'arbre binaire dans CharBitValueTable
	 * la taille du fichier text original est nécéssaire pour enlever
	 * les caractères superflus ajouter lors de la compression.
	 * 
	 * référence : 
	 * http://www.developer.com/java/other/article.php/3603066/
	 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 * 
	 * @param compressedData - ArrayList<Byte> Le texte du fichier texte compressé
	 * @param CharBitValueTable - Hashtable <Character,String> la table de caractère et leur valeur
	 * 							  binaire dans l'arbre binaire. 
	 * @param tailleFichierText - int - la longueur du texte original
	 * @return une chaine de caractère contenant le text décompressé
	 */
	public String decompress(byte[] compressedData){
		
		//garde en mémoire les valeurs reçu en paramètre
		//ça évite de les passer en paramètre par la suite
//		this.compressedData = compressedData;
//		this.encodingTable = encodingTable;
		//TODO avoir un class decompressor qui va creer un nouvel arbre

		//créer une table des possiblités binaire d'un octet pour le décodage
		buildDecodingBitMap();
		
		
		
		//décode l'array de Byte en une chaine de bit (bitText)
		decodeByteToStringBit(compressedData);
		//TODO prendre le début du bitString pour connaitre le header
			//longueur du header en bit ?
			//le premier 11
		
		
		//TODO a remplir avec ce que le header vas nous donner
		//créer la table de décodage (decodingTable)
		DecodingTable();
		
		
		//décode la chaine de bit (decodedText)
		BitToChar();
		
		//retourne le text décompressé et décodé, et supprime les 0 ajoutés
		//lors de l'encodage
		return decodedText;
		//return this.decodedText.substring(0,tailleFichierText);
	}
	
	/**
	 * Fonction qui décode chaque byte du arrayList<Byte> 
	 * et construit une String de 0 et de 1
	 * 
	 * référence : 
	 * http://www.developer.com/java/other/article.php/3603066/
	 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 */
	public void decodeByteToStringBit(byte[] compressedBytes){
		StringBuffer strBuffer = new StringBuffer();

		//fait correspondre chaque byte du arrayList à la table binaire
		//pour faire un match-up
		for(Byte b : compressedBytes){
			byte byteTmp = b;
			strBuffer.append(this.decodingBitMap.get(byteTmp));
		}
		//on est encore en binaire
		this.bitText = strBuffer.toString();
		
		//TODO here we split the header at first instance of 01011111 
//		
//		//la limite interne du header est  :
		int headerBorne1 = bitText.indexOf("00111010");
		//la limite finale du header  est  _
		int headerBorne2 = bitText.indexOf("01011111")+8;
		String headerBits = bitText.substring(0, headerBorne1);
		String header="";
		for(int i=0;i<headerBits.length();i+=8 ){
			//cration du string decoder du header
			int charCode = Integer.parseInt(headerBits.substring(i, i+8), 2);
			header += new Character((char)charCode).toString();
		}

//		int headerBorne2 =  tempUTFString.indexOf("_", headerBorne+1);
//		String lengthStr = bitText.substring(headerBorne1, headerBorne2);
//		this.decodedRealLength = Integer.valueOf(lengthStr);
		bitText = bitText.substring(headerBorne2);
//		System.out.println(headerBorne2);
//		
//		
		System.out.println("===================");
		System.out.println(bitText);
		System.out.println("===================");
	}
	
	/**
	 *  Fonction qui interverti la table d'encodage CharBitValueTable
	 *  dans une autre table pour le décodage. Soit <Character, String>
	 *  devient <String, Character>. L'idée ici est d'avoir une table qui
	 *  retourne un caractère lorsqu'on fait table.get(String) -> retourne character
	 *  ça sera utile pour le décodage du string ex: 0101010100101010101000111010111
	 *  en caractère AABBCCDDEE
	 *  
	 *  référence : 
	 * http://www.developer.com/java/other/article.php/3603066/
	 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 */
	public void DecodingTable(){
		//TODO  cette fonction inverse notre map pour associer char a bits
		Enumeration<Character> enumerator = binaryValues.keys();
		//TODO this has to be built from the new tree made of the header
		while(enumerator.hasMoreElements() ){
			Character nextKey = enumerator.nextElement();
			String nextString = binaryValues.get(nextKey);
			decodingTable.put(nextString,nextKey);
		}
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
	
	/**
	 * On utilise deux buffer de string, un pour construire l'output 
	 * qui sera notre text fully decompressé, et l'autre comme tampon.
	 * On ajoute dans le buffer tampon le caractère en bit à la postion
	 * de l'iterateur de la boucle for puis on vérifie si ce dernier
	 * ce retrouve dans la table de décodage, si oui, on l'ajoute
	 * à l'output
	 * 
	 * référence : 
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
	 * Classe interne pour créer des noeuds dans l'arbre binaire
	 * @author Joel
	 *
	 */
	private class Node implements Comparable<Node>{
		
		private String binaryValue = ""; // ex 111 pour la lettre totalement à droite
		private boolean isLeaf; // s'il s'agit d'un noeud dit "feuille" de l'arbre binaire
		private Node nodeDroit = null ; // lien vers prochain noeuf a sa droite
		private Node nodeGauche = null; // lien vers prochain noeuf a sa gauche
		private Character lettre; // la lettre en byte que contient le noeud
		private int freqLettre; // frequence de cette lettre
		private Node parent; // valeur du noeud parent à ce noeud
		
		//constructeur
		public Node(Character byteValue, int frequence) {
			this.lettre = byteValue;
			this.freqLettre = frequence;
			this.isLeaf = true;
			
		}
		public Node(int frequence) {
			this.isLeaf = false;
			this.freqLettre = frequence;
			
		}

		public boolean isLeaf() {
			return isLeaf;
		}
		public void setLeaf(boolean isLeaf) {
			this.isLeaf = isLeaf;
		}
		public Node getNodeDroit() {
			return nodeDroit;
		}
		public void setNodeDroit(Node nodeDroit) {
			this.nodeDroit = nodeDroit;
		}
		public Node getNodeGauche() {
			return nodeGauche;
		}
		public void setNodeGauche(Node nodeGauche) {
			this.nodeGauche = nodeGauche;
		}
		public Character getLettre() {
			return lettre;
		}
		public void setLettre(Character lettre) {
			this.lettre = lettre;
		}
		public int getFreqLettre() {
			return freqLettre;
		}
		public void setFreqLettre(int freqLettre) {
			this.freqLettre = freqLettre;
		}
		public Node getParent() {
			return parent;
		}
		public void setParent(Node parent) {
			this.parent = parent;
		}
		
		public String toString(){
			
			return ("lettre: "+this.lettre +
					" freq: " +this.freqLettre +
					" "+ this.binaryValue);
		}
		//adaptation de compareTo pour satisfaire les besoin de notre arbre (comparaison par fréquence)
		public int compareTo(Node otherNode){
		    if (freqLettre == otherNode.freqLettre)
		    	//test aléatoire pour résoudre les égalités
		    	return (hashCode() - otherNode.hashCode());
		    else
		    	//retourne negatif si notre fréquence est moindre que l'autre comparable
		    	return freqLettre - otherNode.freqLettre;
		}

		

	}

}
