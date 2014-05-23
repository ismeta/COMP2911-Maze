package maze.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import maze.GUI;
import maze.effect.MazeEffect;
import maze.generator.maze.MazeGenerator;
import maze.player.MazePlayer;
import maze.player.MazePlayerPanel;

public class MazeBasePanel extends JPanel {
	public MazeBasePanel(GUI frameGui) {

		super(true);
		this.setFocusable(true);

		this.timer = null;
		this.mazePlayers = null;
		this.activatedEffects = null;
		this.keyPresses = null;
		this.gameState = MazeGameState.UNSETUP;

		/* GUI */
		/* - Layout */
		this.setLayout(new GridBagLayout());

		/* Maze dimensions */
		Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenWidth = (int) resolution.getWidth();
		this.mazeGamePanelWidth = screenWidth / 2 - 100;
		this.mazeGamePanel = new MazeGamePanel(screenWidth / 2 - 100,
				screenWidth / 2 - 100);

		/* Set up */
		this.isMusicOn = true;
		try {
			this.clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.frameGui = frameGui;

		this.setupGui();
	}

	/**
	 * Set up the base panel gui.
	 */
	private void setupGui() {
		/* Background */
		Image img = new ImageIcon(GUI_BACKGROUND_IMAGE_FILE).getImage();
		this.setBackground(img);

		/* Maze Game Panel */
		g = new GridBagConstraints();
		g.anchor = GridBagConstraints.NORTHEAST;
		g.gridwidth = 1;
		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 0.1;
		this.add(this.mazeGamePanel, g);

		/* Prompt line */
		JLabel headerText = new JLabel("Get to the M1 before the other cars!");
		headerText.setFont(new Font("verdana", Font.PLAIN, 40));
		g.anchor = GridBagConstraints.NORTH;
		g.gridx = 0;
		g.gridy = 0;
		g.insets = new Insets(20, 0, 0, 0);
		this.add(headerText, g);

		/* TopButtons - contains buttons */
		GridLayout buttonsGridLayout = new GridLayout(1, 5);
		JPanel topButtons = new JPanel();
		topButtons.setLayout(buttonsGridLayout);
		topButtons.setPreferredSize(new Dimension(this.screenWidth / 2, 50));
		topButtons.setOpaque(false);
		g.gridx = 1;
		g.gridy = 0;
		g.weighty = 0.1;
		g.insets = new Insets(20, 0, 0, 0);
		this.add(topButtons, g);

		/* - Pause */
		ImageIcon ico = new ImageIcon(MAZE_PAUSE_IMAGE_FILE);
		pause = new JButton(ico);
		pause.setBorderPainted(false);
		pause.setContentAreaFilled(false);

		/* - Sound */
		ico = new ImageIcon(MAZE_SOUND_IMAGE_FILE);
		sound = new JButton(ico);
		sound.setBorderPainted(false);
		sound.setContentAreaFilled(false);

		/* - Back */
		ico = new ImageIcon(MAZE_BACK_IMAGE_FILE);
		JButton back = new JButton(ico);
		back.setBorderPainted(false);
		back.setContentAreaFilled(false);

		/* - Help */
		ico = new ImageIcon(MAZE_HELP_IMAGE_FILE);
		JButton help = new JButton(ico);
		help.setBorderPainted(false);
		help.setContentAreaFilled(false);

		/* - Exit */
		ico = new ImageIcon(MAZE_EXIT_IMAGE_FILE);
		JButton exit = new JButton(ico);
		exit.setBorderPainted(false);
		exit.setContentAreaFilled(false);

		topButtons.add(pause);
		topButtons.add(sound);
		topButtons.add(back);
		topButtons.add(help);
		topButtons.add(exit);

		/* Action Listeners for buttons */
		pause.setFocusable(false);
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gameState.equals(MazeGameState.PLAYING)) {
					/* Pause game */
					pause();

					/* Change image to play */
					pause.setIcon(new ImageIcon(MAZE_PLAY_IMAGE_FILE));

					/* Turn music off */
					stopMusic();
				} else {
					/* Play game */
					unpause();

					/* Change image to pause */
					pause.setIcon(new ImageIcon(MAZE_PAUSE_IMAGE_FILE));

					/* Turn music on */
					playMusic();

				}
			}
		});

		sound.setFocusable(false);
		sound.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isMusicOn == true) {
					/* Mute music */
					isMusicOn = false;
					stopMusic();

					/* Change image to mute */
					sound.setIcon(new ImageIcon(MAZE_MUTE_IMAGE_FILE));
				} else {
					/* Play music */
					isMusicOn = true;
					playMusic();

					/* Change image to play */
					sound.setIcon(new ImageIcon(MAZE_SOUND_IMAGE_FILE));
				}
			}
		});

		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* Go back to play options page */
				stopMusic();
				frameGui.displayPlayOptionsPage();
			}
		});

		help.setFocusable(false);
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (gameState.equals(MazeGameState.PLAYING)) {
					/* Change image to play */
					pause.setIcon(new ImageIcon(MAZE_PLAY_IMAGE_FILE));
				}

				/* Pause the game */
				pause();

				/* Stop the music */
				stopMusic();

				/* Display help window */
				frameGui.displayHelpWindow();
			}
		});

		exit.setFocusable(false);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				/* Exit application properly */
				exit();
			}
		});
	}

	/***
	 * Pump da music! 
	 */
	public void playMusic() {
		try {
			File soundFile = new File(GAME_MUSIC_FILE);
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);

			AudioInputStream soundIn = AudioSystem
					.getAudioInputStream(soundFile);
			AudioFormat format = soundIn.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);

			clip = (Clip) AudioSystem.getLine(info);
			clip.open(ais);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stop the music.
	 */
	public void stopMusic() {
		try {
			clip.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set the background.
	 * 
	 * @param background
	 *            The background to set.
	 */
	public void setBackground(Image background) {
		image = background;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
	}

	/**
	 * @return the mazePlayers
	 */
	public MazePlayer[] getMazePlayers() {
		return mazePlayers;
	}

	/**
	 * @return the activatedEffects
	 */
	public PriorityQueue<MazeEffect> getActivatedEffects() {
		return activatedEffects;
	}

	/**
	 * @return the keyPresses
	 */
	public ConcurrentHashMap<Character, Long> getKeyPresses() {
		return keyPresses;
	}

	public void setup(int size, int numPlayers, MazeGenerator mazeGenerator,
			GUI gui) {
		/*
		 * Play music throughout application unless turned off in system
		 * options.
		 */
		this.playMusic();
		Color purple = new Color(174, 79, 255);
		Color blue = new Color(0, 156, 255);

		LinkedList<Color> colors = new LinkedList<Color>();
		colors.add(Color.RED);
		colors.add(purple);
		colors.add(blue);

		/* Gui */
		JPanel playerStatus = new JPanel();
		playerStatus.setOpaque(false);
		playerStatus.setLayout(new GridBagLayout());

		/* make the players */
		this.mazePlayers = new MazePlayer[numPlayers];

		/* padding is the space between the player panels */
		int padding = 140 / numPlayers;

		/* draw player panels for each player */
		for (int i = 0; i < numPlayers; i++) {
			MazePlayerPanel s = new MazePlayerPanel(colors.get(i), i);
			this.mazePlayers[i] = new MazePlayer(i, s);

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = i;

			gbc.insets = new Insets(0, 0, padding, 0);
			playerStatus.add(s, gbc);
		}

		g.anchor = GridBagConstraints.CENTER;
		g.gridx = 1;
		g.gridy = 1;
		this.add(playerStatus, g);

		/* now we can #mazeit */
		this.mazeGamePanel.setup(size, mazeGenerator, this.mazePlayers);

		/* maze keypresses and listener */
		this.keyPresses = new ConcurrentHashMap<Character, Long>();
		this.addKeyListener(new MazeBasePanelKeyListener(this));
		Component parent = this.getParent();
		while (parent != null) {
			parent.addKeyListener(new MazeBasePanelKeyListener(this));
			parent = parent.getParent();
		}

		/* create the effect priority queue */
		this.activatedEffects = new PriorityQueue<MazeEffect>(10,
				new Comparator<MazeEffect>() {
					@Override
					public int compare(MazeEffect a, MazeEffect b) {
						return (int) (b.getEndTime() - a.getEndTime());
					}
				});

		/* allocate timer and start when ready - MUST BE LAST */
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new MazeBasePanelTimer(this), 0,
				1000 / REFRESH_RATE);

		this.gameState = MazeGameState.PLAYING;
	}

	/**
	 * Called when the game is paused.
	 */
	public void pause() {
		/* change state and last paused stuff */
		this.gameState = MazeGameState.PAUSED;
		this.lastPauseTime = System.currentTimeMillis();

		/* clear the keys pressed */
		this.keyPresses.clear();
	}

	/**
	 * Called when the game is paused.
	 */
	public void unpause() {
		/* Make sure effect countdowns aren't affected */
		long pauseDuration = System.currentTimeMillis() - this.lastPauseTime;
		for (MazeEffect me : this.activatedEffects) {
			me.addEndTime(pauseDuration);
		}
		this.gameState = MazeGameState.PLAYING;
	}

	/**
	 * Called when the game is over - game clean up.
	 */
	public void exit() {
		this.timer.purge();
		this.timer = null;

		this.mazePlayers = null;

		/* clear everything */
		this.activatedEffects.clear();
		this.activatedEffects = null;

		/* remove all the keylisteners */
		for (KeyListener kl : this.getKeyListeners()) {
			this.removeKeyListener(kl);
			Component parent = this.getParent();
			while (parent != null) {
				parent.removeKeyListener(kl);
				parent = parent.getParent();
			}
		}
		this.keyPresses.clear();
		this.keyPresses = null;
		this.gameState = MazeGameState.UNSETUP;

		System.exit(0);
	}

	public void checkGameOver() {
		/* all players assigned rank */
		boolean isFinished = true;

		/*
		 * check if the maze players aren't null - can happen because of race
		 * conditions when the game ends
		 */
		if (this.mazePlayers != null) {
			for (MazePlayer mp : this.mazePlayers) {
				isFinished &= mp.isFinished();
				// Check if player has finished
				if (mp.isFinished()) {
					// Display finished on player panel
					mp.getPlayerPanel().displayFinished();
				}
			}
		}
		if (isFinished) {
			this.gameState = MazeGameState.FINISHED;
		}
	}

	/**
	 * @return the mazeGamePanel
	 */
	public MazeGamePanel getMazeGamePanel() {
		return mazeGamePanel;
	}

	/**
	 * @return the gameState
	 */
	public MazeGameState getGameState() {
		return gameState;
	}

	/**
	 * MazeJPanelTimer allows player listeners and repainting
	 */
	private class MazeBasePanelTimer extends TimerTask {
		private MazeBasePanel mbp;

		private MazeBasePanelTimer(MazeBasePanel mbp) {
			this.mbp = mbp;
		}

		@Override
		public void run() {
			checkGameOver();
			if (mbp.getGameState().equals(MazeGameState.FINISHED)) {
				this.cancel();

				/* Display score board */
				displayScorePanel();

				/* Repaint since panel in mbp has been changed */
				this.mbp.validate();
			} else if (mbp.getGameState().equals(MazeGameState.PLAYING)) {
				/* update all the players */
				long curTime = System.currentTimeMillis();
				for (Entry<Character, Long> e : this.mbp.getKeyPresses()
						.entrySet()) {
					long difference = curTime - e.getValue();
					this.mbp.getMazeGamePanel().updatePlayerMovement(
							e.getKey(), difference);
					this.mbp.getKeyPresses().put(e.getKey(), curTime);
				}

				/* remove unnecessary boosts */
				PriorityQueue<MazeEffect> pq = mbp.getActivatedEffects();
				while (!pq.isEmpty()) {
					if (pq.peek().getEndTime() <= curTime) {
						pq.poll().deactivate(mbp.getMazeGamePanel());
					} else {
						break;
					}
				}
				/* repaint the maze since there are updates */
				this.mbp.repaint();
			}
		}

		/**
		 * Display score panel - when the game is over
		 */
		public void displayScorePanel() {
			/* Remove mazeGamePanel */
			mazeGamePanel.setVisible(false);
			this.mbp.remove(mazeGamePanel);

			/* Replace with score panel */
			MazeScorePanel score = new MazeScorePanel(mazeGamePanelWidth,
					frameGui);
			score.setup(mazeGamePanel.getMazePlayers());
			score.setVisible(true);
			GridBagConstraints x = new GridBagConstraints();
			x.anchor = GridBagConstraints.NORTHEAST;
			x.gridwidth = 1;
			x.gridx = 0;
			x.gridy = 1;
			x.weightx = 0.1;
			this.mbp.add(score, x);
		}
	}

	/**
	 * Key Listener for the Maze JPanel
	 */
	private class MazeBasePanelKeyListener implements KeyListener {
		private MazeBasePanel mbp;

		private MazeBasePanelKeyListener(MazeBasePanel mbp) {
			this.mbp = mbp;
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			char c = Character.toLowerCase(e.getKeyChar());
			if (mbp.getGameState().equals(MazeGameState.PLAYING)) {
				if (c >= 'a' && c <= 'z') {
					/* activate the next maze effect */
					MazePlayer[] mazePlayers = mbp.getMazePlayers();
					for (int i = 0; i < mazePlayers.length; i++) {
						if (MAZE_EFFECT_ACTIVATE_KEYS[i] == c
								&& !mazePlayers[i].isFinished()) {
							mazePlayers[i].activateNextMazeEffect(mbp);
						}
					}
					/* make sure we register the player's key as pressed */
					mbp.getKeyPresses().put(c, System.currentTimeMillis());
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			char c = Character.toLowerCase(e.getKeyChar());
			if (mbp.getGameState().equals(MazeGameState.PLAYING)) {
				if (c >= 'a' && c <= 'z') {
					/* in case remove doesn't exist */
					Long releasedTime = this.mbp.getKeyPresses().remove(c);
					if (releasedTime != null) {
						/* update the player's movement */
						long difference = System.currentTimeMillis()
								- releasedTime;
						this.mbp.getMazeGamePanel().updatePlayerMovement(c,
								difference);
					}
				}
			}
		}
	}

	/* how often the maze should be refreshed every second */
	public static int REFRESH_RATE = 60;

	/* the keys which activate maze effects for different players */
	public static final char[] MAZE_EFFECT_ACTIVATE_KEYS = { 'e', 'y', 'o' };

	private static final long serialVersionUID = 7399404361523168614L;

	/* GUI background image */
	private static final String GUI_BACKGROUND_IMAGE_FILE = "images/gui/nature.png";

	/* music to be played during the game */
	private static final String GAME_MUSIC_FILE = "music/moo.wav";

	/* Image to be displayed for the play button */
	private static final String MAZE_PLAY_IMAGE_FILE = "images/gui/maze_play.png";

	/* Image to be displayed for the pause button */
	private static final String MAZE_PAUSE_IMAGE_FILE = "images/gui/maze_pause.png";

	/* sound image file */
	private static final String MAZE_SOUND_IMAGE_FILE = "images/gui/maze_sound.png";

	/* Mute button image */
	private static final String MAZE_MUTE_IMAGE_FILE = "images/gui/maze_mute.png";

	/* image to display when the back button is clicked */
	private static final String MAZE_BACK_IMAGE_FILE = "images/gui/maze_back.png";

	/* image to display for the help button */
	private static final String MAZE_HELP_IMAGE_FILE = "images/gui/maze_help.png";

	/* image to display for the exit button */
	private static final String MAZE_EXIT_IMAGE_FILE = "images/gui/maze_exit.png";

	private Timer timer;
	private MazePlayer mazePlayers[];
	private PriorityQueue<MazeEffect> activatedEffects;
	private ConcurrentHashMap<Character, Long> keyPresses;

	private MazeGamePanel mazeGamePanel;

	private Image image;
	private GridBagConstraints g;
	private long lastPauseTime;

	/* the current state of the maze */
	private MazeGameState gameState;

	/* width dimensions */
	private int screenWidth;
	private int mazeGamePanelWidth;

	/* toggles for music */
	private boolean isMusicOn;

	/* buttons and audio stream */
	private JButton pause;
	private JButton sound;
	Clip clip;

	private GUI frameGui;
}
