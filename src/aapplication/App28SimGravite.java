package aapplication;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import composantsgraphiques.SceneDepart;
import ecouteurs.FenetreListener;

/**
 * Class qui créer le fenêtre de démarrage.
 * @author Mohammed Ariful Islam
 *
 */
public class App28SimGravite extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private FenetreSimulateur simulateur;
	private static App28SimGravite frame;
	private SceneDepart sceneDepart;
	private Aide fenetreAide;
	private Color mauvePale = new Color(187, 51, 255);
	private Color mauveFonce = new Color(136, 0, 204);

	/**
	 * Démarre l'application.
	 * 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new App28SimGravite();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructeur qui ajoute les composantes à l'interface de démarrage.
	 */
	public App28SimGravite() {
		setTitle("Simulateur de gravit\u00E9");

		simulateur = new FenetreSimulateur();
		simulateur.addFenetreListener(new FenetreListener() {

			@Override
			public void fermer() {
			}

			@Override
			public void aide(int onglet){
				if (!fenetreAide.isVisible()) {
					fenetreAide.setVisible(true);
					sceneDepart.recommencer();
				}
				switch (onglet){
				case 0:
					fenetreAide.instructionsSelectionnee();
					break;
				case 1:
					fenetreAide.conceptsSelectionnee();
					break;
				default:
					fenetreAide.aProposSelectionnee();
				}

				fenetreAide.toFront();
			}

			@Override
			public void menu() {
				frame.setVisible(true);
				simulateur.dispose();
				sceneDepart.recommencer();
			}

		});

		fenetreAide = new Aide();
		fenetreAide.addFenetreListener(new FenetreListener() {

			@Override
			public void fermer() {
				fenetreAide.dispose();
			}

			@Override
			public void aide(int onglet) {
			}

			@Override
			public void menu() {
			}

		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 966, 784);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		sceneDepart = new SceneDepart();
		sceneDepart.setBounds(0, 0, 950, 745);
		contentPane.add(sceneDepart);

		JLabel lblCommencer = new JLabel("COMMENCER");
		lblCommencer.setBounds(326, 224, 297, 97);
		lblCommencer.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
		lblCommencer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblCommencer.setBorder(new BevelBorder(BevelBorder.LOWERED, mauveFonce,mauveFonce, mauvePale, mauvePale));
				lblCommencer.setForeground(mauveFonce);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblCommencer.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
				lblCommencer.setForeground(mauvePale);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!simulateur.isVisible()) {
					simulateur.setVisible(true);
					frame.setVisible(false);
				}
			}
		});
		sceneDepart.setLayout(null);
		lblCommencer.setHorizontalAlignment(SwingConstants.CENTER);
		lblCommencer.setForeground(mauvePale);
		lblCommencer.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 25));
		sceneDepart.add(lblCommencer);

		JLabel lblAide = new JLabel("AIDE");
		lblAide.setBounds(326, 397, 297, 97);
		lblAide.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
		lblAide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblAide.setBorder(new BevelBorder(BevelBorder.LOWERED, mauveFonce, mauveFonce, mauvePale, mauvePale));
				lblAide.setForeground(mauveFonce);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblAide.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
				lblAide.setForeground(mauvePale);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!fenetreAide.isVisible()) {
					fenetreAide.setVisible(true);
					sceneDepart.recommencer();
				}
			}
		});
		lblAide.setHorizontalAlignment(SwingConstants.CENTER);
		lblAide.setForeground(mauvePale);
		lblAide.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 25));
		sceneDepart.add(lblAide);

		JLabel lblQuitter = new JLabel("Quitter");
		lblQuitter.setBounds(326, 570, 297, 97);
		lblQuitter.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
		lblQuitter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				lblQuitter.setBorder(new BevelBorder(BevelBorder.LOWERED, mauveFonce, mauveFonce, mauvePale, mauvePale));
				lblQuitter.setForeground(mauveFonce);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblQuitter.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
				lblQuitter.setForeground(mauvePale);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		lblQuitter.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuitter.setForeground(mauvePale);
		lblQuitter.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 25));
		sceneDepart.add(lblQuitter);

		JLabel lblTitreAvant = new JLabel("SIMULATEUR DE GRAVIT\u00C9");
		lblTitreAvant.setBounds(34, 76, 881, 72);
		lblTitreAvant.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 55));
		lblTitreAvant.setForeground(mauvePale);
		sceneDepart.add(lblTitreAvant);

		JLabel lblTitreArriere = new JLabel("SIMULATEUR DE GRAVIT\u00C9");
		lblTitreArriere.setBounds(31, 73, 881, 72);
		lblTitreArriere.setForeground(mauveFonce);
		lblTitreArriere.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 55));
		sceneDepart.add(lblTitreArriere);
	}
}