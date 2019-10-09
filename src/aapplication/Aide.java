package aapplication;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.EventListenerList;

import composantsgraphiques.SceneDepart;
import ecouteurs.FenetreListener;

/**
 * Class qui créer la fenêtre d'aide.
 * @author Mohammed Ariful Islam
 *
 */
public class Aide extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Color mauvePale = new Color(187, 51, 255);
	private Color mauveFonce = new Color(136, 0, 204);
	private JLabel lblInstructions;
	private JLabel lblConceptsScientifiques;
	private JLabel lblAPropos;
	private JLabel lblAnnee;
	private JLabel lblCegep;
	private JLabel lblProgramme;
	private JLabel lblProgramme2;

	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();
	private JLabel lblNom;
	private SceneDepart sceneDepart;
	private JPanel pnlAPropos;
	private JPanel pnlInstructions;
	private JPanel pnlConcepts;

	/**
	 * Constructeur qui ajoute les composantes à la fenêtre d'aide.
	 */
	public Aide() {
		setTitle("Aide");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1068, 678);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		sceneDepart = new SceneDepart();
		sceneDepart.setBounds(0, 0, 1062, 649);
		contentPane.add(sceneDepart);
		sceneDepart.setLayout(null);

		lblConceptsScientifiques = new JLabel("Concepts Scientifiques");
		lblConceptsScientifiques.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				conceptsSelectionnee();
			}
		});
		lblConceptsScientifiques.setBorder(new BevelBorder(BevelBorder.LOWERED, mauvePale, mauvePale, mauveFonce, mauveFonce));
		lblConceptsScientifiques.setForeground(mauveFonce);
		lblConceptsScientifiques.setHorizontalAlignment(SwingConstants.CENTER);
		lblConceptsScientifiques.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 18));
		lblConceptsScientifiques.setBounds(287, 89, 278, 24);
		sceneDepart.add(lblConceptsScientifiques);

		lblInstructions = new JLabel("Instructions");
		lblInstructions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				instructionsSelectionnee();
			}
		});
		lblInstructions.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
		lblInstructions.setForeground(mauvePale);
		lblInstructions.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 16));
		lblInstructions.setHorizontalAlignment(SwingConstants.CENTER);
		lblInstructions.setBounds(10, 89, 278, 24);
		sceneDepart.add(lblInstructions);

		lblAPropos = new JLabel("\u00C0 Propos");
		lblAPropos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				aProposSelectionnee();
			}
		});
		lblAPropos.setBorder(new BevelBorder(BevelBorder.LOWERED, mauvePale, mauvePale, mauveFonce, mauveFonce));
		lblAPropos.setForeground(mauveFonce);
		lblAPropos.setHorizontalAlignment(SwingConstants.CENTER);
		lblAPropos.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 18));
		lblAPropos.setBounds(565, 89, 278, 24);
		sceneDepart.add(lblAPropos);

		JLabel lblTitreAvant = new JLabel("SIMULATEUR DE GRAVIT\u00C9");
		lblTitreAvant.setForeground(mauvePale);
		lblTitreAvant.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 55));
		lblTitreAvant.setBounds(90, 14, 881, 72);
		sceneDepart.add(lblTitreAvant);

		JLabel lblTitreArriere = new JLabel("SIMULATEUR DE GRAVIT\u00C9");
		lblTitreArriere.setForeground(mauveFonce);
		lblTitreArriere.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 55));
		lblTitreArriere.setBounds(87, 11, 881, 72);
		sceneDepart.add(lblTitreArriere);

		JLabel lblFermer = new JLabel("Fermer");
		lblFermer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblFermer.setBorder(new BevelBorder(BevelBorder.LOWERED, mauveFonce,mauveFonce, mauvePale, mauvePale));
				lblFermer.setForeground(mauveFonce);
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				lblFermer.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
				lblFermer.setForeground(mauvePale);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				leverEvenFermer();
			}
		});
		lblFermer.setBorder(new BevelBorder(BevelBorder.RAISED, mauvePale, mauvePale, mauveFonce, mauveFonce));
		lblFermer.setHorizontalAlignment(SwingConstants.CENTER);
		lblFermer.setForeground(mauvePale);
		lblFermer.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 25));
		lblFermer.setBounds(742, 563, 278, 46);
		sceneDepart.add(lblFermer);

		pnlAPropos = new JPanel();
		pnlAPropos.setVisible(false);
		pnlAPropos.setBorder(new MatteBorder(2, 2, 2, 2, mauveFonce));
		pnlAPropos.setBackground(new Color(187, 51, 255, 100));
		pnlAPropos.setBounds(10, 113, 922, 423);
		sceneDepart.add(pnlAPropos);
		pnlAPropos.setLayout(null);

		pnlInstructions = new JPanel();
		pnlInstructions.setBorder(new MatteBorder(2, 2, 2, 2, mauveFonce));
		pnlInstructions.setBackground(new Color(187, 51, 255, 100));
		pnlInstructions.setBounds(10, 113, 922, 423);
		sceneDepart.add(pnlInstructions);
		pnlInstructions.setLayout(null);
		
		ImageAvecDefilement panInstructions = new ImageAvecDefilement();
		pnlInstructions.add(panInstructions);
		panInstructions.setBounds(0, 0, 922, 423);
		panInstructions.setLargeurCadre(2);
		panInstructions.setBackground(mauvePale);
		panInstructions.setFichierImage("Instructions.jpg");
		
		lblProgramme2 = new JLabel("Sciences Informatiques et Math\u00E9matiques");
		lblProgramme2.setForeground(Color.BLACK);
		lblProgramme2.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 30));
		lblProgramme2.setBounds(36, 345, 850, 35);
		pnlAPropos.add(lblProgramme2);

		lblNom = new JLabel("Mohammed Ariful Islam");
		lblNom.setForeground(Color.BLACK);
		lblNom.setBounds(211, 41, 500, 35);
		pnlAPropos.add(lblNom);
		lblNom.setHorizontalAlignment(SwingConstants.CENTER);
		lblNom.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 30));

		lblProgramme = new JLabel("Int\u00E9gration des apprentissages en");
		lblProgramme.setForeground(Color.BLACK);
		lblProgramme.setBounds(117, 269, 687, 35);
		pnlAPropos.add(lblProgramme);
		lblProgramme.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 30));

		lblCegep = new JLabel("Coll\u00E8ge de Maisonneuve");
		lblCegep.setForeground(Color.BLACK);
		lblCegep.setBounds(211, 117, 500, 35);
		pnlAPropos.add(lblCegep);
		lblCegep.setHorizontalAlignment(SwingConstants.CENTER);
		lblCegep.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 30));

		lblAnnee = new JLabel("2018");
		lblAnnee.setForeground(Color.BLACK);
		lblAnnee.setBounds(410, 193, 101, 35);
		pnlAPropos.add(lblAnnee);
		lblAnnee.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnnee.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 30));

		pnlConcepts = new JPanel();
		pnlConcepts.setBorder(new MatteBorder(2, 2, 2, 2, mauveFonce));
		pnlConcepts.setBackground(new Color(187, 51, 255, 100));
		pnlConcepts.setBounds(10, 113, 922, 423);
		sceneDepart.add(pnlConcepts);
		pnlConcepts.setLayout(null);

		pnlConcepts.setVisible(false);
		ImageAvecDefilement panConcepts = new ImageAvecDefilement();
		pnlConcepts.add(panConcepts);
		panConcepts.setBounds(0, 0, 922, 423);
		panConcepts.setLargeurCadre(2);
		panConcepts.setBackground(mauvePale);
		panConcepts.setFichierImage("ConceptsScientifiques.jpg");
	}

	/**
	 * Sélectionne l'onglet instructions.
	 */
	public void instructionsSelectionnee() {
		lblInstructions.setForeground(mauvePale);
		lblConceptsScientifiques.setForeground(mauveFonce);
		lblAPropos.setForeground(mauveFonce);

		lblInstructions.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
		lblConceptsScientifiques.setBorder(new BevelBorder(BevelBorder.LOWERED, mauvePale, mauvePale, mauveFonce, mauveFonce));
		lblAPropos.setBorder(new BevelBorder(BevelBorder.LOWERED, mauvePale, mauvePale, mauveFonce, mauveFonce));

		pnlAPropos.setVisible(false);
		pnlConcepts.setVisible(false);
		pnlInstructions.setVisible(true);

		sceneDepart.repaint();
	}

	/**
	 * Sélectionne l'onglet Concepts Scientifiques.
	 */
	public void conceptsSelectionnee() {
		lblConceptsScientifiques.setForeground(mauvePale);
		lblAPropos.setForeground(mauveFonce);
		lblInstructions.setForeground(mauveFonce);

		lblConceptsScientifiques.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
		lblInstructions.setBorder(new BevelBorder(BevelBorder.LOWERED, mauvePale, mauvePale, mauveFonce, mauveFonce));
		lblAPropos.setBorder(new BevelBorder(BevelBorder.LOWERED, mauvePale, mauvePale, mauveFonce, mauveFonce));

		pnlAPropos.setVisible(false);
		pnlConcepts.setVisible(true);
		pnlInstructions.setVisible(false);


		sceneDepart.repaint();
	}

	/**
	 * Sélectionne l'onglet À Propos.
	 */
	public void aProposSelectionnee() {
		lblAPropos.setForeground(mauvePale);
		lblConceptsScientifiques.setForeground(mauveFonce);
		lblInstructions.setForeground(mauveFonce);
		lblAPropos.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
		lblConceptsScientifiques.setBorder(new BevelBorder(BevelBorder.LOWERED, mauvePale, mauvePale, mauveFonce, mauveFonce));
		lblInstructions.setBorder(new BevelBorder(BevelBorder.LOWERED, mauvePale, mauvePale, mauveFonce, mauveFonce));

		pnlAPropos.setVisible(true);
		pnlConcepts.setVisible(false);
		pnlInstructions.setVisible(false);

		sceneDepart.repaint();

	}

	/**
	 * Ajoute les écouteurs personnalisés.
	 * @param objEcouteur Les écouteurs.
	 */
	public void addFenetreListener(FenetreListener objEcouteur) {
		OBJETS_ENREGISTRES.add(FenetreListener.class, objEcouteur);
	}

	/**
	 * Écouteur levé lorsque le bouton fermer est cliqué.
	 */
	private void leverEvenFermer() {
		for(FenetreListener ecout : OBJETS_ENREGISTRES.getListeners(FenetreListener.class) ) {
			ecout.fermer();

		}
	}
}
