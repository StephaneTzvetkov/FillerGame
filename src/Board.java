
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;


public class Board extends Canvas {
	
	private Hexa[][] grille;		// grille[i][j] : j represente les lignes, i les �l�ments en colonne de chaque ligne
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
	public Board(int nb, String nomJoueur1, String nomJoueur2, String nomJoueur3, String nomJoueur4) {
		
		// TODO : fractionner le code de ce constructeur, en sous-fonctions etc
		
		setBackground (Color.black);
        setSize(40*nb +50, 40*nb +60);
		
		int decalageX = 0;
		
		this.grille = new Hexa[nb][nb];
		
		for(int i = 0; i < grille.length; i++){
			for(int j = 0; j < grille[0].length; j++){
				
				// ATTENTION il se passe un truc dans les coins : il faut que les 4 coins soient de couleur diff !
				
				if(j %2 == 0){ decalageX = -20;}
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
				
				grille[i][j] = new Hexa(60+i*40+decalageX, 50+j*40, color, null, null, null, null, null, null);
			}
		}
		
		for(int i = 0; i < grille.length; i++){
			for(int j = 0; j < grille[0].length; j++){
				
				if(i == 0 && j == 0){						// Premier Hexa de la premi�re ligne
					
					grille[0][0].setVoisinDroiteHaut(null);
					grille[0][0].setVoisinGaucheHaut(null);
					grille[0][0].setVoisinGauche(null);
					grille[0][0].setVoisinGaucheBas(null);
					
					grille[0][0].setVoisinDroite(grille[1][0]);
					grille[0][0].setVoisinDroiteBas(grille[0][1]);
				}
				else if(i == nb-1 && j == 0){				// Dernier Hexa de la premi�re ligne
					
					grille[nb-1][0].setVoisinDroiteHaut(null);
					grille[nb-1][0].setVoisinDroite(null);
					grille[nb-1][0].setVoisinGaucheHaut(null);
					
					grille[nb-1][0].setVoisinGauche(grille[nb-2][0]);
					grille[nb-1][0].setVoisinGaucheBas(grille[nb-2][1]);
					grille[nb-1][0].setVoisinDroiteBas(grille[nb-1][1]);
				}
				else if(i != 0  && i != nb-1 && j == 0){	// Du deuxi�me � l'avant dernier Hexa de la premi�re ligne
					
					grille[i][0].setVoisinDroiteHaut(null);
					grille[i][0].setVoisinGaucheHaut(null);
					
					grille[i][0].setVoisinDroite(grille[i+1][0]);
					grille[i][0].setVoisinGauche(grille[i-1][0]);
					grille[i][0].setVoisinDroiteBas(grille[i][1]);
					grille[i][0].setVoisinGaucheBas(grille[i-1][1]);
				}
				else if(i == 0 && j != 0 && j != nb-1){		// Du deuxi�me � l'avant dernier Hexa de la premi�re colonne
					
					grille[i][j].setVoisinGauche(null);
					
					if(j % 2 == 0){	// Si d�cal� vers la gauche
						grille[0][j].setVoisinDroiteHaut(grille[0][j-1]);
						grille[0][j].setVoisinDroite(grille[1][j]);
						grille[0][j].setVoisinDroiteBas(grille[0][j+1]);
						
						grille[0][j].setVoisinGaucheHaut(null);
						grille[0][j].setVoisinGaucheBas(null);
					}
					else{			// Sinon
						grille[0][j].setVoisinGaucheHaut(grille[0][j-1]);
						grille[0][j].setVoisinDroiteHaut(grille[1][j-1]);
						grille[0][j].setVoisinDroite(grille[1][j]);
						grille[0][j].setVoisinDroiteBas(grille[1][j+1]);
						grille[0][j].setVoisinGaucheBas(grille[0][j+1]);
					}
				}
				else if(i == nb-1 && j != 0 && j != nb-1){	// Du deuxi�me � l'avant dernier Hexa de la derni�re colonne
					grille[i][j].setVoisinDroite(null);
					
					if(j %2 == 0){	// Si d�cal� vers la gauche
						grille[nb-1][j].setVoisinDroiteHaut(grille[nb-1][j-1]);
						grille[nb-1][j].setVoisinGaucheHaut(grille[nb-2][j-1]);
						grille[nb-1][j].setVoisinGauche(grille[nb-2][j]);
						grille[nb-1][j].setVoisinGaucheBas(grille[nb-2][j+1]);
						grille[nb-1][j].setVoisinDroiteBas(grille[nb-1][j+1]);
					}
					else{			// Sinon
						grille[nb-1][j].setVoisinDroiteHaut(null);
						grille[nb-1][j].setVoisinDroiteBas(null);
						
						grille[nb-1][j].setVoisinGaucheHaut(grille[nb-1][j-1]);
						grille[nb-1][j].setVoisinGauche(grille[nb-2][j]);
						grille[nb-1][j].setVoisinGaucheBas(grille[nb-1][j+1]);
					}
				}
				// ATTENTION : tous les hexas de la derni�re ligne s'articulent diff�rent selon que cette ligne soit paire ou impaire ... (la 1�re ligne est la ligne 0)
				// - Si la derni�re est paire (en partant de 0)	, Alors elle sera d�cal�e � droite
				// - Si la derni�re est impaire...				, Alors elle ne sera pas d�cal�e � droite
				
				else if(i == 0 && j == nb-1){				// Premier Hexa de la derni�re ligne
					
					if(j % 2 == 0){	// Si la derni�re ligne est paire
						
						grille[0][nb-1].setVoisinDroiteBas(null);
						grille[0][nb-1].setVoisinGaucheBas(null);
						grille[0][nb-1].setVoisinGauche(null);
						grille[0][nb-1].setVoisinGaucheHaut(null);
						
						grille[0][nb-1].setVoisinDroiteHaut(grille[0][nb-2]);
						grille[0][nb-1].setVoisinDroite(grille[1][nb-1]);
					}
					if(j % 2 == 1){ // Si la derni�re ligne est impaire
						
						grille[0][nb-1].setVoisinDroiteBas(null);
						grille[0][nb-1].setVoisinGaucheBas(null);
						grille[0][nb-1].setVoisinGauche(null);
						
						grille[0][nb-1].setVoisinGaucheHaut(grille[0][nb-2]);
						grille[0][nb-1].setVoisinDroiteHaut(grille[1][nb-2]);
						grille[0][nb-1].setVoisinDroite(grille[1][nb-1]);
					}
				}
				else if(i == nb-1 && j == nb-1){			// Dernier Hexa de la derni�re ligne
					
					if(j % 2 == 0){	// Si la derni�re ligne est paire
						
						grille[nb-1][nb-1].setVoisinDroite(null);
						grille[nb-1][nb-1].setVoisinDroiteBas(null);
						grille[nb-1][nb-1].setVoisinGaucheBas(null);
						
						grille[nb-1][nb-1].setVoisinDroiteHaut(grille[nb-1][nb-2]);
						grille[nb-1][nb-1].setVoisinGaucheHaut(grille[nb-2][nb-2]);
						grille[nb-1][nb-1].setVoisinGauche(grille[nb-2][nb-1]);
					}
					if(j % 2 == 1){	// Si la derni�re ligne est impaire
						
						grille[nb-1][nb-1].setVoisinDroiteHaut(null);
						grille[nb-1][nb-1].setVoisinDroite(null);
						grille[nb-1][nb-1].setVoisinDroiteBas(null);
						grille[nb-1][nb-1].setVoisinGaucheBas(null);
						
						grille[nb-1][nb-1].setVoisinGauche(grille[nb-2][nb-1]);
						grille[nb-1][nb-1].setVoisinGaucheHaut(grille[nb-1][nb-2]);
					}
				}
				else if(i != 0  && i != nb-1 && j == nb-1){	// Du deuxi�me � l'avant dernier Hexa de la derni�re ligne
					
					grille[i][nb-1].setVoisinDroiteBas(null);
					grille[i][nb-1].setVoisinGaucheBas(null);
					
					if(j % 2 == 0){	// Si la derni�re ligne est paire
						
						grille[i][nb-1].setVoisinDroite(grille[i+1][nb-1]);
						grille[i][nb-1].setVoisinDroiteHaut(grille[i][nb-2]);
						grille[i][nb-1].setVoisinGaucheHaut(grille[i-1][nb-2]);
						grille[i][nb-1].setVoisinGauche(grille[i-1][nb-1]);
					}
					if(j % 2 == 1){	// Si la derni�re ligne est impaire
						
						grille[i][nb-1].setVoisinDroite(grille[i+1][nb-1]);
						grille[i][nb-1].setVoisinDroiteHaut(grille[i+1][nb-2]);
						grille[i][nb-1].setVoisinGaucheHaut(grille[i][nb-2]);
						grille[i][nb-1].setVoisinGauche(grille[i-1][nb-1]);
					}
				}
				else{										// Sinon ... (si l'Hexa ne fait partie d'aucune bordure)
					
					if(j % 2 == 0){
						
						grille[i][j].setVoisinDroiteHaut(grille[i][j-1]);
						grille[i][j].setVoisinDroite(grille[i+1][j]);
						grille[i][j].setVoisinDroiteBas(grille[i][j+1]);
						grille[i][j].setVoisinGaucheBas(grille[i-1][j+1]);
						grille[i][j].setVoisinGauche(grille[i-1][j]);
						grille[i][j].setVoisinGaucheHaut(grille[i-1][j-1]);
					}
					if(j % 2 == 1){
						
						grille[i][j].setVoisinDroiteHaut(grille[i+1][j-1]);
						grille[i][j].setVoisinDroite(grille[i+1][j]);
						grille[i][j].setVoisinDroiteBas(grille[i+1][j+1]);
						grille[i][j].setVoisinGaucheBas(grille[i][j+1]);
						grille[i][j].setVoisinGauche(grille[i-1][j]);
						grille[i][j].setVoisinGaucheHaut(grille[i][j-1]);
					}
				}
			}
		}
		
		
	}
	
	@Override
	public void paint(Graphics g){
		
		super.paint(g);
		
		for(int i = 0; i < grille.length; i++){
			for(int j = 0; j< grille[0].length; j++){
				
				int x[] = {grille[i][j].getCentreX(),	grille[i][j].getCentreX()+20,	grille[i][j].getCentreX()+20,	grille[i][j].getCentreX(), grille[i][j].getCentreX()-20, grille[i][j].getCentreX()-20};
				int y[] = {grille[i][j].getCentreY()+30, grille[i][j].getCentreY()+10, grille[i][j].getCentreY()-10, grille[i][j].getCentreY()-30, grille[i][j].getCentreY()-10, grille[i][j].getCentreY()+10};
				
				g.setColor(grille[i][j].getColor());
				g.fillPolygon(x, y, 6);
				
				g.setColor(Color.black);
				g.drawPolygon(x, y, 6);
			}
		}
	}
}
