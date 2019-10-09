package composantsgraphiques;


import java.awt.geom.AffineTransform;

/**
 * Un objet ModeleAffichage permet de m�moriser un ensemble de valeurs pour passer du monde r�el vers un composant de dessin dont les 
 * coordonn�es sont en pixels. On peut interroger l'objet pour connaitre la matrice de transformation, le nombre de pixels par unit�, 
 * les dimensions dans le monde r�el, etc.
 * 
 * @author Caroline Houle
 */


public class ModeleAffichage {
	private double hautUnitesReelles = -1;
	private double largUnitesReelles;
	private double largPixels;
	private double hautPixels;
	private double xOrigUnitesReelle;
	private double yOrigUnitesReelle;
	private double pixelsParUniteX;
	private double pixelsParUniteY;
	private AffineTransform matMC;


	/**
	 * Permet de cr�er un objet ModelAffichage, pouvant m�moriser la matrice (et autres valeurs) de transformation pour passer du monde vers le composant. les dimensions du monde 
	 * r�el sont pass�es en param�tre (largeur et hauteur). Si le rapport entre les dimensions en pixels n'est pas identique au rapport entre les dimensions r�elles, il y aura distortion.
	 * 
	 * @param largPixels La largeur du composant, en pixels
	 * @param hautPixels La hauteur du composant, en pixels
	 * @param xOrigUnitesReelle L'origine en x de la portion du monde r�el que l'on veut montrer 
	 * @param yOrigUnitesReelle L'origine en y de la portion du monde r�el que l'on veut montrer 
	 * @param largUnitesReelles La largeur consid�r�e dans le monde, en unit� r�elles
	 * @param hautUnitesReelles La hauteur consid�r�e dans le monde, en unit� r�elles  
	 * @param typeLargeur Bool�en qui vaut vrai si la dimension fournie est la largeur du monde, vaut faux si la dimension fournie est la hauteur du monde
	 */
	public ModeleAffichage(double largPixels, double hautPixels, double xOrigUnitesReelle, double yOrigUnitesReelle, double largUnitesReelles, double hautUnitesReelles) {

		this.largPixels = largPixels;
		this.hautPixels = hautPixels;
		this.xOrigUnitesReelle = xOrigUnitesReelle;
		this.yOrigUnitesReelle = yOrigUnitesReelle;
		this.largUnitesReelles = largUnitesReelles;
		this.hautUnitesReelles = hautUnitesReelles;                            

		this.pixelsParUniteX = largPixels / largUnitesReelles;
		this.pixelsParUniteY = hautPixels / hautUnitesReelles ;

		//calcul de la matrice monde-vers-composant
		AffineTransform mat = new AffineTransform();  //donne une matrice identite
		mat.scale( pixelsParUniteX, pixelsParUniteY ); 
		mat.translate(-xOrigUnitesReelle, -yOrigUnitesReelle);
		this.matMC = mat; //on m�morise la matrice (qui pourra �tre retourn�e via le getter associ�)

	}

	/**
	 * Permet de cr�er un objet ModeleAffichage, pouvant m�moriser la matrice (et autres valeurs) de transformation pour passer du monde vers le composant. Une des dimensions du monde 
	 * r�el est pass�e en param�tre (largeur ou hauteur). L'autre dimension sera  calcul�e de fa�on � n'introduire aucune distortion.
	 * 
	 * @param largPixels La largeur du composant, en pixels
	 * @param hautPixels La hauteur du composant, en pixels
	 * @param xOrigUnitesReelle L'origine en x de la portion du monde r�el que l'on veut montrer 
	 * @param yOrigUnitesReelle L'origine en y de la portion du monde r�el que l'on veut montrer 
	 * @param dimensionEnUnitesReelles La dimensions consid�r�e dans le monde, en unit� r�elles (il peut s'agir d'une largeur ou d'une hauteur, d�pendant du dernier param�tre)
	 * @param typeLargeur Bool�en qui vaut vrai si la dimension fournie est la largeur du monde, vaut faux si la dimension fournie est la hauteur du monde
	 */
	public ModeleAffichage(double largPixels, double hautPixels, double xOrigUnitesReelle, double yOrigUnitesReelle, double dimensionEnUnitesReelles, boolean typeLargeur) {

		this.largPixels = largPixels;
		this.hautPixels = hautPixels;
		this.xOrigUnitesReelle = xOrigUnitesReelle;
		this.yOrigUnitesReelle = yOrigUnitesReelle;

		if (typeLargeur) {
			//le parametre "dimensionEnUnitesReelles" represente la LARGEUR voulue
			this.largUnitesReelles = dimensionEnUnitesReelles;

			//on calcule la hauteur correspondante pour �viter toute distortion
			this.hautUnitesReelles = largUnitesReelles * hautPixels/largPixels;

		} else {
			//le parametre "dimensionEnUnitesReelles" ne represente PAS la largeur, il repr�sente plut�t la HAUTEUR voulue
			this.hautUnitesReelles = dimensionEnUnitesReelles;

			//on calcule la largeur correspondante pour �viter toute distortion
			this.largUnitesReelles = hautUnitesReelles * largPixels/hautPixels;
		}

		//pour ce constructeur il n'y a pas de distortion, donc les deux valeurs suivantes seront identiques
		this.pixelsParUniteX = largPixels / largUnitesReelles;
		this.pixelsParUniteY = hautPixels / hautUnitesReelles ;

		//calcul de la matrice monde-vers-composant
		AffineTransform mat = new AffineTransform();  //donne une matrice identite
		mat.scale( pixelsParUniteX, pixelsParUniteY ); 
		mat.translate(-xOrigUnitesReelle, -yOrigUnitesReelle);
		this.matMC = mat; //on m�morise la matrice (qui pourra �tre retourn�e via le getter associ�)

	}//fin constructeur


