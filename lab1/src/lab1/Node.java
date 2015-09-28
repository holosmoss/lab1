package lab1;


/**
 * Classe pour créer des noeuds dans l'arbre binaire
 * @author Joel
 *
 */
public class Node {
	
	private int binaryValue; // 1 = noeud à droite du parent, 0 = à gauche...
	private boolean isLeaf; // s'il s'agit d'un noeud dit "feuille" de l'arbre binaire
	private Node nodeDroit; // lien vers prochain noeuf a sa droite
	private Node nodeGauche; // lien vers prochain noeuf a sa gauche
	private Byte lettre; // la lettre en byte que contient le noeud
	private int freqLettre; // frequence de cette lettre
	private Node parent; // valeur du noeud parent à ce noeud
	
	//constructeur
	public Node(byte byteValue, Integer frequence) {
		this.lettre = byteValue;
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
		
		return ("lettre: "+this.lettre.toString() +"freq: " +this.freqLettre);
	}

}
