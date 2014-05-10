import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

/**
 * MazeJPanel
 * contains the actual panel controls 
 */

/**
 * @author oliver
 *
 */
public class MazeJPanel extends JPanel {
	/**
	 * serialVersionUID magic
	 */
	private static final int REFRESH_RATE = 24;
	private static final int NUM_PLAYERS = 3;
	private static final long serialVersionUID = 4602880844383443785L;
	private MazeTile[][] tiles;
	private int size;
	private Timer timer;
	private MazePlayer players[];

	private ConcurrentHashMap<Character, Long> keyPresses; 

	/**
	 * Generates a new MazeJPanel 
	 * @param size size of the maze
	 */
	public MazeJPanel(int size) {
		// double buffered JPanel
		super(true);
		this.setFocusable(true);
		
		// generate tiles;
		this.tiles = new MazeTile[size][size];
		for (int i = 0; i  < size; i++) {
			for (int j = 0; j < size; j++) {
				this.tiles[i][j] = new MazeTile((new Random()).nextBoolean());
			}
		}
		
		// size
		this.size = size;
		
		// allocate timer
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new MazeJPanelTimer(this), 0, 1000 / REFRESH_RATE);
		
		// key presses
		this.keyPresses = new ConcurrentHashMap<Character, Long>();
		this.addKeyListener(new MazeJPanelKeyListener(this));
		
		// make da players
		this.players = new MazePlayer[NUM_PLAYERS];
		for (int i = 0; i < NUM_PLAYERS; i++) {
			this.players[i] = new MazePlayer(i, this, i == 0 ? Color.red : i == 1 ? Color.blue : Color.green, 0.05);
		}
	}
	
	/**
	 * @return the keyPresses
	 */
	public ConcurrentHashMap<Character, Long> getKeyPresses() {
		return keyPresses;
	}
	
	/**
	 * @return the players
	 */
	public MazePlayer[] getPlayers() {
		return players;
	}

	/**
	 * @param key key pressed
	 * @param time time taken
	 */
	public void updatePlayerMovement(char key, long time) {
		int player;
		int xDir = 0;
		int yDir = 0;
		
		switch (key) {
			case 'w':
				player = 0; 
				yDir = -1;
				break;
			case 'a':
				player = 0; 
				xDir = -1;
				break;
			case 's':
				player = 0; 
				yDir = 1;
				break;
			case 'd':
				player = 0; 
				xDir = 1;
				break;
			case 't':
				player = 1; 
				yDir = -1;
				break;
			case 'f':
				player = 1; 
				xDir = -1;
				break;
			case 'g':
				player = 1; 
				yDir = 1;
				break;
			case 'h':
				player = 1; 
				xDir = 1;
				break;
			case 'i':
				player = 2; 
				yDir = -1;
				break;
			case 'j':
				player = 2; 
				xDir = -1;
				break;
			case 'k':
				player = 2; 
				yDir = 1;
				break;
			case 'l':
				player = 2; 
				xDir = 1;
				break;
			default:
				return;
		}
		
		MazePlayer p = this.players[player];
		if (p != null) {
			p.setPosX((int) (p.getPosX() + p.getSpeed() * time * xDir));
			p.setPosY((int) (p.getPosY() + p.getSpeed() * time * yDir));
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int width = this.getWidth() / size;
		int height = this.getHeight() / size;
		
		// draw tiles
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				this.tiles[i][j].draw(g2d, i * width, j * height, width, height);
			}
		}
		
		// draw players
		for (MazePlayer mp : this.players) {
			if (mp != null) {
				mp.draw(g2d, width, height);
			}
		}
	}
	
	/**
	 * @author oliver
	 * MazeJPanelTimer allows player listeners and repainting
	 */
	private class MazeJPanelTimer extends TimerTask {
		private MazeJPanel m;
		private long lastTime;
		
		private MazeJPanelTimer(MazeJPanel m) {
			this.m = m;
			this.lastTime = System.currentTimeMillis();
		}
		
		@Override
		public void run() {
			// update all the player
			long curTime = System.currentTimeMillis();
			for (Entry<Character, Long> e : this.m.getKeyPresses().entrySet()) {
				long difference = curTime - e.getValue();
	    		this.m.updatePlayerMovement(e.getKey(), difference);
				this.m.getKeyPresses().put(e.getKey(), curTime);
			}
			// repaint the maze since there are updates
			this.m.repaint();
			// time has changed!
			this.lastTime = curTime;
		}
	}
	
	private class MazeJPanelKeyListener implements KeyListener {
		private MazeJPanel m;
		
		private MazeJPanelKeyListener(MazeJPanel m) {
			this.m = m;
		}		
		
		public void keyTyped(KeyEvent e) {
	    }

	    public void keyPressed(KeyEvent e) {
	    	if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
	    		this.m.getKeyPresses().put(e.getKeyChar(), System.currentTimeMillis());
	    	}
	    }

	    public void keyReleased(KeyEvent e) {
	    	if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
	    		long difference = System.currentTimeMillis() - this.m.getKeyPresses().remove(e.getKeyChar());
	    		this.m.updatePlayerMovement(e.getKeyChar(), difference);
	    	}
	    }
	}
}