package util.files;

import java.io.File;

/**
 * Classe utilitaire qui permet de r�cup�rer la derni�re vid�o dans un dossier
 * 
 * @author "Joachim ALIBERT & Guillaume GEDEON"
 * 
 */
public class FilePicker {

	/** File correspondant au dossier dans lequel chercher **/
	private File directory;

	/**
	 * Constructeur de la classe FilePicker. Initialize directory.
	 * 
	 * @param dirPath
	 */
	public FilePicker(String dirPath) {
		directory = new File(dirPath);
	}

	/**
	 * Fonction permettant de récupérer la derni�re vid�o dans le dossier
	 * directory
	 * 
	 * @return Un string correspondant au chemin absolu vers la vid�o
	 */
	public String getLastModifiedVideo() {
		File[] subfiles = directory.listFiles();
		String videoPath = null;

		// Dans le cas o� il existe des sous-fichiers on r�cup�re la derni�re
		if (subfiles != null && subfiles.length != 0) {
			File derniereVideo = subfiles[0];
			for (int i = 0; i < subfiles.length; i++) {
				File subfile = subfiles[i];
				if (subfile.getName().contains(".avi")) {
					if (subfile.lastModified() > derniereVideo.lastModified()) {
						derniereVideo = subfiles[i];
					}
				}
			}
			// On test si le dernier fichier est bien un fichier avi
			if (derniereVideo.getPath().contains(".avi")) {
				videoPath = derniereVideo.getPath();
			}
		}

		System.out.println(videoPath);
		return videoPath;

	}

}
