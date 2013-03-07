package capture.gui;

import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class Launcher {

	private Config configFrame;

	private boolean onWindows;

	public static void main(String[] args) {
		Launcher launcher = new Launcher();
	}

	public Launcher() {
		initialize();
	}

	private void initialize() {
		onWindows = System.getProperty("os.name").contains("Windows");

		if (onWindows) {
			System.out.println("OS = Windows");
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
					"C:\\Program Files (x86)\\VideoLAN\\VLC");
		} else {
			System.out.println("OS = Linux");
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
					"/home/linux/vlc/install/lib");
		}
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

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
