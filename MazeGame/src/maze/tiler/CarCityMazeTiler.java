package maze.tiler;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import maze.game.MazeTile;

/**
 * Sets the images for tiles in the maze according to a "car in the city"
 * style theme.
 */
public class CarCityMazeTiler implements MazeTiler {
	/**
	 * @param tiles
	 * @param size
	 * @param height
	 * @param width
	 */
	public CarCityMazeTiler(MazeTile[][] tiles, int size, int height, int width) {
		this.tiles = tiles;
		this.size = size;
		this.mazeHeight = height;
		this.mazeWidth = width;
		
		/* read in the tile images */
		this.tileImages = new BufferedImage[CarMazeCityTileType.values().length];
		for (int i = 0; i < CarMazeCityTileType.values().length; i++) {
			try {
				tileImages[i] = ImageIO.read(new File("images/tiles/tile_" + i + ".png"));
			} catch (IOException e) {
				throw new RuntimeException("Tile images missing!");
			}
		}
	}	
	
	/**
	 * Set the image tiles for the maze.
	 */
	@Override
	public void tileMaze() {
		this.image = new BufferedImage(this.mazeWidth, this.mazeHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D imageG2D = this.image.createGraphics();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				/* tile it differently depending on whether the current tile
				 * is a wall or not  */
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
	@Override
	public BufferedImage getImage() {
		return this.image;
	}
	
	/**
	 * Sets a wall image for a given tile.
	 * @param imageG2D
	 * @param i which row to set the image for
	 * @param j which column to set the image for
	 */
	private void setWallImage(Graphics2D imageG2D, int i, int j) {
		int width = this.mazeWidth / size;
		int height = this.mazeHeight / size;
		int x = j * width;
		int y = i * height;
		
		Image img = null;
		if (i % 2 == 0) {
			img = new ImageIcon("images/tiles/house.png").getImage();
		} else if (j % 4 == 0 || j % 6 == 0) {
			img = new ImageIcon("images/tiles/building.png").getImage();
		} else {
			img = new ImageIcon("images/tiles/treegreen.png").getImage();
		}
		imageG2D.drawImage(img, x, y, width, height, null);
	}
	
	/**
	 * Set the image for a tile based on the tiles around it - i.e.
	 * what kind of intersection it is. Rotates the image based on 
	 * where the walls are.
	 * @param imageG2D
	 * @param i the row of the tile
	 * @param j the column of the tile
	 * @param neighbours the number of non-wall neighbours the tile has
	 */
	private void setTileImage(Graphics2D imageG2D, int i, int j, int neighbours) {
		BufferedImage tileImage = null;
		boolean flipX = false;
		boolean flipY = false;
		
		switch (neighbours) {
			/* one neighbour means that this tile is a dead end */
			case 1:
				if (j < size - 1 && !tiles[i][j + 1].isWall()) {
					tileImage = tileImages[CarMazeCityTileType.DEAD_END_HORIZONTAL.ordinal()];
					flipX = true;
				} else if (j > 0 && !tiles[i][j - 1].isWall()) {
					tileImage = tileImages[CarMazeCityTileType.DEAD_END_HORIZONTAL.ordinal()];
				} else if (i < size - 1 && !tiles[i + 1][j].isWall()) {
					tileImage = tileImages[CarMazeCityTileType.DEAD_END_VERTICAL.ordinal()];
					flipY = true;
				} else if (i > 0 && !tiles[i - 1][j].isWall()) {
					tileImage = tileImages[CarMazeCityTileType.DEAD_END_VERTICAL.ordinal()];
				} 
				break;
				
			/* two neighbours means that it either is a right angled intersection,
			 * or is just a straight line - just a continuation of a road. */
			case 2:
				if ((j > 0 && j < size - 1) && !tiles[i][j - 1].isWall() && !tiles[i][j+1].isWall()) {
					tileImage = tileImages[CarMazeCityTileType.STRAIGHT_HORIZONTAL.ordinal()];
				} else if ((i > 0 && i < size - 1) && !tiles[i+1][j].isWall() && !tiles[i - 1][j].isWall()) {
					tileImage = tileImages[CarMazeCityTileType.STRAIGHT_VERTICAL.ordinal()];
				} else if (i > 0 && j > 0 && !tiles[i-1][j].isWall() && !tiles[i][j-1].isWall()) {
					tileImage = tileImages[CarMazeCityTileType.RIGHT_ANGLE.ordinal()];
					flipX = true;
				} else if (i > 0 && j < size - 1 && !tiles[i - 1][j].isWall() && !tiles[i][j + 1].isWall()) {
					tileImage = tileImages[CarMazeCityTileType.RIGHT_ANGLE.ordinal()];
				} else if (i < size - 1 && j < size - 1 && !tiles[i + 1][j].isWall() && !tiles[i][j + 1].isWall()) {
					tileImage = tileImages[CarMazeCityTileType.RIGHT_ANGLE.ordinal()];
					flipY = true;
				} else {
					tileImage = tileImages[CarMazeCityTileType.RIGHT_ANGLE.ordinal()];
					flipX = true;
					flipY = true;
				}
				
				break;
				
			/* 3 - T intersection! */
			case 3:
				if (j == size - 1 || (j < size - 1 && tiles[i][j + 1].isWall())) {
					tileImage = tileImages[CarMazeCityTileType.THREE_INTERSECT_VERTICAL.ordinal()];
				} else if (j == 0 || (j > 0 && tiles[i][j - 1].isWall())) {
					tileImage = tileImages[CarMazeCityTileType.THREE_INTERSECT_VERTICAL.ordinal()];
					flipX = true;
				} else if (i == size - 1 || (i < size - 1 && tiles[i + 1][j].isWall())) {
					tileImage = tileImages[CarMazeCityTileType.THREE_INTERSECT_HORIZONTAL.ordinal()];
				} else if (i == 0 || (i > 0 && tiles[i - 1][j].isWall())) {
					tileImage = tileImages[CarMazeCityTileType.THREE_INTERSECT_HORIZONTAL.ordinal()];
					flipY = true;
				}
				break;
				
			/* four neighbours is the easiest case - big intersection! */
			case 4:
				tileImage = tileImages[CarMazeCityTileType.EVERYTHING.ordinal()];
				break;
			default:
				throw new RuntimeException("illegal number of neighbours: " + neighbours);
		}
		
		/* figure out how wide each tile is */
		int width = (int) this.mazeWidth / size;
		int height = (int) this.mazeHeight / size;
		
		/* figure out where on the screen we need to draw the tile */
		int x = j * width;
		int y = i * height;
		
		/* actually draw the image - employ some flipping if necessary  */
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
	
	private enum CarMazeCityTileType {
		EVERYTHING,
		THREE_INTERSECT_HORIZONTAL,
		THREE_INTERSECT_VERTICAL,
		RIGHT_ANGLE,
		STRAIGHT_HORIZONTAL,
		STRAIGHT_VERTICAL,
		DEAD_END_HORIZONTAL,
		DEAD_END_VERTICAL
	}

	@Override
	public void setTiles(MazeTile[][] tiles) {
		this.tiles = tiles;
	}

	/* the images used to tile the image */
	private final BufferedImage[] tileImages;
	private BufferedImage image;
	
	/* the tiles whose images are being set */
	private MazeTile[][] tiles;
	
	/* the width/height of the maze, in terms of tiles */
	private int size;
	
	/* width/height of the maze, in pixels */
	private int mazeHeight;
	private int mazeWidth;
}
