package composantsgraphiques;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import astre.Astre;
import astre.Etoile;
import astre.Planete;
import astre.TypeAstre;
import calcul.Vecteur;
import ecouteurs.ResultatsListener;

/**
 * Classe qui gère la zone de dessin.
 * @author Mohammed Ariful Islam
 *
 */
public class SceneUnivers extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private ArrayList<Astre> astre = new ArrayList<Astre>();
	boolean animationEnCours = false;
	private boolean formeSelctionnee = false;


	private double largeurMonde = 4e11;
	private final double largeurMondeInitiale = largeurMonde;
	private double largeurMondeMax = 1e20;
	private double largeurMondeMin = 1e5;
	private double deplacement;
	private double zoom;
	private double hauteurMonde;
	private double deltaT = 5000;
	private double posX, posY;
	private double posXAvant, posYAvant;

	private final double G = 6.67e-11; //Constante gravitationnelle

	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();
	private double tempsEcoule = 0;
	private double vitesseCourant;
	private double accelerationCourant;
	private double fgCourant;
	private double champ;
	private boolean estSlect = false;
	private boolean dejaSlect = false;
	private GestionAnimationCollision collision = new GestionAnimationCollision();
	
	private int posGradHaut = 10;
	private int posGradLarg = 10;
	
	

	/**
	 * Constructeur qui initialise la scnène
	 * 
	 */
	public SceneUnivers() {
		addKeyListener(new KeyAdapter() {
			//déplace la camera
			@Override
			public void keyPressed(KeyEvent arg0) {
				posXAvant = posX;
				posYAvant = posY;
				deplacement = 0.05*largeurMonde;
				if (arg0.getKeyCode() == KeyEvent.VK_UP) {
					System.out.println(arg0.getKeyCode());
					posY -= deplacement;
				}
				if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					posY += deplacement;
				}
				if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
					posX += deplacement;
				}
				if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
					posX -= deplacement;
				}
				for (Astre astreCourant: astre) {
					astreCourant.setX(astreCourant.getX() - posXAvant + posX);
					astreCourant.setY(astreCourant.getY() - posYAvant + posY);
				}
				repaint();
			}

		});
		addMouseWheelListener(new MouseWheelListener() {
			//Zoom
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				zoom = 0.1*largeurMonde;
				if (arg0.getWheelRotation() > 0) {
					if (largeurMonde < largeurMondeMax) {
						largeurMonde += zoom;
					}					
				} else {
					if (largeurMonde > largeurMondeMin) {
					largeurMonde -= zoom;
					}
				}
				repaint();
			}

		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				for (int i = 0; i < astre.size(); i++) {
					if (astre.get(i).contient(arg0.getX(), arg0.getY())) {
						formeSelctionnee = true;
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				formeSelctionnee = false;
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				dejaSlect = false;
				for (Astre astreCourant: astre) {
					if (!astreCourant.getEstDeplacable() && astreCourant.contient(arg0.getX(), arg0.getY())) {
						astreCourant.setEstSelectionne(true);
					
						if (!dejaSlect) {
							dejaSlect = true;
							estSlect = true;
						}
					} else {
						if (!dejaSlect) {
							estSlect = false;
						}
						astreCourant.setEstSelectionne(false);
					}
				}
				leverEvenSelect();
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {

			//cliquer-glisser un astre
			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (formeSelctionnee) {
					for (Astre astreCourant: astre) {
						if (astreCourant.getEstDeplacable()) {
							astreCourant.setX(regleDeTrois(getWidth(), largeurMonde, arg0.getX()));
							astreCourant.setY(regleDeTrois(getHeight(), hauteurMonde, arg0.getY()));
							repaint();
						}
					}
				}
			}
		});


		setBackground(Color.BLACK);
		demarrer();

	}

	/**
	 * Dessine les éléments de la scène.
	 * 
	 */
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		ModeleAffichage modele = new ModeleAffichage(getWidth(), getHeight(), largeurMonde);
		AffineTransform mat = modele.getMatMC();
		hauteurMonde = modele.getHautUnitesReelles();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < astre.size(); i++) {
			if (astre.get(i).getType() != TypeAstre.ETOILE) {
				astre.get(i).origineVecteurVitesse(regleDeTrois(largeurMonde, getWidth(), astre.get(i).getX()), regleDeTrois(hauteurMonde, getHeight(), astre.get(i).getY()));
			}
			astre.get(i).dessiner(g2d, mat);

		}
		
		collision.dessiner(g2d, mat);
		dessinerEchelle(g2d);

	}

	/**
	 * Dessiner les échelles de la scène.
	 * @param g2d Contexte graphique
	 */
	private void dessinerEchelle(Graphics2D g2d) {
		DecimalFormat fomatteur = new DecimalFormat("0.###E0");
		//longueur
		g2d.setColor(Color.RED);
		g2d.draw(new Line2D.Double(0, getHeight() - posGradLarg, getWidth(), getHeight() - posGradLarg));
		g2d.draw(new Line2D.Double(0, getHeight() - posGradLarg*2, 0, getHeight()));
		g2d.draw(new Line2D.Double(getWidth()-1, getHeight() - posGradLarg*2, getWidth()-1, getHeight()));
		g2d.drawString(fomatteur.format(largeurMonde) + "m", getWidth()/2, getHeight() - posGradLarg);
		
		//hauteur
		g2d.draw(new Line2D.Double(posGradHaut, 0, posGradHaut, getHeight()));
		g2d.draw(new Line2D.Double(0, 0, posGradHaut*2, 0));
		g2d.draw(new Line2D.Double(0, getHeight()-1, posGradHaut*2, getHeight()-1));
		g2d.rotate(Math.toRadians(90));
		g2d.drawString(fomatteur.format(hauteurMonde) + "m", getHeight()/2, 0);

	}

	/**
	 * Gère l'animation.
	 * 
	 */
	@Override
	public void run() {
		
		while (animationEnCours) {
			if (astre.size() != 0) {
				tempsEcoule += deltaT;
				leverEvenTemps();
				for (int i = 0; i < astre.size(); i++) {
					calculerFg();
					astre.get(i).unPasRK4Partie1(deltaT);
					calculerFg();
					astre.get(i).unPasRK4Partie2(deltaT);
					calculerFg();
					astre.get(i).unPasRK4Partie3(deltaT);
					calculerFg();
					astre.get(i).unPasRK4Partie4(deltaT);
					
					if (astre.get(i).getEstSelectionne()) {
						vitesseCourant = astre.get(i).getVecteurVitesse().module();
						accelerationCourant = astre.get(i).getAcceleration();
						fgCourant = astre.get(i).getFg();
						champ = astre.get(i).getChampGravite();
						leverEvenVitesse();
						leverEvenAcceleration();
						leverEvenFg();
						leverChamp();
					}

				}
				verifierCollision();
			}
			repaint();
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Verifie si des astres sont entrés en collision
	 */
	private void verifierCollision() {
		for (int i = 0; i < astre.size(); i++) {
			if (!astre.get(i).getEstDeplacable() && astre.get(i).getType() == TypeAstre.PLANETE) {
				for (int j = 0; j < astre.size(); j++) {
					if (!astre.get(j).getEstDeplacable() && astre.get(j).getType() == TypeAstre.ETOILE) {
						if (i!=j && astre.get(i).instersect(astre.get(j).getShape())) {
							collision.ajouterCollision(astre.get(i).getX(), astre.get(i).getY(), astre.get(i).getRayon());
							astre.remove(i);
							i--;
							j = astre.size();
						}
					}
				}
			}
		}
	}

	/**
	 * Démarre l'animation.
	 */
	public void demarrer() {
		if (!animationEnCours) { 
			Thread animation = new Thread( this);
			animation.start();
			animationEnCours = true;	
		}
	}

	/**
	 * Ajoute une planète dans la scène avec les paramètres spécifiés.
	 * @param masse La masse de la planète.
	 * @param rayon Le rayon de la planète.
	 * @param vitesse La vitesse initiale de la planète.
	 * @param angle L'angle de lancement de la planète.
	 */
	public void ajouterPlanete(double masse, double rayon, double vitesse, int angle) {
		astre.add(new Planete(largeurMonde/2, rayon*2, masse, rayon, vitesse, angle));
		
		repaint();
	}

	/**
	 * Ajoute une étoile dans la scène avec les paramètres spécifiés.
	 * @param masse La masse de l'étoile.
	 * @param rayon Le rayon de l'étoile.
	 */
	public void ajouterEtoile(double masse, double rayon) {
		astre.add(new Etoile(largeurMonde/2, hauteurMonde/2, masse, rayon));
		repaint();
	}

	/**
	 * Supprime le dernier astre ajouter dans la scène.
	 */
	public void annuler() {
		if (astre.size() > 0) {
			astre.remove(astre.size() - 1);
		}
		repaint();
	}

	/**
	 * Supprime l'astre sélectionné de la scène.
	 */
	public void supprimer() {
		for (int i = 0; i < astre.size(); i++) {
			if (astre.get(i).getEstSelectionne()) {
				astre.remove(i);
			}
		}
		estSlect = false;
		leverEvenSelect();
		repaint();
	}

	/**
	 * Fait un calcul d = a*c/b de règle de trois avec les nombres en paramètre .
	 * 
	 * @param a Valeur a dans la formule.
	 * @param b Valeur b dans la formule.
	 * @param c Valeur c dans la formule.
	 * @return La valeur d dans la formule.
	 */
	private double regleDeTrois(double a, double b, double c) {
		return b*c/a;
	}

	/**
	 * Calcule la force gravitationnlle exercée sur les planètes par les étoiles. 
	 */
	private void calculerFg() {
		if (astre.size() >= 2) {
			for (int i = 0; i < astre.size(); i++) {
				Vecteur fg = new Vecteur(0, 0);
				if (astre.get(i).getType() == TypeAstre.PLANETE) {
					for (int j = 0; j < astre.size(); j++) {
						if (i!=j && !astre.get(j).getEstDeplacable()) {
							Vecteur r1 = new Vecteur(astre.get(i).getX(), astre.get(i).getY());
							Vecteur r2 = new Vecteur(astre.get(j).getX(), astre.get(j).getY());
							fg = fg.additionne(r1.soustrait(r2).multiplie(-G*astre.get(i).getMasse()*astre.get(j).getMasse()/(Math.pow(r1.soustrait(r2).module(), 3))));
							astre.get(i).setFg(fg);
						}
					}
				}
			}
		}
	}

	/**
	 * Place l'astre dans la scène pour être animée.
	 */
	public void lancer() {
		if (astre.size() != 0 ) {
			astre.get(astre.size() - 1).setEstDeplacable(false);
		}
	}

	/**
	 * Arrête l'animation.
	 */
	public void arreter() {
		animationEnCours = false;
	}

	/**
	 * Réinitialise la scène de simulation.
	 */
	public void recommencer() {
		largeurMonde = largeurMondeInitiale;
		tempsEcoule = 0;
		leverEvenTemps();
		astre.clear();
		repaint();
		demarrer();
	}

	/**
	 * Ajoute les écouteurs personnalisés.
	 * @param objEcouteur Les écouteurs.
	 */
	public void addResultatsListener(ResultatsListener objEcouteur) {
		OBJETS_ENREGISTRES.add(ResultatsListener.class, objEcouteur);
	}

	/**
	 * Écouteur de la vitesse levé à chaque pas d'animation.
	 */
	private void leverEvenVitesse() {
		for(ResultatsListener ecout : OBJETS_ENREGISTRES.getListeners(ResultatsListener.class) ) {
			ecout.vitesseAstre(vitesseCourant);
		}
	}

	/**
	 * Écouteur de l'accélération levé à chaque pas d'animation.
	 */
	private void leverEvenAcceleration() {
		for(ResultatsListener ecout : OBJETS_ENREGISTRES.getListeners(ResultatsListener.class) ) {
			ecout.accelerationAstre(accelerationCourant);
		}
	}

	/**
	 * Écouteur de la force gravitationnelle levé à chaque pas d'animation.
	 */
	private void leverEvenFg() {
		for(ResultatsListener ecout : OBJETS_ENREGISTRES.getListeners(ResultatsListener.class) ) {
			ecout.fgAstre(fgCourant);

		}
	}

	/**
	 * Écouteur du champ gravitationnelle levé à chaque pas d'animation.
	 */
	private void leverChamp() {
		for(ResultatsListener ecout : OBJETS_ENREGISTRES.getListeners(ResultatsListener.class) ) {
			ecout.champGravite(champ);
		}
	}

	/**
	 * Écouteur du temps levé à chaque pas d'animation.
	 */
	private void leverEvenTemps() {
		for(ResultatsListener ecout : OBJETS_ENREGISTRES.getListeners(ResultatsListener.class) ) {
			ecout.tempsEcoule(tempsEcoule);
		}
	}
	
	/**
	 * Écouteur qui détermine si un astre est sélectionné.
	 */
	private void leverEvenSelect() {
		for(ResultatsListener ecout : OBJETS_ENREGISTRES.getListeners(ResultatsListener.class) ) {
			ecout.astreSelect(estSlect);
		}
	}

	/**
	 * Change la masse du dernier astre ajouté.
	 * @param nouvelleMasse La nouvelle masse de l'astre
	 */
	public void changerMasse(double nouvelleMasse) {
		if (astre.size() != 0) {
			astre.get(astre.size() - 1).setMasse(nouvelleMasse);
			repaint();
		}
	}

	/**
	 * Change le rayon du dernier astre ajouté.
	 * @param nouveauRayon Le nouveau rayon de l'astre
	 */
	public void changerRayon(double nouveauRayon) {
		if (astre.size() != 0) {
			astre.get(astre.size() - 1).setRayon(nouveauRayon);
			repaint();
		}
	}

	/**
	 * Change la vitesse et l'angle de lancement du dernier astre ajouté.
	 * @param nouvelleVitesse La nouvelle vitesse de l'astre
	 * @param angle Le nouveau angle de la vitesse de l'astre
	 */
	public void changerVitesse(double nouvelleVitesse, double angle) {
		if (astre.size() != 0) {
			astre.get(astre.size() - 1).setVecteurVitesse(new Vecteur(nouvelleVitesse*Math.cos(Math.toRadians(angle)), nouvelleVitesse*-Math.sin(Math.toRadians(angle))));
			repaint();
		}
	}

	/**
	 * Change l'angle de lancement de l'astre en train de se faire ajouter
	 * @param angle Le nouveau angle.
	 */
	public void changerAngle(int angle) {
		if (astre.size() != 0) {
			astre.get(astre.size() - 1).setAngle(angle);
			repaint();
		}
	}

	/**
	 * Charge la première scène préréglé.
	 */
	public void preset1() {
		recommencer();
		largeurMonde = 1e12;
		hauteurMonde = regleDeTrois(getWidth(), largeurMonde, getHeight());
		repaint();

		ajouterEtoile(2e30, 7e8);
		lancer();
		astre.add(new Planete(largeurMonde/2, hauteurMonde/2 - 1.5e11, 5e24, 6e6, 30000, 0));
		lancer();

		repaint();
	}

	/**
	 * Charge la deuxième scène préréglé.
	 */
	public void preset2() {
		recommencer();
		largeurMonde = 4e11;
		hauteurMonde = regleDeTrois(getWidth(), largeurMonde, getHeight());
		repaint();

		astre.add(new Etoile(3*largeurMonde/4, hauteurMonde/2, 2e30, 7e8));
		lancer();
		astre.add(new Etoile(largeurMonde/4, hauteurMonde/2, 2e30, 7e8));
		lancer();
		astre.add(new Planete(largeurMonde/2, 6e6*500, 5e24, 6e6, 0, 0));
		lancer();
		
		repaint();
	}

	/**
	 * Charge la troisième scène préréglé.
	 */
	public void preset3() {
		recommencer();
		largeurMonde = 4e11;
		hauteurMonde = regleDeTrois(getWidth(), largeurMonde, getHeight());
		repaint();

		astre.add(new Etoile(largeurMonde/2 - 1.5e11/2, hauteurMonde/2, 2e30, 7e8));
		lancer();
		astre.add(new Etoile(largeurMonde/2 + 1.5e11/2, hauteurMonde/2, 2e30, 7e8));
		lancer();
		astre.add(new Planete(largeurMonde/2 - 1.5e11, hauteurMonde/2, 5e24, 6e6, 35000, 90));
		lancer();
		
		repaint();
	}

}