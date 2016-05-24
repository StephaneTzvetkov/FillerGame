import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Cette classe permet la gestion de tableaux de cellules hexagonales
 */
public class HexaBoard extends Canvas implements Board {
	
	private HexaCell[][] grille;		// grille[i][j] : j represente les lignes, i les �l�ments en colonne de chaque ligne
	private int hauteur;
	private int largeur;
	private Player joueur1;
	private Player joueur2;
	private Player joueur3;
	private Player joueur4;
	
	/*REMARQUE :
	 * Joueur1 : en haut � hauche de la grille
	 * Joueur2 : en bas  � droite de la grille
	 * Joueur3 : en haut � droite de la grille
	 * Joueur4 : en bas  � gaucge de la grille
	 */
	
	/**
	 * Ce constructeur permet la cr�ation d'un tableau en appelant l'initialisation de la grille, la d�finition des voisins des cellules h�xagonales (liaison des cellules entre elles)
	 * et en appelant la d�finition des joueurs li�s au tableau
	 * 
	 * @param nb			: entier repr�sentant la taille d'un c�t� du tableau
	 * @param nomJoueur1	: cha�ne de caract�res repr�sentant le nom du joueur 1
	 * @param nomJoueur2	: cha�ne de caract�res repr�sentant le nom du joueur 2
	 * @param nomJoueur3	: cha�ne de caract�res repr�sentant le nom du joueur 3
	 * @param nomJoueur4	: cha�ne de caract�res repr�sentant le nom du joueur 4
	 * 
	 * @see initialisationGrille(int nb)
	 * @see defVoisins(int nb)
	 * @see defJoueurs(int nb)
	 */
	public HexaBoard(int nb, String nomJoueur1, String nomJoueur2, String nomJoueur3, String nomJoueur4, String IA1, String IA2, String IA3, String IA4) {
		
		int h = 20*nb +60;
		int l = 20*nb +180;
		
		// Settings :
		setBackground (Color.black);
        setSize(h, l);
        
        this.hauteur = h;
        this.largeur = l;
        
        initialisationGrille(nb);
		
		this.grille = defVoisins(this.grille);
		
		defJoueurs(nb, nomJoueur1, nomJoueur2, nomJoueur3, nomJoueur4, IA1, IA2, IA3, IA4);
	}
	
	public HexaBoard(String saveStr){
		
		Scanner scLine = new Scanner(saveStr);
		
		scLine.next();
		int nb = Integer.parseInt(scLine.next());
		scLine.close();
		
		int h = 20*nb +60;
		int l = 20*nb +180;
		
		// Settings :
		setBackground (Color.black);
		setSize(h, l);
		
		this.hauteur = h;
        this.largeur = l;
		
		initialisationGrille(nb, saveStr);
		
		this.grille = defVoisins(this.grille);
		
		defJoueurs(nb, saveStr);
	}
	
	/**
	 * Cette fonction initialise la grille des cellules (ici un pavage d'hexagones) en cr�ant les cellules � leur place et en leur attribuant une couleur al�atoire
	 * 
	 * @param nb	: entier repr�sentant la taille d'un c�t� du tableau
	 */
	private void initialisationGrille(int nb){
		
		int decalageX = 0;
		int margeY = 120;
		
		this.grille = new HexaCell[nb][nb];
		
		for(int i = 0; i < grille.length; i++){
			for(int j = 0; j < grille[0].length; j++){
				
				// ATTENTION il se passe un truc dans les coins : il faut que les 4 coins soient de couleurs diff !
				
				if(j %2 == 0){ decalageX = -10;}
				if(j %2 == 1){ decalageX = 0;}
				
				Color color = Color.black;
				double random = Math.random();
				random *= 6;
				
				if(random < 1){
					color = Color.red;
				}else if(random >= 1 && random < 2){
					color = Color.orange;
				}else if(random >= 2 && random < 3){
					color = Color.yellow;
				}else if(random >= 3 && random < 4){
					color = Color.green;
				}else if(random >= 4 && random < 5){
					color = Color.blue;
				}else if(random >= 5 && random < 6){
					color = Color.magenta;
				}
				
				grille[i][j] = new HexaCell(30+i*20+decalageX, 30+j*20+margeY, color, null, null, null, null, null, null);
			}
		}
	}
	
	public void initialisationGrille(int nb, String saveStr){
		
		int decalageX = 0;
		int margeY = 120;
		
		this.grille = new HexaCell[nb][nb];
		
		Scanner sc = new Scanner(saveStr);
		
		sc.next();
		sc.next();
		
		String[] str =new String[3];
		String colorStr = "";
		Color color = Color.black;
		
		for(int i = 0; i < grille.length; i++){
			for(int j = 0; j < grille[0].length; j++){
				
				if(j %2 == 0){ decalageX = -10;}
				if(j %2 == 1){ decalageX = 0;}
				
				str = sc.next().split("_");
				colorStr = str[2];
				
				if(colorStr.equals("java.awt.Color[r=255,g=0,b=0]"))	{ color = Color.red; 		}
				if(colorStr.equals("java.awt.Color[r=255,g=200,b=0]"))	{ color = Color.orange; 	}
				if(colorStr.equals("java.awt.Color[r=255,g=255,b=0]"))	{ color = Color.yellow; 	}
				if(colorStr.equals("java.awt.Color[r=0,g=255,b=0]"))	{ color = Color.green; 		}
				if(colorStr.equals("java.awt.Color[r=0,g=0,b=255]"))	{ color = Color.blue; 		}
				if(colorStr.equals("java.awt.Color[r=255,g=0,b=255]"))	{ color = Color.magenta;	}
				
				grille[i][j] = new HexaCell(30+i*20+decalageX, 30+j*20+margeY, color, null, null, null, null, null, null);
			}
		}
		
		sc.close();
	}
	
