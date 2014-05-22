package maze.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import maze.effect.MazeEffect;
import maze.generator.maze.MazeGenerator;
import maze.player.MazePlayer;
import maze.player.MazePlayerDirection;
import maze.tiler.CarCityMazeTiler;
import maze.tiler.MazeTiler;

/**
 * MazeJPanel
 * contains the actual panel controls 
 */

/**
 * @author oliver
 *
 */
public class MazeGamePanel extends JPanel {
	
	public MazeGamePanel(int maxHeight, int maxWidth) {
		// double buffered JPanel
		super(true);
		this.setFocusable(true);

		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
		this.setMaximumSize(new Dimension(maxHeight, maxWidth));
		
		this.image = null;
		this.mazeTiles = null;
		this.mazeSize = 0;
		this.mazePlayers = null;
		this.mazeTiler = null;
		this.effectDisplay = null;
	}
	
	public void setup(int size, MazeGenerator mazeGenerator, MazePlayer[] mazePlayers) {
		// size
		this.mazeSize = size;
		// we can now set a preferred height
		this.setPreferredSize(new Dimension((int) (this.maxHeight / size) * size, (int) (this.maxWidth / size) * size));
		this.setMinimumSize(new Dimension((int) (this.maxHeight / size) * size, (int) (this.maxWidth / size) * size));
		
		// generate tiles - startX, startY represent the x-y coordinate
		// of the starting tile for all the players
		int startX = 0;
		int startY = 0;
		
		this.mazeTiles = new MazeTile[size][size];
		mazeGenerator.setStartTile(startX, startY);
		mazeGenerator.setDifficulty(10);
		mazeGenerator.generateMaze(this.mazeTiles);

		// tile maze
		this.mazeTiler = new CarCityMazeTiler(mazeTiles, size, (int) this.getPreferredSize().getHeight(), (int) this.getPreferredSize().getWidth());
		this.mazeTiler.tileMaze();
		this.image = this.mazeTiler.getImage();
		
		// add players
		this.mazePlayers = mazePlayers;
		
		MazePlayerDirection direction;
		
		/* determine the starting direction of the car */
		if (startX > 0 && !this.mazeTiles[startX - 1][startY].isWall()) {
			direction = MazePlayerDirection.UP;
		} else if (startX < size - 1 && !this.mazeTiles[startX + 1][startY].isWall()) {
			direction = MazePlayerDirection.DOWN;
		} else if (startY > 0 && !this.mazeTiles[startX][startY - 1].isWall()) {
			direction = MazePlayerDirection.LEFT;
		} else {
			direction = MazePlayerDirection.RIGHT;
		}
		
		for (MazePlayer player : this.mazePlayers) {
			player.setDirection(direction);
		}
	}
	
		
	/**
	 * rotates the map right
	 */
	public void rotateRight() {
		// rotate tiles
		MazeTile tiles[][] = new MazeTile[this.mazeSize][this.mazeSize];
	    for (int i = 0; i < this.mazeSize; ++i) {
	        for (int j = 0; j < this.mazeSize; ++j) {
	            tiles[i][j] = this.mazeTiles[this.mazeSize - j - 1][i];
	        }
	    }
	    this.mazeTiles = tiles;
	    this.mazeTiler.setTiles(this.mazeTiles);
	    this.mazeTiler.tileMaze();
		this.image = this.mazeTiler.getImage();
	    // rotate players
	    for (MazePlayer p : this.mazePlayers) {
	    	double curX = p.getPosX();
	    	double curY = p.getPosY();
	    	p.setPosX(this.getWidth() - curY - this.getWidth() / this.mazeSize);
	    	p.setPosY(curX);
	    }
	}
	
