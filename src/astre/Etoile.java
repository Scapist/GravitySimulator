package astre;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * Class qui définie une étoile.
 * 
 * @author Mohammed Ariful Islam
 *
 */
public class Etoile extends Astre {

	private Ellipse2D.Double etoile;
	private Image image = null;
	private final int epaisseur = 3;
	private int factAgrandissement = 10;
	private double rotation = 0;
	private double deltaRotation = 0.1;
	
	

	/**
	 * Crée une étoile avec les paramètres spécifiés.
	 * 
	 * @param x La position en x de l'étoile.
	 * @param y La position en y de l'étoile.
	 * @param masse La masse de l'étoile.
	 * @param rayon Le rayon de l'étoile.
	 */
	public Etoile(double x, double y, double masse, double rayon) {
		super(x, y, masse, rayon, 0, 0);
		this.type = TypeAstre.ETOILE;

		URL fich = getClass().getClassLoader().getResource("Etoile.jpg");
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
		AffineTransform matLocale = new AffineTransform(mat);
		AffineTransform g2dAvant = g2d.getTransform();
		Color couleurAvant = g2d.getColor();
		Shape clipAvant = g2d.getClip();
		Stroke strokeAvant = g2d.getStroke();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		etoile = new Ellipse2D.Double(x - rayon*factAgrandissement/2, y - rayon*factAgrandissement/2, rayon*factAgrandissement, rayon*factAgrandissement);
		cercleFantome = matLocale.createTransformedShape(etoile);
		
		if (rotation >= 360) {
			rotation = 0;
		}
		rotation = rotation + deltaRotation;
		matLocale.rotate(Math.toRadians(rotation), x, y);
		
		g2d.setClip(cercleFantome);
		double fact = rayon*factAgrandissement*2 / image.getWidth(null);
		matLocale.translate(this.x , this.y);
		matLocale.scale(fact, fact);
		matLocale.translate(-image.getWidth(null)/2, -image.getHeight(null)/2);
		g2d.drawImage(image, matLocale, null);
		//g2d.fill(cercleFantome);
		
		if (estSelectionne) {
			g2d.setColor(Color.BLUE);
			g2d.setStroke(new BasicStroke(epaisseur));
			g2d.draw(cercleFantome);			
		}

		if (estDeplacable) {
			g2d.setStroke(new BasicStroke(epaisseur));
			g2d.setColor(Color.RED);
			g2d.draw(cercleFantome);
		}

		//rétablir le g2d
		g2d.setTransform(g2dAvant);
		g2d.setColor(couleurAvant);
		g2d.setClip(clipAvant);
		g2d.setStroke(strokeAvant);

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
	
	}

	@Override
	public void unPasRK4Partie2(double deltaT) {
		
	}

	@Override
	public void unPasRK4Partie3(double deltaT) {
		
	}

	@Override
	public void unPasRK4Partie4(double deltaT) {
		
	}
}