	/**
	 * Cette fonction d�finie les voisins de chaque cellule h�xagonale (liaison des cellules entre elles) en fonction de son positionnement sur la grille
	 * 
	 */
	private HexaCell[][] defVoisins(HexaCell[][] grille){
		
		for(int i = 0; i < grille.length; i++){
			for(int j = 0; j < grille.length; j++){
				
				if(j % 2 == 0){
					try{ grille[i][j].setVoisinDroiteHaut(grille[i][j-1]);	}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinDroiteHaut(null); 	}
					try{ grille[i][j].setVoisinDroite(grille[i+1][j]); 		}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinDroite(null); 		}
					try{ grille[i][j].setVoisinDroiteBas(grille[i][j+1]);	}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinDroiteBas(null); 	}
					try{ grille[i][j].setVoisinGaucheBas(grille[i-1][j+1]);	}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinGaucheBas(null); 	}
					try{ grille[i][j].setVoisinGauche(grille[i-1][j]);		}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinGauche(null); 		}
					try{ grille[i][j].setVoisinGaucheHaut(grille[i-1][j-1]);}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinGaucheHaut(null); 	}
				}
				if(j % 2 == 1){
					try{ grille[i][j].setVoisinDroiteHaut(grille[i+1][j-1]);}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinDroiteHaut(null); 	}
					try{ grille[i][j].setVoisinDroite(grille[i+1][j]);		}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinDroite(null); 		}
					try{ grille[i][j].setVoisinDroiteBas(grille[i+1][j+1]);	}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinDroiteBas(null);	}
					try{ grille[i][j].setVoisinGaucheBas(grille[i][j+1]);	}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinGaucheBas(null); 	}
					try{ grille[i][j].setVoisinGauche(grille[i-1][j]);		}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinGauche(null); 		}
					try{ grille[i][j].setVoisinGaucheHaut(grille[i][j-1]);	}catch(ArrayIndexOutOfBoundsException e){ grille[i][j].setVoisinGaucheHaut(null); 	}
				}
			}
		}
		
		return grille;
	}
	
	/**
	 * Cette fonction d�finie les joueurs li�s au tableau en les cr�ant 
	 * 
	 * @param nomJoueur1	: cha�ne de caract�re repr�sentant le nom du joueur 1
	 * @param nomJoueur2	: cha�ne de caract�re repr�sentant le nom du joueur 2
	 * @param nomJoueur3	: cha�ne de caract�re repr�sentant le nom du joueur 3
	 * @param nomJoueur4	: cha�ne de caract�re repr�sentant le nom du joueur 4
	 * @param nb			: entier repr�sentant la taille d'un c�t� du tableau
	 * 
	 * @see getConnectedSameColorHexas(ArrayList<Hexa> listeHexa)
	 */
	private void defJoueurs( int nb, String nomJoueur1, String nomJoueur2, String nomJoueur3, String nomJoueur4, String IA1, String IA2, String IA3, String IA4){
		
		ArrayList<HexaCell> listeInitialeJ1 = new ArrayList<HexaCell>();
		ArrayList<HexaCell> listeInitialeJ2 = new ArrayList<HexaCell>();
		
		listeInitialeJ1.add(grille[0][0]);			grille[0][0].setCtrlBy(nomJoueur1);
		listeInitialeJ1 = getConnectedCellsOfSameColor(listeInitialeJ1);
		
		this.joueur1 = new Player(nomJoueur1, grille[0][0].getColor(), listeInitialeJ1.size(), listeInitialeJ1, IA1);
		this.joueur1.setMyTurn(true);
		
		listeInitialeJ2.add(grille[nb-1][nb-1]);	grille[nb-1][nb-1].setCtrlBy(nomJoueur2);
		listeInitialeJ2 = getConnectedCellsOfSameColor(listeInitialeJ2);
		
		this.joueur2 = new Player(nomJoueur2, grille[nb-1][nb-1].getColor(), listeInitialeJ2.size(), listeInitialeJ2, IA2);
		
		if(!nomJoueur3.isEmpty()){
			
			ArrayList<HexaCell> listeInitialeJ3 = new ArrayList<HexaCell>();
			
			listeInitialeJ3.add(grille[nb-1][0]);	grille[nb-1][0].setCtrlBy(nomJoueur3);
			listeInitialeJ3 = getConnectedCellsOfSameColor(listeInitialeJ3);
			
			this.joueur3 = new Player(nomJoueur3, grille[nb-1][0].getColor(), listeInitialeJ3.size(), listeInitialeJ3, IA3);
		}
		else{
			this.joueur3 = null;
		}
		
		if(!nomJoueur4.isEmpty()){
			
			ArrayList<HexaCell> listeInitialeJ4 = new ArrayList<HexaCell>();
			
			listeInitialeJ4.add(grille[0][nb-1]);	grille[0][nb-1].setCtrlBy(nomJoueur4);
			listeInitialeJ4 = getConnectedCellsOfSameColor(listeInitialeJ4);
			
			this.joueur4 = new Player(nomJoueur4, grille[0][nb-1].getColor(), listeInitialeJ4.size(), listeInitialeJ4, IA4);
		}
		else{
			this.joueur4 = null;
		}
	}
	
	public void defJoueurs(int nb, String saveStr){
		//TODO
		
		Scanner sc = new Scanner(saveStr);
		
		sc.next(); // Type partie
		sc.next(); // Nb
		for(int i = 0; i < nb; i++){ sc.nextLine(); } // Grille
		
		sc.nextLine();
		sc.nextLine();
		Scanner scLine = new Scanner(sc.nextLine());
		String nomJoueur1 = scLine.next();	System.out.println("NOM JOUEUR 1 : "+nomJoueur1);
		String IA1 = scLine.next(); if(IA1.equals("IA")){ IA1 += " "+scLine.next(); }
		String isJ1TurnStr = scLine.next();
		String colorJ1Str = scLine.next();
		
		Color colorJ1 = null;
		if(colorJ1Str.equals("255_0_0"))	{ colorJ1 = Color.red; 		}
		if(colorJ1Str.equals("255_200_0"))	{ colorJ1 = Color.orange; 	}
		if(colorJ1Str.equals("255_255_0"))	{ colorJ1 = Color.yellow; 	}
		if(colorJ1Str.equals("0_255_0"))	{ colorJ1 = Color.green; 	}
		if(colorJ1Str.equals("0_0_255"))	{ colorJ1 = Color.blue; 	}
		if(colorJ1Str.equals("255_0_255"))	{ colorJ1 = Color.magenta; 	}
		
		ArrayList<HexaCell> listeJ1 = new ArrayList<HexaCell>();
		
		String[] ij = new String[2];
		while(scLine.hasNext()){
			ij = scLine.next().split("_");
			
			int i = Integer.parseInt(ij[0]);
			int j = Integer.parseInt(ij[1]);
			
			this.grille[i][j].setCtrlBy(nomJoueur1);
			listeJ1.add(grille[i][j]);
		}
		
		this.joueur1 = new Player(nomJoueur1, colorJ1, listeJ1.size(), listeJ1, IA1);
		if(isJ1TurnStr.equals("true"))	{ this.joueur1.setMyTurn(true); }
		else							{ this.joueur1.setMyTurn(false);}
		
		scLine.close();
		scLine = new Scanner(sc.nextLine());
		String nomJoueur2 = scLine.next();	System.out.println("NOM JOUEUR 2 : "+nomJoueur2);
		String IA2 = scLine.next(); if(IA2.equals("IA")){ IA2 += " "+scLine.next(); }
		String isJ2TurnStr = scLine.next();
		String colorJ2Str = scLine.next();
		
		Color colorJ2 = null;
		if(colorJ2Str.equals("255_0_0"))	{ colorJ2 = Color.red; 		}
		if(colorJ2Str.equals("255_200_0"))	{ colorJ2 = Color.orange; 	}
		if(colorJ2Str.equals("255_255_0"))	{ colorJ2 = Color.yellow; 	}
		if(colorJ2Str.equals("0_255_0"))	{ colorJ2 = Color.green; 	}
		if(colorJ2Str.equals("0_0_255"))	{ colorJ2 = Color.blue; 	}
		if(colorJ2Str.equals("255_0_255"))	{ colorJ2 = Color.magenta; 	}
		
		ArrayList<HexaCell> listeJ2 = new ArrayList<HexaCell>();
		
		ij = new String[2];
		while(scLine.hasNext()){
			ij = scLine.next().split("_");
			
			int i = Integer.parseInt(ij[0]);
			int j = Integer.parseInt(ij[1]);
			
			this.grille[i][j].setCtrlBy(nomJoueur2);
			listeJ2.add(grille[i][j]);
		}
		
		this.joueur2 = new Player(nomJoueur2, colorJ2, listeJ2.size(), listeJ2, IA2);
		if(isJ2TurnStr.equals("true"))	{ this.joueur2.setMyTurn(true); }
		else							{ this.joueur2.setMyTurn(false);}
		
		scLine.close();
		if(sc.hasNextLine()){
			
			scLine = new Scanner(sc.nextLine());
			String nomJoueur3 = scLine.next();	System.out.println("NOM JOUEUR 3 : "+nomJoueur3);
			String IA3 = scLine.next(); if(IA3.equals("IA")){ IA3 += " "+scLine.next(); }
			String isJ3TurnStr = scLine.next();
			String colorJ3Str = scLine.next();
			
			Color colorJ3 = null;
			if(colorJ3Str.equals("255_0_0"))	{ colorJ3 = Color.red; 		}
			if(colorJ3Str.equals("255_200_0"))	{ colorJ3 = Color.orange; 	}
			if(colorJ3Str.equals("255_255_0"))	{ colorJ3 = Color.yellow; 	}
			if(colorJ3Str.equals("0_255_0"))	{ colorJ3 = Color.green; 	}
			if(colorJ3Str.equals("0_0_255"))	{ colorJ3 = Color.blue; 	}
			if(colorJ3Str.equals("255_0_255"))	{ colorJ3 = Color.magenta; 	}
			
			ArrayList<HexaCell> listeJ3 = new ArrayList<HexaCell>();
			
			ij = new String[2];
			while(scLine.hasNext()){
				ij = scLine.next().split("_");
				
				int i = Integer.parseInt(ij[0]);
				int j = Integer.parseInt(ij[1]);
				
				this.grille[i][j].setCtrlBy(nomJoueur3);
				listeJ3.add(grille[i][j]);
			}
			
			this.joueur3 = new Player(nomJoueur3, colorJ3, listeJ3.size(), listeJ3, IA3);
			if(isJ3TurnStr.equals("true"))	{ this.joueur3.setMyTurn(true); }
			else							{ this.joueur3.setMyTurn(false);}
		}
		scLine.close();
		if(sc.hasNextLine()){
			
			scLine = new Scanner(sc.nextLine());
			String nomJoueur4 = scLine.next();	System.out.println("NOM JOUEUR 4 : "+nomJoueur4);
			String IA4 = scLine.next(); if(IA4.equals("IA")){ IA4 += " "+scLine.next(); }
			String isJ4TurnStr = scLine.next();
			String colorJ4Str = scLine.next();
			
			Color colorJ4 = null;
			if(colorJ4Str.equals("255_0_0"))	{ colorJ4 = Color.red; 		}
			if(colorJ4Str.equals("255_200_0"))	{ colorJ4 = Color.orange; 	}
			if(colorJ4Str.equals("255_255_0"))	{ colorJ4 = Color.yellow; 	}
			if(colorJ4Str.equals("0_255_0"))	{ colorJ4 = Color.green; 	}
			if(colorJ4Str.equals("0_0_255"))	{ colorJ4 = Color.blue; 	}
			if(colorJ4Str.equals("255_0_255"))	{ colorJ4 = Color.magenta; 	}
			
			ArrayList<HexaCell> listeJ4 = new ArrayList<HexaCell>();
			
			ij = new String[2];
			while(scLine.hasNext()){
				ij = scLine.next().split("_");
				
				int i = Integer.parseInt(ij[0]);
				int j = Integer.parseInt(ij[1]);
				
				this.grille[i][j].setCtrlBy(nomJoueur4);
				listeJ4.add(grille[i][j]);
			}
			
			this.joueur4 = new Player(nomJoueur4, colorJ4, listeJ4.size(), listeJ4, IA4);
			if(isJ4TurnStr.equals("true"))	{ this.joueur4.setMyTurn(true); }
			else							{ this.joueur4.setMyTurn(false);}
		}
	}
	
	/**
	 * Cette fonction r�cursive permet d'obtenir, � partir d'une liste initiale de celulles hexagonales, une liste "augment�e" : comprennant en plus leurs voisines de m�me couleur 
	 * 
	 * @param liste		: liste des cellules hexagonales � partir desquels on souhaite obtenir la liste "augment�e" 
	 * @return liste	: liste des cellules hexagonales comprennant la liste de d�part et la liste "augment�e"
	 */
	public static ArrayList<HexaCell> getConnectedCellsOfSameColor(ArrayList<HexaCell> listeIni){
		
		ArrayList<HexaCell> liste = (ArrayList<HexaCell>) listeIni.clone();
		
		boolean add = false;
		
		for(int i = 0; i < liste.size(); i++){
			
			HexaCell hexa = liste.get(i);
			
			if( hexa.getVoisinDroiteHaut() != null && !liste.contains(hexa.getVoisinDroiteHaut()) ){
				if( hexa.getVoisinDroiteHaut().getColor().getRGB() == hexa.getColor().getRGB() && hexa.getVoisinDroiteHaut().getCtrlBy().isEmpty()){
					
					liste.add(hexa.getVoisinDroiteHaut());	add = true;
				}
			}
			if( hexa.getVoisinDroite() != null && !liste.contains(hexa.getVoisinDroite()) ){
				if( hexa.getVoisinDroite().getColor().getRGB() == hexa.getColor().getRGB() && hexa.getVoisinDroite().getCtrlBy().isEmpty()){
					
					liste.add(hexa.getVoisinDroite());		add = true;
				}
			}
			if( hexa.getVoisinDroiteBas() != null && !liste.contains(hexa.getVoisinDroiteBas()) ){
				if( hexa.getVoisinDroiteBas().getColor().getRGB() == hexa.getColor().getRGB() && hexa.getVoisinDroiteBas().getCtrlBy().isEmpty()){
					
					liste.add(hexa.getVoisinDroiteBas());	add = true;
				}
			}
			if(hexa.getVoisinGaucheBas() != null && !liste.contains(hexa.getVoisinGaucheBas()) ){
				if( hexa.getVoisinGaucheBas().getColor().getRGB() == hexa.getColor().getRGB() && hexa.getVoisinGaucheBas().getCtrlBy().isEmpty()){
					
					liste.add(hexa.getVoisinGaucheBas());	add = true;
				}
			}
			if(hexa.getVoisinGauche() != null && !liste.contains(hexa.getVoisinGauche()) ){
				if( hexa.getVoisinGauche().getColor().getRGB() == hexa.getColor().getRGB() && hexa.getVoisinGauche().getCtrlBy().isEmpty()){
					
					liste.add(hexa.getVoisinGauche());		add = true;
				}
			}
			if(hexa.getVoisinGaucheHaut() != null && !liste.contains(hexa.getVoisinGaucheHaut()) ){
				if( hexa.getVoisinGaucheHaut().getColor().getRGB() == hexa.getColor().getRGB() && hexa.getVoisinGaucheHaut().getCtrlBy().isEmpty()){
					
					liste.add(hexa.getVoisinGaucheHaut());	add = true;
				}
			}
		}
		if(add == false){	return liste;	}	// Condition de sortie de la r�cursion
		
		return getConnectedCellsOfSameColor(liste);
	}
	
	
	/**
	 * Cette fonction fait progresser la partie, �tape par �tape, selon les couleurs d'actions prises : red�finissant ainsi les listes de cellules contr�l�es par les joueurs
	 * Cette fonction rafraichit ensuite le tableau (suite au diff�rents changements de couleur) en appelant la fonction "repaint()"
	 * 
	 * @see getConnectedSameColorHexas(ArrayList<Hexa> liste)
	 * @see setCasesCtrl(ArrayList<Hexa> liste)
	 * @see repaint()
	 * 
	 * @param couleur	: couleur de l'action qu'on a d�cid� de prendre ce tour ci
	 */
	public Player nextMove(Color couleur){
		
		boolean flag = true;
		
		Player joueurAct = null;
		Player joueurSui = null;
		
		if(this.joueur1.isMyTurn() && flag){
			
			flag = false;
			
			joueurAct = this.joueur1;
			
			this.joueur1.setMyTurn(false);
			this.joueur2.setMyTurn(true);
			joueurSui = this.joueur2;
		}
		
		if(this.joueur2.isMyTurn() && flag){
			
			flag = false;
			
			joueurAct = this.joueur2;
			
			if(this.joueur3 == null){
				this.joueur2.setMyTurn(false);
				this.joueur1.setMyTurn(true);
				joueurSui = this.joueur1;
			}
			if(this.joueur3 != null){
				this.joueur2.setMyTurn(false);
				this.joueur3.setMyTurn(true);
				joueurSui = this.joueur3;
			}
		}
		
		if(this.joueur3 != null){
			if(this.joueur3.isMyTurn() && flag){
				
				flag = false;
				
				joueurAct = this.joueur3;
				
				if(this.joueur4 == null){
					this.joueur3.setMyTurn(false);
					this.joueur1.setMyTurn(true);
					joueurSui = this.joueur1;
				}
				if(this.joueur4 != null){
					this.joueur3.setMyTurn(false);
					this.joueur4.setMyTurn(true);
					joueurSui = this.joueur4;
				}
			}
		}
		
		if(this.joueur4 != null){
			if(this.joueur4.isMyTurn() && flag){
				
				flag = false;
				
				joueurAct = this.joueur4;
				
				this.joueur4.setMyTurn(false);
				this.joueur1.setMyTurn(true);
				joueurSui = this.joueur1;
			}
		}
		
		ArrayList<HexaCell> hexasCtrl = joueurAct.getCasesCtrl();
		
		for(int i = 0; i < hexasCtrl.size(); i++){
			hexasCtrl.get(i).setColor(couleur);
		}
		
		hexasCtrl = getConnectedCellsOfSameColor(hexasCtrl);
		
		joueurAct.setCasesCtrl(hexasCtrl);
		for(HexaCell hexa : hexasCtrl){
			hexa.setCtrlBy(joueurAct.getNom());
		}
		
		repaint();
		
		if(!joueurSui.getIA().equals("Sans")){
			
			return joueurSui;
		}
		else{
			return null;
		}
	}
	
	public Color nextEasyIAMove(){	// IA Al�atoire
		
		ArrayList<Color> freeColors = getFreeColors();
		
		Random r = new Random();
		int alea = r.nextInt(freeColors.size());
		
		Color color = freeColors.get(alea);
		
		return color;
	}
	
	public Color nextTroubleIAMove(Player joueur){
		
		Player nextPlayer = null;
		
		if(joueur == this.joueur1){ nextPlayer = this.joueur2; }
		if(joueur == this.joueur2){
			if		(this.joueur3 != null)	{ nextPlayer = this.joueur3; }
			else if (this.joueur4 != null)	{ nextPlayer = this.joueur4; }
			else							{ nextPlayer = this.joueur1; }
		}
		if(joueur == this.joueur3){
			if(this.joueur4 != null){ nextPlayer = this.joueur4; }
			else					{ nextPlayer = this.joueur1 ;}
		}
		if(joueur == this.joueur4){ nextPlayer = this.joueur1; }
		
		
		ArrayList<Color> freeColors = getFreeColors();
		
		int max  = nextPlayer.getCasesCtrl().size();
		ArrayList<HexaCell> hexasCtrl = nextPlayer.getCasesCtrl();
		
		Color colorIni = nextPlayer.getCasesCtrl().get(0).getColor();
		Color color = Color.black;
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.red); 	}
		int redList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( redList >= max 		&& freeColors.contains(Color.red))		{ color = Color.red; 		max = redList;		}
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.orange); 	}
		int orangeList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( orangeList >= max 	&& freeColors.contains(Color.orange))	{ color = Color.orange; 	max = orangeList;	}
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.yellow); 	}
		int yellowList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( yellowList >= max 	&& freeColors.contains(Color.yellow))	{ color = Color.yellow; 	max = yellowList;	}
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.green); 	}
		int greenList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( greenList >= max 	&& freeColors.contains(Color.green))	{ color = Color.green; 		max = greenList;	}
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.blue); 	}
		int blueList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( blueList >= max 		&& freeColors.contains(Color.blue))	{ color = Color.blue; 		max = blueList;		}
		
		for(int i = 0; i < hexasCtrl.size(); i++){ hexasCtrl.get(i).setColor(Color.magenta); 	}
		int magentaList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( magentaList >= max 	&& freeColors.contains(Color.magenta))	{ color = Color.magenta; 	max = magentaList;	}
		
		for(int i = 0; i < hexasCtrl.size(); i++){ hexasCtrl.get(i).setColor(colorIni); }
		
		return color;
	}
	
	public Color nextHardIAMove(Player joueur){
		
		ArrayList<Color> freeColors = getFreeColors();
		
		int max  = joueur.getCasesCtrl().size();
		ArrayList<HexaCell> hexasCtrl = joueur.getCasesCtrl();
		
		Color colorIni = joueur.getCasesCtrl().get(0).getColor();
		Color color = Color.black;
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.red); 	}
		int redList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( redList >= max 		&& freeColors.contains(Color.red))		{ color = Color.red; 		max = redList;		}
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.orange); 	}
		int orangeList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( orangeList >= max 	&& freeColors.contains(Color.orange))	{ color = Color.orange; 	max = orangeList;	}
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.yellow); 	}
		int yellowList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( yellowList >= max 	&& freeColors.contains(Color.yellow))	{ color = Color.yellow; 	max = yellowList;	}
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.green); 	}
		int greenList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( greenList >= max 	&& freeColors.contains(Color.green))	{ color = Color.green; 		max = greenList;	}
		
		for(int i = 0; i < hexasCtrl.size(); i++)	{ hexasCtrl.get(i).setColor(Color.blue); 	}
		int blueList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( blueList >= max 		&& freeColors.contains(Color.blue))	{ color = Color.blue; 		max = blueList;		}
		
		for(int i = 0; i < hexasCtrl.size(); i++){ hexasCtrl.get(i).setColor(Color.magenta); 	}
		int magentaList = getConnectedCellsOfSameColor(hexasCtrl).size();
		if( magentaList >= max 	&& freeColors.contains(Color.magenta))	{ color = Color.magenta; 	max = magentaList;	}
		
		for(int i = 0; i < hexasCtrl.size(); i++){ hexasCtrl.get(i).setColor(colorIni); }
		
		return color;
	}
	
	public Color nextAdvancedIAMove(){
		
		int gridSize = (this.grille.length)*(this.grille.length);
		
		HexaCell[][] gridSimu = new HexaCell[grille.length][grille.length];
		
		for(int i = 0; i < grille.length; i++){
			for(int j = 0; j < grille.length; j++){
				gridSimu[i][j] = grille[i][j].clone();
			}
		}
		
		gridSimu = defVoisins(gridSimu);
		
		Player playerSimu1 = this.joueur1.clone(gridSimu);
		Player playerSimu2 = this.joueur2.clone(gridSimu);
		Player playerSimu3 = null;
		Player playerSimu4 = null;
		
		if(this.joueur3 != null){ playerSimu3 = this.joueur3.clone(gridSimu); }
		if(this.joueur4 != null){ playerSimu4 = this.joueur4.clone(gridSimu); }
		
		Player[] players = {playerSimu1, playerSimu2, playerSimu3, playerSimu4};
		
		int nbJoueurs = 2;
		if(players[2] != null){ nbJoueurs++; }
		if(players[3] != null){ nbJoueurs++; }
		
		//System.out.println("!!! "+playerSimu1.getCasesCtrl().size());
		
		DecisionTree dt = new DecisionTree();
		dt = decisionSimu(dt, gridSize, nbJoueurs, 0, players, gridSimu);
		
		int h = DecisionTree.heightTree(dt);
		
		System.out.println("hauteur : "+h);
		
		printSmallTree(dt, gridSize);
		
		return null;
	}
	
	public void nextLimitedIAMove(){
		
		int nbPlayers = 2;
		
		ArrayList<Color> couleursNonDispo = new ArrayList<Color>();
		
		String playerName1 = this.joueur1.getNom();		Color color1 = this.joueur1.getCouleur();	couleursNonDispo.add(color1);
		String playerName2 = this.joueur2.getNom();		Color color2 = this.joueur2.getCouleur();	couleursNonDispo.add(color2);
		String playerName3 = ""; 						Color color3 = null;	
		String playerName4 = ""; 						Color color4 = null;	
		
		if(this.joueur3 != null){ playerName3 = this.joueur3.getNom(); color3 = this.joueur3.getCouleur(); couleursNonDispo.add(color3); nbPlayers++;}
		if(this.joueur4 != null){ playerName4 = this.joueur4.getNom(); color4 = this.joueur4.getCouleur(); couleursNonDispo.add(color4); nbPlayers++;}
		
		ArrayList<Color> couleursDispo = getFreeLimitedColors(couleursNonDispo);
	}
	
	public void limitedSimu(){
		
	}
	
	private ArrayList<Color> getFreeLimitedColors(ArrayList<Color> couleursNonDispo){
		
		ArrayList<Color> couleursDispo = new ArrayList<Color>();
		
		if(!couleursNonDispo.contains(Color.red)	){ 	couleursDispo.add(Color.red); 		}
		if(!couleursNonDispo.contains(Color.orange)	){ 	couleursDispo.add(Color.orange); 	}
		if(!couleursNonDispo.contains(Color.yellow)	){ 	couleursDispo.add(Color.yellow); 	}
		if(!couleursNonDispo.contains(Color.green)	){ 	couleursDispo.add(Color.green); 	}
		if(!couleursNonDispo.contains(Color.blue)	){ 	couleursDispo.add(Color.blue); 		}
		if(!couleursNonDispo.contains(Color.magenta)){	couleursDispo.add(Color.magenta);	}
		
		return couleursDispo;
	}
	
	public void printSmallTree(DecisionTree dt, int gridSize){	// Juste pour afficher un arbre d'une grille 4x4, pour voir un peu � quoi ressemble l'arbe...
	
		if(gridSize == 16){
			
			//for(int i = 0; i < 40; i++) System.out.print(" ");
			//System.out.print(dt.getPlayer().getNom()+" : "+dt.getPlayer().getCouleur());
			
			System.out.print("\n");
									if(dt.getRedChild() 	!= null){ 	System.out.print(dt.getRedChild().getPlayer().getNom()		+"r"); }else{	System.out.print("  "); }
			System.out.print("  ");	if(dt.getOrangeChild() 	!= null){	System.out.print(dt.getOrangeChild().getPlayer().getNom()	+"o"); }else{	System.out.print("  "); }
			System.out.print("  ");	if(dt.getYellowChild()	!= null){ 	System.out.print(dt.getYellowChild().getPlayer().getNom()	+"y"); }else{	System.out.print("  "); }
			System.out.print("  ");	if(dt.getGreenChild() 	!= null){ 	System.out.print(dt.getGreenChild().getPlayer().getNom()	+"g"); }else{	System.out.print("  "); }
			System.out.print("  ");	if(dt.getBlueChild() 	!= null){ 	System.out.print(dt.getBlueChild().getPlayer().getNom()		+"b"); }else{	System.out.print("  "); }
			System.out.print("  ");	if(dt.getMagentaChild() != null){ 	System.out.print(dt.getMagentaChild().getPlayer().getNom()	+"m"); }else{	System.out.print("  "); }
			
			System.out.print("\n");
			if(dt.getRedChild() != null){
				
				if(dt.getRedChild().getRedChild()     != null) 	System.out.print(dt.getRedChild().getRedChild().getPlayer().getNom()		+"r");
				if(dt.getRedChild().getOrangeChild()  != null) 	System.out.print(dt.getRedChild().getOrangeChild().getPlayer().getNom()		+"o");
				if(dt.getRedChild().getYellowChild()  != null) 	System.out.print(dt.getRedChild().getYellowChild().getPlayer().getNom()		+"y");
				if(dt.getRedChild().getGreenChild()   != null) 	System.out.print(dt.getRedChild().getGreenChild().getPlayer().getNom()		+"g");
				if(dt.getRedChild().getBlueChild() 	  != null) 	System.out.print(dt.getRedChild().getBlueChild().getPlayer().getNom()		+"b");
				if(dt.getRedChild().getMagentaChild() != null) 	System.out.print(dt.getRedChild().getMagentaChild().getPlayer().getNom()	+"m");
			}else{	System.out.print("  "); }
					System.out.print("  ");
			if(dt.getOrangeChild() != null){
				
				if(dt.getOrangeChild().getRedChild()     != null) 	System.out.print(dt.getOrangeChild().getRedChild().getPlayer().getNom()		+"r");
				if(dt.getOrangeChild().getOrangeChild()  != null) 	System.out.print(dt.getOrangeChild().getOrangeChild().getPlayer().getNom()	+"o");
				if(dt.getOrangeChild().getYellowChild()  != null) 	System.out.print(dt.getOrangeChild().getYellowChild().getPlayer().getNom()	+"y");
				if(dt.getOrangeChild().getGreenChild()   != null) 	System.out.print(dt.getOrangeChild().getGreenChild().getPlayer().getNom()	+"g");
				if(dt.getOrangeChild().getBlueChild() 	 != null) 	System.out.print(dt.getOrangeChild().getBlueChild().getPlayer().getNom()	+"b");
				if(dt.getOrangeChild().getMagentaChild() != null) 	System.out.print(dt.getOrangeChild().getMagentaChild().getPlayer().getNom()	+"m");
			}else{	System.out.print("  "); }
					System.out.print("  ");
			if(dt.getYellowChild() != null){
				
				if(dt.getYellowChild().getRedChild()     != null) 	System.out.print(dt.getYellowChild().getRedChild().getPlayer().getNom()		+"r");
				if(dt.getYellowChild().getOrangeChild()  != null) 	System.out.print(dt.getYellowChild().getOrangeChild().getPlayer().getNom()	+"o");
				if(dt.getYellowChild().getYellowChild()  != null) 	System.out.print(dt.getYellowChild().getYellowChild().getPlayer().getNom()	+"y");
				if(dt.getYellowChild().getGreenChild()   != null) 	System.out.print(dt.getYellowChild().getGreenChild().getPlayer().getNom()	+"g");
				if(dt.getYellowChild().getBlueChild() 	 != null) 	System.out.print(dt.getYellowChild().getBlueChild().getPlayer().getNom()	+"b");
				if(dt.getYellowChild().getMagentaChild() != null) 	System.out.print(dt.getYellowChild().getMagentaChild().getPlayer().getNom()	+"m");
			}else{	System.out.print("  "); }
					System.out.print("  ");
			if(dt.getGreenChild() != null){
				
				if(dt.getGreenChild().getRedChild()     != null) 	System.out.print(dt.getGreenChild().getRedChild().getPlayer().getNom()		+"r");
				if(dt.getGreenChild().getOrangeChild()  != null) 	System.out.print(dt.getGreenChild().getOrangeChild().getPlayer().getNom()	+"o");
				if(dt.getGreenChild().getYellowChild()  != null) 	System.out.print(dt.getGreenChild().getYellowChild().getPlayer().getNom()	+"y");
				if(dt.getGreenChild().getGreenChild()   != null) 	System.out.print(dt.getGreenChild().getGreenChild().getPlayer().getNom()	+"g");
				if(dt.getGreenChild().getBlueChild() 	!= null) 	System.out.print(dt.getGreenChild().getBlueChild().getPlayer().getNom()		+"b");
				if(dt.getGreenChild().getMagentaChild() != null) 	System.out.print(dt.getGreenChild().getMagentaChild().getPlayer().getNom()	+"m");
			}else{	System.out.print("  "); }
					System.out.print("  ");
			if(dt.getBlueChild() != null){
				
				if(dt.getBlueChild().getRedChild()     	!= null) 	System.out.print(dt.getBlueChild().getRedChild().getPlayer().getNom()		+"r");
				if(dt.getBlueChild().getOrangeChild()  	!= null) 	System.out.print(dt.getBlueChild().getOrangeChild().getPlayer().getNom()	+"o");
				if(dt.getBlueChild().getYellowChild()  	!= null) 	System.out.print(dt.getBlueChild().getYellowChild().getPlayer().getNom()	+"y");
				if(dt.getBlueChild().getGreenChild()   	!= null) 	System.out.print(dt.getBlueChild().getGreenChild().getPlayer().getNom()		+"g");
				if(dt.getBlueChild().getBlueChild() 	!= null) 	System.out.print(dt.getBlueChild().getBlueChild().getPlayer().getNom()		+"b");
				if(dt.getBlueChild().getMagentaChild() 	!= null) 	System.out.print(dt.getBlueChild().getMagentaChild().getPlayer().getNom()	+"m");
			}else{	System.out.print("  "); }
					System.out.print("  ");
			if(dt.getMagentaChild() != null){
				
				if(dt.getMagentaChild().getRedChild()     	!= null) 	System.out.print(dt.getMagentaChild().getRedChild().getPlayer().getNom()	+"r");
				if(dt.getMagentaChild().getOrangeChild()  	!= null) 	System.out.print(dt.getMagentaChild().getOrangeChild().getPlayer().getNom()	+"o");
				if(dt.getMagentaChild().getYellowChild()  	!= null) 	System.out.print(dt.getMagentaChild().getYellowChild().getPlayer().getNom()	+"y");
				if(dt.getMagentaChild().getGreenChild()   	!= null) 	System.out.print(dt.getMagentaChild().getGreenChild().getPlayer().getNom()	+"g");
				if(dt.getMagentaChild().getBlueChild() 		!= null) 	System.out.print(dt.getMagentaChild().getBlueChild().getPlayer().getNom()	+"b");
				if(dt.getMagentaChild().getMagentaChild() 	!= null) 	System.out.print(dt.getMagentaChild().getMagentaChild().getPlayer().getNom()+"m");
			}
		}
	}
	
	/*
	 * Cr�ation de l'arbre des d�cisions possibles ...
	 */
	public DecisionTree decisionSimu(DecisionTree dt, int gridSize, int nbJoueurs, int joueurAct, Player[] players, HexaCell[][] gridSimu){

		ArrayList<Color> couleursDispo = getFreeSimuColors(players);
		
		Player player = players[joueurAct];
		
		ArrayList<HexaCell> hexasCtrl = player.getCasesCtrl();
		int prevNbHexasCtrl = 0;
		int actNbHexasCtrl  = 0;
		
		for(Color itemColor : couleursDispo){
			
			for(int i = 0; i < hexasCtrl.size(); i++){
				hexasCtrl.get(i).setColor(itemColor);
			}
			player.setCouleur(itemColor);
			
			prevNbHexasCtrl = hexasCtrl.size();
			hexasCtrl = HexaBoard.getConnectedCellsOfSameColor(hexasCtrl);
			actNbHexasCtrl  = hexasCtrl.size();
	
			if((actNbHexasCtrl > prevNbHexasCtrl)){
	
				player.setCasesCtrl(hexasCtrl);
				for(HexaCell hexa : hexasCtrl){
					hexa.setCtrlBy(player.getNom());
				}
	
				if(hexasCtrl.size() > (gridSize/nbJoueurs)){
					player.setIsWinner(true);
					return dt;
				}
	
				player.setMyTurn(false);
	
				gridSimu = new HexaCell[grille.length][grille.length];
				
				for(int i = 0; i < grille.length; i++){
					for(int j = 0; j < grille.length; j++){
						gridSimu[i][j] = grille[i][j].clone();
					}
				}
				
				gridSimu = defVoisins(gridSimu);
	
				if(itemColor == Color.red) 		{ dt.setRedChild(		new DecisionTree(player.clone(gridSimu)));	}
				if(itemColor == Color.orange) 	{ dt.setOrangeChild(	new DecisionTree(player.clone(gridSimu)));	}
				if(itemColor == Color.yellow) 	{ dt.setYellowChild(	new DecisionTree(player.clone(gridSimu)));	}
				if(itemColor == Color.green) 	{ dt.setGreenChild(		new DecisionTree(player.clone(gridSimu)));	}
				if(itemColor == Color.blue) 	{ dt.setBlueChild(		new DecisionTree(player.clone(gridSimu)));	}
				if(itemColor == Color.magenta) 	{ dt.setMagentaChild(	new DecisionTree(player.clone(gridSimu)));	}
	
				if(joueurAct == 3)				{ joueurAct = 0; }
				if(players[joueurAct+1] == null){ joueurAct = 0; }
				else							{ joueurAct++;   }
	
				players[joueurAct].setMyTurn(true);
				
				Player[] playersBis = new Player[4];
				
				playersBis[0] = players[0].clone(gridSimu);
				playersBis[1] = players[1].clone(gridSimu);
				
				if(players[2] != null){ playersBis[2] = players[2].clone(gridSimu); } else{ playersBis[2] = null; }
				if(players[3] != null){ playersBis[3] = players[3].clone(gridSimu); } else{ playersBis[3] = null; }
				
				if(playersBis[0].getNom().equals(player.getNom())){ playersBis[0] = player.clone(gridSimu); }
				if(playersBis[1].getNom().equals(player.getNom())){ playersBis[1] = player.clone(gridSimu); }
				
				if(playersBis[2] != null){	if(playersBis[2].getNom().equals(player.getNom())){ playersBis[2] = player.clone(gridSimu); }}
				if(playersBis[3] != null){	if(playersBis[3].getNom().equals(player.getNom())){ playersBis[3] = player.clone(gridSimu); }}
	
				if(itemColor == Color.red) 		{ decisionSimu(dt.getRedChild(), 		gridSize, nbJoueurs, joueurAct, playersBis, gridSimu);	}
				if(itemColor == Color.orange) 	{ decisionSimu(dt.getOrangeChild(), 	gridSize, nbJoueurs, joueurAct, playersBis, gridSimu);	}	
				if(itemColor == Color.yellow) 	{ decisionSimu(dt.getYellowChild(), 	gridSize, nbJoueurs, joueurAct, playersBis, gridSimu);	}
				if(itemColor == Color.green) 	{ decisionSimu(dt.getGreenChild(),		gridSize, nbJoueurs, joueurAct, playersBis, gridSimu);	}
				if(itemColor == Color.blue) 	{ decisionSimu(dt.getBlueChild(),		gridSize, nbJoueurs, joueurAct, playersBis, gridSimu);	}
				if(itemColor == Color.magenta)	{ decisionSimu(dt.getMagentaChild(),	gridSize, nbJoueurs, joueurAct, playersBis, gridSimu);	}
				
			}
		}
		
		return dt;
	}
	
	
	public ArrayList<Color> getFreeSimuColors(Player[] players){
		
		ArrayList<Color> couleursNonDispo = new ArrayList<Color>();
		
		Color couleur1 = players[0].getCasesCtrl().get(0).getColor();
		Color couleur2 = players[1].getCasesCtrl().get(0).getColor();
		
		Color couleur3 = null;
		Color couleur4 = null;
		
		if(this.getJoueur3() != null){
			couleur3 = players[2].getCasesCtrl().get(0).getColor();
		}
		if(this.getJoueur4() != null){
			couleur4 = players[3].getCasesCtrl().get(0).getColor();
		}
		
		couleursNonDispo.add(couleur1);
		couleursNonDispo.add(couleur2);
		
		if(players[2] != null){ couleursNonDispo.add(couleur3); }
		if(players[3] != null){ couleursNonDispo.add(couleur4); }
		
		ArrayList<Color> couleursDispo = new ArrayList<Color>();

		if(!couleursNonDispo.contains(Color.red))		couleursDispo.add(Color.red);
		if(!couleursNonDispo.contains(Color.orange))	couleursDispo.add(Color.orange);
		if(!couleursNonDispo.contains(Color.yellow))	couleursDispo.add(Color.yellow);
		if(!couleursNonDispo.contains(Color.green))		couleursDispo.add(Color.green);
		if(!couleursNonDispo.contains(Color.blue))		couleursDispo.add(Color.blue);
		if(!couleursNonDispo.contains(Color.magenta))	couleursDispo.add(Color.magenta);
		
		return couleursDispo;
	}
	
	/**
	 * Cette fonction retourne les couleurs occup�es par tous les joueurs pr�sents
	 * 
	 * @return couleurs		: retourne les couleurs de tous les joueurs autour du tableau
	 */
	public ArrayList<Color> getColorsFromPlayers(){
		
		ArrayList<Color> couleurs = new ArrayList<Color>();
		
		Color couleur1 = this.getJoueur1().getCasesCtrl().get(0).getColor();
		Color couleur2 = this.getJoueur2().getCasesCtrl().get(0).getColor();
		
		Color couleur3 = null;
		Color couleur4 = null;
		
		if(this.getJoueur3() != null){
			couleur3 = this.getJoueur3().getCasesCtrl().get(0).getColor();
		}
		if(this.getJoueur4() != null){
			couleur4 = this.getJoueur4().getCasesCtrl().get(0).getColor();
		}
		
		couleurs.add(couleur1);
		couleurs.add(couleur2);
		
		if(joueur3 != null){ couleurs.add(couleur3); }
		if(joueur4 != null){ couleurs.add(couleur4); }
		
		return couleurs;
	}
	
	/**
	 * Cette fonction retourne les couleurs libres qu'aucun joueur n'occupe
	 * 
	 * @return couleursLibres	: retourne les couleurs libres du tableau
	 */
	public ArrayList<Color> getFreeColors(){
		
		ArrayList<Color> couleursLibres = new ArrayList<Color>();
		ArrayList<Color> couleursOccupees = this.getColorsFromPlayers();
		
		if(!couleursOccupees.contains(Color.red)){
			couleursLibres.add(Color.red);
		}
		if(!couleursOccupees.contains(Color.orange)){
			couleursLibres.add(Color.orange);
		}
		if(!couleursOccupees.contains(Color.yellow)){
			couleursLibres.add(Color.yellow);
		}
		if(!couleursOccupees.contains(Color.green)){
			couleursLibres.add(Color.green);
		}
		if(!couleursOccupees.contains(Color.blue)){
			couleursLibres.add(Color.blue);
		}
		if(!couleursOccupees.contains(Color.magenta)){
			couleursLibres.add(Color.magenta);
		}
		return couleursLibres;
	}
	
	public boolean isTheGameOver(){
		
		boolean isTheGameOver = false;
		
		int nbJoueurs = 2;
		
		if(this.joueur3 != null) nbJoueurs++;
		if(this.joueur4 != null) nbJoueurs++;
		
		if(this.joueur1.getCasesCtrl().size() >= (this.grille.length*this.grille.length / nbJoueurs)){ isTheGameOver = true; }
		if(this.joueur2.getCasesCtrl().size() >= (this.grille.length*this.grille.length / nbJoueurs)){ isTheGameOver = true; }
		
		if(this.joueur3 != null){
			if(this.joueur3.getCasesCtrl().size() >= (this.grille.length*this.grille.length / nbJoueurs)){ isTheGameOver = true; }
		}
		
		if(this.joueur4 != null){
			if(this.joueur4.getCasesCtrl().size() >= (this.grille.length*this.grille.length / nbJoueurs)){ isTheGameOver = true; }
		}
		
		if(this.joueur1 == null){ isTheGameOver = false; } // Car dans ce cas la partie n'a m�me pas commenc�...
		
		return isTheGameOver;
	}
	
	public String generateSaveString(){
		//TODO
		
		String saveStr = new String("");
		
		ArrayList<String> listeJoueur1 = new ArrayList<String>();
		ArrayList<String> listeJoueur2 = new ArrayList<String>();
		ArrayList<String> listeJoueur3 = new ArrayList<String>();
		ArrayList<String> listeJoueur4 = new ArrayList<String>();
		
		// Ins�re le type de partie
		saveStr += "HEXA\r\n";
		
		// Ins�re la taille d'un c�t� 
		saveStr += this.grille.length+"\r\n";
		
		// Ins�re tous les �l�ments de la grille
		for(int i = 0; i < grille.length; i++){
			for(int j = 0; j < grille[0].length; j++){
				saveStr += i+"_"+j+"_"+grille[i][j].getColor()+" ";
				
				if(!grille[i][j].getCtrlBy().isEmpty()){
					if(grille[i][j].getCtrlBy().equals(this.joueur1.getNom())){listeJoueur1.add(i+"_"+j);}
					if(grille[i][j].getCtrlBy().equals(this.joueur2.getNom())){listeJoueur2.add(i+"_"+j);}
					if(this.joueur3 != null){
						if(grille[i][j].getCtrlBy().equals(this.joueur3.getNom())){listeJoueur3.add(i+"_"+j);}
					}
					if(this.joueur4 != null){
						if(grille[i][j].getCtrlBy().equals(this.joueur4.getNom())){listeJoueur4.add(i+"_"+j);}
					}
				}
			}
			saveStr += "\r\n";
		}
		
		// Ins�re les caract�ristiques des diff�rents joueurs
		saveStr += "\r\n"+this.joueur1.getNom()+" "+this.joueur1.getIA()+" "+this.joueur1.isMyTurn()+" "
						 +this.joueur1.getCouleur().getRed()+"_"+this.joueur1.getCouleur().getGreen()+"_"+this.joueur1.getCouleur().getBlue()+" ";
		for(String str : listeJoueur1){ saveStr += str+" "; }
		
		saveStr += "\r\n"+this.joueur2.getNom()+" "+this.joueur2.getIA()+" "+this.joueur2.isMyTurn()+" "
						 +this.joueur2.getCouleur().getRed()+"_"+this.joueur2.getCouleur().getGreen()+"_"+this.joueur2.getCouleur().getBlue()+" ";
		for(String str : listeJoueur2){ saveStr += str+" "; }
		
		if(this.joueur3 != null){
			saveStr += "\r\n"+this.joueur3.getNom()+" "+this.joueur3.getIA()+" "+this.joueur3.isMyTurn()+" "
							 +this.joueur3.getCouleur().getRed()+"_"+this.joueur3.getCouleur().getGreen()+"_"+this.joueur3.getCouleur().getBlue()+" ";
			for(String str : listeJoueur3){ saveStr += str+" "; }
		}
		if(this.joueur4 != null){
			saveStr += "\r\n"+this.joueur4.getNom()+" "+this.joueur4.getIA()+" "+this.joueur3.isMyTurn()+" "
							 +this.joueur4.getCouleur().getRed()+"_"+this.joueur4.getCouleur().getGreen()+"_"+this.joueur4.getCouleur().getBlue()+" ";
			for(String str : listeJoueur4){ saveStr += str+" "; }
		}
		
		return saveStr;
	}
	
	public Player getWinner(){
		
		Player winner = this.joueur1;
		
		if(this.joueur2.getCasesCtrl().size() > winner.getCasesCtrl().size()) winner = this.joueur2;
		
		if(this.joueur3 != null){
			if(this.joueur3.getCasesCtrl().size() > winner.getCasesCtrl().size()) winner = this.joueur3;
		}
		
		if(this.joueur4 != null){
			if(this.joueur4.getCasesCtrl().size() > winner.getCasesCtrl().size()) winner = this.joueur4;
		}
		
		return winner;
	}
	
	@Override
	/**
	 * Cette fonction dessine le tableau en tenant compte des coordon�es et des couleurs de chaque cellule, ici il s'agit d'un pavage d'hexagones
	 */
	public void paint(Graphics g){
		
		super.paint(g);
		
		g.setColor(Color.white);
		
		if(isTheGameOver() ) g.drawString("FIN DE LA PARTIE ==> GAGNANT : "+getWinner().getNom(), 10, 15);
		if(isTheGameOver() ) g.drawString("Cliquez sur \"Play\" pour une nouvelle partie !", 10, 30);
		
		g.drawString("Joueur1 : \""+joueur1.getNom()+"\"      |      Score : "+joueur1.getCasesCtrl().size(), 10, 60);
		g.drawString("Joueur2 : \""+joueur2.getNom()+"\"      |      Score : "+joueur2.getCasesCtrl().size(), 10, 75);
		
		if(joueur3 != null){
			g.drawString("Joueur3 : \""+joueur3.getNom()+"\"     |      Score : "+joueur3.getCasesCtrl().size(), 10, 90);
		}
		if(joueur4 != null){
			g.drawString("Joueur4 : \""+joueur4.getNom()+"\"     |      Score : "+joueur4.getCasesCtrl().size(), 10, 105);
		}
		
		for(int i = 0; i < grille.length; i++){
			for(int j = 0; j< grille[0].length; j++){
				
				int x[] = {grille[i][j].getCentreX(),		grille[i][j].getCentreX()+10,	grille[i][j].getCentreX()+10,	grille[i][j].getCentreX(), 		grille[i][j].getCentreX()-10, 	grille[i][j].getCentreX()-10};
				int y[] = {grille[i][j].getCentreY()+15,	grille[i][j].getCentreY()+5, 	grille[i][j].getCentreY()-5, 	grille[i][j].getCentreY()-15, 	grille[i][j].getCentreY()-5, 	grille[i][j].getCentreY()+5};
				
				g.setColor(grille[i][j].getColor());
				g.fillPolygon(x, y, 6);
				
				g.setColor(Color.black);
				g.drawPolygon(x, y, 6);
				
				if(joueur1.getCasesCtrl().contains(grille[i][j])){
					g.drawString("1", grille[i][j].getCentreX()-2, grille[i][j].getCentreY()+5);
				}
				if(joueur2.getCasesCtrl().contains(grille[i][j])){
					g.drawString("2", grille[i][j].getCentreX()-2, grille[i][j].getCentreY()+5);
				}
				if(joueur3 != null){
					if(joueur3.getCasesCtrl().contains(grille[i][j])){
						g.drawString("3", grille[i][j].getCentreX()-2, grille[i][j].getCentreY()+5);
					}
				}
				if(joueur4 != null){
					if(joueur4.getCasesCtrl().contains(grille[i][j])){
						g.drawString("4", grille[i][j].getCentreX()-2, grille[i][j].getCentreY()+5);
					}
				}
			}
		}
	}
	
	@Override
	/**
	 * Cette fonction permet de raffraichir le dessin du tableau
	 */
	public void repaint(){ super.repaint(); }
	
	/**
	 * Getter retournant la hauteur du tableau
	 */
	public int getHauteur(){ return this.hauteur; }
	
	/**
	 * Getter retournant la largeur du tableau
	 */
	public int getLargeur(){ return this.largeur; }
	
	/**
	 * Getter retournant le joueur 1
	 */
	public Player getJoueur1() { return joueur1; }

	/**
	 * Getter retournant le joueur 2
	 */
	public Player getJoueur2() { return joueur2; }

	/**
	 * Getter retournant le joueur 3
	 */
	public Player getJoueur3() { return joueur3; }

	/**
	 * Getter retournant le joueur 4
	 */
	public Player getJoueur4() { return joueur4; }
}
