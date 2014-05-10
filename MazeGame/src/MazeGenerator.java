
public interface MazeGenerator {
	/**
	 * Given a two dimensional array of empty tiles, generates
	 * a maze and modifies the tiles accordingly.
	 * @param tiles the tiles to be modified to form a maze.
	 * @param size the number of tiles heightwise and lengthwise
	 * of the maze (assumes a square maze). Must be greater than 2.
	 */
	public void generateMaze(MazeTile[][] tiles, int size);
}
