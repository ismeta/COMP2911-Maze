package maze.tiler;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import maze.game.MazeTile;

public class CarCityMazeTiler implements MazeTiler {
	private final BufferedImage[] tileImages;
	
	private BufferedImage image;
	private MazeTile[][] tiles;
	private int size;
	private int tileHeight;
	private int tileWidth;
	
	/**
	 * @param tiles
	 * @param size
	 * @param height
	 * @param width
	 */
	public CarCityMazeTiler(MazeTile[][] tiles, int size, int height, int width) {
		this.tiles = tiles;
		this.size = size;
		this.tileHeight = height;
		this.tileWidth = width;
		
		this.tileImages = new BufferedImage[CarMazeCityTileType.values().length];
		for (int i = 0; i < CarMazeCityTileType.values().length; i++) {
			try {
				tileImages[i] = ImageIO.read(new File("images/tiles/tile_" + i + ".png"));
			} catch (IOException e) {
				throw new RuntimeException("Tile images missing!");
			}
		}
	}	
	
	@Override
	public void tileMaze() {
		this.image = new BufferedImage(this.tileWidth, this.tileHeight, BufferedImage.TYPE_INT_ARGB);
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
	@Override
	public BufferedImage getImage() {
		return this.image;
	}
	
	private void setWallImage(Graphics2D imageG2D, int i, int j) {
		int width = this.tileWidth / size;
		int height = this.tileHeight / size;
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
			case 4:
				tileImage = tileImages[CarMazeCityTileType.EVERYTHING.ordinal()];
				break;
			default:
				throw new RuntimeException("you fucked up");
		}
		
		int width = (int) this.tileWidth / size;
		int height = (int) this.tileHeight / size;
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
}
