import java.awt.Color;
import java.util.ArrayList;


public class Player {

	private String nom;
	private Color couleur;
	private int nbCases;
	private ArrayList<Hexa> casesCtrl;
	private boolean myTurn;
	private boolean isIA;
	
	public Player(String nom, Color couleur, int nbCases, ArrayList<Hexa> casesCtrl, boolean isIA){
		
		this.nom = nom;
		this.couleur = couleur;
		this.nbCases = nbCases;
		this.casesCtrl = casesCtrl;
		this.myTurn = false;
		this.isIA = isIA;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Color getCouleur() {
		return couleur;
	}
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}
	public int getNbCases() {
		return nbCases;
	}
	public void setNbCases(int nbCases) {
		this.nbCases = nbCases;
	}
	public ArrayList<Hexa> getCasesCtrl() {
		return casesCtrl;
	}
	public void setCasesCtrl(ArrayList<Hexa> casesCtrl) {
		this.casesCtrl = casesCtrl;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}

	public boolean isIA() {
		return isIA;
	}

	public void setIA(boolean isIA) {
		this.isIA = isIA;
	}
}
