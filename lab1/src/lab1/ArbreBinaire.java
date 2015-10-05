package lab1;

import java.util.*;



public class ArbreBinaire {
	
	private TreeSet<Node> nodeList;
	private Node root;
	private Hashtable<String,Byte> tableBinaire = new Hashtable<String,Byte>();
	private String header = "";
	public Hashtable <Character,String> binaryValues = new Hashtable<Character,String>();
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

	/**
	 * Fonction utiliser apres que la longueur du text est connue
	 *
	 * @return le header complet en binaire
	 */
	public String printHeader(){
		header+="::"+outputLength+"__";
		return binaryStringConverter(header);
	}
	/**
	 * utilise pour convertir le header en binaire sans passer par le hashmap huffman
	 * @param txt
	 * @return un string de bits
	 */
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
	/**
	 * Fonction recursive qui vide le TreeSet de Node graduellement en les regroupant en Noeud
	 * 
	 * @param nodeList2
	 * @return root de l<arbre Binaire
	 */
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
	 * Prend le text et retourne la version transformer en string de bit encoded values selon huffman
	 * 
	 * @param text
	 * @return le string de bits selon larbre huffman
	 * 
	 * référence : 
	 * http://www.developer.com/java/other/article.php/3603066/
	 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 */
	private String charToBit(String text){
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
	
	/**
	 * Fonction public servant de handle pour compresser un text selon huffman
	 * Doit etre appeler seulement apres  arbreBin.tableBinaire();
	 * 
	 * @param text
	 * @return byte[] des valeurs binaire compresser pretes a etre mise dans la file
	 * 
	 * référence : 
	 * http://www.developer.com/java/other/article.php/3603066/
	 * Understanding-the-Huffman-Data-Compression-Algorithm-in-Java.htm
	 */
	public byte[] doCompress(String text){
		
		String textEnBits = charToBit(text);
		
		//garde en memoire la longueur du text
		this.outputLength = textEnBits.length();
		//ajoute le header au text
		String fullOutput = printHeader()+textEnBits;
		
		
		/*
		 *Agrandi la String de text maintenant en bit pour quelle
		 *soit de longueur multiple de 8
		 */
		int bitRestant = fullOutput.length()%8;
		for(int i = 0;i < (8 - bitRestant);i++){
			fullOutput += "0";
		}
		/*
		 * Divise la String de text maintenant en bits par échantillon de 8 bits(1octet)
		 * puis match-up l'octet avec la table binaire et ajoute le résultat
		 * dans l'arrayList de byte
		 */
		ArrayList<Byte> compressedData = new ArrayList<Byte>();
		for(int j = 0;j < fullOutput.length();j += 8){
			//prend les 8 premiers caractère de la string
			String tempString  = fullOutput.substring(j,j+8);
			//cherche dans la table binaire
			byte bitsSample = this.tableBinaire.get(tempString);			  
			compressedData.add(bitsSample);			
		}
		
		//convertion de Arraylist a byte[]
		//TODO est-ce necessaire ?
		byte tabByte[] = new byte[compressedData.size()];
		for(int u = 0;u < compressedData.size(); u++)
			tabByte[u] = compressedData.get(u);
		
		
		return tabByte;
	}
	
	

	
	/**
	 * Fonction qui créer une table binaire de tous les possibilités
	 * binaire qu'un octet peux prendre. On s'en servira pour faire un match-up
	 * lors de l'encodage. 
	 * Elle permet de diminuer la longueur de notre string encoder en compressant nos bits en groupe de 8
	 * 
	 * 
	 * référence (copy pasted): 
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

		public int getFreqLettre() {
			return freqLettre;
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
