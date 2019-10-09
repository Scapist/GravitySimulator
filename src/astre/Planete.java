package astre;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import calcul.Vecteur;

/**
 * Class qui définie une planète.
 * @author Mohammed Ariful Islam
 *
 */
public class Planete extends Astre{

	private Ellipse2D.Double planete;
	private Image image = null;
	private final int epaisseur = 3;
	private int factAgrandissement = 1000;

	private ArrayList<Vecteur> listePos = new ArrayList<Vecteur>();
	private int longueurTrajectoire = 505;

	private double vitesseReduction = 5e5;//Module de vitesse pour déterminer si on doit appliqué le factReduction2.
	private int factReduction1 = 500; //Facteur de réduction pour réduire la longueur du vitesse de l'astre.
	private int factReduction2 = 600; //Facteur de réduction pour réduire la longueur du vitesse de l'astre
	//lorsque la vitesse est plus grand que 5e5 m/s.
	private double rotation = 0;
	private double deltaRotation = 0.1;

	/**
	 * Crée une planète avec les paramètres spécifiés.
	 * @param x La position en x de la planète.
	 * @param y La position en y de la planète.
	 * @param masse La masse de la planète.
	 * @param rayon Le rayon de la planète.
	 * @param vitesse La vitesse de la planète.
	 * @param angle L'angle de lancement de la planète.
	 */
	public Planete(double x, double y, double masse, double rayon, double vitesse, int angle){
		super(x, y, masse, rayon, vitesse, angle);
		this.type = TypeAstre.PLANETE;

		URL fich = getClass().getClassLoader().getResource("Planete.jpg");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier d'image introuvable!");
		} else {
			try {
				image = ImageIO.read(fich);

			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture du fichier d'image");
			}
		}

	}

	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat) {
		AffineTransform matLocale = new  AffineTransform(mat);
		AffineTransform g2dAvant = g2d.getTransform();
		Color couleurAvant = g2d.getColor();
		Shape clipAvant = g2d.getClip();
		Stroke strokeAvant = g2d.getStroke();

		g2d.setColor(Color.red);
		planete = new Ellipse2D.Double(x-rayon*factAgrandissement/2, y - rayon*factAgrandissement/2, rayon*factAgrandissement, rayon*factAgrandissement);
		cercleFantome = matLocale.createTransformedShape(planete);

		dessinerTrajectoire(g2d, matLocale);

		if (rotation >= 360) {
			rotation = 0;
		}
		rotation = rotation + deltaRotation;
		matLocale.rotate(Math.toRadians(rotation), x, y);


		g2d.setClip(cercleFantome);
		double fact = rayon * factAgrandissement * 2 / image.getWidth(null);
		matLocale.translate(this.x, this.y);
		matLocale.scale(fact, fact);
		matLocale.translate(-image.getWidth(null)/2, -image.getHeight(null)/2);

		g2d.drawImage(image, matLocale, null);
		//g2d.fill(cercleFantome);

		//rétablir la matrice de transformation
		matLocale = new AffineTransform(mat);


		if (estSelectionne) {
			dessinerEstSelectionne(g2d, matLocale);
		}

		if (estDeplacable) {
			g2d.setStroke((new BasicStroke(epaisseur)));
			g2d.setColor(Color.BLUE);
			g2d.draw(cercleFantome);

			g2d.setStroke(strokeAvant);
			g2d.setClip(clipAvant);

			vecteurVitesseGraphique.setXY(vecteurVitesse.getX()/factReduction1, vecteurVitesse.getY()/factReduction1);

			if (vecteurVitesse.module() > vitesseReduction) {
				vecteurVitesseGraphique.setXY(vecteurVitesse.getX()/factReduction2, vecteurVitesse.getY()/factReduction2);
			}


			vecteurVitesseGraphique.dessiner(g2d);
		}

		//rétablir le g2d
		g2d.setTransform(g2dAvant);
		g2d.setColor(couleurAvant);
		g2d.setClip(clipAvant);
		g2d.setStroke(strokeAvant);

	}

	/**
	 * Dessine la trajectoire parcourue par l'astre/
	 * @param g2d Contexte graphique.
	 * @param mat La matrice de treansformation.
	 */
	private void dessinerTrajectoire(Graphics2D g2d, AffineTransform mat) {
		for (int i = 0; i < listePos.size() - 5; i++) {
			g2d.setColor(new Color(0, i/2, 255, i/10));
			g2d.fill(mat.createTransformedShape(new Ellipse2D.Double(listePos.get(i).getX() - rayon*i/4, listePos.get(i).getY() - rayon*i/4, rayon*i/2, rayon*i/2)));
		}
	}

	/**
	 * Dessine le contour de l'astre en rouge s'il est sélectionné.
	 *@param g2d Contexte graphique.
	 * @param mat La matrice de treansformation.
	 */
	private void dessinerEstSelectionne(Graphics2D g2d, AffineTransform mat) {
		g2d.setStroke(new BasicStroke(epaisseur));
		g2d.setColor(Color.RED);
		g2d.draw(cercleFantome);
	}

	@Override
	public boolean contient(int x, int y) {
		if (cercleFantome.contains(new Point(x, y))) {
			return true;
		}
		return false;
	}

	@Override
	public void unPasRK4Partie1(double deltaT) {
		if (!estDeplacable) {
			
			posI = new Vecteur(x, y);
			vI =  new Vecteur(vecteurVitesse.getX(), vecteurVitesse.getY());
			acceleration = new Vecteur(fg.getX()/masse, fg.getY()/masse);
			aI = new Vecteur(acceleration.getX(), acceleration.getY());
			
			Vecteur pos1 = posI.additionne(vI.multiplie(deltaT));
			Vecteur vitesse1 = vI.additionne(aI.multiplie(deltaT));

			setX(pos1.getX());
			setY(pos1.getY());
			vecteurVitesse.setX(vitesse1.getX());
			vecteurVitesse.setY(vitesse1.getY());
			
		}
	}

	@Override
	public void unPasRK4Partie2(double deltaT) {
		if (!estDeplacable) {
			acceleration = new Vecteur(fg.getX()/masse, fg.getY()/masse);
			a1 = new Vecteur(acceleration.getX(), acceleration.getY());

			Vecteur posMid = posI.additionne(vI.multiplie(deltaT/2)).additionne(aI.multiplie(0.5).multiplie((deltaT/2)*(deltaT/2)));
			Vecteur vMid = vI.additionne((aI.multiplie(0.75).additionne(a1.multiplie(0.25))).multiplie(deltaT/2));

			setX(posMid.getX());
			setY(posMid.getY());
			vecteurVitesse.setX(vMid.getX());
			vecteurVitesse.setY(vMid.getY());	
		}
	}

	@Override
	public void unPasRK4Partie3(double deltaT) {
		if (!estDeplacable) {
			
			acceleration = new Vecteur(fg.getX()/masse, fg.getY()/masse);
			aMid = new Vecteur(acceleration.getX(), acceleration.getY());

			Vecteur pos2 = posI.additionne(vI.multiplie(deltaT)).additionne(aI.multiplie(deltaT*deltaT*0.5));
			Vecteur v2 = vI.additionne((aI.multiplie(0.5).additionne(a1.multiplie(0.5))).multiplie(deltaT));

			setX(pos2.getX());
			setY(pos2.getY());
			vecteurVitesse.setX(v2.getX());
			vecteurVitesse.setY(v2.getY());

		}
	}

	@Override
	public void unPasRK4Partie4(double deltaT) {
		if (!estDeplacable) {
			
			acceleration = new Vecteur(fg.getX()/masse, fg.getY()/masse);
			a2 = new Vecteur(acceleration.getX(), acceleration.getY());

			Vecteur posF = posI.additionne(vI.multiplie(deltaT)).additionne((aI.multiplie((1/3d)).additionne(aMid.multiplie((2/3d)))).multiplie(0.5*deltaT*deltaT));
			Vecteur vF = vI.additionne((aI.multiplie(1/6d).additionne(aMid.multiplie(4/6d)).additionne(a2.multiplie(1/6))).multiplie(deltaT));

			setX(posF.getX());
			setY(posF.getY());
			
			listePos.add(posF);
			if (listePos.size() > longueurTrajectoire) {
				listePos.remove(0);
			}
			
			vecteurVitesse.setX(vF.getX());
			vecteurVitesse.setY(vF.getY());
		}
	}
}
