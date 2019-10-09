package calcul;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class VecteurGraphique extends Vecteur implements DessinableVecteur {

	//caractï¿½ristiques supplemetaires utiles pour le dessin
	private double origX=0, origY=0;			 //originep our dessiner le vecteur
	private Line2D.Double corps, traitDeTete;    //pour tracer la flï¿½che
	private double angleTete = 0.5;              //angle entre les deux segments formant la tete de fleche
	private double longueurTete = 20;            //longueur des segments formant la tete (en pixels)


	public VecteurGraphique() {
		super();
	}

	public VecteurGraphique(double x, double y) {
		super(x, y);
	}

	public VecteurGraphique(Vecteur v) {
		super(v.x, v.y);
	}

	public VecteurGraphique (double x, double y, double origX, double origY) {
		super(x, y);
		this.origX = origX;
		this.origY = origY;
	}

	/**
	 * Cree les formes geometriques qui constituent le vecteur (ï¿? la position d'origine 0,0)
	 */
	private void creerRepresentationGeometrique() {
		//on cree la geometriea l'origine. Le tout sera dessinï¿? avec une translation si son origine est ailleurs
		corps = new Line2D.Double(0, 0, x, y);
		double moduleVec  = module();
		double ratio = (moduleVec - longueurTete)/moduleVec;
		traitDeTete = new Line2D.Double( x*ratio, y*ratio, x, y);
	}

	/**
	 * Dessine le vecteur sous la forme d'une flï¿½che orientï¿½e
	 * @param g2d Le contexte graphique
	 */
	public void dessiner(Graphics2D g2d) {	

		creerRepresentationGeometrique();
		AffineTransform mat = g2d.getTransform();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.translate(origX, origY);
		g2d.draw( corps );  										//ligne formant le vecteur lui-meme
		g2d.rotate(angleTete/2, x,  y);
		g2d.draw(traitDeTete); 	
		g2d.rotate(-angleTete, x,  y);
		g2d.draw(traitDeTete);
		//un des deux traits qui forment la tete du vecteur
		g2d.setTransform(mat);
	}// fin


	public double getAngleTete() {
		return angleTete;
	}

	public void setAngleTete(double angleTete) {
		this.angleTete = angleTete;
	}

	/**
	 * Modifie l'origine du vecteur pour son dessin
	 * @param origX origine en x
	 * @param origY origine en y
	 */
	public void setOrigineXY(double origX, double origY) {
		this.origX = origX;
		this.origY = origY;
	}

	/**
	 * Retourne la longueur du segment utilisï¿? pour tracer la flï¿½che formant l'extrï¿½mitï¿? du vecteur
	 * @return Longueur du segment
	 */
	public double getLongueurTete() {
		return longueurTete;
	}

	/**
	 * Modifie la longueur du segment utilisï¿? pour tracer la flï¿½che formant l'extrï¿½mitï¿? du vecteur
	 * @param longueurTete longueur du segment
	 */
	public void setLongueurTete(double longueurTete) {
		this.longueurTete = longueurTete;
	}


}
