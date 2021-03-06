package gui.player;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.AudioOutput;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.WindowsCanvas;
import util.files.UtilitiesFiles;

/**
 * Simple test harness creates an AWT Window and plays a video.
 * <p>
 * This is <strong>very</strong> basic but should give you an idea of how to
 * build a media player.
 * <p>
 * In case you didn't realize, you can press F12 to toggle the visibility of the
 * player controls.
 * <p>
 * Java7 provides -Dsun.java2d.xrender=True or -Dsun.java2d.xrender=true, might
 * give some general performance improvements in graphics rendering.
 */
public class SuperPlayer extends JFrame{

	private static final long serialVersionUID = -6281704860617633866L;

	private Canvas videoSurface;
	private final ControlPanel controlsPanel;

	private MediaPlayerFactory mediaPlayerFactory;

	public EmbeddedMediaPlayer mediaPlayer;

	private String directory;

	public SuperPlayer() {
		super("VLCJ Test Player");
		directory = new String();

		setUndecorated(true);
		if (RuntimeUtil.isWindows()) {
			// If running on Windows and you want the mouse/keyboard event
			// hack...
			videoSurface = new WindowsCanvas();
		} else {
			videoSurface = new Canvas();
		}

		Logger.debug("videoSurface={}", videoSurface);

		videoSurface.setBackground(Color.black);
		videoSurface.setSize(800, 600); // Only for initial layout
		
		this.setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);

		// Since we're mixing lightweight Swing components and heavyweight AWT
		// components this is probably a good idea
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		List<String> vlcArgs = new ArrayList<String>();

		vlcArgs.add("--no-plugins-cache");
		vlcArgs.add("--no-video-title-show");
		vlcArgs.add("--no-snapshot-preview");
		vlcArgs.add("--quiet");
		vlcArgs.add("--quiet-synchro");
		vlcArgs.add("--intf");
		vlcArgs.add("dummy");

		// Special case to help out users on Windows (supposedly this is not
		// actually needed)...
		// if(RuntimeUtil.isWindows()) {
		// vlcArgs.add("--plugin-path=" + WindowsRuntimeUtil.getVlcInstallDir()
		// + "\\plugins");
		// }
		// else {
		// vlcArgs.add("--plugin-path=/home/linux/vlc/lib");
		// }
		//
		// vlcArgs.add("--plugin-path=" + System.getProperty("user.home") +
		// "/.vlcj");
		//
		// Logger.debug("vlcArgs={}", vlcArgs);

		setIconImage(new ImageIcon(getClass().getResource(
				"/icons/vlcj-logo.png")).getImage());

		FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(
				this);

		mediaPlayerFactory = new MediaPlayerFactory(
				vlcArgs.toArray(new String[vlcArgs.size()]));
		mediaPlayerFactory.setUserAgent("vlcj test player");

		List<AudioOutput> audioOutputs = mediaPlayerFactory.getAudioOutputs();
		Logger.debug("audioOutputs={}", audioOutputs);

		mediaPlayer = mediaPlayerFactory
				.newEmbeddedMediaPlayer(fullScreenStrategy);
		mediaPlayer.setVideoSurface(mediaPlayerFactory
				.newVideoSurface(videoSurface));
		mediaPlayer.setPlaySubItems(true);

		mediaPlayer.setEnableKeyInputHandling(false);
		mediaPlayer.setEnableMouseInputHandling(false);

		controlsPanel = new ControlPanel(this);

		setLayout(new BorderLayout());
		setBackground(Color.black);
		add(videoSurface, BorderLayout.CENTER);
		add(controlsPanel, BorderLayout.SOUTH);
		pack();

