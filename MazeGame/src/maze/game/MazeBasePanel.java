package maze.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

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
	
	public MazeBasePanel() {
		super(true);
		this.setFocusable(true);
		
		this.timer = null;
		this.mazePlayers = null;
		this.activatedEffects = null;
		this.keyPresses = null;
		this.mazeGamePanel = new MazeGamePanel(600, 600);
		this.add(this.mazeGamePanel);
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
		// make da players
		this.mazePlayers = new MazePlayer[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			this.mazePlayers[i] = new MazePlayer(i);
			
			// temporary
			MazePlayerPanel s = new MazePlayerPanel(this.mazePlayers[i]);
			this.add(s);
		}
		
		// now we can #mazeit
		this.mazeGamePanel.setup(size, mazeGenerator, this.mazePlayers);
		
		// maze keypresses and listener
		this.keyPresses = new ConcurrentHashMap<Character, Long>();
		this.addKeyListener(new MazeBasePanelKeyListener(this));
		gui.getCards().addKeyListener(new MazeBasePanelKeyListener(this));
		
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
	}
	
	public void exit(GUI gui) {
		this.timer.purge();
		this.timer = null;
		
		this.mazePlayers = null;
		
		this.activatedEffects.clear();
		this.activatedEffects = null;
		
		for (KeyListener kl : this.getKeyListeners()) {
			this.removeKeyListener(kl);
			gui.getCards().removeKeyListener(kl);
		}
		this.keyPresses.clear();
		this.keyPresses = null;
	}
	
	
	
	/**
	 * @return the mazeGamePanel
	 */
	public MazeGamePanel getMazeGamePanel() {
		return mazeGamePanel;
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

	    public void keyReleased(KeyEvent e) {
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
