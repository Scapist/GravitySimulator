package composantsgraphiques;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Class qui créer le fond d'écran pour la fenêtre de simulation.
 * @author Mohammed Ariful Islam
 *
 */

public class BackgroundSimulateur extends JPanel {

	private static final long serialVersionUID = 1L;

	private Image backgroud = null;

	/**
	 * Constructeur qui charge l'image.
	 */
	public BackgroundSimulateur() {

		URL fich = getClass().getClassLoader().getResource("backgroundSim.jpg");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier d'image introuvable!");
		} else {
			try {
				backgroud = ImageIO.read(fich);

			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture du fichier d'image");
			}
		}
	}

	/**
	 * Dessine l'image.
	 * 
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(backgroud, 0, 0, getWidth(), getHeight(), null);
	}


}
