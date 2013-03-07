package util.files;

import java.io.File;

public class FilePicker {

	private File file;

	public FilePicker(String filePath) {
		file = new File(filePath);
	}

	public String getLastModifiedVideo() {
		/*********************************************************************/
		File[] subfiles = file.listFiles();
		String videoPath = null;

		if (subfiles != null) {
			File derniereVideo = subfiles[0];
			for (int i = 0; i < subfiles.length; i++) {
				File subfile = subfiles[i];
				if (subfile.getName().contains(".avi")) {
					if (subfile.lastModified() > derniereVideo.lastModified()) {
						derniereVideo = subfiles[i];
					}
				}
			}
			if (derniereVideo.getPath().contains(".avi")) {
				videoPath = derniereVideo.getPath();
			}
		}

		System.out.println(videoPath);
		return videoPath;

	}

}
