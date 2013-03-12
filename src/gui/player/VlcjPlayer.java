package gui.player;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class VlcjPlayer extends JFrame {
	
	private static final long serialVersionUID = 5346384001716602634L;
	
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private JFileChooser chooser;
	private String vidPath;
	
	public VlcjPlayer() {
		super("Video Vlcj Player");
		
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		
		setContentPane(mediaPlayerComponent);
		setLocation(100, 100);
		setSize(1050, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		chooser = new JFileChooser(".");
		chooser.showDialog(this, "Open");
		
		vidPath = chooser.getSelectedFile().getAbsolutePath();
		
		mediaPlayerComponent.getMediaPlayer().playMedia(vidPath);
	}
}