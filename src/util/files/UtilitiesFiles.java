package util.files;

import java.io.File;

/**
 * Classe utilitaire qui permet de récupérer la dernière vidéo dans un dossier
 * 
 * @author "Joachim ALIBERT & Guillaume GEDEON"
 * 
 */
public class UtilitiesFiles {

	/**
	 * Fonction permettant de récupérer la dernière vidéo dans le dossier
	 * directory
	 * 
	 * @return Un string correspondant au chemin absolu vers la vidéo
	 */
	public static String getLastModifiedVideo(String dirPath) { 
		File[] subfiles = (new File(dirPath)).listFiles();
		String videoPath = null;

		// Dans le cas où il existe des sous-fichiers on récupère la dernière
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