	/**
	 * rotates the map right
	 */
	public void rotateLeft() {
		// rotate tiles
		MazeTile tiles[][] = new MazeTile[this.mazeSize][this.mazeSize];
	    for (int i = 0; i < this.mazeSize; ++i) {
	        for (int j = 0; j < this.mazeSize; ++j) {
	            tiles[i][j] = this.mazeTiles[j][this.mazeSize - i - 1];
	        }
	    }
	    this.mazeTiles = tiles;
	    this.mazeTiler.setTiles(this.mazeTiles);
	    this.mazeTiler.tileMaze();
		this.image = this.mazeTiler.getImage();
	    
	    // rotate players
	    for (MazePlayer p : this.mazePlayers) {
	    	double curX = p.getPosX();
	    	double curY = p.getPosY();
	    	p.setPosX(curY);
	    	p.setPosY(this.getHeight() - curX - this.getHeight() / this.mazeSize);
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
		if (this.mazePlayers.length > player) {
			MazePlayer p = this.mazePlayers[player];
			if (p != null && !p.isFinished()) {			
				// prospective to destination
				// moving from the current position + the distance movable per second * time key held 
				double xTo = (p.getPosX() + (this.getWidth() / this.mazeSize) * TILES_PER_SECOND * p.getSpeedModifier() * (time / 1000.0) * xDir);
				double yTo = (p.getPosY() + (this.getHeight() / this.mazeSize) * TILES_PER_SECOND * p.getSpeedModifier() * (time / 1000.0) * yDir);
							
				// get nearest obstacle x-ways
				int tileWidth = this.getWidth() / mazeSize;
				int tileHeight = this.getHeight() / mazeSize;
				
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
						MazeEffect effect = this.mazeTiles[tileY][tileX].getEffect();
						if (effect != null) {
							// activate effect
							this.mazeTiles[tileY][tileX].setEffect(null);
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
						for (i = tileX + 1; i < mazeSize; i++) {
							if (mazeTiles[tileY][i].isWall()) {
								break;
							}
						}
						if (i < mazeSize) {
							// make sure we don't go past the nearest wall
							xTo = Math.min((i - 1) * tileWidth, xTo);
						}
						// vice versa for negative in X direction
						for (i = tileX; i >= 0; i--) {
							if (mazeTiles[tileY][i].isWall()) {
								break;
							}
						}
						if (i >= 0) {
							// make sure we don't go past that wall either
							xTo = Math.max((i + 1) * tileWidth, xTo);
						}
						
						// and we do the same thing for the Y direction
					    for (i = tileY + 1; i < mazeSize; i++) {
			                if (mazeTiles[i][tileX].isWall()) {
			                    break;
			                }
			            }
			            if (i < mazeSize) {
			                yTo = Math.min((i - 1) * tileHeight, yTo);
			            }
					    for (i = tileY; i >= 0; i--) {
			                if (mazeTiles[i][tileX].isWall()) {
			                    break;
			                }
			            }
			            if (i >= 0) {
			                yTo = Math.max((i + 1) * tileHeight, yTo);
			            }
					}
				}
				// bounded by 4 walls
				xTo = Math.max(0, Math.min(xTo, tileWidth * (mazeSize - 1)));
				yTo = Math.max(0, Math.min(yTo, tileHeight * (mazeSize - 1)));
				// change direction to face
				if (Math.abs(xTo - p.getPosX()) >= 0.01 || Math.abs(yTo - p.getPosY()) >= 0.01) {
					if (xDir == -1) {
						p.setDirection(MazePlayerDirection.LEFT);
					} else if (xDir == 1) {
						p.setDirection(MazePlayerDirection.RIGHT);
					} else if (yDir == -1) {
						p.setDirection(MazePlayerDirection.UP);
					} else if (yDir == 1) {
						p.setDirection(MazePlayerDirection.DOWN);
					} else {
						throw new RuntimeException(
								"unrecognised player direction: " + xDir + ", " + yDir);
					}
				}
				// set position - but make sure we don't fall off the grid :)
				p.setPosX(xTo);
				p.setPosY(yTo);
				
				// are we ABSOLUTELY at the goal?
				if (this.mazeTiles[(int) (p.getPosY() / tileHeight)][(int) (p.getPosX() / tileWidth)].isGoal() &&
						((int) (p.getPosY() / tileHeight) * tileHeight) == (int) yTo &&
						((int) (p.getPosX() / tileWidth) * tileWidth) == (int) xTo) {
					p.setRanking(this.minRanking++);
				}
			}
		}		
	}
	
	
	
	/**
	 * @return the effectDisplay
	 */
	public MazeGameEffectPopup getEffectDisplay() {
		return effectDisplay;
	}

	/**
	 * @param effectDisplay the effectDisplay to set
	 */
	public void setEffectDisplay(MazeGameEffectPopup effectDisplay) {
		this.effectDisplay = effectDisplay;
	}

	/**
	 * @return the mazePlayers
	 */
	public MazePlayer[] getMazePlayers() {
		return mazePlayers;
	}
	
	/**
	 * @return an array of the maze tiles
	 */
	public MazeTile[][] getMazeTiles() {
		return mazeTiles;
	}
	
	/**
	 * @return the height/width of the maze (a square)
	 */
	public int getMazeSize() {
		return mazeSize;
	}
	
	
	
	/**
	 * @return the minRanking
	 */
	public int getMinRanking() {
		return minRanking;
	}

	/**
	 * @param minRanking the minRanking to set
	 */
	public void setMinRanking(int minRanking) {
		this.minRanking = minRanking;
	}

	/**
	 * Takes an array of size at least 2 and writes the player's coordinates
	 * into the first two locations in the array. location[0] will store
	 * the row of the player, location[1] will store the column of the player.
	 * @param location The array to write the player's x, y coordinates into.
	 * @param p the player to check the coordinates for.
	 */
	public void getPlayerTileLocation(int[] location, MazePlayer p) {
		/* get the tile width/height */
		int tileWidth  = this.getWidth() / mazeSize;
		int tileHeight = this.getHeight() / mazeSize;
		
		location[0] = (int) p.getPosX() / tileHeight;
		location[1] = (int) p.getPosY() / tileWidth;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		int tileWidth = this.getWidth() / mazeSize;
		int tileHeight = this.getHeight() / mazeSize;
		
		if (this.image != null) {
			// draw base map
			g2d.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), null);
	
			// draw effects
			for (int i = 0; i < this.mazeTiles.length; i++) {
				for (int j = 0; j < this.mazeTiles[i].length; j++) {
					this.mazeTiles[i][j].draw(g2d, j * tileHeight, i * tileWidth, tileWidth, tileHeight);
				}
			}
		}
		if (this.mazePlayers != null) {
			// draw players
			for (MazePlayer p : this.mazePlayers) {
				if (p != null) {
					p.draw(g2d, tileWidth, tileHeight);
				}
			}
		}

		if (this.effectDisplay != null) {
			if (this.effectDisplay.getFinishTime() >= System.currentTimeMillis()) {
				this.effectDisplay.paint(g, this.getWidth(), this.getHeight());
			} else {
				this.effectDisplay = null;
			}
		}
		
		// ensure clean and up to date
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}	
	
	private static final double TILES_PER_SECOND = 4;
	private static final long serialVersionUID = 4602880844383443785L;
	
	// maze tiles
	private MazeTiler mazeTiler;
	private MazeTile[][] mazeTiles;
	// maze property
	private int mazeSize;	
	// ensure player is drawn correctly
	private Image image;
	// players
	private MazePlayer[] mazePlayers;
	// preferred dimensions
	private int maxHeight, maxWidth;
	// current ranking
	private int minRanking;	
	// effect display
	private MazeGameEffectPopup effectDisplay;
}