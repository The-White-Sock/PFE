package vlcj.test;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class VlcPlayer {

	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private final JFileChooser chooser;
	private final String vidPath;
	
	public static void main(String[] args) {
		boolean onWindows = System.getProperty("os.name").contains("Windows");
		
		if(onWindows) {
			System.out.println("OS = Windows");
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		}
		else {
			System.out.println("OS = Linux");
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/home/linux/vlc/install/lib");
		}
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
		
		SwingUtilities.invokeLater(
			new Runnable() {
				@Override
				public void run() {
					new VlcPlayer();
				}
			}
		);
	}
	
	private VlcPlayer() {
		JFrame frame = new JFrame("vlcj Test");
		
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		
		frame.setContentPane(mediaPlayerComponent);
		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		chooser = new JFileChooser(".");
		chooser.showDialog(frame, "Open");
		
		vidPath = chooser.getSelectedFile().getAbsolutePath();
		
		System.out.println(vidPath);
		
		mediaPlayerComponent.getMediaPlayer().playMedia(vidPath);
	}
}