package composantsgraphiques;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import interfaces.Dessinable;

/**
 * Classe qui contrôle l'animation d'une collision.
 * @author Mohammed Ariful Islam
 *
 */
public class GestionAnimationCollision implements Dessinable{

	private ArrayList<AnimationCollision> list = new ArrayList<AnimationCollision>();

	/**
	 * Ajoute une animation de collision à controler.
	 * @param x La position en x de la collision.
	 * @param y La position en x de la collision.
	 * @param taille La taille de l'animation.
	 */
	public void ajouterCollision(double x, double y, double taille) {
		list.add(new AnimationCollision(x, y, taille));
	}

	/**
	 * Dessine l'animation 
	 * @param g2d Le contexte graphique.
	 * @param mat La matrice de transformation en unité réelle.
	 */
	@Override
	public void dessiner(Graphics2D g2d, AffineTransform mat) {
		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).estFini()) {
				list.get(i).dessiner(g2d, mat);
			} else {
				list.remove(i);
				i--;
			}
		}

	}
}
