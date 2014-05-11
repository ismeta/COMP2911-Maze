import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map.Entry;
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
public class Maze extends JPanel {
	/**
	 * serialVersionUID magic
	 */
	private static final int REFRESH_RATE = 30;
	private static final int NUM_PLAYERS = 3;
	private static final long serialVersionUID = 4602880844383443785L;
	private static final double TILES_PER_SECOND = 4;
	
	private MazeTile[][] tiles;
	private int size;
	private Timer timer;
	private MazePlayer players[];

	// keys current held - mapped by character-last processing time
	private ConcurrentHashMap<Character, Long> keyPresses; 

	/**
	 * Generates a new MazeJPanel 
	 * @param size size of the maze
	 */
	public Maze(int size) {
		// double buffered JPanel
		super(true);
		this.setFocusable(true);

		// size
		this.size = size;
		
		// generate tiles;
		this.tiles = new MazeTile[size][size];
		
		RandomMazeGenerator heh = new RandomMazeGenerator(size);
		heh.generateMaze(tiles);
		
		// allocate timer
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new MazeJPanelTimer(this), 0, 1000 / REFRESH_RATE);
		
		// key presses
		this.keyPresses = new ConcurrentHashMap<Character, Long>();
		this.addKeyListener(new MazeJPanelKeyListener(this));
		
