package maze.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import maze.effect.GlobalSpeedDownEffect;
import maze.effect.MazeEffect;
import maze.effect.RotateLeftEffect;
import maze.effect.RotateRightEffect;
import maze.effect.SelfSpeedUpEffect;
import maze.generator.MazeGenerator;
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
	private static final double TILES_PER_SECOND = 4;
	private static final long serialVersionUID = 4602880844383443785L;
	
	// maze tiles
	private MazeTiler mazeTiler;
	private MazeTile[][] tiles;
	// maze property
	private int size;	
	// ensure player is drawn correctly
	private Image image;
	// players
	private MazePlayer[] mazePlayers;
	// preferred dimensions
	private int maxHeight, maxWidth;
	
	public MazeGamePanel(int maxHeight, int maxWidth) {
		// double buffered JPanel
		super(true);
		this.setFocusable(true);

		this.maxHeight = maxHeight;
		this.maxWidth = maxWidth;
		this.setMaximumSize(new Dimension(maxHeight, maxWidth));
		
		this.image = null;
		this.tiles = null;
		this.size = 0;
		this.mazePlayers = null;
		this.mazeTiler = null;
	}
	
	public void setup(int size, MazeGenerator mazeGenerator, MazePlayer[] mazePlayers) {
		// size
		this.size = size;
		// we can now set a preferred height
		this.setPreferredSize(new Dimension((int) (this.maxHeight / size) * size, (int) (this.maxWidth / size) * size));
		this.setMinimumSize(new Dimension((int) (this.maxHeight / size) * size, (int) (this.maxWidth / size) * size));
		
		// generate tiles - startX, startY represent the x-y coordinate
		// of the starting tile for all the players
		int startX = 0;
		int startY = 0;
		
		this.tiles = new MazeTile[size][size];
		mazeGenerator.setStartTile(startX, startY);
		mazeGenerator.setDifficulty(10);
		mazeGenerator.generateMaze(this.tiles);
		
		// (temporary) set effects
		this.tiles[2][0].setEffect(new SelfSpeedUpEffect());
		this.tiles[4][0].setEffect(new GlobalSpeedDownEffect());
		this.tiles[4][4].setEffect(new RotateLeftEffect());
		this.tiles[6][6].setEffect(new RotateRightEffect());

		// tile maze
		this.mazeTiler = new CarCityMazeTiler(tiles, size, (int) this.getPreferredSize().getHeight(), (int) this.getPreferredSize().getWidth());
		this.mazeTiler.tileMaze();
		this.image = this.mazeTiler.getImage();
		
		// add players
		this.mazePlayers = mazePlayers;
		
		int carDirX = 0;
		int carDirY = 0;
		
		/* determine the starting direction of the car */
		if (startX > 0 && !this.tiles[startX - 1][startY].isWall()) {
			carDirY = -1;
		} else if (startX < size - 1 && !this.tiles[startX + 1][startY].isWall()) {
			carDirY = 1;
		} else if (startY > 0 && !this.tiles[startX][startY - 1].isWall()) {
			carDirX = -1;
		} else {
			carDirX = 1;
		}
		
		for (MazePlayer player : this.mazePlayers) {
			player.setDirY(carDirY);
			player.setDirX(carDirX);
		}
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
	    this.mazeTiler.setTiles(this.tiles);
	    this.mazeTiler.tileMaze();
		this.image = this.mazeTiler.getImage();
	    // rotate players
	    for (MazePlayer p : this.mazePlayers) {
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
	    this.mazeTiler.setTiles(this.tiles);
	    this.mazeTiler.tileMaze();
		this.image = this.mazeTiler.getImage();
	    
	    // rotate players
	    for (MazePlayer p : this.mazePlayers) {
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
		if (this.mazePlayers.length > player) {
			MazePlayer p = this.mazePlayers[player];
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
	}
	
	/**
	 * @return the mazePlayers
	 */
	public MazePlayer[] getMazePlayers() {
		return mazePlayers;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		int tileWidth = this.getWidth() / size;
		int tileHeight = this.getHeight() / size;
		
		if (this.image != null) {
			// draw base map
			g2d.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), null);
	
			// draw effects
			for (int i = 0; i < this.tiles.length; i++) {
				for (int j = 0; j < this.tiles[i].length; j++) {
					this.tiles[i][j].draw(g2d, j * tileHeight, i * tileWidth, tileWidth, tileHeight);
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
		
		// ensure clean and up to date
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}
}