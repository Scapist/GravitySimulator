package composantsgraphiques;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import astre.Astre;
import astre.Etoile;
import astre.Planete;
import astre.TypeAstre;
import calcul.Vecteur;

/**
 * Class qui créer le fond d'écran pour la fenêtre de démarrage.
 * @author Mohammed Ariful Islam
 *
 */
public class SceneDepart extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private Image backgroud = null;
	private final double G = 6.67e-11;

	private ArrayList<Astre> astre = new ArrayList<Astre>();
	boolean animationEnCours = false;
	private double deltaT = 5000;
	private final double largeurMonde = 4e11;
	private double hauteurMonde;
	private boolean astresAjoutes = false;
	
	/**
	 * Constructeur qui charge l'image.
	 */
	public SceneDepart() {

		URL fich = getClass().getClassLoader().getResource("background.jpg");
		if (fich == null) {
			JOptionPane.showMessageDialog(null, "Fichier d'image introuvable!");
		} else {
			try {
				backgroud = ImageIO.read(fich);

			} catch (IOException e) {
				System.out.println("Erreur pendant la lecture du fichier d'image");
			}
		}


		demarrer();
	}

	/**
	 * Dessine l'image.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(backgroud, 0, 0, getWidth(), getHeight(), null);

		ModeleAffichage modele = new ModeleAffichage(getWidth(), getHeight(), largeurMonde);
		hauteurMonde = modele.getHautUnitesReelles();
		AffineTransform mat = modele.getMatMC();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (!astresAjoutes) {
			astre.add(new Etoile(largeurMonde/2, hauteurMonde/2, 2e30, 7e8));
			astre.add(new Planete(largeurMonde/2, hauteurMonde/2 - 0.9e11, 5e24, 6e6, 30000, 0));
			astre.add(new Planete(largeurMonde/2, hauteurMonde/2 + 0.9e11, 5e24, 6e6, 30000, 180));
			astre.add(new Planete(largeurMonde/2 - 0.9e11, hauteurMonde/2, 5e24, 6e6, 30000, 90));
			astre.add(new Planete(largeurMonde/2 + 0.9e11, hauteurMonde/2, 5e24, 6e6, 30000, 270));

			astresAjoutes = true;	
		}

		for (int i = 0; i < astre.size(); i++) {
			astre.get(i).setEstDeplacable(false);
		}
		for (int i = 0; i < astre.size(); i++) {
			astre.get(i).dessiner(g2d, mat);
		}
		
		

	}

	/**
	 * Gère l'animation.
	 * 
	 */
	@Override
	public void run() {
		while (animationEnCours) {
			if (astre.size() != 0) {
				for (int i = 0; i < astre.size(); i++) {
					calculerFg();
					astre.get(i).unPasRK4Partie1(deltaT);
					calculerFg();
					astre.get(i).unPasRK4Partie2(deltaT);
					calculerFg();
					astre.get(i).unPasRK4Partie3(deltaT);
					calculerFg();
					astre.get(i).unPasRK4Partie4(deltaT);
				}
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
							if (astre.get(j).getType() == TypeAstre.ETOILE && astre.get(i).instersect(astre.get(j).getShape())) {
								astre.remove(i);
								i--;
							}
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
			Thread animation = new Thread(this);
			animation.start();
			animationEnCours = true;
		}
	}

	public void recommencer() {
		astre.clear();
		astresAjoutes = false;
	}
}
