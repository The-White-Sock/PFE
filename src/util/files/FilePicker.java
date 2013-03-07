package util.files;

import java.io.File;

public class FilePicker {
	
	private File file;
	
	public FilePicker(String filePath)
	{
		file = new File(filePath);	
	}
	
	public String getLastModifiedVideo()
	{
		/*********************************************************************/
		File[] subfiles = file.listFiles();
		File derniereVideo = null;
		if (subfiles.length > 0)
		{
			derniereVideo = subfiles[0];
			for (int i = 1; i < subfiles.length; i++) 
			{
				File subfile = subfiles[i];
				if (subfile.getName().contains(".avi")) 
				{
					if (subfile.lastModified() > derniereVideo.lastModified()) 
					{
						derniereVideo = subfiles[i];
					}
				}
			}

		}
		//System.out.println(derniereVideo.getAbsolutePath());
		return derniereVideo.getAbsolutePath();
		
	}

}
