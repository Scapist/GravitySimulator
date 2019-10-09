package astre;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import calcul.Vecteur;
import calcul.VecteurGraphique;
import interfaces.Dessinable;

/**
 * Class qui d�finie un astre.
 * @author Mohammed Ariful Islam
 *
 */
public abstract class Astre implements Dessinable{
	double x, y;
	Vecteur xy;
	Vecteur vecteurVitesse;
	VecteurGraphique vecteurVitesseGraphique;
	double rayon;
	double masse;
	double champGravite;
	Vecteur fg = new Vecteur(0, 0);
	Vecteur acceleration = new Vecteur(0, 0);
	TypeAstre type;
	boolean estDeplacable = true;
	boolean estSelectionne = false;
	int angle;
	Shape cercleFantome;

	private final double G = 6.67e-11; //Constante gravitationnelle

	//N�cessaire pour completer l'algorithme RK4
	Vecteur posI;
	Vecteur vI;
	Vecteur aI;
	Vecteur aMid;
	Vecteur a2;
	Vecteur a1;

	/**
	 * Cr�e un astre avec les param�tres sp�cifi�s.
	 * @param x La position en x de l'astre.
	 * @param y La position en y de l'astre.
	 * @param masse La masse de l'astre.
	 * @param rayon Le rayon de l'astre.
	 * @param vitesse La vitesse de l'astre.
	 * @param angle L'angle de lancement de l'astre.
	 */
	public Astre(double x, double y, double masse, double rayon, double vitesse, int angle) {
		this.x = x;
		this.y = y;
		this.masse = masse;
		this.rayon = rayon;
		this.champGravite = G*masse/(rayon*rayon);
		this.vecteurVitesse = new Vecteur(vitesse*Math.cos(Math.toRadians(angle)), vitesse*-Math.sin(Math.toRadians(angle)));
		this.vecteurVitesseGraphique = new VecteurGraphique(vecteurVitesse);
		this.angle = angle;
	}

	/**
	 * Dessine l'astre.
	 * @param g2d Le contexte graphique.
	 * @param mat La matrice de transformation en unit� r�elle.
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat) {}

	/**
	 * Retourne vrai si la point est dans l'astre, faux sinon.
	 * @param x La coordon�e en x du point.
	 * @param y La coordon�e en y du point.
	 * @return Vrai si la point est dans l'astre, faux sinon.
	 */
	public abstract boolean contient(int x, int y);

	
	//public abstract void unPasEuler(double deltaT);

	/**
	 * Calcule la premi�re position et la vitesse final de l'astre apr�s un certain temps deltaT avec l'algorithme RK4.
	 * @param deltaT Le temps.
	 */
	public abstract void unPasRK4Partie1(double deltaT);
	
	/**
	 * Calcule la position et la vitesse � demi-temps de l'astre apr�s un certain temps deltaT avec l'algorithme RK4.
	 * @param deltaT Le temps.
	 */
	public abstract void unPasRK4Partie2(double deltaT);
	
	/**
	 * Calcule la deuxi�me position et la vitesse final de l'astre apr�s un certain temps deltaT avec l'algorithme RK4.
	 * @param deltaT Le temps.
	 */
	public abstract void unPasRK4Partie3(double deltaT);
	
	/**
	 * Calcule la position et la vitesse final avec pond�ration de l�acc�l�ration de l'astre apr�s un certain temps deltaT avec l'algorithme RK4.
	 * @param deltaT Le temps.
	 */
	public abstract void unPasRK4Partie4(double deltaT);

	/**
	 * Retourne la position en x courante de l'astre.
	 * @return La position en x courante de l'astre.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Retourne la position en y.
	 * @return La position en y.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Change la position en x de l'astre.
	 * @param x La nouvelle position en x.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Change la position en y de l'astre.
	 * @param y La nouvelle position en y.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Retourne la masse de l'astre.
	 * @return La masse de l'astre.
	 */
	public double getMasse() {
		return masse;
	}

	/**
	 * Change la masse de l'astre.
	 * @param masse La nouvelle masse de l'astre.
	 */
	public void setMasse(double masse) {
		this.masse = masse;
		this.champGravite = G*masse/(rayon*rayon);
	}

	/**
	 * Retourne le rayon de l'astre.
	 * @return Le rayon de l'astre.
	 */
	public double getRayon() {
		return rayon;
	}

