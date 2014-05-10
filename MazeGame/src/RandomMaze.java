import java.util.ArrayList;
import java.util.Random;

/**
 * This implementation of MazeGenerator employs the below strategy:
 * 
 * - Separates the tiles into three sets:
 * -- start tiles, which are connected to the beginning tile *only*
 * -- end tiles, which are connected to the goal tile *only*
 * -- unvisited tiles, which are not connected to any other tile
 * - In our implementation, we only need to really keep a list of tiles that we 
 *   want to investigate. This keeps memory required down.
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
 * A limitation of this class is that the size must be odd, and the provided
 * 2D array of tiles must all be nulls. The outer rows and columns will all be walls.
 * 
 * @author Vanessa
 */
public class RandomMaze implements MazeGenerator {
	public void generateMaze(MazeTile[][] tiles, int size) {
		assert(size > 2 && size % 2 == 1);
		assert(tiles != null);
		
		Point startPoint = new Point(0, 0);
		Point goalPoint  = new Point(size - 1, size - 1);
		
		/* Keeps track of which set each tile belongs to. */
		boolean[][] inStartSet = new boolean[size][size];
		inStartSet[0][0] = true;
		inStartSet[size - 1][size - 1] = false;
		
		/* There are several ways we can keep track of what 
		 * tile to visit. Here, we simply keep a list of tiles
		 * we can visit and randomly pick from it (checking if it's
		 * been visited already). Tiles are added to this list
		 * after a tile is explored. It's not efficient to clean up
		 * this list.
		 */
		ArrayList<PointState> tilesToVisit = new ArrayList<PointState>();
		
		/* Add the tiles adjacent to our start and end tiles to our
		 * tiles that we can visit.
		 */
		tilesToVisit.add(new PointState(
				new Point(startPoint.x + 2, startPoint.y), startPoint));
		tilesToVisit.add(new PointState(
				new Point(startPoint.x, startPoint.y + 2), startPoint));
		tilesToVisit.add(new PointState(
				new Point(goalPoint.x - 2, goalPoint.y), goalPoint));
		tilesToVisit.add(new PointState(
				new Point(goalPoint.x, goalPoint.y - 2), goalPoint));
		
		/* Don't forget to initialise our start/end tiles :P */
		tiles[0][0] = new MazeTile(false);
		tiles[size - 1][size - 1] = new MazeTile(false);
		
		/* Finally, we need a counter of how many tiles we need to visit
		 * so we know when we should stop. We could have a list of all the
		 * unvisited tiles, but this is less efficient.
		 * 
		 * For a tile, there should be walls all around it. The tile next to
		 * it will be the same, so they share a common wall:
		 * - tile - tile - tile -
		 * (where a dash represents a wall) Thus for n tiles,
		 * there should be n + 1 walls in total in one dimension. So to retrieve
		 * the number of tiles we want to visit, we halve the actual maze size.
		 * (We should actually subtract one first, but since it rounds down it 
		 * doesn't matter)
		 * We square this number as it's a two dimensional maze 
		 * (one dimensional? how even), and subtract two as we have already 
		 * visited the start/goal tiles.
		 */
		int unvisitedTiles = (((size / 2) + 1) * ((size / 2) + 1)) - 2;
		System.out.println(unvisitedTiles + " tiles you need to visit");
	
		/* Phase 1: randomly visit tiles until they have all been visited. */
		Random rand = new Random();
		PointState pointToCheck;
		Point p, prev;

		/* we need this list for later - this stores all the possible locations
		 * where we could possibly remove a wall to join tiles from the start
		 * and goal sets.
		 */
		ArrayList<Point> boundaries = new ArrayList<Point>();
		
		while (unvisitedTiles > 0) {
			/* Randomly determine which tile to check out */
			int tileToRemove = rand.nextInt(tilesToVisit.size());
			
			pointToCheck = tilesToVisit.remove(tileToRemove);
			prev = pointToCheck.previous;
			p = pointToCheck.current;
			
			/* Some tile checks:
			 * - If this tile is out of bounds, don't even try! 
			 * - If this tile has actually been visited before, we don't
			 *   worry about it; but first we check if it's a state different
			 *   to what this is. If it's in the goal set and we're in the start
			 *   set, then we could potentially add a wall between the two. 
			 *   (and vice versa)
			 */
			if (p.x < 0 || p.y < 0 || p.x >= size || p.y >= size) {
				continue;
			} else if (tiles[p.x][p.y] != null) {
				if (inStartSet[p.x][p.y] != inStartSet[prev.x][prev.y]) {
					/* add potentially removable wall to the set - the wall we can
					 * remove is determined by the midpoint of our two tiles because,
					 * well, it's in between our two tiles. */
					boundaries.add(new Point((p.x + prev.x) / 2, (p.y + prev.y) / 2));
				}
				continue;
			}
			
			/* otherwise, make this a not-wall as well as the wall joining
			 * it to the previous node
			 */
			tiles[p.x][p.y] = new MazeTile(false);
			
			/* the tile between this and the previous point can be fetched
			 * by taking the midpoint of their coordinates.
			 */
			tiles[(prev.x + p.x) / 2][(prev.y + p.y) / 2] = new MazeTile(false);
			
			/* update which set this tile belongs to (just whatever the previous
			 * tile belonged to)
			 */
			inStartSet[p.x][p.y] = inStartSet[prev.x][prev.y];
			
			/* add the tiles around it to the to-be-visited list */
			tilesToVisit.add(new PointState(new Point(p.x + 2, p.y), p));
			tilesToVisit.add(new PointState(new Point(p.x, p.y + 2), p));
			tilesToVisit.add(new PointState(new Point(p.x - 2, p.y), p));
			tilesToVisit.add(new PointState(new Point(p.x, p.y - 2), p));
			
			/* aaaand don't forget to update our condition */
			unvisitedTiles--;
		}
		
		/* Phase 2: remove n walls between the start and goal tiles, where
		 * n is a number of our choosing.
		 */
		Point wallToRemove;
		int wallsToRemove = 4;
		
		System.out.println(boundaries.size() + " potential boundaries");
		while (wallsToRemove > 0) {
			/* remove a random wall */
			wallToRemove = boundaries.remove(rand.nextInt(boundaries.size()));
			tiles[wallToRemove.x][wallToRemove.y] = new MazeTile(false);
			wallsToRemove--;
		}
		
		/* Phase 3: Fill out all the uninitialised tiles as walls. */
		/* Stop bitching at my British English spelling, Eclipse...*/
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (tiles[i][j] == null) {
					tiles[i][j] = new MazeTile(true);
				}
			}
		}
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
		 
		public final int x;
		public final int y;
	}
	
	/**
	 * Another helper class to store state in our random search.
	 */
	private class PointState {
		/**
		 * @param current the coordinates of the current node.
		 * @param previous the coordinates of the node it was added from.
		 */
		public PointState(Point current, Point previous) {
			this.current  = current;
			this.previous = previous; 
		}
		
		public final Point current;
		public final Point previous;
	}
}
