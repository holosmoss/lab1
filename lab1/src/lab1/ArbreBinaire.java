package lab1;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.*;


public class ArbreBinaire {
	
	private TreeSet<Node> nodeList;
	private Node root;
	private Hashtable <String,Byte> tableBinaire = new Hashtable<String,Byte>();
	private ArrayList<Byte> compressedData = new ArrayList<Byte>();
	
	/**
	 * Constructeur qui cr�er l'abre binaire pour l'encodage
	 * 
	 * @param frequence hashMap contenant les bytes et leur fr�quence
	 * @param freqSortedList arrayList contenant les bytes tri�s
	 */
	public ArbreBinaire(Map<Byte, Integer> frequence, List<Byte> freqSortedList) {
		
		nodeList = new TreeSet<Node>();
		
		//cr�er les noeuds de chaque caract�re(byte) avec leur fr�quence
		for(Byte b : freqSortedList){
			Node n = new Node(b.byteValue(),frequence.get(b.byteValue() ));
			nodeList.add(n);			
		}
		
		root = creerArbre(nodeList);
		binaryNames(root);
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
			
			//ajoute le nouveau noeud � la fin
			nodeList2.add(newNode);
		}
		
		//return le noeud/racine de l'arbre
		return nodeList2.first();
		
	}
	
	/**
	 * Fonction r�cursive qui nomme la chaine de caract�re 
	 * binaryValue de chaque noeud de l'arbre binaire.
	 * la chaine correspond au chemin pour retrouver le caract�re
	 * dans l'arbre exemple a = 010
	 * @param node - la node initial doit �tre la racine de l'arbre 
	 * 
	 */
	private void binaryNames(Node node){
			
			String parentBVal = node.binaryValue;
			//assigne la racine de l'arbre � 0
			if( node.binaryValue.equals("") ){
				node.binaryValue = "root";
				
			}
			Node kidDroit = node.getNodeDroit();
			Node kidGauche = node.getNodeGauche();
			
			if(kidDroit != null){
				kidDroit.binaryValue = parentBVal+"1";				
				
				if(!kidDroit.isLeaf){
					binaryNames(kidDroit);
				}
			}
			if(kidGauche != null){
				kidGauche.binaryValue = parentBVal+"0";
				if(!kidGauche.isLeaf){
					binaryNames(kidGauche);
				}
			}
			
		}

	/**
	 * Remplace les caract�res du fichier texte dans un String par 
	 * leur valeur binaire respective dans l'arbre puis 
	 * retourne la String encod�...
	 * 
	 */
	private String charToBit(Node node, String text){
		Node kidDroit = node.getNodeDroit();
		Node kidGauche = node.getNodeGauche();
		String tmpText = text;
//		si  l'enfant droit est une feuille
		if(kidDroit != null && kidDroit.isLeaf)
//			remplace tout les caract�res comme la lettre de la feuille par sa valeur binaire
//			garde le texte modifi� dans le tempText
			tmpText = tmpText.replaceAll(String.valueOf(byteToChar(kidDroit.lettre) ),
							kidDroit.binaryValue);
//		si  l'enfant gauche est une feuille
		if(kidGauche != null && kidGauche.isLeaf)
//			utilise le tempText  et continue de remplacer tout les caract�res comme la lettre de la feuille par sa valeur binaire
//			garde le texte modifi� dans le tempText
			tmpText = tmpText.replaceAll(String.valueOf(byteToChar(kidGauche.lettre) ),
					kidGauche.binaryValue);
		
//		si l'enfant droit est un noeud 		
		if(kidDroit != null && !kidDroit.isLeaf)
//			on lui dit de compresser r�cursivement de son bord en sauvant encore dans temp
			tmpText = charToBit(kidDroit,tmpText);

//		si l'enfant gauche est un noeud 
		if(kidGauche != null && !kidGauche.isLeaf)
//			on lui dit de compresser r�cursivement de son bord en sauvant encore dans temp
			tmpText = charToBit(kidGauche,tmpText);
		
		
		
		return tmpText;
	}
	
	public ArrayList<Byte> doCompress(String text){
		
		String textEnBits = charToBit(this.root, text);
		System.out.println(textEnBits);
		
		/*
		 *Agrandi la String de text maintenant en bit pour quelle
		 *soit de longueur multiple de 8
		 */
		int bitRestant = textEnBits.length()%8;
		for(int i = 0;i < (8 - bitRestant);i++){
			textEnBits += "0";
		}
		//System.out.println(textEnBits);
		display48(textEnBits);
		
		/*
		 * Divise la String de text maintenant en bits par �chantillon de 8 bits(1octet)
		 * puis match-up l'octet avec la table binaire et ajoute le r�sultat
		 * dans l'arrayList de byte
		 */
		for(int j = 0;j < textEnBits.length();j += 8){
			//prend les 8 premiers caract�re de la string
			String tempString  = textEnBits.substring(j,j+8);
			//cherche dans la table binaire
			byte bitsSample = this.tableBinaire.get(tempString);
			  
			compressedData.add(bitsSample);
		}		
		
		
		return this.compressedData;
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
	
	
	public char byteToChar(byte b){ 
		byte ba[] = {b};
		StringBuilder buffer = new StringBuilder();
	    buffer.append((char)ba[0]); 
        return buffer.charAt(0);
	}
	
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
	 * Fonction qui cr�er une table binaire de tous les possibilit�s
	 * binaire qu'un octet peux prendre. On s'en servira pour faire un match-up
	 * lors de l'encodage
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
	 * Classe interne pour cr�er des noeuds dans l'arbre binaire
	 * @author Joel
	 *
	 */
	private class Node implements Comparable<Node>{
		
		private String binaryValue = ""; // ex 111 pour la lettre totalement � droite
		private boolean isLeaf; // s'il s'agit d'un noeud dit "feuille" de l'arbre binaire
		private Node nodeDroit = null ; // lien vers prochain noeuf a sa droite
		private Node nodeGauche = null; // lien vers prochain noeuf a sa gauche
		private byte lettre; // la lettre en byte que contient le noeud
		private int freqLettre; // frequence de cette lettre
		private Node parent; // valeur du noeud parent � ce noeud
		
		//constructeur
		public Node(byte byteValue, int frequence) {
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
		public Byte getLettre() {
			return lettre;
		}
		public void setLettre(Byte lettre) {
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
			
			return ("lettre: "+byteToChar(this.lettre) +
					" freq: " +this.freqLettre +
					" "+ this.binaryValue);
		}
		//adaptation de compareTo pour satisfaire les besoin de notre arbre (comparaison par fr�quence)
		public int compareTo(Node otherNode){
		    if (freqLettre == otherNode.freqLettre)
		    	//test al�atoire pour r�soudre les �galit�s
		    	return (hashCode() - otherNode.hashCode());
		    else
		    	//retourne negatif si notre fr�quence est moindre que l'autre comparable
		    	return freqLettre - otherNode.freqLettre;
		}

		

	}

}
