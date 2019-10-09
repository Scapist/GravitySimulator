
package composantsgraphiques;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import interfaces.Dessinable;

/**
 * Classe qui créer une animation d'un feu lorsqu'il y a une collision.
 * @author Mohammed Ariful Islam
 *
 */
public class AnimationCollision implements Runnable, Dessinable {

	private final int NB_IMAGES = 81;
	private static BufferedImage images[] = null;
	private boolean enCoursDAnimation = false;
	private	int compteur = 1; //compteurs pour lire les images
	private	int k = 1; //compteurs pour dessiner les images
	private static BufferedImage image;
	
	private int hautImage = 100;
	private int longImage = 100;
	private int nbCol = 10;
	private int nbLig = 8;
	
	private double x;
	private double y;
	private double taille;
	private boolean fini = false;
	
	private int factAgrandissement = 1000;

	/**
	 * Constructeur qui créer une nouvelle animation lors d'une collision.
	 * @param x La position en x du feu.
	 * @param y La position en x du feu.
	 * @param taille La taille du feu.
	 */
	public AnimationCollision(double x, double y, double taille) {
		this.x = x;
		this.y = y;
		this.taille = taille;
		if (images == null) {
			lireLesImages();
		}
		demarrer();

	}

	/**
	 * Constructeur qui créer une nouvelle animation lors d'une collision.
	 */
	public AnimationCollision() {
		if (images == null) {
			lireLesImages();
		}
	}

	/**
	 * Gère l'animation.
	 */
	@Override
	public void run() {
		while (enCoursDAnimation) {
			if (k < images.length - 1) {
				k++;
			} else {
				fini = true;
			}
			try {
				Thread.sleep(20);
			}
			catch (InterruptedException e) {
				System.out.println("Processus interrompu!");
			}
		}
	}

	/**
	 * Démarre l'animation
	 */
	public void demarrer() {
		if (!enCoursDAnimation) { 
			Thread processusAnim = new Thread(this);
			processusAnim.start();
			enCoursDAnimation = true;
		}
	}

	/**
	 * Enregistre les images du feu afin de faire l'animation.
	 */
	private void lireLesImages() {
		images = new BufferedImage[NB_IMAGES];
		URL fich = getClass().getClassLoader().getResource("feu.png");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier d'image introuvable!");
		} else {
			try {
				image = ImageIO.read(fich);
			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture du fichier d'image");
			}
		}
		for (int lig = 1; lig <= nbLig; lig++) {
			for (int col = 1; col <= nbCol; col++) {
				images[compteur] = prendreImage(col, lig, longImage, hautImage);
				compteur++;
			}
		}
	}

	/**
	 * Prend une partie d'une image avec la position et la taille spécifée.
	 * @param col La colonne de la partie de l'image à prendre.
	 * @param lig La ligne de la partie de l'image à prendre.
	 * @param width La longueur de la partie de l'image à prendre.
	 * @param height La hauteur de la partie de l'image à prendre.
	 * @return La partie de l'image spécifiée.
	 */
	private BufferedImage prendreImage(int col, int lig, int width, int height) {
		BufferedImage img = image.getSubimage((col - 1) * hautImage, (lig - 1) * longImage, width, height);
		return img;
	}

	/**
	 * Dessine le feu.
	 * @param g2d Le contexte graphique.
	 * @param mat La matrice de transformation en unité réelle.
	 */
	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat) {
		if (!fini) {
			AffineTransform matLocale = new  AffineTransform(mat);

			double fact = taille*factAgrandissement*2 / images[k].getWidth(null);
			matLocale.translate(x, y);
			matLocale.scale(fact, fact);
			matLocale.translate(-images[k].getWidth(null)/2, -images[k].getHeight(null)/2);

			g2d.drawImage(images[k], matLocale, null);
		}
	}

	/**
	 * Retourne Vrai si l'animation est fini, faux si elle ne l'est pas.
	 * @return Vrai si l'animation est fini, faux si elle ne l'est pas.
	 */
	public boolean estFini() {
		return fini;
	}

}
