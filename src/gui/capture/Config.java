package gui.capture;

import gui.player.SuperPlayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Fenêtre de configuration de la capture
 * 
 * @author "Joachim ALIBERT & Guillaume GEDEON"
 * 
 */
public class Config extends JFrame implements ActionListener {

	private static final long serialVersionUID = -305141590333309995L;

	/** Lien vers la location des scripts sous Linux */
	private static final String LOCATION_SCRIPTS = "/home/administrateur/MesScripts";

	private JPanel contentPane;

	private JLabel lblDirectory;
	private JTextField tfDirectory;
	private JButton btChooseDirectory;

	private JLabel lblInterSnap;
	private JSpinner spInterSnap;

	private JLabel lblDurTot;
	private JLabel lblDurTotH;
	private JSpinner spDuTotH;
	private JLabel lblDurTotM;
	private JSpinner spDurTotM;
	private JLabel lblDurTotS;
	private JSpinner spDurTotS;

	private JLabel lblJPGQuality;
	private JSpinner spJPGQuality;
	private JLabel lblInterVid;
	private JLabel lblDurCapt;
	private JSpinner spInterVidH;
	private JLabel lblInterVidH;
	private JSpinner spInterVidM;
	private JLabel lblInterVidM;
	private JSpinner spInterVidS;
	private JLabel lblInterVidS;
	private JSpinner spDurCaptH;
	private JLabel lblDurCaptH;
	private JSpinner spDurCaptM;
	private JLabel lblDurCaptM;
	private JSpinner spDurCaptS;
	private JLabel label;
	private JButton btnLaunch;
	private JLabel lblDurVid;
	private JSpinner spDurVidM;
	private JLabel lblDurVidM;
	private JSpinner spDurVidS;
	private JLabel lblDurVidS;

	private JCheckBox chckbxPreview;

	private JFileChooser fileChooser;

	private String directory;
	private int interSnap;
	private int durTot;
	private int jpgQuality;
	private int interVid;
	private int durCapt;
	private int durVid;
	private boolean preview;

	private String cmd;
	private String[] commandLine = new String[7];
	private boolean onWindows;
	private SuperPlayer superPlayer;

	/**
	 * Constructeur de la classe Config crée et affiche la fenêtre de
	 * configuration de la capture. Certaines options différent en fonction de
	 * l'os
	 * 
	 * @param onWindows
	 *            boolean permettant de dï¿½terminer si le programme est lancï¿½
	 *            sous Windows. Permet de lancer les commandes correspondantes
	 *            ï¿½ l'os
	 */
	public Config(boolean onWindows) {
		super("Configuration de la capture");

		this.onWindows = onWindows;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		superPlayer = new SuperPlayer();

		// initialisation du fileChooser en fonction de l'OS
		if (this.onWindows)
			fileChooser = new JFileChooser(".");
		else
			fileChooser = new JFileChooser("/home/administrateur/");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		/***********************************/
		/** INITIALISATION DE L'INTERFACE **/
		/***********************************/
		setBounds(100, 100, 623, 219);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		setContentPane(contentPane);

		lblDirectory = new JLabel(
				"Dossier d'enregistrement des prises de vue :");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDirectory, 10,
				SpringLayout.WEST, contentPane);
		contentPane.add(lblDirectory);

