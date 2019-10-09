package interfaces;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 * Interface qui permet à un object de se faire dessiner. 
 * @author Mohammed Ariful Islam
 *
 */
public interface Dessinable {

	/**
	 * Dessine les géométries.
	 * @param g2d Le contexte graphique.
	 * @param mat La matrice de transformation.
	 */
	public void dessiner(Graphics2D g2d, AffineTransform mat);

}
