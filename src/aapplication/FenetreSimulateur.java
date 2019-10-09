package aapplication;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import composantsgraphiques.BackgroundSimulateur;
import composantsgraphiques.SceneUnivers;
import ecouteurs.FenetreListener;
import ecouteurs.ResultatsListener;

/**
 * Class qui permet de faire une simulation de la gravity dans l'espace.
 * @author Mohammed Ariful Islam
 *
 */

public class FenetreSimulateur extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JSlider sldAngle;
	private JSpinner spnMasse;
	private JSpinner spnRayon;
	private JSpinner spnVitesse;
	private JSpinner spnAngle;
	private SceneUnivers sceneUnivers;
	private JSpinner spnExposantMasse;
	private JSpinner spnExposantRayon;
	private JSpinner spnExposantVitesse;
	private JLabel lblVitesseCourant;
	private JLabel lblAccelerationCourant;
	private JLabel lblFgCourant;
	private Color mauvePale = new Color(187, 51, 255);
	private Color mauveFonce = new Color(136, 0, 204);
	private JLabel lblTemps;
	private JButton btnAnnuler;
	private JButton btnAjouter;
	private JButton btnPlanete;
	private JButton btnEtoile;

	private final EventListenerList OBJETS_ENREGISTRES = new EventListenerList();
	private int onglet;
	private JLabel lblFgCourant2;
	private JLabel lblChampGravite;
	private JLabel lblChmapGravite2;

	private DecimalFormat formatteur = new DecimalFormat("0.####E0");
	private JMenuItem mntmConfiguration1;
	private JMenuItem mntmConfiguration2;
	private JMenuItem mntmConfiguration3;

	private boolean etatDemarrer = true;
	private boolean etatArreter = false;
	private JLabel lblArreter;
	private JLabel lblDemarrer;
	private JButton btnSupprimer;


	/**
	 * Constructeur qui ajoute les composantes à l'interface du simulateur.
	 */
	public FenetreSimulateur() {

		setTitle("Simulateur de gravit\u00E9");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1901, 900);

		JMenuBar barMenu = new JMenuBar();
		setJMenuBar(barMenu);

		JMenu menuAide = new JMenu("Aide");
		barMenu.add(menuAide);

		JMenuItem mntmMenu = new JMenuItem("Menu");
		mntmMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				leverEvenMenu();
				
				sceneUnivers.recommencer();
				enableAstre();
				disableParam();
				
				etatDemarrer = true;
				lblDemarrer.setEnabled(false);
				lblDemarrer.setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));
		
				etatArreter = false;
				lblArreter.setEnabled(true);
				lblArreter.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));

				btnPlanete.setEnabled(false);
			}
		});
		menuAide.add(mntmMenu);

		JMenuItem mntmInstructions = new JMenuItem("Instructions");
		mntmInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onglet = 0;
				leverEvenAide();
			}
		});
		menuAide.add(mntmInstructions);

		JMenuItem mntmConceptsScientifiques = new JMenuItem("Concepts Scientifiques");
		mntmConceptsScientifiques.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onglet = 1;
				leverEvenAide();
			}
		});
		menuAide.add(mntmConceptsScientifiques);

		JMenuItem mntmAPropos = new JMenuItem("\u00C0 Propos");
		mntmAPropos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onglet = 3;
				leverEvenAide();
			}
		});
		menuAide.add(mntmAPropos);

		JMenuItem mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		menuAide.add(mntmQuitter);

		JMenu mnConfiguration = new JMenu("Configuration");
		barMenu.add(mnConfiguration);

		mntmConfiguration1 = new JMenuItem("Système Terre-Soleil");
		mntmConfiguration1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneUnivers.preset1();
				mntmConfiguration1.setEnabled(false);
				mntmConfiguration2.setEnabled(true);
				mntmConfiguration3.setEnabled(true);
				
				enableAstre();

			}
		});
		mnConfiguration.add(mntmConfiguration1);

		mntmConfiguration2 = new JMenuItem("2 \u00C9toiles et une plan\u00E8te au centre");
		mntmConfiguration2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneUnivers.preset2();
				mntmConfiguration1.setEnabled(true);
				mntmConfiguration2.setEnabled(false);
				mntmConfiguration3.setEnabled(true);
				
				enableAstre();
			}
		});
		mnConfiguration.add(mntmConfiguration2);

		mntmConfiguration3 = new JMenuItem("Trajectoire en 8");
		mntmConfiguration3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneUnivers.preset3();
				mntmConfiguration1.setEnabled(true);
				mntmConfiguration2.setEnabled(true);
				mntmConfiguration3.setEnabled(false);
				
				enableAstre();	
			}
		});
		mnConfiguration.add(mntmConfiguration3);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		sceneUnivers = new SceneUnivers();
		sceneUnivers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				sceneUnivers.requestFocus();
			}
		});
		sceneUnivers.addResultatsListener(new ResultatsListener() {
			public void accelerationAstre(double acceleration) {
				lblAccelerationCourant.setText(String.format("L'acceleration de l'astre est de %.3f m/s\u00B2.", acceleration));
			}
			public void vitesseAstre(double vitesse) {
				lblVitesseCourant.setText(String.format("La vitesse de l'astre est de %.3f m/s.", vitesse));
			}
			public void fgAstre(double fg) {
				lblFgCourant2.setText("est de " + formatteur.format(fg) + " N.");

			}
			public void tempsEcoule(double temps) {
				lblTemps.setText(String.format("Le temps totale écoulé de %.0f s.", temps));
			}
			@Override
			public void champGravite(double champ) {
				lblChmapGravite2.setText(String.format("est de " + formatteur.format(champ) + " N/kg.", champ));
			}
			@Override
			public void astreSelect(boolean estSlect) {
				if (estSlect) {
					btnSupprimer.setEnabled(true);
				} else {
					btnSupprimer.setEnabled(false);
				}
				
			}
		});
		sceneUnivers.setBounds(10, 82, 1272, 616);
		contentPane.add(sceneUnivers);
		sceneUnivers.setFocusable(true);

		BackgroundSimulateur backgroundSimulateur = new BackgroundSimulateur();
		backgroundSimulateur.setBounds(0, 0, 1885, 839);
		contentPane.add(backgroundSimulateur);
		backgroundSimulateur.setLayout(null);

		lblDemarrer = new JLabel("D\u00E9marrer");
		lblDemarrer.setHorizontalAlignment(SwingConstants.CENTER);
		lblDemarrer.setForeground(mauvePale);
		lblDemarrer.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 40));
		lblDemarrer.setBounds(10, 710, 381, 67);
		lblDemarrer.setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));
		lblDemarrer.setEnabled(false);
		lblDemarrer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (!etatDemarrer) {
					lblDemarrer.setBorder(new BevelBorder(BevelBorder.LOWERED, mauveFonce, mauveFonce, mauvePale, mauvePale));
					lblDemarrer.setForeground(mauveFonce);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!etatDemarrer) {
					lblDemarrer.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
					lblDemarrer.setForeground(mauvePale);
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!etatDemarrer) {
					sceneUnivers.demarrer();
					etatDemarrer = true;
					lblDemarrer.setEnabled(false);
					lblDemarrer.setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));

					etatArreter = false;
					lblArreter.setEnabled(true);
					lblArreter.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce, mauveFonce, mauvePale, mauvePale));
					lblArreter.setForeground(mauvePale);
				}
			}
		});
		backgroundSimulateur.add(lblDemarrer);

		JPanel pnlParametre = new JPanel();
		pnlParametre.setBorder(new TitledBorder(null, "Param\u00E8tres", TitledBorder.LEADING, TitledBorder.TOP, new Font("Rockwell Condensed", Font.PLAIN, 18), null));
		pnlParametre.setBounds(1287, 84, 287, 546);
		backgroundSimulateur.add(pnlParametre);
		pnlParametre.setLayout(null);

		btnAnnuler = new JButton("Annuler");
		btnAnnuler.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		btnAnnuler.setEnabled(false);
		btnAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneUnivers.annuler();
				enableAstre();
				disableParam();
				sceneUnivers.requestFocus();
			}
		});
		btnAnnuler.setBounds(11, 461, 127, 44);
		pnlParametre.add(btnAnnuler);

		btnAjouter = new JButton("Ajouter");
		btnAjouter.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		btnAjouter.setEnabled(false);
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneUnivers.lancer();
				disableParam();
				enableAstre();
				sceneUnivers.requestFocus();
			}
		});
		btnAjouter.setBounds(149, 461, 127, 44);
		pnlParametre.add(btnAjouter);

		JLabel lblTypeDastre = new JLabel("Type d'astre:");
		lblTypeDastre.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblTypeDastre.setBounds(9, 39, 92, 20);
		pnlParametre.add(lblTypeDastre);

		btnEtoile = new JButton("\u00C9toile");
		btnEtoile.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		btnEtoile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sceneUnivers.ajouterEtoile((double)spnMasse.getValue() * Math.pow(10, (int) spnExposantMasse.getValue()), (double)spnRayon.getValue() * Math.pow(10, (int) spnExposantRayon.getValue()));
				enableParam();
				disableAstre();

				spnMasse.setValue((double)2);
				spnExposantMasse.setValue((int)30);
				spnRayon.setValue((double)7);
				spnExposantRayon.setValue((int)8);

				spnVitesse.setEnabled(false);
				spnExposantVitesse.setEnabled(false);
				spnAngle.setEnabled(false);
				sldAngle.setEnabled(false);
				sceneUnivers.requestFocus();
			}
		});
		btnEtoile.setBounds(149, 70, 127, 51);
		pnlParametre.add(btnEtoile);

		spnMasse = new JSpinner();
		spnMasse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				sceneUnivers.changerMasse((double)spnMasse.getValue() * Math.pow(10, (int) spnExposantMasse.getValue()));
			}
		});
		spnMasse.setEnabled(false);
		spnMasse.setModel(new SpinnerNumberModel(2.0, 0.0, 10.0, 1.0));
		spnMasse.setBounds(65, 159, 77, 23);
		pnlParametre.add(spnMasse);

		JLabel lblMasse = new JLabel("Masse:");
		lblMasse.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblMasse.setOpaque(true);
		lblMasse.setBounds(9, 160, 61, 20);
		pnlParametre.add(lblMasse);

		JLabel lblRayon = new JLabel("Rayon:");
		lblRayon.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblRayon.setBounds(9, 219, 61, 20);
		pnlParametre.add(lblRayon);

		spnRayon = new JSpinner();
		spnRayon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sceneUnivers.changerRayon((double)spnRayon.getValue() * Math.pow(10, (int) spnExposantRayon.getValue()));
			}
		});
		spnRayon.setEnabled(false);
		spnRayon.setModel(new SpinnerNumberModel(7.0, 1.0, 10.0, 1.0));
		spnRayon.setBounds(65, 218, 77, 23);
		pnlParametre.add(spnRayon);

		spnVitesse = new JSpinner();
		spnVitesse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sceneUnivers.changerVitesse((double)spnVitesse.getValue() * Math.pow(10, (int) spnExposantVitesse.getValue()), (int)spnAngle.getValue());
			}
		});
		spnVitesse.setEnabled(false);
		spnVitesse.setModel(new SpinnerNumberModel(0.0, 0.0, 10.0, 1.0));
		spnVitesse.setBounds(65, 277, 77, 23);
		pnlParametre.add(spnVitesse);

		JLabel lblVitesse = new JLabel("Vitesse:");
		lblVitesse.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblVitesse.setBounds(9, 278, 61, 20);
		pnlParametre.add(lblVitesse);

		spnAngle = new JSpinner();
		spnAngle.setEnabled(false);
		spnAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sldAngle.setValue((int)spnAngle.getValue());
				sceneUnivers.requestFocus();
			}
		});
		spnAngle.setModel(new SpinnerNumberModel(0, 0, 360, 1));
		spnAngle.setBounds(204, 370, 57, 23);
		pnlParametre.add(spnAngle);

		JLabel lblAngleDeLancement = new JLabel("Angle de lancement:");
		lblAngleDeLancement.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblAngleDeLancement.setBounds(9, 337, 127, 20);
		pnlParametre.add(lblAngleDeLancement);

		btnPlanete = new JButton("Plan\u00E8te");
		btnPlanete.setEnabled(false);
		btnPlanete.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		btnPlanete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneUnivers.ajouterPlanete((double)spnMasse.getValue() * Math.pow(10, (int) spnExposantMasse.getValue()), (double)spnRayon.getValue() * Math.pow(10, (int) spnExposantRayon.getValue()),
						(double)spnVitesse.getValue() * Math.pow(10, (int) spnExposantVitesse.getValue()), (int)spnAngle.getValue());

				spnMasse.setValue((double)5);
				spnExposantMasse.setValue((int)24);
				spnRayon.setValue((double)6);
				spnExposantRayon.setValue((int)6);
				spnVitesse.setValue((double)3);
				spnExposantVitesse.setValue((int)4);
				enableParam();
				disableAstre();
				sceneUnivers.requestFocus();
			}
		});
		btnPlanete.setBounds(11, 70, 127, 51);
		pnlParametre.add(btnPlanete);

		spnExposantMasse = new JSpinner();
		spnExposantMasse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sceneUnivers.changerMasse((double)spnMasse.getValue() * Math.pow(10, (int) spnExposantMasse.getValue()));
			}
		});
		spnExposantMasse.setEnabled(false);
		spnExposantMasse.setModel(new SpinnerNumberModel(30, 0, 50, 1));
		spnExposantMasse.setBounds(180, 145, 40, 20);
		pnlParametre.add(spnExposantMasse);

		spnExposantRayon = new JSpinner();
		spnExposantRayon.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sceneUnivers.changerRayon((double)spnRayon.getValue() * Math.pow(10, (int) spnExposantRayon.getValue()));
			}
		});
		spnExposantRayon.setEnabled(false);
		spnExposantRayon.setModel(new SpinnerNumberModel(8, 0, 17, 1));
		spnExposantRayon.setBounds(180, 204, 40, 20);
		pnlParametre.add(spnExposantRayon);

		spnExposantVitesse = new JSpinner();
		spnExposantVitesse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				sceneUnivers.changerVitesse((double)spnVitesse.getValue() * Math.pow(10, (int) spnExposantVitesse.getValue()), (int)spnAngle.getValue());
			}
		});
		spnExposantVitesse.setEnabled(false);
		spnExposantVitesse.setModel(new SpinnerNumberModel(4, 0, 6, 1));
		spnExposantVitesse.setBounds(180, 263, 40, 20);
		pnlParametre.add(spnExposantVitesse);

		sldAngle = new JSlider();
		sldAngle.setEnabled(false);
		sldAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				spnAngle.setValue(sldAngle.getValue());
				sceneUnivers.changerVitesse((double)spnVitesse.getValue() * Math.pow(10, (int) spnExposantVitesse.getValue()), (int)spnAngle.getValue());
				sceneUnivers.requestFocus();
				sceneUnivers.changerAngle((int)spnAngle.getValue());
			}
		});
		sldAngle.setValue(0);
		sldAngle.setMaximum(360);
		sldAngle.setBounds(9, 368, 190, 26);
		pnlParametre.add(sldAngle);

		JLabel lblx10Masse = new JLabel("x 10");
		lblx10Masse.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblx10Masse.setBounds(149, 163, 46, 14);
		pnlParametre.add(lblx10Masse);

		JLabel lblx10Rayon = new JLabel("x 10");
		lblx10Rayon.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblx10Rayon.setBounds(149, 222, 46, 14);
		pnlParametre.add(lblx10Rayon);

		JLabel lblx10Vitesse = new JLabel("x 10");
		lblx10Vitesse.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblx10Vitesse.setBounds(149, 281, 46, 14);
		pnlParametre.add(lblx10Vitesse);

		JLabel lblKg = new JLabel("Kg");
		lblKg.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblKg.setBounds(234, 160, 46, 20);
		pnlParametre.add(lblKg);

		JLabel lblM = new JLabel("m");
		lblM.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblM.setBounds(234, 219, 46, 20);
		pnlParametre.add(lblM);

		JLabel lblMParS = new JLabel("m/s");
		lblMParS.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblMParS.setBounds(234, 278, 46, 20);
		pnlParametre.add(lblMParS);

		JLabel lblDegree = new JLabel("\u00B0");
		lblDegree.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		lblDegree.setBounds(263, 372, 46, 14);
		pnlParametre.add(lblDegree);

		lblArreter = new JLabel("Arr\u00EAter");
		lblArreter.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (!etatArreter) {
					lblArreter.setBorder(new BevelBorder(BevelBorder.LOWERED, mauveFonce,mauveFonce, mauvePale, mauvePale));
					lblArreter.setForeground(mauveFonce);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (!etatArreter) {
					lblArreter.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
					lblArreter.setForeground(mauvePale);
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!etatArreter) {
					sceneUnivers.arreter();
					etatArreter = true;
					lblArreter.setEnabled(false);
					lblArreter.setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));

					etatDemarrer = false;
					lblDemarrer.setEnabled(true);
					lblDemarrer.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
				}
			}
		});
		lblArreter.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
		lblArreter.setHorizontalAlignment(SwingConstants.CENTER);
		lblArreter.setForeground(mauvePale);
		lblArreter.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 40));
		lblArreter.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
		lblArreter.setBounds(449, 710, 381, 67);
		backgroundSimulateur.add(lblArreter);

		JLabel lblRecommencer = new JLabel("Recommencer");
		lblRecommencer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				lblRecommencer.setBorder(new BevelBorder(BevelBorder.LOWERED, mauveFonce,mauveFonce, mauvePale, mauvePale));
				lblRecommencer.setForeground(mauveFonce);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblRecommencer.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
				lblRecommencer.setForeground(mauvePale);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				sceneUnivers.recommencer();
				enableAstre();
				disableParam();
				
				etatDemarrer = true;
				lblDemarrer.setEnabled(false);
				lblDemarrer.setBorder(new BevelBorder(BevelBorder.RAISED, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));
		
				etatArreter = false;
				lblArreter.setEnabled(true);
				lblArreter.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));

				btnPlanete.setEnabled(false);
			}
		});
		lblRecommencer.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
		lblRecommencer.setHorizontalAlignment(SwingConstants.CENTER);
		lblRecommencer.setForeground(mauvePale);
		lblRecommencer.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 40));
		lblRecommencer.setBorder(new BevelBorder(BevelBorder.RAISED, mauveFonce,mauveFonce, mauvePale, mauvePale));
		lblRecommencer.setBounds(891, 710, 381, 67);
		backgroundSimulateur.add(lblRecommencer);

		JLabel lblTitreAvant = new JLabel("SIMULATEUR DE GRAVIT\u00C9");
		lblTitreAvant.setForeground(mauvePale);
		lblTitreAvant.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 55));
		lblTitreAvant.setBounds(349, 14, 881, 72);
		backgroundSimulateur.add(lblTitreAvant);

		JLabel lblTitreArriere = new JLabel("SIMULATEUR DE GRAVIT\u00C9");
		lblTitreArriere.setForeground(mauveFonce);
		lblTitreArriere.setFont(new Font("Rockwell Extra Bold", Font.BOLD, 55));
		lblTitreArriere.setBounds(346, 11, 881, 72);
		backgroundSimulateur.add(lblTitreArriere);

		JPanel pnlResultats = new JPanel();
		pnlResultats.setBorder(new TitledBorder(null, "R\u00E9sultats", TitledBorder.LEADING, TitledBorder.TOP, new Font("Rockwell Condensed", Font.PLAIN, 18), null));
		pnlResultats.setBounds(1584, 84, 287, 546);
		backgroundSimulateur.add(pnlResultats);
		pnlResultats.setLayout(null);

		lblVitesseCourant = new JLabel("La vitesse de l'astre est de 0 m/s.");
		lblVitesseCourant.setFont(new Font("Rockwell Condensed", Font.PLAIN, 17));
		lblVitesseCourant.setBounds(10, 59, 267, 20);
		pnlResultats.add(lblVitesseCourant);

		lblAccelerationCourant = new JLabel("L'acc\u00E9l\u00E9ration de l'astre est de 0 m/s\u00B2.");
		lblAccelerationCourant.setFont(new Font("Rockwell Condensed", Font.PLAIN, 17));
		lblAccelerationCourant.setBounds(10, 138, 267, 20);
		pnlResultats.add(lblAccelerationCourant);

		lblFgCourant = new JLabel("La force gravitationnelle exerc\u00E9e sur l'astre");
		lblFgCourant.setFont(new Font("Rockwell Condensed", Font.PLAIN, 17));
		lblFgCourant.setBounds(10, 217, 267, 20);
		pnlResultats.add(lblFgCourant);

		lblTemps = new JLabel("Temps totale \u00E9coul\u00E9: 0 s.");
		lblTemps.setFont(new Font("Rockwell Condensed", Font.PLAIN, 17));
		lblTemps.setBounds(10, 375, 267, 20);
		pnlResultats.add(lblTemps);

		lblChampGravite = new JLabel("Champ gravitationnelle de l'astre");
		lblChampGravite.setFont(new Font("Rockwell Condensed", Font.PLAIN, 17));
		lblChampGravite.setBounds(10, 296, 267, 20);
		pnlResultats.add(lblChampGravite);

		btnSupprimer = new JButton("Supprimer l'astre");
		btnSupprimer.setEnabled(false);
		btnSupprimer.setFont(new Font("Rockwell Condensed", Font.PLAIN, 18));
		btnSupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sceneUnivers.supprimer();
				sceneUnivers.requestFocus();
			}
		});
		btnSupprimer.setBounds(61, 454, 165, 33);
		pnlResultats.add(btnSupprimer);

		lblFgCourant2 = new JLabel("est de 0 N.");
		lblFgCourant2.setFont(new Font("Rockwell Condensed", Font.PLAIN, 17));
		lblFgCourant2.setBounds(10, 237, 267, 20);
		pnlResultats.add(lblFgCourant2);

		lblChmapGravite2 = new JLabel("est de 0 N/kg.");
		lblChmapGravite2.setFont(new Font("Rockwell Condensed", Font.PLAIN, 17));
		lblChmapGravite2.setBounds(10, 316, 267, 20);
		pnlResultats.add(lblChmapGravite2);

	}


	/**
	 * Active les composants qui permette de changer les paramètres d'un astre.
	 */
	private void enableParam() {
		spnAngle.setEnabled(true);
		spnMasse.setEnabled(true);
		spnRayon.setEnabled(true);
		spnVitesse.setEnabled(true);
		spnExposantMasse.setEnabled(true);
		spnExposantRayon.setEnabled(true);
		spnExposantVitesse.setEnabled(true);
		sldAngle.setEnabled(true);
		btnAnnuler.setEnabled(true);
		btnAjouter.setEnabled(true);

	}


	/**
	 * Désactive les composants qui permette de changer les paramètres d'un astre.
	 */
	private void disableParam() {
		spnAngle.setEnabled(false);
		spnMasse.setEnabled(false);
		spnRayon.setEnabled(false);
		spnVitesse.setEnabled(false);
		spnExposantMasse.setEnabled(false);
		spnExposantRayon.setEnabled(false);
		spnExposantVitesse.setEnabled(false);
		sldAngle.setEnabled(false);
		btnAnnuler.setEnabled(false);
		btnAjouter.setEnabled(false);

	}

	/**
	 * Active les boutons qui ajoute des astres.
	 */
	private void enableAstre() {
		btnEtoile.setEnabled(true);
		btnPlanete.setEnabled(true);
	}

	/**
	 * Désactive les boutons qui ajoute des astres.
	 */
	private void disableAstre() {
		btnEtoile.setEnabled(false);
		btnPlanete.setEnabled(false);
	}

	/**
	 * Ajoute les écouteurs personnalisés.
	 * @param objEcouteur Les écouteurs.
	 */
	public void addFenetreListener(FenetreListener objEcouteur) {
		OBJETS_ENREGISTRES.add(FenetreListener.class, objEcouteur);
	}

	/**
	 * Écouteur levé lorsque l'aide est sélectionné.
	 */
	private void leverEvenAide() {
		for(FenetreListener ecout : OBJETS_ENREGISTRES.getListeners(FenetreListener.class) ) {
			ecout.aide(onglet);
		}
	}

	/**
	 * Écouteur levé lorsque le menu est sélectionné.
	 */
	private void leverEvenMenu() {
		for(FenetreListener ecout : OBJETS_ENREGISTRES.getListeners(FenetreListener.class) ) {
			ecout.menu();
		}
	}
}