		lblInterSnap = new JLabel("Intervale entre chaque capture");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblInterSnap, 32,
				SpringLayout.NORTH, contentPane);
		contentPane.add(lblInterSnap);

		lblDurTot = new JLabel("Dur\u00E9e totale de la capture");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDurTot, 6,
				SpringLayout.SOUTH, lblInterSnap);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurTot, 10,
				SpringLayout.WEST, contentPane);
		contentPane.add(lblDurTot);

		lblJPGQuality = new JLabel("Qualit\u00E9 de la conversion JPEG");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblJPGQuality, 0,
				SpringLayout.NORTH, lblInterSnap);
		contentPane.add(lblJPGQuality);

		lblInterVid = new JLabel("Intervale entre chaque vid\u00E9o");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblInterVid, 6,
				SpringLayout.SOUTH, lblDurTot);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblInterVid, 0,
				SpringLayout.WEST, lblDirectory);
		contentPane.add(lblInterVid);

		lblDurCapt = new JLabel(
				"Dur\u00E9e de capture utilis\u00E9e pour une vid\u00E9o");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDurCapt, 6,
				SpringLayout.SOUTH, lblInterVid);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurCapt, 0,
				SpringLayout.WEST, lblDirectory);
		contentPane.add(lblDurCapt);

		tfDirectory = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, lblJPGQuality, 10,
				SpringLayout.WEST, tfDirectory);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblDirectory, -11,
				SpringLayout.WEST, tfDirectory);
		sl_contentPane.putConstraint(SpringLayout.WEST, tfDirectory, 300,
				SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, tfDirectory, 537,
				SpringLayout.WEST, contentPane);
		contentPane.add(tfDirectory);
		tfDirectory.setColumns(10);

		btChooseDirectory = new JButton("...");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btChooseDirectory, 6,
				SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, btChooseDirectory, 6,
				SpringLayout.EAST, tfDirectory);
		sl_contentPane.putConstraint(SpringLayout.EAST, btChooseDirectory, -10,
				SpringLayout.EAST, contentPane);
		contentPane.add(btChooseDirectory);

		spInterSnap = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDirectory, -21,
				SpringLayout.NORTH, spInterSnap);
		sl_contentPane.putConstraint(SpringLayout.WEST, spInterSnap, 213,
				SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, spInterSnap, -346,
				SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblInterSnap, -203,
				SpringLayout.WEST, spInterSnap);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblInterSnap, -6,
				SpringLayout.WEST, spInterSnap);
		sl_contentPane.putConstraint(SpringLayout.NORTH, spInterSnap, 31,
				SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblDirectory, -5,
				SpringLayout.NORTH, spInterSnap);
		spInterSnap.setModel(new SpinnerNumberModel(1, 1, 60, 1));
		contentPane.add(spInterSnap);

		chckbxPreview = new JCheckBox(
				"Activer la pr\u00E9visualitation de la capture");
		sl_contentPane.putConstraint(SpringLayout.WEST, chckbxPreview, 0,
				SpringLayout.WEST, lblDirectory);
		contentPane.add(chckbxPreview);

		spJPGQuality = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.EAST, lblJPGQuality, -11,
				SpringLayout.WEST, spJPGQuality);
		sl_contentPane.putConstraint(SpringLayout.NORTH, tfDirectory, -24,
				SpringLayout.NORTH, spJPGQuality);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, tfDirectory, -2,
				SpringLayout.NORTH, spJPGQuality);
		sl_contentPane.putConstraint(SpringLayout.NORTH, spJPGQuality, -1,
				SpringLayout.NORTH, lblInterSnap);
		sl_contentPane.putConstraint(SpringLayout.WEST, spJPGQuality, 513,
				SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, spJPGQuality, -38,
				SpringLayout.EAST, contentPane);
		spJPGQuality.setModel(new SpinnerNumberModel(50, 1, 100, 1));
		contentPane.add(spJPGQuality);

		spDuTotH = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.WEST, spDuTotH, 185,
				SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblDurTot, -9,
				SpringLayout.WEST, spDuTotH);
		sl_contentPane.putConstraint(SpringLayout.NORTH, spDuTotH, 2,
				SpringLayout.SOUTH, spInterSnap);
		spDuTotH.setModel(new SpinnerNumberModel(9, 0, 24, 1));
		contentPane.add(spDuTotH);

		lblDurTotH = new JLabel("heure(s)");
		sl_contentPane.putConstraint(SpringLayout.EAST, spDuTotH, -6,
				SpringLayout.WEST, lblDurTotH);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurTotH, 230,
				SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblDurTotH, 289,
				SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDurTotH, 3,
				SpringLayout.SOUTH, spInterSnap);
		contentPane.add(lblDurTotH);

		spDurTotM = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spDurTotM, 53,
				SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, spDurTotM, 0,
				SpringLayout.WEST, tfDirectory);
		sl_contentPane.putConstraint(SpringLayout.EAST, spDurTotM, 39,
				SpringLayout.WEST, tfDirectory);
		spDurTotM.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		contentPane.add(spDurTotM);

		spDurTotS = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spDurTotS, 5,
				SpringLayout.SOUTH, lblJPGQuality);
		spDurTotS.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		contentPane.add(spDurTotS);

		lblDurTotM = new JLabel("minute(s)");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDurTotM, 6,
				SpringLayout.SOUTH, lblJPGQuality);
		sl_contentPane.putConstraint(SpringLayout.WEST, spDurTotS, 6,
				SpringLayout.EAST, lblDurTotM);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurTotM, 6,
				SpringLayout.EAST, spDurTotM);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblDurTotM, 72,
				SpringLayout.EAST, spDurTotM);
		contentPane.add(lblDurTotM);

		lblDurTotS = new JLabel("seconde(s)");
		sl_contentPane.putConstraint(SpringLayout.EAST, spDurTotS, -6,
				SpringLayout.WEST, lblDurTotS);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurTotS, 462,
				SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblDurTotS, 542,
				SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDurTotS, 3,
				SpringLayout.SOUTH, spJPGQuality);
		contentPane.add(lblDurTotS);

		spInterVidH = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spInterVidH, -1,
				SpringLayout.NORTH, lblInterVid);
		sl_contentPane.putConstraint(SpringLayout.WEST, spInterVidH, 6,
				SpringLayout.EAST, lblInterVid);
		spInterVidH.setModel(new SpinnerNumberModel(0, 0, 24, 1));
		contentPane.add(spInterVidH);

		lblInterVidH = new JLabel("heure(s)");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblInterVidH, 6,
				SpringLayout.EAST, spInterVidH);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblInterVidH, 0,
				SpringLayout.SOUTH, lblInterVid);
		contentPane.add(lblInterVidH);

		spInterVidM = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spInterVidM, -1,
				SpringLayout.NORTH, lblInterVid);
		sl_contentPane.putConstraint(SpringLayout.WEST, spInterVidM, 10,
				SpringLayout.WEST, spDurTotM);
		spInterVidM.setModel(new SpinnerNumberModel(10, 0, 60, 1));
		contentPane.add(spInterVidM);

		lblInterVidM = new JLabel("minute(s)");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblInterVidM, 0,
				SpringLayout.NORTH, lblInterVid);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblInterVidM, 6,
				SpringLayout.WEST, lblDurTotM);
		contentPane.add(lblInterVidM);

		spInterVidS = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spInterVidS, -1,
				SpringLayout.NORTH, lblInterVid);
		sl_contentPane.putConstraint(SpringLayout.WEST, spInterVidS, 10,
				SpringLayout.WEST, spDurTotS);
		spInterVidS.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		contentPane.add(spInterVidS);

		lblInterVidS = new JLabel("seconde(s)");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblInterVidS, 6,
				SpringLayout.WEST, lblDurTotS);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblInterVidS, 0,
				SpringLayout.SOUTH, lblInterVid);
		contentPane.add(lblInterVidS);

		spDurCaptH = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spDurCaptH, -1,
				SpringLayout.NORTH, lblDurCapt);
		sl_contentPane.putConstraint(SpringLayout.WEST, spDurCaptH, 6,
				SpringLayout.EAST, lblDurCapt);
		spDurCaptH.setModel(new SpinnerNumberModel(3, 0, 24, 1));
		contentPane.add(spDurCaptH);

		lblDurCaptH = new JLabel("heure(s)");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDurCaptH, 0,
				SpringLayout.NORTH, lblDurCapt);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurCaptH, 7,
				SpringLayout.WEST, spInterVidM);
		contentPane.add(lblDurCaptH);

		spDurCaptM = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spDurCaptM, -1,
				SpringLayout.NORTH, lblDurCapt);
		sl_contentPane.putConstraint(SpringLayout.WEST, spDurCaptM, 11,
				SpringLayout.EAST, lblDurCaptH);
		spDurCaptM.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		contentPane.add(spDurCaptM);

		lblDurCaptM = new JLabel("minute(s)");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDurCaptM, 0,
				SpringLayout.NORTH, lblDurCapt);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurCaptM, 4,
				SpringLayout.EAST, spDurCaptM);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblDurCaptM, 17,
				SpringLayout.NORTH, lblDurCapt);
		contentPane.add(lblDurCaptM);

		spDurCaptS = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.EAST, lblDurCaptM, -13,
				SpringLayout.WEST, spDurCaptS);
		sl_contentPane.putConstraint(SpringLayout.NORTH, spDurCaptS, -1,
				SpringLayout.NORTH, lblDurCapt);
		sl_contentPane.putConstraint(SpringLayout.EAST, spDurCaptS, 0,
				SpringLayout.EAST, tfDirectory);
		spDurCaptS.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		contentPane.add(spDurCaptS);

		label = new JLabel("seconde(s)");
		sl_contentPane.putConstraint(SpringLayout.NORTH, label, 0,
				SpringLayout.NORTH, lblDurCapt);
		sl_contentPane.putConstraint(SpringLayout.EAST, label, 0,
				SpringLayout.EAST, contentPane);
		contentPane.add(label);

		btnLaunch = new JButton("Lancer la capture");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnLaunch, 0,
				SpringLayout.WEST, spDurTotS);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnLaunch, 0,
				SpringLayout.SOUTH, contentPane);
		contentPane.add(btnLaunch);

		lblDurVid = new JLabel("Dur\u00E9e de la vid\u00E9o");
		sl_contentPane.putConstraint(SpringLayout.NORTH, chckbxPreview, 6,
				SpringLayout.SOUTH, lblDurVid);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDurVid, 6,
				SpringLayout.SOUTH, lblDurCapt);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurVid, 0,
				SpringLayout.WEST, lblDirectory);
		contentPane.add(lblDurVid);

		spDurVidM = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spDurVidM, -1,
				SpringLayout.NORTH, lblDurVid);
		sl_contentPane.putConstraint(SpringLayout.WEST, spDurVidM, 6,
				SpringLayout.EAST, lblDurVid);
		spDurVidM.setModel(new SpinnerNumberModel(2, 0, 60, 1));
		contentPane.add(spDurVidM);

		lblDurVidM = new JLabel("minute(s)");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurVidM, 6,
				SpringLayout.EAST, spDurVidM);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblDurVidM, 0,
				SpringLayout.SOUTH, lblDurVid);
		contentPane.add(lblDurVidM);

		spDurVidS = new JSpinner();
		sl_contentPane.putConstraint(SpringLayout.NORTH, spDurVidS, -1,
				SpringLayout.NORTH, lblDurVid);
		sl_contentPane.putConstraint(SpringLayout.EAST, spDurVidS, 0,
				SpringLayout.EAST, lblDirectory);
		spDurVidS.setModel(new SpinnerNumberModel(0, 0, 60, 1));
		contentPane.add(spDurVidS);

		lblDurVidS = new JLabel("seconde(s)");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDurVidS, 0,
				SpringLayout.WEST, tfDirectory);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblDurVidS, 0,
				SpringLayout.SOUTH, lblDurVid);
		contentPane.add(lblDurVidS);

		// Ajout de listener pour les deux boutons
		btChooseDirectory.addActionListener(this);
		btnLaunch.addActionListener(this);
	}

	/**
	 * Gestion des ï¿½vï¿½nements liï¿½s aux boutons
	 */
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action");

		// Choix Du dossier
		if (e.getSource() == btChooseDirectory) {
			int returnVal = fileChooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				tfDirectory.setText(fileChooser.getSelectedFile()
						.getAbsolutePath());
			}
		}

		// Lancement de la capture
		else if (e.getSource() == btnLaunch) {
			// rï¿½cupï¿½ration des diffï¿½rents paramï¿½tres
			directory = tfDirectory.getText();
			interSnap = (Integer) spInterSnap.getValue();
			durTot = (Integer) spDuTotH.getValue() * 3600
					+ (Integer) spDurTotM.getValue() * 60
					+ (Integer) spDurTotS.getValue();
			jpgQuality = (Integer) spJPGQuality.getValue();
			interVid = (Integer) spInterVidH.getValue() * 3600
					+ (Integer) spInterVidM.getValue() * 60
					+ (Integer) spInterVidS.getValue();
			durCapt = (Integer) spDurCaptH.getValue() * 3600
					+ (Integer) spDurCaptM.getValue() * 60
					+ (Integer) spDurCaptS.getValue();
			durVid = (Integer) spDurVidM.getValue() * 60
					+ (Integer) spDurVidS.getValue();
			preview = chckbxPreview.isSelected();

			if (validParameters()) {
				// CrÃ©ation de la commande pour Windows
				if (onWindows) {
					cmd = "autoExe " + interSnap + " " + durTot + " "
							+ jpgQuality + " " + preview + " " + interVid + " "
							+ durCapt + " " + durVid;
					System.out.println(cmd);
				}

				// CrÃ©ation de la commande pour Linux
				else {
					commandLine[0] = "mainScript.sh";
					commandLine[1] = String.valueOf(interSnap);
					commandLine[2] = String.valueOf(jpgQuality);
					commandLine[3] = String.valueOf(interVid);
					commandLine[4] = String.valueOf(durCapt);
					commandLine[5] = String.valueOf(durVid);
					commandLine[6] = directory;
					System.out.println("Commande Linux : " + commandLine[0]
							+ " " + commandLine[1] + " " + commandLine[2] + " "
							+ commandLine[3] + " " + commandLine[4] + " "
							+ commandLine[5] + " " + commandLine[6]);
				}

				// ExÃ©cution de la commande dans un shell
				try {
					// Commande Windows
					if (System.getProperty("os.name").contains("Windows")) {
						Runtime.getRuntime().exec("cmd.exe /c start " + cmd,
								null, new File(directory));
						// Ajout du dossier Video Ã  directory pour le lecteur
						directory += "\\Video";
					}
					// Commande Linux
					else {
						Runtime.getRuntime().exec(commandLine, null, null);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				System.out.println("Vid directory : " + directory);

				// Ouverture de la fenÃªtre du Player
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							superPlayer.setVisible(true);
							superPlayer.changeDirectory(directory);

							superPlayer.mediaPlayer.toggleFullScreen();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	}

	private boolean validParameters() {
		boolean validParameters = true;
		String errorMessage = "";

		if (directory.equals("")) {
			errorMessage += "Veuillez spécifier un dossier !\n";
		}
		
		if (durTot == 0) {
			errorMessage += "La durée totale de la capture doit être supérieur à 0 !\n";
		} else if (interSnap > durTot) {
			errorMessage += "L'intervale entre chaque prise de vue doit être inférieure à la durée totale !\n";
		}
		
		if (interVid == 0) {
			errorMessage += "L'intervale entre chaque vidéo doit être supérieur à 0 !\n";
		} else if (interVid > durTot) {
			errorMessage += "L'intervale entre chaque vidéo doit être inférieure à la durée totale !\n";
		}
		
		if (durCapt == 0) {
			errorMessage += "La durée de la capture pour une vidéo doit être supérieur à 0 !\n";
		} else if (durCapt > durTot) {
			errorMessage += "La durée de la capture pour une vidéo doit être inférieure à la durée totale !\n";
		}

		if (durVid == 0) {
			errorMessage += "La durée de la vidéo doit être supérieur à 0 !\n";
		} else if (((durCapt / interSnap) / durVid) < 10) {
			errorMessage += "Le nombre de fps la vidéo sera inférieur à 10 fps !"
					+ "\n    Augmentez la durée de la capture ou diminuez la durée de la vidéo.\n";
		}

		if (!errorMessage.equals("")) {
			JOptionPane.showMessageDialog(this, errorMessage,
					"Paramètres invalides !", JOptionPane.ERROR_MESSAGE);
			validParameters = false;
		}

		return validParameters;
	}

}