	/**
	 * Change le rayon de l'astre.
	 * @param rayon Le nouveau rayon de l'astre.
	 */
	public void setRayon(double rayon) {
		this.rayon = rayon;
		this.champGravite = G*masse/(rayon*rayon);
	}

	/**
	 * Retourne l'acc�l�ration courante de l'astre.
	 * @return L'acc�l�ration courante de l'astre.
	 */
	public double getAcceleration() {
		return acceleration.module();
	}

	/**
	 * Change l'acc�l�ration de l'astre.
	 * @param acceleration La nouvelle acc�l�ration de l'astre.
	 */
	public void setAcceleration(Vecteur acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * Retourne la force gravitationnelle excerc�e sur l'astre.
	 * @return La force gravitationnelle excerc�e sur l'astre.
	 */
	public double getFg() {
		return fg.module();
	}

	/**
	 * Change la force gravitationnelle excerc�e sur l'astre.
	 * @param fg La nouvelle force gravitationnelle excerc�e sur l'astre.
	 */
	public void setFg(Vecteur fg) {
		this.fg = fg;
	}

	/**
	 * Retourne le type d'astre, soit une �toile, une plan�te ou une satellite.
	 * @return Le type d'astre.
	 */
	public TypeAstre getType() {
		return type;
	}

	/**
	 * D�finie si l'astre est d�pla�able ou non.
	 * @param b Vrai si d�pla�able, faux sinon.
	 */
	public void setEstDeplacable(boolean b) {
		estDeplacable = b;
	}

	/**
	 * Retourne vrai si l'astre peut �tre d�plac�, faux si elle ne peut pas.
	 * @return Vrai si l'astre peut �tre d�plac�, faux si elle ne peut pas.
	 */
	public boolean getEstDeplacable() {
		return estDeplacable;
	}

	/**
	 * Retourne vrai si l'astre est s�lectionn�, faux si elle ne peut pas.
	 * @return Vrai si l'astre s�lectionn�, faux si elle ne peut pas.
	 */
	public boolean getEstSelectionne() {
		return estSelectionne;
	}

	/**
	 * D�finie si l'astre est s�lectionn� ou non.
	 * @param estSelectionne Vrai si d�pla�able, faux sinon.
	 */
	public void setEstSelectionne(boolean estSelectionne) {
		this.estSelectionne = estSelectionne;
	}

	/**
	 * Retourne le vecteur vitesse de l'astre.
	 * @return Le vecteur de vitesse de l'astre.
	 */
	public Vecteur getVecteurVitesse() {
		return vecteurVitesse;
	}

	/**
	 * Change le vecteur vitesse de l'astre.
	 * @param vecteurVitesse  Le nouveau vectuer de vitesse de l'astre.
	 */
	public void setVecteurVitesse(Vecteur vecteurVitesse) {
		this.vecteurVitesse = vecteurVitesse;
		this.vecteurVitesseGraphique = new VecteurGraphique(vecteurVitesse);
	}

	/**
	 * Change le point d'origine du vecteur vitesse de l'astre.
	 * @param x Le nouveau abscine de l'orgine du vecteur vitesse.
	 * @param y Le nouveau ordonn�e de l'orgine du vecteur vitesse.
	 */
	public void origineVecteurVitesse(double x, double y) {
		this.vecteurVitesseGraphique.setOrigineXY(x, y);
	}

	/**
	 * Retourne la valeur du champ gravitationnelle de l'astre.
	 * @return La valeur du champ gravitationnelle de l'astre.
	 */
	public double getChampGravite() {
		return champGravite;
	}

	/**
	 * Change l'angle de lancement de l'astre.
	 * @param angle Le nouveau angle de lancement.
	 */
	public void setAngle(int angle) {
		this.angle = angle;

	}

	/**
	 * Retourne la cercle qui forme l'astre.
	 * @return La forme de l'astre.
	 */
	public Shape getShape() {
		return cercleFantome;
	}

	/**
	 * D�termine si la forme de l'astre est en collision avec la forme d'un autre astre.
	 * @param shapeAstre2 La forme de l'autre l'astre
	 * @return Vrai, s'il y a une collsion. Faux, sinon.
	 */
	public boolean instersect(Shape shapeAstre2) {
		if (!estDeplacable && cercleFantome != null && shapeAstre2 != null) {

			Area astre1 = new Area(cercleFantome);
			Area astre2 = new Area(shapeAstre2);
			astre1.intersect(astre2);
			if (!astre1.isEmpty()) {
				return true;
			}
		}
		return false;
	}


}