		// make da players
		this.players = new MazePlayer[NUM_PLAYERS];
		for (int i = 0; i < NUM_PLAYERS; i++) {
			this.players[i] = new MazePlayer(i, i == 0 ? Color.red : i == 1 ? Color.blue : Color.green);
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
	 * rotates the map right
	 */
	public void rotateRight() {
		// rotate tiles
		MazeTile tiles[][] = new MazeTile[this.size][this.size];
	    for (int i = 0; i < this.size; ++i) {
	        for (int j = 0; j < this.size; ++j) {
	            tiles[i][j] = this.tiles[this.size - j - 1][i];
	        }
	    }
	    this.tiles = tiles;
	    
	    // rotate players
	    for (MazePlayer p : this.players) {
	    	double curX = p.getPosX();
	    	double curY = p.getPosY();
	    	p.setPosX(this.getWidth() - curY - this.getWidth() / this.size);
	    	p.setPosY(curX);
	    }
	}

	/**
	 * @param key key pressed
	 * @param time time taken
	 */
	public void updatePlayerMovement(char key, long time) {
		int player;
		int xDir = 0;
		int yDir = 0;
		
		// find the player and directions the player will move 
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
		
		// ensure player is playing this game
		MazePlayer p = this.players[player];
		if (p != null) {
			// prospective to destination
			// moving from the current position + the distance movable per second * time key held 
			double xTo = (p.getPosX() + (this.getWidth() / this.size) * TILES_PER_SECOND * (time / 1000.0) * xDir);
			double yTo = (p.getPosY() + (this.getHeight() / this.size) * TILES_PER_SECOND * (time / 1000.0) * yDir);
						
			// get nearest obstacle x-ways
			int tileWidth = this.getWidth() / size;
			int tileHeight = this.getHeight() / size;
			
			int leeWayX = 0;
			int leeWayY = 0;
			
			// since being EXACT is hard, we allow a little bit of leeway
			// for players, so turning corners is nicer
			// calculate distance from the start of current tile
			int distFromTileStartX = (int) p.getPosX() % tileWidth;			
			if (distFromTileStartX + 2 > tileWidth) {
				// you are almost at the end of one tile
				// make it so that you are there
				leeWayX = tileWidth - distFromTileStartX;
			} else if (distFromTileStartX < 2) {
				// you are almost at the start of the tile
				// shift yourself there
				leeWayX = -distFromTileStartX;
			}
			
			// do something similar for y
			int distFromTileStartY = (int) p.getPosY() % tileHeight;
			if (distFromTileStartY + 2 > tileHeight) {
				leeWayY = tileHeight - distFromTileStartY;
			} else if (distFromTileStartY < 2) {
				leeWayY = -distFromTileStartY;
			}
			
			// figure out which tiles the player is overlapping with
			// we find the tile (in the array) of the leftmost tile
			// and the tile at the right hand size
			int tileXs[] = { (int) (p.getPosX() + leeWayX) / tileWidth, (int) ((p.getPosX() + tileWidth  + leeWayX - 1) / tileWidth) } ;
			int tileYs[] = { (int) (p.getPosY() + leeWayY) / tileHeight, (int) ((p.getPosY() + tileHeight  + leeWayY - 1) / tileHeight) };	
			
			
			// scan through all potential collisions between the player's current position
			// and where there will potentially move to
			for (int tileX : tileXs) {
				for (int tileY : tileYs) {
					int i;
				
					// check moving  positive in X direction
					// looks for closest wall 
					for (i = tileX + 1; i < size; i++) {
						if (tiles[tileY][i].isWall()) {
							break;
						}
					}
					if (i < size) {
						// make sure we don't go past the nearest wall
						xTo = Math.min((i - 1) * tileWidth, xTo);
					}
					// vice versa for negative in X direction
					for (i = tileX; i >= 0; i--) {
						if (tiles[tileY][i].isWall()) {
							break;
						}
					}
					if (i >= 0) {
						xTo = Math.max((i + 1) * tileWidth, xTo);
					}
					
					// and we do the same thing for the Y direction
				    for (i = tileY + 1; i < size; i++) {
		                if (tiles[i][tileX].isWall()) {
		                    break;
		                }
		            }
		            if (i < size) {
		                yTo = Math.min((i - 1) * tileHeight, yTo);
		            }
				    for (i = tileY; i >= 0; i--) {
		                if (tiles[i][tileX].isWall()) {
		                    break;
		                }
		            }
		            if (i >= 0) {
		                yTo = Math.max((i + 1) * tileHeight, yTo);
		            }
				}
			}
			// set position - but make sure we don't fall off the grid :)
			p.setPosX(Math.max(0, Math.min(xTo, tileWidth * (size - 1))));
			p.setPosY(Math.max(0, Math.min(yTo, tileHeight * (size - 1))));
		}		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int tileWidth = this.getWidth() / size;
		int tileHeight = this.getHeight() / size;
		
		// draw tiles
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < this.tiles.length; i++) {
			for (int j = 0; j < this.tiles[i].length; j++) {
				this.tiles[i][j].draw(g2d, j * tileHeight, i * tileWidth, tileWidth, tileHeight);
			}
		}
		
		// draw players
		for (MazePlayer p : this.players) {
			if (p != null) {
				p.draw(g2d, tileWidth, tileHeight);
			}
		}
	}
	
	/**
	 * @author oliver
	 * MazeJPanelTimer allows player listeners and repainting
	 */
	private class MazeJPanelTimer extends TimerTask {
		private Maze m;
		
		private MazeJPanelTimer(Maze m) {
			this.m = m;
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
		}
	}
	
	/**
	 * @author oliver
	 * Key Listener for the Maze JPanel
	 */
	private class MazeJPanelKeyListener implements KeyListener {
		private Maze m;
		
		private MazeJPanelKeyListener(Maze m) {
			this.m = m;
		}		
		
		public void keyTyped(KeyEvent e) {
	    }

	    public void keyPressed(KeyEvent e) {
	    	if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
	    		if (e.getKeyChar() == 'e') {
	    			m.rotateRight();
	    		}
	    		// make sure we register the player's key as pressed
	    		this.m.getKeyPresses().put(e.getKeyChar(), System.currentTimeMillis());
	    	}
	    }

	    public void keyReleased(KeyEvent e) {
	    	if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
	    		// update the player's movement
	    		long difference = System.currentTimeMillis() - this.m.getKeyPresses().remove(e.getKeyChar());
	    		this.m.updatePlayerMovement(e.getKeyChar(), difference);
	    	}
	    }
	}
}