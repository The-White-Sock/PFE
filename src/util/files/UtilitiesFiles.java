package util.files;

import java.io.File;

/**
 * Classe utilitaire qui permet de r�cup�rer la derni�re vid�o dans un dossier
 * 
 * @author "Joachim ALIBERT & Guillaume GEDEON"
 * 
 */
public class UtilitiesFiles {

	/**
	 * Fonction permettant de récupérer la derni�re vid�o dans le dossier
	 * directory
	 * 
	 * @return Un string correspondant au chemin absolu vers la vid�o
	 */
	public static String getLastModifiedVideo(String dirPath) { 
		File[] subfiles = (new File(dirPath)).listFiles();
		String videoPath = null;


		// Dans le cas o� il existe des sous-fichiers on r�cup�re la derni�re
		if (subfiles != null && subfiles.length > 0) {
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