		// Global AWT key handler, you're better off using Swing's InputMap and
		// ActionMap with a JFrame - that would solve all sorts of focus issues
		// too
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event instanceof KeyEvent) {
					KeyEvent keyEvent = (KeyEvent) event;
					if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {

						if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
							mediaPlayer.enableOverlay(false);
							mediaPlayerFactory.release();
							//mediaPlayer.toggleFullScreen();
							
							setUndecorated(true);
							
							setVisible(false);
							/**
							 * Arrête les scripts lorsque la fenêtre est fermée
							 */
							if (!System.getProperty("os.name").contains("Windows")) {

								try 
								{
									Runtime.getRuntime().exec("killScript.sh", null, null);
								} 
								catch (IOException e) 
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);

		mediaPlayer
				.addMediaPlayerEventListener(new SuperPlayerMediaPlayerEventListener());
	}

	public void changeVideoInPlay(String ehdirectory) {
		mediaPlayer.stop();
		String videoToPlay = UtilitiesFiles.getLastModifiedVideo(directory);
		if (videoToPlay != null) {
			mediaPlayer.enableOverlay(false);
			mediaPlayer.playMedia(videoToPlay);
			mediaPlayer.enableOverlay(true);
		} else {
			JOptionPane.showMessageDialog(this,
					"Il n'y a pas encore de vidéo disponible",
					"Vidéo indisponible", JOptionPane.ERROR_MESSAGE);
		}
	}

	public EmbeddedMediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public String getVideoInPlay() {
		return mediaPlayer.mrl();
	}

	public String getDirectory() {
		return directory;
	}

	public void changeDirectory(String directory) {
		mediaPlayer.toggleFullScreen();
		this.directory = directory;
		changeVideoInPlay(directory);
	}

	private final class SuperPlayerMediaPlayerEventListener extends
	MediaPlayerEventAdapter {
		@Override
		public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media,
				String mrl) {
			Logger.debug("mediaChanged(mediaPlayer={},media={},mrl={})",
					mediaPlayer, media, mrl);
		}

		@Override
		public void finished(MediaPlayer mediaPlayer) {
			Logger.debug("finished(mediaPlayer={})", mediaPlayer);
		}

		@Override
		public void paused(MediaPlayer mediaPlayer) {
			Logger.debug("paused(mediaPlayer={})", mediaPlayer);
		}

		@Override
		public void playing(MediaPlayer mediaPlayer) {
			Logger.debug("playing(mediaPlayer={})", mediaPlayer);
			MediaDetails mediaDetails = mediaPlayer.getMediaDetails();
			Logger.info("mediaDetails={}", mediaDetails);
		}

		@Override
		public void stopped(MediaPlayer mediaPlayer) {
			Logger.debug("stopped(mediaPlayer={})", mediaPlayer);
		}

		@Override
		public void error(MediaPlayer mediaPlayer) {
			Logger.debug("error(mediaPlayer={})", mediaPlayer);
		}

		@Override
		public void mediaSubItemAdded(MediaPlayer mediaPlayer,
				libvlc_media_t subItem) {
			Logger.debug("mediaSubItemAdded(mediaPlayer={},subItem={})",
					mediaPlayer, subItem);
		}

		@Override
		public void mediaDurationChanged(MediaPlayer mediaPlayer,
				long newDuration) {
			Logger.debug("mediaDurationChanged(mediaPlayer={},newDuration={})",
					mediaPlayer, newDuration);
		}

		@Override
		public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus) {
			Logger.debug("mediaParsedChanged(mediaPlayer={},newStatus={})",
					mediaPlayer, newStatus);
		}

		@Override
		public void mediaFreed(MediaPlayer mediaPlayer) {
			Logger.debug("mediaFreed(mediaPlayer={})", mediaPlayer);
		}

		@Override
		public void mediaStateChanged(MediaPlayer mediaPlayer, int newState) {
			Logger.debug("mediaStateChanged(mediaPlayer={},newState={})",
					mediaPlayer, newState);
		}

		@Override
		public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType) {
			Logger.debug("mediaMetaChanged(mediaPlayer={},metaType={})",
					mediaPlayer, metaType);
		}
	}

	/**
	 * 
	 * 
	 * @param enable
	 */
	@SuppressWarnings("unused")
	private void enableMousePointer(boolean enable) {
		Logger.debug("enableMousePointer(enable={})", enable);
		if (enable) {
			videoSurface.setCursor(null);
		} else {
			Image blankImage = new BufferedImage(1, 1,
					BufferedImage.TYPE_INT_ARGB);
			videoSurface.setCursor(Toolkit.getDefaultToolkit()
					.createCustomCursor(blankImage, new Point(0, 0), ""));
		}
	}
}
