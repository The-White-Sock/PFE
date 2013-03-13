package gui.player;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.filter.swing.SwingFileFilterFactory;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = 5804564268115781391L;

	private static final int SKIP_TIME_MS = 10 * 1000;

	private final ScheduledExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor();

	private final EmbeddedMediaPlayer mediaPlayer;
	private JLabel timeLabel;
	private JSlider positionSlider;

	private JButton rewindButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton playButton;
	private JButton fastForwardButton;

	private JFileChooser fileChooser;

	private boolean mousePressedPlaying = false;

	private SuperPlayer parent;

	public ControlPanel(SuperPlayer parent) {
		this.parent = parent;

		this.mediaPlayer = parent.getMediaPlayer();

		createUI();

		executorService.scheduleAtFixedRate(new UpdateRunnable(mediaPlayer),
				0L, 1L, TimeUnit.SECONDS);
	}

	private void createUI() {
		createControls();
		layoutControls();
		registerListeners();
	}

	private void createControls() {
		timeLabel = new JLabel("hh:mm:ss");

		positionSlider = new JSlider();
		positionSlider.setMinimum(0);
		positionSlider.setMaximum(1000);
		positionSlider.setValue(0);
		positionSlider.setToolTipText("Position");
		positionSlider.setEnabled(false);

		rewindButton = new JButton();
		rewindButton.setIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("icons/control_rewind_blue.png")));
		rewindButton.setToolTipText("Retour arri\u00E8re");

		stopButton = new JButton();
		stopButton.setIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("icons/control_stop_blue.png")));
		stopButton.setToolTipText("Stop");

		pauseButton = new JButton();
		pauseButton.setIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("icons/control_pause_blue.png")));
		pauseButton.setToolTipText("Lecture/pause");

		playButton = new JButton();
		playButton.setIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("icons/control_play_blue.png")));
		playButton.setToolTipText("Lecteur");

		fastForwardButton = new JButton();
		fastForwardButton.setIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("icons/control_fastforward_blue.png")));
		fastForwardButton.setToolTipText("Avance rapide");

		fileChooser = new JFileChooser();
		fileChooser.setApproveButtonText("Lire");
		fileChooser.setFileFilter(SwingFileFilterFactory.newVideoFileFilter());
	}

	private void layoutControls() {
		setBorder(new EmptyBorder(4, 4, 4, 4));

		setLayout(new BorderLayout());

		JPanel positionPanel = new JPanel();
		positionPanel.setLayout(new GridLayout(1, 1));
		positionPanel.add(positionSlider);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(8, 0));

		topPanel.add(timeLabel, BorderLayout.WEST);
		topPanel.add(positionPanel, BorderLayout.CENTER);

		add(topPanel, BorderLayout.NORTH);

		JPanel bottomPanel = new JPanel();

		bottomPanel.setLayout(new FlowLayout());

		bottomPanel.add(rewindButton);
		bottomPanel.add(stopButton);
		bottomPanel.add(pauseButton);
		bottomPanel.add(playButton);
		bottomPanel.add(fastForwardButton);

		add(bottomPanel, BorderLayout.SOUTH);
	}

	private void updateUIState() {
		if (!mediaPlayer.isPlaying()) {
			// Resume play or play a few frames then pause to show current
			// position in video
			mediaPlayer.play();
			if (!mousePressedPlaying) {
				try {
					// Half a second probably gets an iframe
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// Don't care if unblocked early
				}
				mediaPlayer.pause();
			}
		}
		long time = mediaPlayer.getTime();
		int position = (int) (mediaPlayer.getPosition() * 1000.0f);
		updateTime(time);
		updatePosition(position);
	}

	private void skip(int skipTime) {
		// Only skip time if can handle time setting
		if (mediaPlayer.getLength() > 0) {
			mediaPlayer.skip(skipTime);
			updateUIState();
		}
	}

	private void registerListeners() {

		rewindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skip(-SKIP_TIME_MS);
			}
		});

		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.stop();
				positionSlider.setValue(0);
				timeLabel.setText("hh:mm:ss");
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.pause();
			}
		});

		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.changeVideoInPlay(parent.getDirectory());
			}
		});

		fastForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skip(SKIP_TIME_MS);
			}
		});
	}

	private final class UpdateRunnable implements Runnable {

		private final MediaPlayer mediaPlayer;

		private UpdateRunnable(MediaPlayer mediaPlayer) {
			this.mediaPlayer = mediaPlayer;
		}

		@Override
		public void run() {
			final long time = mediaPlayer.getTime();
			final int position = (int) (mediaPlayer.getPosition() * 1000.0f);

			// Updates to user interface components must be executed on the
			// Event
			// Dispatch Thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (mediaPlayer.isPlaying()) {
						updateTime(time);
						updatePosition(position);
					}
				}
			});
		}
	}

	private void updateTime(long millis) {
		String s = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
		timeLabel.setText(s);
	}

	private void updatePosition(int value) {
		positionSlider.setValue(value);
	}
}
