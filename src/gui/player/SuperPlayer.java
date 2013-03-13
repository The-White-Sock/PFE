package gui.player;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.AudioOutput;
import uk.co.caprica.vlcj.player.MediaDetails;
import uk.co.caprica.vlcj.player.MediaMeta;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.WindowsCanvas;
import util.files.FilePicker;

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

	private EmbeddedMediaPlayer mediaPlayer;

	private FilePicker filePicker;

	private String videoInPlay;

	public SuperPlayer(String directory) {
		super("VLCJ Test Player");
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

		// Since we're mixing lightweight Swing components and heavyweight AWT
		// components this is probably a good idea
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		TestPlayerMouseListener mouseListener = new TestPlayerMouseListener();
		videoSurface.addMouseListener(mouseListener);
		videoSurface.addMouseMotionListener(mouseListener);

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

		filePicker = new FilePicker(directory);

		controlsPanel = new ControlPanel(this);

		setLayout(new BorderLayout());
		setBackground(Color.black);
		add(videoSurface, BorderLayout.CENTER);
		add(controlsPanel, BorderLayout.SOUTH);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				Logger.debug("windowClosing(evt={})", evt);

				if (videoSurface instanceof WindowsCanvas) {
					((WindowsCanvas) videoSurface).release();
				}

				if (mediaPlayer != null) {
					mediaPlayer.release();
					mediaPlayer = null;
				}

				if (mediaPlayerFactory != null) {
					mediaPlayerFactory.release();
					mediaPlayerFactory = null;
				}
				
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
		});

		// Global AWT key handler, you're better off using Swing's InputMap and
		// ActionMap with a JFrame - that would solve all sorts of focus issues
		// too
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event instanceof KeyEvent) {
					KeyEvent keyEvent = (KeyEvent) event;
					if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
						if (keyEvent.getKeyCode() == KeyEvent.VK_F12) {
							controlsPanel.setVisible(!controlsPanel.isVisible());
							getJMenuBar()
							.setVisible(!getJMenuBar().isVisible());
							invalidate();
							validate();
						}
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);

		setVisible(true);

		/**************************************************************/
		videoInPlay = filePicker.getLastModifiedVideo();
		if (videoInPlay != null) {
			mediaPlayer.enableOverlay(false);
			mediaPlayer.playMedia(videoInPlay);
			mediaPlayer.enableOverlay(true);
		} else {
			JOptionPane.showMessageDialog(this,
					"Il n'y a pas encore de vidéo disponible",
					"Vidéo indisponible", JOptionPane.ERROR_MESSAGE);
		}

		/**************************************************************/

		mediaPlayer
		.addMediaPlayerEventListener(new SuperPlayerMediaPlayerEventListener());
		
		mediaPlayer.toggleFullScreen();
	}

	public EmbeddedMediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public String getLastVid() {
		return filePicker.getLastModifiedVideo();
	}

	public String getVideoInPlay() {
		return videoInPlay;
	}

	public void setVideoInPlay(String vidPath) {
		videoInPlay = vidPath;
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
		public void videoOutput(MediaPlayer mediaPlayer, int newCount) {
			Logger.debug("videoOutput(mediaPlayer={},newCount={})",
					mediaPlayer, newCount);
			if (newCount == 0) {
				return;
			}

			MediaDetails mediaDetails = mediaPlayer.getMediaDetails();
			Logger.info("mediaDetails={}", mediaDetails);

			MediaMeta mediaMeta = mediaPlayer.getMediaMeta();
			Logger.info("mediaMeta={}", mediaMeta);

			final Dimension dimension = mediaPlayer.getVideoDimension();
			Logger.debug("dimension={}", dimension);
			if (dimension != null) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						videoSurface.setSize(dimension);
						pack();
					}
				});
			}

			// You can set a logo like this if you like...
			File logoFile = new File("./etc/vlcj-logo.png");
			if (logoFile.exists()) {
				mediaPlayer.setLogoFile(logoFile.getAbsolutePath());
				mediaPlayer.setLogoOpacity(0.5f);
				mediaPlayer.setLogoLocation(10, 10);
				mediaPlayer.enableLogo(true);
			}

			// Demo the marquee
			mediaPlayer.setMarqueeText("vlcj java bindings for vlc");
			mediaPlayer.setMarqueeSize(40);
			mediaPlayer.setMarqueeOpacity(95);
			mediaPlayer.setMarqueeColour(Color.white);
			mediaPlayer.setMarqueeTimeout(5000);
			mediaPlayer.setMarqueeLocation(50, 120);
			mediaPlayer.enableMarquee(true);

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

	/**
	 *
	 */
	private final class TestPlayerMouseListener extends MouseAdapter {
		@Override
		public void mouseMoved(MouseEvent e) {
			Logger.trace("mouseMoved(e={})", e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			Logger.debug("mousePressed(e={})", e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Logger.debug("mouseReleased(e={})", e);
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			Logger.debug("mouseWheelMoved(e={})", e);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			Logger.debug("mouseEntered(e={})", e);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			Logger.debug("mouseExited(e={})", e);
		}
	}


}
