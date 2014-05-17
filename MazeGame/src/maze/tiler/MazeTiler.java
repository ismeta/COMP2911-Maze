package maze.tiler;

import java.awt.image.BufferedImage;

import maze.game.MazeTile;

public interface MazeTiler {
	/**
	 * tiles the maze, given the necessary arguments
	 */
	public void tileMaze();
	
	/**
	 * get the image of a tiled maze.  
	 * tileMaze() must be called first
	 * @return the image of the maze
	 */
	public BufferedImage getImage();
	
	/**
	 * @param tiles tiles to set
	 */
	public void setTiles(MazeTile[][] tiles);
}
