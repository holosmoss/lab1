package lab1;

import java.util.*;

public class ArbreBinaire {
	
	private ArrayList<Node> nodeList;
	
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
		
		//reste à créer les noeuds des feuilles et tout linké ensemble... + codage decodage...
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
	
public void ArbreBinaire11(Map<Byte, Integer> frequence, List<Byte> freqSortedList) {
		
		nodeList = new ArrayList<Node>();
		
		//créer les noeuds de chaque caractère(byte) avec leur fréquence
		for(Byte b : freqSortedList){
			
			Node n = new Node(b.byteValue(),frequence.get(b.byteValue() ));
			nodeList.add(n);
		}
		Byte kk = null;
		int dernier = nodeList.get(nodeList.size() ).getFreqLettre();
		int avDernier = nodeList.get(nodeList.size()-1 ).getFreqLettre();
		Node nouv = new Node(kk,dernier+avDernier);
		nouv.setParent(null);
		if(dernier>avDernier){
			nouv.setNodeDroit(nodeList.get(nodeList.size()-1));
			nouv.setNodeGauche(nodeList.get(nodeList.size()));
		}else{
			nouv.setNodeDroit(nodeList.get(nodeList.size()));
			nouv.setNodeGauche(nodeList.get(nodeList.size()-1));
		}
		
		
		//reste à créer les noeuds des feuilles et tout linké ensemble... + codage decodage...
	}

	
	

}
