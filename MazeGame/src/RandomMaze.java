/**
 * This implementation of MazeGenerator employs the below strategy:
 * 
 * - Separates the tiles into three sets:
 * -- start tiles, which are connected to the beginning tile *only*
 * -- end tiles, which are connected to the goal tile *only*
 * -- unvisited tiles, which are not connected to any other tile
 * 
 * - We randomly remove walls between tiles in the start/end tiles, and the unvisited
 *   tiles set. (never between tiles in the same set; this creates loops.) The
 *   tile uncovered by removing a wall is removed from the unvisited set, and
 *   added to the respective set.
 *   
 * - When we have no unvisited tiles, we pick an positive integer and remove
 *   that number of walls between the start and end tiles. (If we pick 0, then
 *   there will be no way to get from any tile in the start set to the end set.)
 *   
 * This algorithm was based off http://stackoverflow.com/a/22308159 and modified
 * to work with our implementation of maze tiles.
 * 
 * @author Vanessa
 */
public class RandomMaze implements MazeGenerator {
	public void generateMaze(MazeTile[][] tiles, int size) {
		assert(size > 2);
		assert(tiles != null);
		
		
	}
	
	/**
	 * Little helper class that we use to store points, so we can have
	 * a set of points in our RandomMaze implementation.
	 * @author Vanessa
	 */
	private class Point {
		public Point (int x, int y) {
			this.x = x;
			this.y = y;
		}
		 
		private final int x;
		private final int y;
	}
}
