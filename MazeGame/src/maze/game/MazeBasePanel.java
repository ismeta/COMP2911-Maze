package maze.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import maze.GUI;
import maze.effect.MazeEffect;
import maze.generator.MazeGenerator;

public class MazeBasePanel extends JPanel {
	private static final long serialVersionUID = 7399404361523168614L;
	public static int REFRESH_RATE = 60;
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
	
	// maze state
	private MazeGameState gameState;
	
	public MazeBasePanel(GUI frameGui) {
		super(true);
		this.setFocusable(true);
		
		this.setLayout(new GridBagLayout());
		this.timer = null;
		this.mazePlayers = null;
		this.activatedEffects = null;
		this.keyPresses = null;
		this.mazeGamePanel = new MazeGamePanel(800, 800);
		this.image = null;
		this.frameGui = frameGui;
		
		this.gameState = MazeGameState.UNSETUP;
		this.setupGui();
	}
	
	/**
	 * @author davina
	 */
	private void setupGui() {
		// Header
		JPanel header = new JPanel();
		header.setPreferredSize(new Dimension(700, 65));
		header.setOpaque(false);
		
		g = new GridBagConstraints();
		g.gridx = 1;
		g.gridy = 0;
		g.weighty = 1;
		this.add(header, g);
		
		// Buttons
		JButton pause = new JButton(new ImageIcon("images/player/pauseb.png"));
		pause.setBorderPainted(false);
		pause.setContentAreaFilled(false);
		
		JButton help = new JButton(new ImageIcon("images/player/help.png"));
		help.setBorderPainted(false);
		help.setContentAreaFilled(false);
		
		JButton exit = new JButton(new ImageIcon("images/player/exit.png"));
		exit.setBorderPainted(false);
		exit.setContentAreaFilled(false);
		
		header.add(pause);
		header.add(help);
		header.add(exit);
		
		// Action Listeners for buttons
		exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	frameGui.dispose();
            }
        });
		
		help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	frameGui.displayHelpWindow();
            }
        });
		
		// Game panel
		g.anchor = GridBagConstraints.NORTH;
		g.gridwidth = 1;
		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 0.1;
		this.add(this.mazeGamePanel, g);
		
		// Background
		Image img = new ImageIcon("images/gui/nature.png").getImage();
		this.setBackground(img);
	}
	
    public void setBackground(Image image) {
        this.image = image;
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
		
		LinkedList<Color> colors = new LinkedList<Color>();
		colors.add(Color.RED);
		colors.add(new Color(174, 79, 255));
		colors.add(new Color(0, 156, 255));
		// Gui
		JPanel playerStatus = new JPanel();
		playerStatus.setOpaque(false);
		playerStatus.setLayout(new GridBagLayout());
		
		// make da players
		this.mazePlayers = new MazePlayer[numPlayers];
		
		int padding = 200 / numPlayers;
		for (int i = 0; i < numPlayers; i++) {
			MazePlayerPanel s = new MazePlayerPanel(colors.get(i), i);
			this.mazePlayers[i] = new MazePlayer(i, s);
			// temporary
			//MazePlayerPanel s = new MazePlayerPanel(this.mazePlayers[i]);
			// Gui
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.weighty = 0.5;
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
		this.gameState = MazeGameState.PAUSED;
		this.lastPauseTime = System.currentTimeMillis();
	}
	
	public void unpause() {
		long pauseDuration = System.currentTimeMillis() - this.lastPauseTime;
		for (MazeEffect me : this.activatedEffects) {
			me.addEndTime(pauseDuration)
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
		
		this.frameGui.dispose();
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
	    	if (mbp.getGameState().equals(MazeGameState.PLAYING)) {
		    	char c = Character.toLowerCase(e.getKeyChar());
		    	if (c >= 'a' && c <= 'z') {
		    		// activate the next maze effect
		    		MazePlayer[] mazePlayers = mbp.getMazePlayers();
		    		for (int i = 0; i < mazePlayers.length; i++) {
		    			if (MAZE_EFFECT_ACTIVATE_KEYS[i] == c) {
		    				mazePlayers[i].activateNextMazeEffect(mbp);
		    			}
		    		}
		    		// make sure we register the player's key as pressed
		    		mbp.getKeyPresses().put(c, System.currentTimeMillis());
		    	}
	    	}
	    }

	    public void keyReleased(KeyEvent e) {
	    	if (mbp.getGameState().equals(MazeGameState.PLAYING)) {
		    	char c = Character.toLowerCase(e.getKeyChar());
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
}
