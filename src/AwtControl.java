import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 *	<b>Cette classe est la classe principale du logiciel "The Filler Game".
 *	Elle permet la gestion de tous les composants graphiques et de l'utilisation g�n�ral du programme.</b>
 *
 *	@version 06.05.2016 (version finale)
 * 	@author Stephane Tzvetkov & Laetitia Bouvier
 */
public class AwtControl{

	private Frame mainFrame;
	private Panel controlPanel;
	private Board board;
	
	/**
	 * Ce constructeur permet de selectionner quel tableau on souhaite g�n�rer � l'�cran, et pr�pare les principaux composants graphiques.
	 * 
	 * @param tableau	( String ) 	: <br>nom du tableau que l'on souhaite voir 		</br><br>
	 * @param nb		( int )		: <br>taille de l'un des c�t� du tableau � g�n�rer 	</br><br>
	 * @param joueur1	( String )	: <br>nom du joueur 1 								</br><br>
	 * @param joueur2	( String )	: <br>nom du joueur 2 								</br><br>
	 * @param joueur3	( String )	: <br>nom du joueur 3 								</br><br>
	 * @param joueur4	( String )	: <br>nom du joueur 4 								</br><br>
	 * @param IA1		( String )	: <br>type d'IA associ�e au joueur1 				</br><br>
	 * @param IA2		( String )	: <br>type d'IA associ�e au joueur2 				</br><br>
	 * @param IA3		( String )	: <br>type d'IA associ�e au joueur3 				</br><br>
	 * @param IA4		( String )	: <br>type d'IA associ�e au joueur4 				</br><br>
	 */
	public AwtControl(String tableau, int nb, String joueur1, String joueur2, String joueur3, String joueur4, String IA1, String IA2, String IA3, String IA4){

		//S�lectionne le tableau � g�n�rer
		if(tableau.equals("INTRO") || tableau.contains("PARAM") || tableau.equals("WEB")){
			this.board = new IntroBoard(600, 300, false);
		}
		if(tableau.contains("ERROR")){
			this.board = new IntroBoard(600, 300, true);
		}
		if(tableau.equals("HEXA")){
			this.board = new HexaBoard(nb, joueur1, joueur2, joueur3, joueur4, IA1, IA2, IA3, IA4);
		}
		if(tableau.length() > 10){
			Scanner sc = new Scanner (tableau);
			if(sc.next().equals("HEXA")){
				this.board = new HexaBoard(tableau);
			}else{}

			sc.close();
		}
		if(tableau.contains("CREATEHEXAWEB")){

			String[] str = tableau.split("_");
			String monAdresse = str[1];
			String sonAdresse = str[2];

			System.out.println("Nb : "+nb);
			System.out.println("monAdress : "+monAdresse+" | sonAdresse : "+sonAdresse);

			this.board = new HexaWebBoard(nb, joueur1, joueur2, monAdresse, sonAdresse);
		}
		if(tableau.contains("JOINHEXAWEB")){
			
			String[] str = tableau.split("_");
			String monAdresse = str[1];
			String sonAdresse = str[2];
			
			Web.envoiePaquets(sonAdresse, monAdresse+"_J2");
			
			String saveStr = Web.ecoutePaquets();
			
			this.board = new HexaWebBoard(saveStr, monAdresse, sonAdresse);
		}
		if(tableau.equals("SQUARE")){
			this.board = new SquareBoard(nb, joueur1, joueur2, joueur3, joueur4, IA1, IA2, IA3, IA4);
		}
		if(tableau.length() > 10){
			Scanner sc = new Scanner (tableau);
			if(sc.next().equals("SQUARE")){
				this.board = new SquareBoard(tableau);
			}else{}

			sc.close();
		}
		if(tableau.equals("DIAMOND")){
			this.board = new DiamondBoard(nb, joueur1, joueur2, joueur3, joueur4, IA1, IA2, IA3, IA4);
		}
		if(tableau.length() > 10){
			Scanner sc = new Scanner (tableau);
			if(sc.next().equals("DIAMOND")){
				this.board = new DiamondBoard(tableau);
			}else{}

			sc.close();
		}
		//Pr�pare le cadre principale
		mainFrame = new Frame("The Filler Game");
		mainFrame.setSize(1440,1440);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent){
				System.exit(0);
			}
		});

		//Cr�� le panneau de contr�le, qui acceuillera les diff�rents boutons, et l'ajoute au cadre principale
		controlPanel = new Panel();
		mainFrame.add(controlPanel);
		mainFrame.setVisible(true);
		//mainFrame.setResizable(false);
	}

	/**
	 * Cette fonction est la fonction principale/d'entr�e : elle commence simplement par afficher la vue d'introduction
	 */
	public static void main(String[] args){

		AwtControl awtControl = new AwtControl("INTRO", 0, "", "", "", "", "", "", "", "");
		awtControl.show("INTRO", "", false);
	}

	/**
	 * Cette fonction permet l'affichages de diff�rents �l�ments graphiques : le cadre principale, la barre de menu, le tableau et les boutons
	 * 
	 * @param tableau			( String ) 	: <br>nom du tableau que l'on souhaite voir 			</br><br>
	 * @param choixIAJoueur1	( String )	: <br>type d'IA associ�e au joueur 1					</br><br>
	 * @param isWebGame			( boolean )	: <br>est-ce que la partie g�n�r�e sera en r�seau ou non</br><br>
	 * 
	 * @see #setMenu()
	 * @see #setBoardAndButtons()
	 */
	private void show(String tableau, String choixIAJoueur1, boolean isWebGame){

		if(tableau.isEmpty() && choixIAJoueur1.isEmpty() && !isWebGame){

			choixIAJoueur1 = board.getJoueur1().getIA();

			System.out.println("Hauteur : "+board.getHauteur());
			System.out.println("Largeur : "+board.getLargeur());

		}

		controlPanel.setBackground(Color.black);

		controlPanel.setSize(board.getHauteur(),board.getLargeur());
		mainFrame.setSize(board.getHauteur()+25,board.getLargeur()+450);

		setMenu();
		setBoardAndButtons(tableau);

		mainFrame.setVisible(true);

		if(!choixIAJoueur1.equals("Sans") && !choixIAJoueur1.isEmpty()){

			Player nextPlayer = board.getJoueur1();

			do{
				if(nextPlayer.getIA().equals("IA Simple")){			nextPlayer = board.nextMove(board.nextEasyIAMove());				}
				else if(nextPlayer.getIA().equals("IA Penible")){	nextPlayer = board.nextMove(board.nextTroubleIAMove(nextPlayer));	}
				else if(nextPlayer.getIA().equals("IA Difficile")){	nextPlayer = board.nextMove(board.nextHardIAMove(nextPlayer));		}
				setAllowedButtons();
				setRestrictedButtons();
			}while(nextPlayer != null);
		}
	}
	
	/**
	 * Cette fonction param�tre et affiche le tableau et les boutons (en cr�ant ces derniers)
	 * 
	 * @param tableau	( String ) 	: <br>nom du tableau que l'on souhaite voir	</br><br>
	 * 
	 * @see #ButtonClickListener
	 */
	private void setBoardAndButtons(String tableau){

		if(tableau.equals("INTRO")){
			FlowLayout layout = new FlowLayout();
			controlPanel.setLayout(layout);

			controlPanel.add((Component) board);
		}
		else if(tableau.equals("WEBJOIN")){
			
			String sonAdresse = JOptionPane.showInputDialog(null, "Vous devez entrez l'adresse de la partie que vous souhaitez rejoindre, "
					+ "si vous le souhaitez cliquez sur OK sinon cliquez sur CANCEL.", "");
			
			String monAdresse = Web.obtenirMonAdresse();
			
			if(sonAdresse != null && !sonAdresse.isEmpty() && !sonAdresse.equals("null") && !sonAdresse.equals(" ")){
				
				mainFrame.dispose();
				AwtControl awtControl = new AwtControl("JOINHEXAWEB_"+monAdresse+"_"+sonAdresse, 0, "", "", "", "", "", "", "", "");
				awtControl.show("JOINHEXAWEB", "", true);
			}
		}
		else if(tableau.equals("WEBCREATE") || tableau.equals("WEB_ERROR")){

			GridBagLayout layout = new GridBagLayout();

			controlPanel.setLayout(layout);						//Ajoute l'agencement au panneau de contr�le (l� o� l'on souhaite voir le tableau et les boutons)
			GridBagConstraints gbc = new GridBagConstraints();	//Cr�� des contraintes sur les �l�ments de la grille, ici des contraintes de positionnement :

			Label intro = new Label("Veuillez param�trer votre prochaine partie en ligne :", Label.CENTER);
			intro.setForeground(Color.white);

			Label nb = new Label("Taille d'un c�t� :", Label.RIGHT);
			nb.setForeground(Color.white);
			final TextField nbText = new TextField(2);

			Label joueur = new Label("Entrez votre pseudo :");
			joueur.setForeground(Color.white);
			final TextField joueurText = new TextField(16);

			Label vide1 = new Label("");

			Button play = new Button("Play");
			play.setActionCommand("PLAYWEB");
			play.addActionListener(new ButtonClickListener());

			play.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					boolean cond = true;
					int tailleCote = 8;

					if(joueurText.getText().isEmpty()){ cond = false; }

					try{   tailleCote = Integer.parseInt(nbText.getText());   }catch (NumberFormatException ex){ cond = false; }

					if(tailleCote < 8){ cond = false; }

					if(cond){

						String monAdresse = Web.obtenirMonAdresse();
						System.out.println("Mon Adresse : "+monAdresse);

						boolean wannaPlay = true;
						Object[] options = { "OK", "CANCEL" };
						Object selectedValue = JOptionPane.showOptionDialog(null, "Vous deverez patienter jusqu'� ce qu'un ami rejoigne"
								+ " votre partie,\n CLIQUEZ SUR OK AVANT QUE VOTRE AMI NE VALIDE VOTRE ADRESSE,\n sinon cliquez sur CANCEL."
								+ " L'adresse de votre partie est : "+monAdresse, "C�ation partie",
								JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
								null, options, options[0]);
						if(selectedValue.toString().equals("1")){
							wannaPlay = false;
						}

						if(wannaPlay){

							String ecoute  = Web.ecoutePaquets();
							String[] split = ecoute.split("_");

							String sonAdresse = split[0];
							String nomJoueur2 = split[1];

							String nomJoueur1 = joueurText.getText();

							mainFrame.dispose();
							AwtControl awtControl = new AwtControl("CREATEHEXAWEB_"+monAdresse+"_"+sonAdresse, tailleCote, nomJoueur1, nomJoueur2, "", "", "", "", "", "");
							awtControl.show("CREATEHEXAWEB", "", true);
						}else{

							mainFrame.dispose();
							AwtControl awtControl = new AwtControl("INTRO", 0, "", "", "", "", "", "", "", "");
							awtControl.show("INTRO", "", false);
						}
					}else{
						mainFrame.dispose();
						AwtControl awtControl = new AwtControl("WEB_ERROR", 0, "", "", "", "", "", "", "", "");
						awtControl.show("WEB_ERROR", "", false);
					}
				}
			} );

			gbc.anchor = GridBagConstraints.CENTER;

			gbc.gridx = 0; gbc.gridy = 0; 		controlPanel.add((Component) board, gbc);

			gbc.gridx = 0; gbc.gridy = 1; 		controlPanel.add(intro, gbc);

			gbc.gridx = 0; gbc.gridy = 2; 		controlPanel.add(vide1, gbc);

			gbc.anchor = GridBagConstraints.LINE_START;
			gbc.weightx = 0.25;

			gbc.gridx = 0; gbc.gridy = 3; 		controlPanel.add(nb, gbc);

			gbc.gridx = 1; gbc.gridy = 3; 		controlPanel.add(nbText, gbc);

			gbc.gridx = 0; gbc.gridy = 4; 		controlPanel.add(vide1, gbc);

			gbc.gridx = 0; gbc.gridy = 5; 		controlPanel.add(joueur, gbc);
			gbc.gridx = 1; gbc.gridy = 5; 		controlPanel.add(joueurText, gbc);

			gbc.gridx = 0; gbc.gridy = 6; 		controlPanel.add(vide1, gbc);

			gbc.gridx = 0; gbc.gridy = 7; 		controlPanel.add(play, gbc);

		}
		else if(this.board.getJoueur1() != null && this.board.getJoueur2() != null){	// Si on veut afficher un tableau qui n�cessite des boutons pour jouer...

			//Cr�ation des boutons et d�finition de leur couleur
			Button redButton 		= new Button("ROUGE");		redButton.setBackground(Color.red);
			Button orangeButton 	= new Button("ORANGE");		orangeButton.setBackground(Color.orange);
			Button yellowButton		= new Button("JAUNE");		yellowButton.setBackground(Color.yellow);
			Button greenButton 		= new Button("VERT");		greenButton.setBackground(Color.green);
			Button blueButton 		= new Button("BLEU");		blueButton.setBackground(Color.blue);
			Button magentaButton	= new Button("MAGENTA");	magentaButton.setBackground(Color.magenta);

			//Associaion des boutons � une commande propre et � un �couteur particulier (ButtonClickListener)
			redButton.setActionCommand("ROUGE");				redButton.addActionListener		(new ButtonClickListener()); 
			orangeButton.setActionCommand("ORANGE");			orangeButton.addActionListener	(new ButtonClickListener()); 
			yellowButton.setActionCommand("JAUNE");				yellowButton.addActionListener	(new ButtonClickListener());
			greenButton.setActionCommand("VERT");				greenButton.addActionListener	(new ButtonClickListener()); 
			blueButton.setActionCommand("BLEU");				blueButton.addActionListener	(new ButtonClickListener()); 
			magentaButton.setActionCommand("MAGENTA");			magentaButton.addActionListener (new ButtonClickListener()); 

			//Cr�ation d'un type d'agencement en "sac de grille" (GridBagLayout) et ajout des boutons et du tableau dans cette grille
			GridBagLayout layout = new GridBagLayout();

			controlPanel.setLayout(layout);						//Ajoute l'agencement au panneau de contr�le (l� o� l'on souhaite voir le tableau et les boutons)
			GridBagConstraints gbc = new GridBagConstraints();	//Cr�� des contraintes sur les �l�ments de la grille, ici des contraintes de positionnement :

			gbc.gridx = 0; gbc.gridy = 0; 		controlPanel.add((Component) board,gbc);

			gbc.fill = GridBagConstraints.HORIZONTAL;

			gbc.gridx = 0; gbc.gridy = 1; 		controlPanel.add(redButton,gbc); 

			gbc.gridx = 0; gbc.gridy = 2;		controlPanel.add(orangeButton,gbc); 

			gbc.gridx = 0; gbc.gridy = 3; 		controlPanel.add(yellowButton,gbc);  

			gbc.gridx = 0; gbc.gridy = 4; 		controlPanel.add(greenButton,gbc);

			gbc.gridx = 0; gbc.gridy = 5; 		controlPanel.add(blueButton,gbc);

			gbc.gridx = 0; gbc.gridy = 6; 		controlPanel.add(magentaButton,gbc);

			setAllowedButtons();
			setRestrictedButtons();
		}
		else {

			GridBagLayout layout = new GridBagLayout();

			controlPanel.setLayout(layout);						//Ajoute l'agencement au panneau de contr�le (l� o� l'on souhaite voir le tableau et les boutons)
			GridBagConstraints gbc = new GridBagConstraints();	//Cr�� des contraintes sur les �l�ments de la grille, ici des contraintes de positionnement :

			Label intro = new Label("Veuillez entrez les param�tres de la prochaine partie que vous souhaitez jouer :", Label.CENTER);
			intro.setForeground(Color.white);

			Label nb = new Label("Taille d'un c�t� :", Label.RIGHT);
			nb.setForeground(Color.white);
			final TextField nbText = new TextField(2);

			Label j1 = new Label("Entrez le nom du joueur 1 :");
			j1.setForeground(Color.white);
			final TextField j1Text = new TextField(16);

			Label j2 = new Label("Entrez le nom du joueur 2 :");
			j2.setForeground(Color.white);
			final TextField j2Text = new TextField(16);

			Label j3 = new Label("Entrez le nom du joueur 3 : (laissez vide si vous ne souhaitez pas en ajouter)");
			j3.setForeground(Color.white);
			final TextField j3Text = new TextField(16);

			Label j4 = new Label("Entrez le nom du joueur 4 : (laissez vide si vous ne souhaitez pas en ajouter)");
			j4.setForeground(Color.white);
			final TextField j4Text = new TextField(16);

			Label ia1 = new Label("IA du Joueur 1 :");
			ia1.setForeground(Color.white);

			Label ia2 = new Label("IA du Joueur 2 :");
			ia2.setForeground(Color.white);

			Label ia3 = new Label("IA du Joueur 3 :");
			ia3.setForeground(Color.white);

			Label ia4 = new Label("IA du Joueur 4 :");
			ia4.setForeground(Color.white);

			Label vide1 = new Label("");
			Label vide2 = new Label("");
			Label vide3 = new Label("");
			Label vide4 = new Label("");

			final Choice IAList1 = new Choice();
			final Choice IAList2 = new Choice();
			final Choice IAList3 = new Choice();
			final Choice IAList4 = new Choice();

			IAList1.add("Sans");		IAList2.add("Sans");		IAList3.add("Sans");		IAList4.add("Sans");
			IAList1.add("IA Simple");	IAList2.add("IA Simple");	IAList3.add("IA Simple");	IAList4.add("IA Simple");
			IAList1.add("IA Penible");	IAList2.add("IA Penible");	IAList3.add("IA Penible");	IAList4.add("IA Penible");
			IAList1.add("IA Difficile");IAList2.add("IA Difficile");IAList3.add("IA Difficile");IAList4.add("IA Difficile");

			Button play = new Button("Play");
			play.setActionCommand("PLAY");
			play.addActionListener(new ButtonClickListener());
			//version hexa
			if (tableau.equals("HEXA_PARAM") || tableau.equals("HEXA_ERROR")){

				play.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						boolean cond = okCreation(IAList1, IAList2, IAList3, IAList4, j1Text, j2Text, j3Text, j4Text, nbText);

						if(cond){
							mainFrame.dispose();
							AwtControl awtControl = new AwtControl("HEXA", Integer.parseInt(nbText.getText()), j1Text.getText(), j2Text.getText(), j3Text.getText(), j4Text.getText(),
									IAList1.getItem(IAList1.getSelectedIndex()), IAList2.getItem(IAList2.getSelectedIndex()), IAList3.getItem(IAList3.getSelectedIndex()), IAList4.getItem(IAList4.getSelectedIndex()));
							awtControl.show("HEXA", IAList1.getItem(IAList1.getSelectedIndex()), false);
						}else{
							mainFrame.dispose();
							AwtControl awtControlDemo = new AwtControl("HEXA_ERROR", 0, "", "", "", "", "", "", "", "");
							awtControlDemo.show("HEXA_ERROR", "", false);
						}
					}
				} );
			}
			//version carr�s
			else if(tableau.equals("SQUARE_PARAM") || tableau.equals("SQUARE_ERROR")){

				play.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						boolean cond = okCreation(IAList1, IAList2, IAList3, IAList4, j1Text, j2Text, j3Text, j4Text, nbText);

						if(cond){
							mainFrame.dispose();
							AwtControl awtControl = new AwtControl("SQUARE", Integer.parseInt(nbText.getText()), j1Text.getText(), j2Text.getText(), j3Text.getText(), j4Text.getText(),
									IAList1.getItem(IAList1.getSelectedIndex()), IAList2.getItem(IAList2.getSelectedIndex()), IAList3.getItem(IAList3.getSelectedIndex()), IAList4.getItem(IAList4.getSelectedIndex()));
							awtControl.show("SQUARE", IAList1.getItem(IAList1.getSelectedIndex()), false);
						}else{
							mainFrame.dispose();
							AwtControl awtControlDemo = new AwtControl("SQUARE_ERROR", 0, "", "", "", "", "", "", "", "");
							awtControlDemo.show("SQUARE_ERROR", "", false);
						}
					}
				} );
			}
			//version losanges
			else if(tableau.equals("DIAMOND_PARAM") || tableau.equals("DIAMOND_ERROR")){

				play.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						boolean cond = okCreation(IAList1, IAList2, IAList3, IAList4, j1Text, j2Text, j3Text, j4Text, nbText);

						if(cond){
							mainFrame.dispose();
							AwtControl awtControl = new AwtControl("DIAMOND", Integer.parseInt(nbText.getText()), j1Text.getText(), j2Text.getText(), j3Text.getText(), j4Text.getText(),
									IAList1.getItem(IAList1.getSelectedIndex()), IAList2.getItem(IAList2.getSelectedIndex()), IAList3.getItem(IAList3.getSelectedIndex()), IAList4.getItem(IAList4.getSelectedIndex()));
							awtControl.show("DIAMOND", IAList1.getItem(IAList1.getSelectedIndex()), false);
						}else{
							mainFrame.dispose();
							AwtControl awtControlDemo = new AwtControl("DIAMOND_ERROR", 0, "", "", "", "", "", "", "", "");
							awtControlDemo.show("DIAMOND_ERROR", "", false);
						}
					}
				} );
			}

			gbc.anchor = GridBagConstraints.CENTER;

			gbc.gridx = 0; gbc.gridy = 0; 		controlPanel.add((Component) board, gbc);

			gbc.gridx = 0; gbc.gridy = 1; 		controlPanel.add(intro, gbc);

			gbc.gridx = 0; gbc.gridy = 2; 		controlPanel.add(vide1, gbc);

			gbc.anchor = GridBagConstraints.LINE_START;
			gbc.weightx = 0.25;

			gbc.gridx = 0; gbc.gridy = 3; 		controlPanel.add(nb, gbc);

			gbc.gridx = 1; gbc.gridy = 3; 		controlPanel.add(nbText, gbc);

			gbc.gridx = 0; gbc.gridy = 4; 		controlPanel.add(vide2, gbc);

			gbc.gridx = 0; gbc.gridy = 5; 		controlPanel.add(j1, gbc);
			gbc.gridx = 1; gbc.gridy = 5; 		controlPanel.add(j1Text, gbc);

			gbc.gridx = 0; gbc.gridy = 6; 		controlPanel.add(j2, gbc);
			gbc.gridx = 1; gbc.gridy = 6; 		controlPanel.add(j2Text, gbc);

			gbc.gridx = 0; gbc.gridy = 7; 		controlPanel.add(j3, gbc);
			gbc.gridx = 1; gbc.gridy = 7; 		controlPanel.add(j3Text, gbc);

			gbc.gridx = 0; gbc.gridy = 8; 		controlPanel.add(j4, gbc);
			gbc.gridx = 1; gbc.gridy = 8; 		controlPanel.add(j4Text, gbc);

			gbc.gridx = 0; gbc.gridy = 9; 		controlPanel.add(vide3, gbc);

			gbc.gridx = 0; gbc.gridy = 10; 		controlPanel.add(ia1, gbc);
			gbc.gridx = 1; gbc.gridy = 10; 		controlPanel.add(IAList1, gbc);

			gbc.gridx = 0; gbc.gridy = 11; 		controlPanel.add(ia2, gbc);
			gbc.gridx = 1; gbc.gridy = 11; 		controlPanel.add(IAList2, gbc);

			gbc.gridx = 0; gbc.gridy = 12; 		controlPanel.add(ia3, gbc);
			gbc.gridx = 1; gbc.gridy = 12; 		controlPanel.add(IAList3, gbc);

			gbc.gridx = 0; gbc.gridy = 13; 		controlPanel.add(ia4, gbc);
			gbc.gridx = 1; gbc.gridy = 13; 		controlPanel.add(IAList4, gbc);

			gbc.gridx = 0; gbc.gridy = 14; 		controlPanel.add(vide4, gbc);

			gbc.gridx = 0; gbc.gridy = 15; 		controlPanel.add(play, gbc);
		}
	}

	/**
	 * Cette fonction v�rifie les conditions de cr�ation d'une partie 
	 * 
	 * @param IAList1	( Choice )		: <br>choix du type d'IA associ�e au joueur 1 	</br><br>
	 * @param IAList2	( Choice )		: <br>choix du type d'IA associ�e au joueur 2 	</br><br>
	 * @param IAList3	( Choice )		: <br>choix du type d'IA associ�e au joueur 3 	</br><br>
	 * @param IAList4	( Choice )		: <br>choix du type d'IA associ�e au joueur 4 	</br><br>
	 * @param j1Text	( TextField )	: <br>nom du joueur 1 							</br><br>
	 * @param j2Text	( TextField )	: <br>nom du joueur 2							</br><br>
	 * @param j3Text	( TextField )	: <br>nom du joueur 3							</br><br>
	 * @param j4Text	( TextField )	: <br>nom du joueur 4							</br><br>
	 * @param nbText	( TextField )	: <br>taille d'un c�t� du tableau � g�n�rer		</br><br>
	 * 
	 * @return	( boolean )	Retourne si oui ou non une partie peut �tre cr��e � partir des param�tres donn�s
	 */
	public static boolean okCreation(Choice IAList1, Choice IAList2, Choice IAList3, Choice IAList4, TextField j1Text, TextField j2Text, TextField j3Text, TextField j4Text, TextField nbText){
		
		boolean cond = true;

		if(!IAList1.getItem(IAList1.getSelectedIndex()).equals("Sans") && !IAList2.getItem(IAList2.getSelectedIndex()).equals("Sans")){

			if(j3Text.getText().isEmpty() && j4Text.getText().isEmpty()){ cond = false; }

			if(!j3Text.getText().isEmpty() && j4Text.getText().isEmpty()){

				if(!IAList3.getItem(IAList3.getSelectedIndex()).equals("Sans")){ cond = false; }
			}

			if(j3Text.getText().isEmpty() && !j4Text.getText().isEmpty()){

				if(!IAList4.getItem(IAList4.getSelectedIndex()).equals("Sans")){ cond = false; }
			}

			if(!j3Text.getText().isEmpty() && !j4Text.getText().isEmpty()){

				if(!IAList3.getItem(IAList3.getSelectedIndex()).equals("Sans") && !IAList4.getItem(IAList4.getSelectedIndex()).equals("Sans")){ cond = false; }
			}
		}

		if(j1Text.getText().equals(j2Text.getText()) || j1Text.getText().equals(j3Text.getText()) || j1Text.getText().equals(j4Text.getText())
				|| j2Text.getText().equals(j3Text.getText()) || j2Text.getText().equals(j4Text.getText()) ){ cond = false; }

		if(!j3Text.getText().isEmpty() && !j4Text.getText().isEmpty()){
			if(j3Text.getText().equals(j4Text.getText())){
				cond = false;
			}
		}

		int tailleCote = 8;

		try{   tailleCote = Integer.parseInt(nbText.getText());   }catch (NumberFormatException ex){ cond = false; }

		if(tailleCote < 8){ cond = false; }

		return cond;
	}

	/**
	 * Cette fonction param�tre et affiche la barre de menu
	 * 
	 * @see #MenuItemListener
	 */
	private void setMenu(){

		//Cr�ation de la barre de menus
		final MenuBar menuBar = new MenuBar();

		//Cr�ation des menus
		Menu fileMenu = new Menu("File");
		Menu homeMenu = new Menu("Home");
		Menu playMenu = new Menu("Play");
		Menu webMenu  = new Menu("Web");

		//Cr�ation des items du menu "File"
		MenuItem openMenuItem = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O));
		openMenuItem.setActionCommand("OPEN");

		MenuItem saveMenuItem = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
		saveMenuItem.setActionCommand("SAVE");

		MenuItem exitMenuItem = new MenuItem("Exit");
		exitMenuItem.setActionCommand("EXIT");

		//Cr�ation de l'item du menu "Home"
		MenuItem homeMenuItem = new MenuItem("Retour � l'accueil");
		homeMenuItem.setActionCommand("INTRO");

		//Cr�ation des items du menu "Play"
		MenuItem hexaMenuItem = new MenuItem("Hexa");
		hexaMenuItem.setActionCommand("HEXA_PARAM");
		MenuItem squareMenuItem = new MenuItem("Square");
		squareMenuItem.setActionCommand("SQUARE_PARAM");
		MenuItem diamondMenuItem = new MenuItem("Diamond");
		diamondMenuItem.setActionCommand("DIAMOND_PARAM");

		//Cr�ation des items du menu "Web"
		MenuItem createGameMenuItem = new MenuItem("Cr�er partie");
		createGameMenuItem.setActionCommand("CREATEGAME");

		MenuItem joinGameMenuItem = new MenuItem("Rejoindre partie");
		joinGameMenuItem.setActionCommand("JOINGAME");

		//Cr�ation d'un �couteur d'item, et mise sur �coute des items cr��s
		MenuItemListener menuItemListener = new MenuItemListener();


		openMenuItem.addActionListener(menuItemListener);
		saveMenuItem.addActionListener(menuItemListener);
		exitMenuItem.addActionListener(menuItemListener);
		homeMenuItem.addActionListener(menuItemListener);
		hexaMenuItem.addActionListener(menuItemListener);
		squareMenuItem.addActionListener(menuItemListener);
		diamondMenuItem.addActionListener(menuItemListener);
		createGameMenuItem.addActionListener(menuItemListener);
		joinGameMenuItem.addActionListener(menuItemListener);

		//Ajout des items au menu "File"
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);

		//Ajout de l'item au menu "Home"
		homeMenu.add(homeMenuItem);

		//AJout des items au menu "Play"
		playMenu.add(hexaMenuItem);
		playMenu.add(squareMenuItem);
		playMenu.add(diamondMenuItem);

		//Ajout des item au menu "Web"
		webMenu.add(createGameMenuItem);
		webMenu.add(joinGameMenuItem);

		//Ajout des menus � la barre de menus
		menuBar.add(fileMenu);
		menuBar.add(homeMenu);
		menuBar.add(playMenu);
		menuBar.add(webMenu);

		//Ajout de la barre de menu au cadre
		mainFrame.setMenuBar(menuBar);

		mainFrame.setVisible(true);  
	}

	/**
	 * Cette fonction restreint l'utilisation des boutons selon les couleurs des joueurs
	 * 
	 *  @see #getColorsFromPlayers()
	 */
	public void setRestrictedButtons(){

		ArrayList<Color> couleursOccupees = board.getColorsFromPlayers();

		for(int i = 0; i < couleursOccupees.size(); i++){

			if(couleursOccupees.get(i) == Color.red){
				controlPanel.getComponent(1).setBackground(Color.black);
				controlPanel.getComponent(1).setEnabled(false);
			}
			if(couleursOccupees.get(i) == Color.orange){
				controlPanel.getComponent(2).setBackground(Color.black);
				controlPanel.getComponent(2).setEnabled(false);
			}
			if(couleursOccupees.get(i) == Color.yellow){
				controlPanel.getComponent(3).setBackground(Color.black);
				controlPanel.getComponent(3).setEnabled(false);
			}
			if(couleursOccupees.get(i) == Color.green){
				controlPanel.getComponent(4).setBackground(Color.black);
				controlPanel.getComponent(4).setEnabled(false);
			}
			if(couleursOccupees.get(i) == Color.blue){
				controlPanel.getComponent(5).setBackground(Color.black);
				controlPanel.getComponent(5).setEnabled(false);
			}
			if(couleursOccupees.get(i) == Color.magenta){
				controlPanel.getComponent(6).setBackground(Color.black);
				controlPanel.getComponent(6).setEnabled(false);
			}
		}
	}

	/**
	 * Cette fonction permet d'autoriser l'utilisation des boutons selon les couleurs des joueurs 
	 * 
	 * @see #setAllowedButtons()
	 */
	public void setAllowedButtons(){

		ArrayList<Color> couleursLibres = board.getFreeColors();

		for(int i = 0; i < couleursLibres.size(); i++){

			if(couleursLibres.get(i) == Color.red){
				controlPanel.getComponent(1).setBackground(Color.red);
				controlPanel.getComponent(1).setEnabled(true);
			}
			if(couleursLibres.get(i) == Color.orange){
				controlPanel.getComponent(2).setBackground(Color.orange);
				controlPanel.getComponent(2).setEnabled(true);
			}
			if(couleursLibres.get(i) == Color.yellow){
				controlPanel.getComponent(3).setBackground(Color.yellow);
				controlPanel.getComponent(3).setEnabled(true);
			}
			if(couleursLibres.get(i) == Color.green){
				controlPanel.getComponent(4).setBackground(Color.green);
				controlPanel.getComponent(4).setEnabled(true);
			}
			if(couleursLibres.get(i) == Color.blue){
				controlPanel.getComponent(5).setBackground(Color.blue);
				controlPanel.getComponent(5).setEnabled(true);
			}
			if(couleursLibres.get(i) == Color.magenta){
				controlPanel.getComponent(6).setBackground(Color.magenta);
				controlPanel.getComponent(6).setEnabled(true);
			}
		}
	}

	/**
	 * Cette fonction met fin � la partie
	 */
	public void setAllButtonsToBlack(){

		controlPanel.getComponent(1).setBackground(Color.black);
		controlPanel.getComponent(1).setEnabled(false);

		controlPanel.getComponent(2).setBackground(Color.black);
		controlPanel.getComponent(2).setEnabled(false);

		controlPanel.getComponent(3).setBackground(Color.black);
		controlPanel.getComponent(3).setEnabled(false);

		controlPanel.getComponent(4).setBackground(Color.black);
		controlPanel.getComponent(4).setEnabled(false);

		controlPanel.getComponent(5).setBackground(Color.black);
		controlPanel.getComponent(5).setEnabled(false);

		controlPanel.getComponent(6).setBackground(Color.black);
		controlPanel.getComponent(6).setEnabled(false);
	}

	/**
	 * Cette classe interne g�re l'�coute des boutons et les actions � prendre selon lesquels sont cliqu�s
	 */
	private class ButtonClickListener implements ActionListener{

		/**
		 * Cette fonction g�re les actions � lancer selon le bouton cliqu�
		 * 
		 * @param e	( ActionEvent ) : <br>action associ� au click </br><br>
		 */
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();

			if(board.isTheGameOver() && !command.equals("PLAY")){

				setAllButtonsToBlack();

			}else{

				if( command.equals( "ROUGE"	)) {
					Player nextPlayer = board.nextMove(Color.red);
					setAllowedButtons();
					setRestrictedButtons();
					
					 if ( board instanceof HexaWebBoard) {
						 HexaWebBoard hxb = (HexaWebBoard) board;   hxb.waitAndListen();
						 setAllowedButtons();   setRestrictedButtons();
					 }

					while(nextPlayer != null){
						if(nextPlayer.getIA().equals("IA Simple")){		nextPlayer = board.nextMove(board.nextEasyIAMove());				}
						else if(nextPlayer.getIA().equals("IA Penible")){	nextPlayer = board.nextMove(board.nextTroubleIAMove(nextPlayer));	}
						else if(nextPlayer.getIA().equals("IA Difficile")){	nextPlayer = board.nextMove(board.nextHardIAMove(nextPlayer));		}
						setAllowedButtons();
						setRestrictedButtons();
					}
				}
				else if( command.equals( "ORANGE")) {
					Player nextPlayer = board.nextMove(Color.orange);
					setAllowedButtons();
					setRestrictedButtons();
					
					if ( board instanceof HexaWebBoard) {
						 HexaWebBoard hxb = (HexaWebBoard) board;   hxb.waitAndListen();
						 setAllowedButtons();   setRestrictedButtons();
					 }

					while(nextPlayer != null){
						if(nextPlayer.getIA().equals("IA Simple")){		nextPlayer = board.nextMove(board.nextEasyIAMove());				}
						else if(nextPlayer.getIA().equals("IA Penible")){	nextPlayer = board.nextMove(board.nextTroubleIAMove(nextPlayer));	}
						else if(nextPlayer.getIA().equals("IA Difficile")){	nextPlayer = board.nextMove(board.nextHardIAMove(nextPlayer));		}
						setAllowedButtons();
						setRestrictedButtons();
					}
				}
				else if( command.equals( "JAUNE"	)) {
					Player nextPlayer = board.nextMove(Color.yellow);
					setAllowedButtons();
					setRestrictedButtons();
					
					if ( board instanceof HexaWebBoard) {
						 HexaWebBoard hxb = (HexaWebBoard) board;   hxb.waitAndListen();
						 setAllowedButtons();   setRestrictedButtons();
					 }

					while(nextPlayer != null){
						if(nextPlayer.getIA().equals("IA Simple")){		nextPlayer = board.nextMove(board.nextEasyIAMove());				}
						else if(nextPlayer.getIA().equals("IA Penible")){	nextPlayer = board.nextMove(board.nextTroubleIAMove(nextPlayer));	}
						else if(nextPlayer.getIA().equals("IA Difficile")){	nextPlayer = board.nextMove(board.nextHardIAMove(nextPlayer));		}
						setAllowedButtons();
						setRestrictedButtons();
					}
				}
				else if( command.equals( "VERT"  )) {
					Player nextPlayer = board.nextMove(Color.green);
					setAllowedButtons();
					setRestrictedButtons();
					
					if ( board instanceof HexaWebBoard) {
						 HexaWebBoard hxb = (HexaWebBoard) board;   hxb.waitAndListen();
						 setAllowedButtons();   setRestrictedButtons();
					 }

					while(nextPlayer != null){
						if(nextPlayer.getIA().equals("IA Simple")){		nextPlayer = board.nextMove(board.nextEasyIAMove());				}
						else if(nextPlayer.getIA().equals("IA Penible")){	nextPlayer = board.nextMove(board.nextTroubleIAMove(nextPlayer));	}
						else if(nextPlayer.getIA().equals("IA Difficile")){	nextPlayer = board.nextMove(board.nextHardIAMove(nextPlayer));		}
						setAllowedButtons();
						setRestrictedButtons();
					}
				}
				else if( command.equals( "BLEU"  )) {
					Player nextPlayer = board.nextMove(Color.blue);
					setAllowedButtons();
					setRestrictedButtons();
					
					if ( board instanceof HexaWebBoard) {
						 HexaWebBoard hxb = (HexaWebBoard) board;   hxb.waitAndListen();
						 setAllowedButtons();   setRestrictedButtons();
					 }

					while(nextPlayer != null){
						if(nextPlayer.getIA().equals("IA Simple")){		nextPlayer = board.nextMove(board.nextEasyIAMove());				}
						else if(nextPlayer.getIA().equals("IA Penible")){	nextPlayer = board.nextMove(board.nextTroubleIAMove(nextPlayer));	}
						else if(nextPlayer.getIA().equals("IA Difficile")){	nextPlayer = board.nextMove(board.nextHardIAMove(nextPlayer));		}
						setAllowedButtons();
						setRestrictedButtons();
					}
				}
				else if( command.equals( "MAGENTA"	)) {
					Player nextPlayer = board.nextMove(Color.magenta);
					setAllowedButtons();
					setRestrictedButtons();
					
					if ( board instanceof HexaWebBoard) {
						 HexaWebBoard hxb = (HexaWebBoard) board;   hxb.waitAndListen();
						 setAllowedButtons();   setRestrictedButtons();
					 }

					while(nextPlayer != null){
						if(nextPlayer.getIA().equals("IA Simple")){		nextPlayer = board.nextMove(board.nextEasyIAMove());				}
						else if(nextPlayer.getIA().equals("IA Penible")){	nextPlayer = board.nextMove(board.nextTroubleIAMove(nextPlayer));	}
						else if(nextPlayer.getIA().equals("IA Difficile")){	nextPlayer = board.nextMove(board.nextHardIAMove(nextPlayer));		}
						setAllowedButtons();
						setRestrictedButtons();
					}
				}
			}
		}
	}

	/**
	 * Cette classe interne g�re l'�coute des menus et les actions � prendre selon lesquels sont cliqu�s
	 */
	class MenuItemListener implements ActionListener {

		/**
		 * Cette fonction g�re les actions � lancer selon l'item de menu cliqu�
		 * 
		 * @param e	( ActionEvent ) : <br>action associ� au click </br><br>
		 */
		public void actionPerformed(ActionEvent e) {

			String command = e.getActionCommand();

			if(command.equals("HEXA_PARAM")){

				mainFrame.dispose();
				AwtControl awtControlDemo = new AwtControl(command, 0, "", "", "", "", "", "", "", "");
				awtControlDemo.show(command, "", false);
			}
			if(command.equals("SQUARE_PARAM")){

				mainFrame.dispose();
				AwtControl awtControlDemo = new AwtControl(command, 0, "", "", "", "", "", "", "", "");
				awtControlDemo.show(command, "", false);
			}
			if(command.equals("DIAMOND_PARAM")){

				mainFrame.dispose();
				AwtControl awtControlDemo = new AwtControl(command, 0, "", "", "", "", "", "", "", "");
				awtControlDemo.show(command, "", false);
			}
			if(command.equals("INTRO")){
				mainFrame.dispose();
				AwtControl awtControlDemo = new AwtControl(command, 0, "", "", "", "", "", "", "", "");
				awtControlDemo.show(command, "", false);
			}
			if(command.equals("SAVE")){

				if(board.getJoueur1() == null){
					JOptionPane.showMessageDialog(null, "Vous ne pouvez pas sauvegarder de partie avant que "
							+ "celle-ci ne commence !");
				}
				else{
					Frame frame = new Frame("The Filler Game");
					frame.setSize(1440,1440);

					FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.SAVE);
					fd.setDirectory("C:\\");
					fd.setFile("*.txt");
					fd.setVisible(true);

					String directoryName = fd.getDirectory();
					String fileName = fd.getFile();
					if (directoryName == null)	{   System.out.println("You cancelled your choice");   }
					else						{
						System.out.println("You chose "+directoryName+fileName);

						String saveStr = board.generateSaveString();

						try {
							File f = new File(directoryName+fileName);
							f.createNewFile();

							FileWriter fw = new FileWriter(f);

							BufferedWriter bw = new BufferedWriter(fw);

							bw.write(saveStr);

							bw.close();
							fw.close();

						} catch (IOException e1) {  e1.printStackTrace(); }
					}
				}
			}
			if(command.equals("OPEN")){

				Frame frame = new Frame("The Filler Game");
				frame.setSize(1440,1440);

				FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
				fd.setDirectory("C:\\");
				fd.setFile("*.txt");
				fd.setVisible(true);

				String directoryName = fd.getDirectory();
				String fileName = fd.getFile();

				if (directoryName == null)	{   System.out.println("You cancelled your choice");   }
				else						{
					System.out.println("You chose "+directoryName+fileName);
					String saveStr = "";

					try {
						BufferedReader buffer = new BufferedReader(new FileReader(directoryName+fileName));

						String ligne;  
						while ((ligne = buffer.readLine()) != null) {  

							saveStr += ligne+"\r\n";
						}
						buffer.close();

					} catch (IOException e1) { e1.printStackTrace(); }

					mainFrame.dispose();
					AwtControl awtControl = new AwtControl(saveStr, 0, "", "", "", "", "", "", "", "");
					awtControl.show("", "", false);
				}
			}

			if(command.equals("CREATEGAME")){

				mainFrame.dispose();
				AwtControl awtControl = new AwtControl("WEB", 0, "", "", "", "", "", "", "", "");
				awtControl.show("WEBCREATE", "", false);
			}
			
			if(command.equals("JOINGAME")){
				
				mainFrame.dispose();
				AwtControl awtControl = new AwtControl("WEB", 0, "", "", "", "", "", "", "", "");
				awtControl.show("WEBJOIN", "", false);
			}
			
			if(command.equals("EXIT")){
				mainFrame.dispose();
			}
		}    
	}
}