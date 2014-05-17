package maze.generator;
import maze.game.MazeTile;


public interface MazeGenerator {
	/**
	 * Given a two dimensional array of empty tiles, generates
	 * a maze and modifies the tiles accordingly.
	 * @param tiles the tiles to be modified to form a maze.
	 * @param size the number of tiles heightwise and lengthwise
	 * of the maze (assumes a square maze). Must be greater than 2.
	 */
	public void generateMaze(MazeTile[][] tiles);
	
	/**
	 * This function can be optionally called before generateMaze() is
	 * called. It sets the start tile for the maze. Note x and y must be
	 * even numbers, and between 0 and size - 1, inclusive. size is not
	 * specified here, but in the generateMaze call. (0, 0) refers to the
	 * leftmost uppermost tile. It must be different from the end tile,
	 * if it is specified! 
	 * 
	 * The default start tile is (0, 0).
	 * @param x the x coordinate for the start tile.
	 * @param y the y coordinate for the start tile.
	 */
	public void setStartTile(int x, int y);
	
	/**
	 * This function can be optionally called before generateMaze() is
	 * called. It sets the end tile for the maze. Note x and y must be
	 * even numbers, and between 0 and size - 1, inclusive. size is not
	 * specified here, but in the generateMaze call. (0, 0) refers to the
	 * leftmost uppermost tile. The end tile must be different from the
	 * start tile! 
	 * 
	 * The default end tile is (size - 1, size - 1).
	 * @param x the x coordinate for the end tile.
	 * @param y the y coordinate for the end tile.
	 */
	public void setEndTile(int x, int y);
	
	/**
	 * This function can be optionally called before generateMaze().
	 * 1 represents the least difficulty and 10 represents the highest
	 * difficulty. If not called, the default difficulty is 5.
	 * @param difficulty the difficulty of the maze to be generated;
	 * must be between 1 and 10, inclusive.
	 */
	public void setDifficulty(int difficulty);
}