	/**
	 * Cr�ation d'un objet ModeleAffichage o� la dimension pass�e en param�tre est forc�ment la largeur du monde
	 * 
	 * @param largPixels La largeur du composant, en pixels
	 * @param hautPixels La hauteur du composant, en pixels
	 * @param xOrigUnitesReelle L'origine en x de la portion du monde r�el que l'on veut montrer 
	 * @param yOrigUnitesReelle L'origine en y de la portion du monde r�el que l'on veut montrer 
	 * @param dimensionEnUnitesReelles La dimensions consid�r�e dans le monde, en unit� r�elles (il peut s'agir d'une largeur ou d'une hauteur, d�pendant du dernier param�tre)
	 */
	public ModeleAffichage(double largPixels, double hautPixels, double xOrigUnitesReelle, double yOrigUnitesReelle, double dimensionEnUnitesReelles) {
		this( largPixels, hautPixels, xOrigUnitesReelle, yOrigUnitesReelle, dimensionEnUnitesReelles, true);
	}


	/**
	 * Cr�ation d'un objet ModeleAffichage o� l'origine du monde r�el est � 0,0
	 * 
	 * @param largPixels La largeur du composant, en pixels
	 * @param hautPixels La hauteur du composant, en pixels
	 * @param dimensionEnUnitesReelles La dimensions consid�r�e dans le monde, en unit� r�elles (il peut s'agir d'une largeur ou d'une hauteur, d�pendant du dernier param�tre)
	 * @param typeLargeur Bool�en qui vaut vrai si la dimension fournie est la largeur du monde, qui vaut faux si la dimension fournie est la hauteur du monde
	 */
	public ModeleAffichage(double largPixels, double hautPixels, double dimensionEnUnitesReelles, boolean typeLargeur) {
		this(largPixels, hautPixels, 0, 0, dimensionEnUnitesReelles, typeLargeur);
	}

	/**
	 *  Cr�ation d'un objet ModeleAffichage o� l'origine du monde r�el est � 0,0, et o� la dimension pass�e en param�tre est forc�ment la largeur du monde
	 *  
	 * @param largPixels La largeur du composant, en pixels
	 * @param hautPixels La hauteur du composant, en pixels
	 * @param xOrigUnitesReelle L'origine en x de la portion du monde r�el que l'on veut montrer 
	 * @param yOrigUnitesReelle L'origine en y de la portion du monde r�el que l'on veut montrer 
	 * @param dimensionEnUnitesReelles La dimensions consid�r�e dans le monde, en unit� r�elles (il peut s'agir d'une largeur ou d'une hauteur, d�pendant du dernier param�tre)
	 */
	public ModeleAffichage(double largPixels, double hautPixels, double dimensionEnUnitesReelles) {
		this(largPixels, hautPixels, 0, 0, dimensionEnUnitesReelles, true);
	}

	/**
	 * Retourne une copie de la matrice monde-vers-composant qui a �t� calcul�e dans le constructeur
	 * @return La matrice monde-vers-composant
	 */
	public AffineTransform getMatMC() {
		//on d�cide de retourner une copie de celle qui est m�moris�e, pour �viter qu'elle soit modifi�e
		return new AffineTransform (matMC);
	}

	/**
	 * Retourne la hauteur du monde, en unit�s r�elles
	 * @return La hauteur du monde, en unit�s r�elles
	 */
	public double getHautUnitesReelles() {
		return hautUnitesReelles;
	}

	/**
	 * Retourne la largeur du monde, en unit�s r�elles
	 * @return La largeur du monde, en unit�s r�elles
	 */
	public double getLargUnitesReelles() {
		return largUnitesReelles;
	}

	/**
	 * Retourne le nombre de pixels contenus dans une unit� du monde r�el, en x
	 * @return Le nombre de pixels contenus dans une unit� du monde r�el, en x
	 */
	public double getPixelsParUniteX() {
		return pixelsParUniteX;
	}

	/**
	 * Retourne le nombre de pixels contenus dans une unit� du monde r�el, en y
	 * @return Le nombre de pixels contenus dans une unit� du monde r�el, en y
	 */
	public double getPixelsParUniteY() {
		return pixelsParUniteY;
	}

	/**
	 * Retourne la largeur en pixels du composant auquel s'appliquera la transformation 
	 * @return La largeur en pixels 
	 */
	public double getLargPixels() {
		return largPixels;
	}

	/**
	 * Retourne la hauteur en pixels du composant auquel s'appliquera la transformation 
	 * @return La hauteur en pixels 
	 */
	public double getHautPixels() {
		return hautPixels;
	}

	/**
	 * Retourne l'origine, en x, de la portion du monde r�el consid�r�e
	 * @return L'origine en x, de la portion du monde r�el consid�r�e
	 */
	public double getxOrigUnitesReelle() {
		return xOrigUnitesReelle;
	}

	/**
	 * Retourne l'origine, en y, de la portion du monde r�el consid�r�e
	 * @return L'origine en y, de la portion du monde r�el consid�r�e
	 */
	public double getyOrigUnitesReelle() {
		return yOrigUnitesReelle;
	}


}//fin classe
