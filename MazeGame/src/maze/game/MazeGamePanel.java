package maze.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import maze.effect.GlobalSpeedDownEffect;
import maze.effect.MazeEffect;
import maze.effect.RotateLeftEffect;
import maze.effect.RotateRightEffect;
import maze.effect.SelfSpeedUpEffect;
import maze.generator.MazeGenerator;
import maze.generator.RandomMazeGenerator;

/**
 * MazeJPanel
 * contains the actual panel controls 
 */

/**
 * @author oliver
 *
 */
public class MazeGamePanel extends JPanel {
	private static final char[] MAZE_EFFECT_ACTIVATE_KEYS = { 'e', 'y', 'o' };
	private static final int REFRESH_RATE = 60;
	public static final int NUM_PLAYERS = 3;
	public static final double TILES_PER_SECOND = 4;
	private static final long serialVersionUID = 4602880844383443785L;
	
	private final BufferedImage[] tileImages;
	
	private MazeTile[][] tiles;
	private int size;
	private BufferedImage image;
	
	private Timer timer;
	private MazePlayer players[];
	private PriorityQueue<MazeEffect> activatedEffects;
	private ConcurrentHashMap<Character, Long> keyPresses; 

	/**
	 * Generates a new MazeJPanel 
	 * @param size size of the maze
	 */
	public MazeGamePanel(int size) {
		// double buffered JPanel
		super(true);
		this.setFocusable(true);

		// size
		this.size = size;
		
		// generate tiles;
		this.tiles = new MazeTile[size][size];
		
		this.tileImages = new BufferedImage[TileType.values().length];
		for (int i = 0; i < TileType.values().length; i++) {
			try {
				System.out.println(i);
				tileImages[i] = ImageIO.read(new File("images/tiles/tile_" + i + ".png"));
			} catch (IOException e) {
				throw new RuntimeException("Tile images missing!");
			}
		}
		
		MazeGenerator heh = new RandomMazeGenerator(size);
		heh.generateMaze(tiles);
		heh.setDifficulty(10);
		
		setTileImages();
		
		// key presses
		this.keyPresses = new ConcurrentHashMap<Character, Long>();
		this.addKeyListener(new MazeJPanelKeyListener(this));
		
		// make da players
		this.players = new MazePlayer[NUM_PLAYERS];
		for (int i = 0; i < NUM_PLAYERS; i++) {
			this.players[i] = new MazePlayer(i);
		}
		
		// create the effects PQ
		this.activatedEffects = new PriorityQueue<MazeEffect>(10, new Comparator<MazeEffect>() {
			@Override
			public int compare(MazeEffect a, MazeEffect b) {
				return (int) (b.getEndTime() - a.getEndTime());
			}
		});
		
		this.tiles[2][0].setEffect(new SelfSpeedUpEffect());
		this.tiles[4][0].setEffect(new GlobalSpeedDownEffect());
		this.tiles[4][4].setEffect(new RotateLeftEffect());
		this.tiles[6][6].setEffect(new RotateRightEffect());
		
		// allocate timer and start when ready - MUST BE LAST
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new MazeJPanelTimer(this), 0, 1000 / REFRESH_RATE);
	}
	
	public void setTileImages() {
		this.image = new BufferedImage((int) this.getPreferredSize().getWidth(), (int) this.getPreferredSize().getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D imageG2D = this.image.createGraphics();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!tiles[i][j].isWall()) {
					int neighbours = numNeighbours(i, j);
					setTileImage(imageG2D, i, j, neighbours);
				} else {
					setWallImage(imageG2D, i, j);
				}
			}
		}
		imageG2D.dispose();
	}
	
	private void setWallImage(Graphics2D imageG2D, int i, int j) {
		int width = (int) this.getPreferredSize().getWidth() / size;
		int height = (int) this.getPreferredSize().getHeight() / size;
		int x = j * width;
		int y = i * height;
		
		Image img = null;
		if (i % 4 == 0 || i % 6 == 0 || i % 2 == 0) {
			img = new ImageIcon("images/tiles/house.png").getImage();
		} else if (j % 4 == 0 || j % 6 == 0) {
			img = new ImageIcon("images/tiles/building.png").getImage();
		} else {
			img = new ImageIcon("images/tiles/treegreen.png").getImage();
		}
		imageG2D.drawImage(img, x, y, width, height, null);
	}
	
	
	private void setTileImage(Graphics2D imageG2D, int i, int j, int neighbours) {
		BufferedImage tileImage = null;
		boolean flipX = false;
		boolean flipY = false;
		
		switch (neighbours) {
			case 1:
				if (j < size - 1 && !tiles[i][j + 1].isWall()) {
					tileImage = tileImages[TileType.DEAD_END_HORIZONTAL.ordinal()];
					flipX = true;
				} else if (j > 0 && !tiles[i][j - 1].isWall()) {
					tileImage = tileImages[TileType.DEAD_END_HORIZONTAL.ordinal()];
				} else if (i < size - 1 && !tiles[i + 1][j].isWall()) {
					tileImage = tileImages[TileType.DEAD_END_VERTICAL.ordinal()];
					flipY = true;
				} else if (i > 0 && !tiles[i - 1][j].isWall()) {
					tileImage = tileImages[TileType.DEAD_END_VERTICAL.ordinal()];
				} 
				break;
			case 2:
				if ((j > 0 && j < size - 1) && !tiles[i][j - 1].isWall() && !tiles[i][j+1].isWall()) {
					tileImage = tileImages[TileType.STRAIGHT_HORIZONTAL.ordinal()];
				} else if ((i > 0 && i < size - 1) && !tiles[i+1][j].isWall() && !tiles[i - 1][j].isWall()) {
					tileImage = tileImages[TileType.STRAIGHT_VERTICAL.ordinal()];
				} else if (i > 0 && j > 0 && !tiles[i-1][j].isWall() && !tiles[i][j-1].isWall()) {
					tileImage = tileImages[TileType.RIGHT_ANGLE.ordinal()];
					flipX = true;
				} else if (i > 0 && j < size - 1 && !tiles[i - 1][j].isWall() && !tiles[i][j + 1].isWall()) {
					tileImage = tileImages[TileType.RIGHT_ANGLE.ordinal()];
				} else if (i < size - 1 && j < size - 1 && !tiles[i + 1][j].isWall() && !tiles[i][j + 1].isWall()) {
					tileImage = tileImages[TileType.RIGHT_ANGLE.ordinal()];
					flipY = true;
				} else {
					tileImage = tileImages[TileType.RIGHT_ANGLE.ordinal()];
					flipX = true;
					flipY = true;
				}
				
				break;
			case 3:
				if (j == size - 1 || (j < size - 1 && tiles[i][j + 1].isWall())) {
					tileImage = tileImages[TileType.THREE_INTERSECT_VERTICAL.ordinal()];
				} else if (j == 0 || (j > 0 && tiles[i][j - 1].isWall())) {
					tileImage = tileImages[TileType.THREE_INTERSECT_VERTICAL.ordinal()];
					flipX = true;
				} else if (i == size - 1 || (i < size - 1 && tiles[i + 1][j].isWall())) {
					tileImage = tileImages[TileType.THREE_INTERSECT_HORIZONTAL.ordinal()];
				} else if (i == 0 || (i > 0 && tiles[i - 1][j].isWall())) {
					tileImage = tileImages[TileType.THREE_INTERSECT_HORIZONTAL.ordinal()];
					flipY = true;
				}
				break;
			case 4:
				tileImage = tileImages[TileType.EVERYTHING.ordinal()];
				break;
			default:
				throw new RuntimeException("you fucked up");
		}
		
		int width = (int) this.getPreferredSize().getWidth() / size;
		int height = (int) this.getPreferredSize().getHeight() / size;
		int x = j * width;
		int y = i * height;
		if (flipX && !flipY) {
			imageG2D.drawImage(tileImage, x + width, y, -width, height, null);
		} else if (flipX && flipY) {
			imageG2D.drawImage(tileImage, x + width, y + height, -width, -height, null);
		} else if (!flipX && flipY) {
			imageG2D.drawImage(tileImage, x, y + height, width, -height, null);
		} else {
			imageG2D.drawImage(tileImage, x, y, width, height, null);
		}
	}
	
	/**
	 * @return the number of non-wall neighbours of a specified tile.
	 */
	private int numNeighbours(int i, int j) {
		int neighbours = 0;
		
		/* Check if there's a neighbour to the left */
		if (i > 0 && !tiles[i-1][j].isWall()) {
			neighbours++;
		}
		
		/* Check if there's a neighbour on top */
		if (j > 0 && !tiles[i][j-1].isWall()) {
			neighbours++;
		}
		
		/* Check if there's a neighbour on the right */
		if (i < size - 1 && !tiles[i+1][j].isWall()) {
			neighbours++;
		}
		
		/* Check if there's a neighbour on the bottom */
		if (j < size - 1 && !tiles[i][j+1].isWall()) {
			neighbours++;
		}
		
		return neighbours;
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
	    setTileImages();
	    // rotate players
	    for (MazePlayer p : this.players) {
	    	double curX = p.getPosX();
	    	double curY = p.getPosY();
	    	p.setPosX(this.getWidth() - curY - this.getWidth() / this.size);
	    	p.setPosY(curX);
	    }
	}
	
	/**
	 * rotates the map right
	 */
	public void rotateLeft() {
		// rotate tiles
		MazeTile tiles[][] = new MazeTile[this.size][this.size];
	    for (int i = 0; i < this.size; ++i) {
	        for (int j = 0; j < this.size; ++j) {
	            tiles[i][j] = this.tiles[j][this.size - i - 1];
	        }
	    }
	    this.tiles = tiles;
	    setTileImages();
	    
	    // rotate players
	    for (MazePlayer p : this.players) {
	    	double curX = p.getPosX();
	    	double curY = p.getPosY();
	    	p.setPosX(curY);
	    	p.setPosY(this.getHeight() - curX - this.getHeight() / this.size);
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
				// nothing we're worried about :)
				return;
		}
		
		// ensure player is playing this game
		MazePlayer p = this.players[player];
		if (p != null) {			
			// prospective to destination
			// moving from the current position + the distance movable per second * time key held 
			double xTo = (p.getPosX() + (this.getWidth() / this.size) * TILES_PER_SECOND * p.getSpeedModifier() * (time / 1000.0) * xDir);
			double yTo = (p.getPosY() + (this.getHeight() / this.size) * TILES_PER_SECOND * p.getSpeedModifier() * (time / 1000.0) * yDir);
						
			// get nearest obstacle x-ways
			int tileWidth = this.getWidth() / size;
			int tileHeight = this.getHeight() / size;
			
			int leeWayX = 0;
			int leeWayY = 0;
			
			int maxLeeWayX = Math.max(tileWidth > 2 ? 2 : (tileWidth > 1 ? 1 : 0), (int) (tileWidth * 0.2));
			int maxLeeWayY = Math.max(tileHeight > 2 ? 2 : (tileHeight > 0 ? 1 : 0), (int) (tileHeight * 0.2));
						
			// since being EXACT is hard, we allow a little bit of leeway
			// for players, so turning corners is nicer
			// calculate distance from the start of current tile
			int distFromTileStartX = (int) p.getPosX() % tileWidth;			
			if (distFromTileStartX + maxLeeWayX > tileWidth) {
				// you are almost at the end of one tile
				// make it so that you are there
				leeWayX = tileWidth - distFromTileStartX;
			} else if (distFromTileStartX < maxLeeWayX) {
				// you are almost at the start of the tile
				// shift yourself there
				leeWayX = -distFromTileStartX;
			}
			
			// do something similar for y
			int distFromTileStartY = (int) p.getPosY() % tileHeight;
			if (distFromTileStartY + maxLeeWayY > tileHeight) {
				leeWayY = tileHeight - distFromTileStartY;
			} else if (distFromTileStartY < maxLeeWayY) {
				leeWayY = -distFromTileStartY;
			}
			
			// figure out which tiles the player is overlapping with
			// we find the tile (in the array) of the leftmost tile
			// and the tile at the right hand size
			int currentTileXs[] = { (int) (p.getPosX()) / tileWidth, (int) ((p.getPosX() + tileWidth - 1) / tileWidth) } ;
			int currentTileYs[] = { (int) (p.getPosY()) / tileHeight, (int) ((p.getPosY() + tileHeight - 1) / tileHeight) };
			for (int tileX : currentTileXs) {
				for (int tileY : currentTileYs) {
					MazeEffect effect = this.tiles[tileY][tileX].getEffect();
					if (effect != null) {
						// activate effect
						this.tiles[tileY][tileX].setEffect(null);
						p.addMazeEffect(effect);				
					}
				}
			}
			
			// add leeways to the tiles, so players don't bash against the wall
			// if they're one pixel off
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
						// make sure we don't go past that wall either
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
			// bounded by 4 walls
			xTo = Math.max(0, Math.min(xTo, tileWidth * (size - 1)));
			yTo = Math.max(0, Math.min(yTo, tileHeight * (size - 1)));
			// change direction to face
			if (Math.abs(xTo - p.getPosX()) >= 0.01 || Math.abs(yTo - p.getPosY()) >= 0.01) {
				p.setDirX(xDir);
				p.setDirY(yDir);
			}
			// set position - but make sure we don't fall off the grid :)
			p.setPosX(xTo);
			p.setPosY(yTo);
		}		
	}
	
	
	/**
	 * @return the activatedEffects
	 */
	public PriorityQueue<MazeEffect> getActivatedEffects() {
		return activatedEffects;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int tileWidth = this.getWidth() / size;
		int tileHeight = this.getHeight() / size;
		Graphics2D g2d = (Graphics2D) g;
		
		// draw base map
		g2d.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), null);

		// draw effects
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
		// ensure clean and up to date
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
	
	/**
	 * @author oliver
	 * MazeJPanelTimer allows player listeners and repainting
	 */
	private class MazeJPanelTimer extends TimerTask {
		private MazeGamePanel m;
		
		private MazeJPanelTimer(MazeGamePanel m) {
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
			// remove unnecessary boosts
			PriorityQueue<MazeEffect> pq = m.getActivatedEffects();
			while (!pq.isEmpty()) {
				if (pq.peek().getEndTime() <= curTime) {
					System.out.println("die potato");
					pq.poll().deactivate(m);
				} else {
					break;
				}
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
		private MazeGamePanel m;
		
		private MazeJPanelKeyListener(MazeGamePanel m) {
			this.m = m;
		}		
		
		public void keyTyped(KeyEvent e) {
	    }

	    public void keyPressed(KeyEvent e) {
	    	char c = Character.toLowerCase(e.getKeyChar());
	    	if (c >= 'a' && c <= 'z') {
	    		// activate the next maze effect
	    		MazePlayer[] mazePlayers = m.getPlayers();
	    		for (int i = 0; i < mazePlayers.length; i++) {
	    			if (MAZE_EFFECT_ACTIVATE_KEYS[i] == c) {
	    				mazePlayers[i].activateNextMazeEffect(m);
	    			}
	    		}
	    		// make sure we register the player's key as pressed
	    		this.m.getKeyPresses().put(c, System.currentTimeMillis());
	    	}
	    }

	    public void keyReleased(KeyEvent e) {
	    	char c = Character.toLowerCase(e.getKeyChar());
	    	if (c >= 'a' && c <= 'z') {
	    		// in case remove doesn't exist
	    		Long releasedTime = this.m.getKeyPresses().remove(c);
	    		if (releasedTime != null) {
		    		// update the player's movement
		    		long difference = System.currentTimeMillis() - releasedTime;
		    		this.m.updatePlayerMovement(c, difference);
	    		}
	    	}
	    }
	}
	
	private enum TileType {
		EVERYTHING,
		THREE_INTERSECT_HORIZONTAL,
		THREE_INTERSECT_VERTICAL,
		RIGHT_ANGLE,
		STRAIGHT_HORIZONTAL,
		STRAIGHT_VERTICAL,
		DEAD_END_HORIZONTAL,
		DEAD_END_VERTICAL
	}
}