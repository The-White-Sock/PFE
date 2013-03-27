package gui.capture;

import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * Classe principale qui g�re l'application
 * 
 * @author "Joachim ALIBERT & Guillaume GEDEON"
 * 
 */
public class Launcher {

	/**
	 * Fen�tre de configuration
	 */
	private Config configFrame;

	/**
	 * Bool�en permettant de d�finir si l'application est lanc�e sur Windows
	 */
	private boolean onWindows;

	/**
	 * Classe main qui lance une fen�tre de configuration
	 * 
	 * @param args
	 *            Param�tres pass� lors de l'ex�cution (inutilis�s)
	 */
	public static void main(String[] args) {
		Launcher launcher = new Launcher();
	}

	/**
	 * Constructeur de la classe Launcher qui fait appel � la fonction
	 * initialize
	 */
	public Launcher() {
		initialize();
	}

	/**
	 * Fonction initialize qui lance la fen�tre de configuration en fonction de
	 * l'os
	 */
	private void initialize() {
		// On r�cup�re les diff�rents param�tre du syst�me
		onWindows = RuntimeUtil.isWindows();
		String osName = System.getProperty("os.name");
		String osArch = System.getProperty("os.arch");
		String libPath;

		System.out.println("OS : " + osName + "\nArchi : " + osArch);

		// Configure le chemin d'acc�s aux librairies de VLC en fonction de l'os
		if (onWindows) {
//			if (osArch.equals("x86")) {
//				libPath = "C:\\Program Files\\VideoLAN\\VLC";
//			} else {
//				libPath = "C:\\Program Files (x86)\\VideoLAN\\VLC";
//			}
			libPath = "C:\\Program Files\\VideoLAN\\VLC";
		} else {
			libPath = "/home/linux/vlc/install/lib";
		}

		// On ajoute le chemin d'acc�s aux librairies puis on les charge avec
		// jna
		NativeLibrary
				.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), libPath);
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		// Ouverture de la fen�tre de configuration
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					configFrame = new Config(onWindows);
					configFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
}
