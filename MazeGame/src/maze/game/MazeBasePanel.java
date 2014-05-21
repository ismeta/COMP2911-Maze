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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

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
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


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
		this.screenHeight = (int) resolution.getHeight();
		this.mazeGamePanel = new MazeGamePanel(screenWidth/2 - 100, screenWidth/2 - 100);
		/* Set up */
		this.isMusicOn = true;
		this.isPlaying = true;
		this.as = null;
		this.setupGui();
	}
	
	/**
	 * @author davina
	 */
	private void setupGui() {
		/* Background */
		Image img = new ImageIcon("images/gui/nature.png").getImage();
		this.setBackground(img);
		
		/* Maze Game Panel */
		g = new GridBagConstraints();
		g.anchor = GridBagConstraints.NORTHEAST;
		g.gridwidth = 1;
		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 0.1;
		this.add(this.mazeGamePanel, g);
		
		/* Prompt line  */
		JLabel headerText = new JLabel("Get to the M1 before the other cars!");
		headerText.setFont(new Font("verdana", Font.PLAIN, 40));
		g.anchor = GridBagConstraints.NORTH;
		g.gridx = 0;
		g.gridy = 0;
		g.insets = new Insets(20, 0, 0, 0);
		this.add(headerText, g);
		
		/* TopButtons - contains buttons*/
		GridLayout buttonsGridLayout = new GridLayout(1, 5);
		JPanel topButtons = new JPanel();
		topButtons.setLayout(buttonsGridLayout);
		topButtons.setPreferredSize(new Dimension(this.screenWidth/2, 50));
		topButtons.setOpaque(false);
		g.gridx = 1;
		g.gridy = 0;
		g.weighty = 0.1;
		g.insets = new Insets(20,0,0,0);
		this.add(topButtons, g);
		
		/* - Pause */
		ImageIcon ico = new ImageIcon("images/gui/maze_pause.png");
		pause = new JButton(ico);
		pause.setBorderPainted(false);
		pause.setContentAreaFilled(false);
		/* - Sound */
		ico = new ImageIcon("images/gui/maze_sound.png");
		sound = new JButton(ico);
		sound.setBorderPainted(false);
		sound.setContentAreaFilled(false);
		/* - Back */
		ico = new ImageIcon("images/gui/maze_back.png");
		JButton back = new JButton(ico);
		back.setBorderPainted(false);
		back.setContentAreaFilled(false);
		/* - Help */
		ico = new ImageIcon("images/gui/maze_help.png");
		JButton help = new JButton(ico);
		help.setBorderPainted(false);
		help.setContentAreaFilled(false);
		/* - Exit */
		ico = new ImageIcon("images/gui/maze_exit.png");
		JButton exit = new JButton(ico);
		exit.setBorderPainted(false);
		exit.setContentAreaFilled(false);
		
		topButtons.add(pause);
		topButtons.add(sound);
		//topButtons.add(back);
		topButtons.add(help);
		topButtons.add(exit);
		
		/* Action Listeners for buttons */
		pause.setFocusable(false);
		pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	if (isPlaying) {
            		// Pause game
            		isPlaying = false;
            		pause();
            		// Change image to play
            		pause.setIcon(new ImageIcon("images/gui/maze_play.png"));
            		// Turn music off
            		stopMusic();
            	} else {
            		// Play game
            		isPlaying = true;
            		unpause();
            		// Change image to pause
            		pause.setIcon(new ImageIcon("images/gui/maze_pause.png"));
            		// Turn music on
            		playMusic();
            		
            	}
            }
        });
		
		sound.setFocusable(false);
		sound.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	if (isMusicOn == true) {
            		// Mute music
            		isMusicOn = false;
            		stopMusic();
            		// Change image to mute
            		sound.setIcon(new ImageIcon("images/gui/maze_mute.png"));
            	} else {
            		// Play music
            		isMusicOn = true;
            		playMusic();
            		// Change image to play
            		sound.setIcon(new ImageIcon("images/gui/maze_sound.png"));
            	}
            }
        });
		/*
		back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	// Go back to play options page
            		CardLayout cl = (CardLayout) (frameGui.getPages().getLayout());
            		cl.show(frameGui.getPages(), "play");
            }
        });
		*/
		
		help.setFocusable(false);
		help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	// Pause the game
            	pause();
            	if (isPlaying) {
            		isPlaying = false;
            		// Change image to play
            		pause.setIcon(new ImageIcon("images/gui/maze_play.png"));
            	}
            	// Stop the music
            	stopMusic();
            	// Display help window
            	JFrame helpFrame = new JFrame("Help");
            	helpFrame.setFocusable(false);
        		helpFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        		helpFrame.setMinimumSize(new Dimension(500, 500));
        		helpFrame.setVisible(true);
            }
        });
		
		exit.setFocusable(false);
		exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	// Exit application properly
            	exit();
            }
        });
	}
	
	
	/***
	 * Pump da music!
	 * TODO: Continuous and mute
	 * maybe different music between MazeGameState = UNSETUP and PLAY,PAUSED and SUCCESS
	 * @param filename
	 */
	public void playMusic() {
		String filename = "music/Pamgaea.wav";
		InputStream in = null;
		//AudioStream as = null;
		try {
			//create audio data source
			in = new FileInputStream(new File(filename));
		} catch(FileNotFoundException fnfe) {
			System.err.println("The audio file was not found");
		}
		
		try {
			//create audio stream from file stream
			as = new AudioStream(in);
		} catch(IOException ie) {
			System.err.println("Audio stream could not be created");
		}
		AudioPlayer.player.start(as);
	}
	
	public void stopMusic() {
		AudioPlayer.player.stop(as);
	}
	
    public void setBackground(Image background) {
        image = background;
    }

    @Override
    public void paintComponent(Graphics G) {
        super.paintComponent(G);
        G.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
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

	public void setup(int size, int numPlayers, MazeGenerator mazeGenerator, GUI gui) {
		/* Play music throughout application unless turned off in system options. */
		this.playMusic();
		Color purple = new Color(174, 79, 255);
		Color blue   = new Color(0, 156, 255);
		
		LinkedList<Color> colors = new LinkedList<Color>();
		colors.add(Color.RED);
		colors.add(purple);
		colors.add(blue);
		
		// Gui
		JPanel playerStatus = new JPanel();
		playerStatus.setOpaque(false);
		playerStatus.setLayout(new GridBagLayout());
		
		// make da players
		this.mazePlayers = new MazePlayer[numPlayers];
		
		int padding = 140 / numPlayers;
		for (int i = 0; i < numPlayers; i++) {
			MazePlayerPanel s = new MazePlayerPanel(colors.get(i), i);
			this.mazePlayers[i] = new MazePlayer(i, s);
			// temporary
			//MazePlayerPanel s = new MazePlayerPanel(this.mazePlayers[i]);
			// Gui
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = i;
			//gbc.weighty = 0.5;
			gbc.insets = new Insets(0,0,padding,0);
			playerStatus.add(s, gbc);
		}
		g.anchor = GridBagConstraints.CENTER;
		g.gridx = 1;
		g.gridy = 1;
		this.add(playerStatus, g);		
		
		// now we can #mazeit
		this.mazeGamePanel.setup(size, mazeGenerator, this.mazePlayers);
		
		// maze keypresses and listener
		this.keyPresses = new ConcurrentHashMap<Character, Long>();
		this.addKeyListener(new MazeBasePanelKeyListener(this));
		Component parent = this.getParent();
		while (parent != null) {
			parent.addKeyListener(new MazeBasePanelKeyListener(this));
			parent = parent.getParent();
		}
		
		// create the effects PQ
		this.activatedEffects = new PriorityQueue<MazeEffect>(10, new Comparator<MazeEffect>() {
			@Override
			public int compare(MazeEffect a, MazeEffect b) {
				return (int) (b.getEndTime() - a.getEndTime());
			}
		});
		
		// allocate timer and start when ready - MUST BE LAST
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new MazeBasePanelTimer(this), 0, 1000 / REFRESH_RATE);
		
		this.gameState = MazeGameState.PLAYING;
	}
	
	public void pause() {
		// change state and last paused stuff
		this.gameState = MazeGameState.PAUSED;
		this.lastPauseTime = System.currentTimeMillis();
		// clear the keys pressed
		this.keyPresses.clear();
	}
	
	public void unpause() {
		long pauseDuration = System.currentTimeMillis() - this.lastPauseTime;
		for (MazeEffect me : this.activatedEffects) {
			me.addEndTime(pauseDuration);
		}
		this.gameState = MazeGameState.PLAYING;
	}
	
	public void exit() {
		this.timer.purge();
		this.timer = null;
		
		this.mazePlayers = null;
		
		this.activatedEffects.clear();
		this.activatedEffects = null;
		
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
		// all players assigned rank
		boolean isFinished = true;
		for (MazePlayer mp : this.mazePlayers) {
			isFinished &= mp.isFinished();
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
	 * @author oliver
	 * MazeJPanelTimer allows player listeners and repainting
	 */
	private class MazeBasePanelTimer extends TimerTask {
		private MazeBasePanel mbp;
		
		private MazeBasePanelTimer(MazeBasePanel mbp) {
			this.mbp = mbp;
		}
		
		@Override
		public void run() {
			if (mbp.getGameState().equals(MazeGameState.PLAYING)) {
				// update all the player
				long curTime = System.currentTimeMillis();
				for (Entry<Character, Long> e : this.mbp.getKeyPresses().entrySet()) {
					long difference = curTime - e.getValue();
		    		this.mbp.getMazeGamePanel().updatePlayerMovement(e.getKey(), difference);
					this.mbp.getKeyPresses().put(e.getKey(), curTime);
				}
				// remove unnecessary boosts
				PriorityQueue<MazeEffect> pq = mbp.getActivatedEffects();
				while (!pq.isEmpty()) {
					if (pq.peek().getEndTime() <= curTime) {
						pq.poll().deactivate(mbp.getMazeGamePanel());
					} else {
						break;
					}
				}
				// repaint the maze since there are updates
				this.mbp.repaint();
			}
		}
	}

	/**
	 * @author oliver
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
		    		// activate the next maze effect
		    		MazePlayer[] mazePlayers = mbp.getMazePlayers();
		    		for (int i = 0; i < mazePlayers.length; i++) {
		    			if (MAZE_EFFECT_ACTIVATE_KEYS[i] == c && !mazePlayers[i].isFinished()) {
		    				mazePlayers[i].activateNextMazeEffect(mbp);
		    			}
		    		}
		    		// make sure we register the player's key as pressed
		    		mbp.getKeyPresses().put(c, System.currentTimeMillis());
		    	}
	    	}
	    }

	    public void keyReleased(KeyEvent e) {
	    	char c = Character.toLowerCase(e.getKeyChar());
	    	if (mbp.getGameState().equals(MazeGameState.PLAYING)) {
		    	if (c >= 'a' && c <= 'z') {
		    		// in case remove doesn't exist
		    		Long releasedTime = this.mbp.getKeyPresses().remove(c);
		    		if (releasedTime != null) {
			    		// update the player's movement
			    		long difference = System.currentTimeMillis() - releasedTime;
			    		this.mbp.getMazeGamePanel().updatePlayerMovement(c, difference);
		    		}
		    	}
	    	}
	    }
	}
	
	private static final long serialVersionUID = 7399404361523168614L;
	
	/* how often the maze should be refreshed every second */
	public static int REFRESH_RATE = 60;
	
	/* the keys which activate maze effects for different players */
	public static final char[] MAZE_EFFECT_ACTIVATE_KEYS = { 'e', 'y', 'o' };
	
	private Timer timer;
	private MazePlayer mazePlayers[];
	private PriorityQueue<MazeEffect> activatedEffects;
	private ConcurrentHashMap<Character, Long> keyPresses;
	
	private MazeGamePanel mazeGamePanel;
	
	private Image image;
	private GUI frameGui;
	private GridBagConstraints g;
	private long lastPauseTime;
	
	/* the current state of the maze */
	private MazeGameState gameState;
	
	private int screenWidth;
	private int screenHeight;
	
	private boolean isMusicOn;
	private boolean isPlaying;
	
	private JButton pause;
	private JButton sound;
	private AudioStream as;
}
