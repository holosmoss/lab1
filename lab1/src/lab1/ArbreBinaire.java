package lab1;

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
		System.out.println(root.toString() );
		System.out.println(root.getNodeDroit().toString() );
		System.out.println(root.getNodeGauche().toString() );
		System.out.println(root.getNodeDroit().getNodeDroit().toString() );
		System.out.println(root.getNodeDroit().getNodeGauche().toString() );
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
	
	
	

}
