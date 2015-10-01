package lab1;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.*;

public class ArbreBinaire {
	
	private ArrayList<Node> nodeList;
	private Node root;
	
	/**
	 * Constructeur qui créer l'abre binaire pour l'encodage
	 * 
	 * @param frequence hashMap contenant les bytes et leur fréquence
	 * @param freqSortedList arrayList contenant les bytes triés
	 */
	public ArbreBinaire(Map<Byte, Integer> frequence, List<Byte> freqSortedList) {
		
		nodeList = new ArrayList<Node>();
		
		//créer les noeuds de chaque caractère(byte) avec leur fréquence
		for(Byte b : freqSortedList){
			Node n = new Node(b.byteValue(),frequence.get(b.byteValue() ));
			nodeList.add(n);			
		}
		
		root = creerArbre(nodeList);
		binaryNames(root);
		
//		System.out.println(root.toString() );
//		System.out.println(root.getNodeDroit().toString() );
//		System.out.println(root.getNodeGauche().toString() );
//		System.out.println(root.getNodeDroit().getNodeDroit().toString() );
//		System.out.println(root.getNodeDroit().getNodeGauche().toString() );
	}

	
	
	public Node creerArbre(ArrayList<Node> nodeList2) {
		
		while(nodeList2.size() > 1){
			
			//enlève les deux noeuds de la fin
			Node tmpNode1 = nodeList2.remove( nodeList2.size() -1);
			Node tmpNode2 = nodeList2.remove( nodeList2.size() -1);
			
			int newFreq = tmpNode1.getFreqLettre() + tmpNode2.getFreqLettre();
			
			//combine les deux noeuds dans un nouveau noeuds
			Node newNode = new Node(newFreq);
			
			if( tmpNode1.getFreqLettre() < tmpNode2.getFreqLettre() ){
				newNode.setNodeDroit(tmpNode1);
				newNode.setNodeGauche(tmpNode2);
			}
			else{
				newNode.setNodeDroit(tmpNode2);
				newNode.setNodeGauche(tmpNode1);
			}			
			tmpNode1.setParent(newNode);
			tmpNode2.setParent(newNode);
			
			//ajoute le nouveau noeud à la fin
			nodeList2.add(newNode);
		}
		
		//return le noeud/racine de l'arbre
		return nodeList2.get(0);
		
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

	private String compress(Node node, String text){
		Node kidDroit = node.getNodeDroit();
		Node kidGauche = node.getNodeGauche();
		String tmpText = text;
		
		if(kidDroit != null && kidDroit.isLeaf)
			tmpText = tmpText.replaceAll(String.valueOf(byteToChar(kidDroit.lettre) ),
							kidDroit.binaryValue);
		

		if(kidGauche != null && kidGauche.isLeaf)
			tmpText = tmpText.replaceAll(String.valueOf(byteToChar(kidGauche.lettre) ),
					kidGauche.binaryValue);
		
		if(kidDroit != null && !kidDroit.isLeaf)
			tmpText = compress(kidDroit,tmpText);
		
		if(kidGauche != null && !kidGauche.isLeaf)
			tmpText = compress(kidGauche,tmpText);
		
		

		return tmpText;
	}
	
	public String doCompress(String text){
		return compress(this.root, text);
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

	public ArrayList<Node> getNodeList() {
		return nodeList;
	}
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public char byteToChar(byte b){ 
		byte ba[] = {b};
		StringBuilder buffer = new StringBuilder();
	    buffer.append((char)ba[0]); 
        return buffer.charAt(0);
	}
	
	
	/**
	 * Classe interne pour créer des noeuds dans l'arbre binaire
	 * @author Joel
	 *
	 */
	private class Node {
		
		private String binaryValue = ""; // ex 111 pour la lettre totalement à droite
		private boolean isLeaf; // s'il s'agit d'un noeud dit "feuille" de l'arbre binaire
		private Node nodeDroit = null ; // lien vers prochain noeuf a sa droite
		private Node nodeGauche = null; // lien vers prochain noeuf a sa gauche
		private byte lettre; // la lettre en byte que contient le noeud
		private int freqLettre; // frequence de cette lettre
		private Node parent; // valeur du noeud parent à ce noeud
		
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
		
		

	}

}
