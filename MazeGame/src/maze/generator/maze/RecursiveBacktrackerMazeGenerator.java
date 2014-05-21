package maze.generator.maze;

import java.util.ArrayList;
import java.util.Random;

import maze.game.MazeTile;
import maze.generator.boost.BoostGenerator;
import maze.generator.boost.RandomBoostGenerator;

/**
 * "It's so easy"
 * - Oliver Tan, 2014
 * @author Vanessa
 */
public class RecursiveBacktrackerMazeGenerator implements MazeGenerator {
	public RecursiveBacktrackerMazeGenerator (int size) {
		assert(size > 2 && size % 2 == 1);
		this.size = size;
		this.startP = new Point(0, 0);
		this.goalP = new Point(size - 1, size - 1);
	}

	@Override
	public void generateMaze(MazeTile[][] tiles) {
		/* some checks: make sure we aren't handed bad input, and that
		 * the start/end tiles aren't the same!
		 */
		assert(tiles != null);
		assert(startP.x != goalP.x || startP.y != goalP.y);
		
		Random rand = new Random();
		
		/* Don't forget to initialise our start tile :P */
		tiles[startP.x][startP.y] = new MazeTile();
		tiles[startP.x][startP.y].setStart(true);
		
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
		int unvisitedTiles = (((size / 2) + 1) * ((size / 2) + 1)) - 1;
		
		/* Phase 1: randomly visit tiles until they have all been visited. */
		Point p, newP;

		/* we need this list for later - this stores all the possible locations
		 * where we could possibly remove a wall to join tiles from the start
		 * and goal sets.
		 */
		ArrayList<Point> previous = new ArrayList<Point>();
		previous.add(startP);
		
		while (unvisitedTiles > 0) {
			/* get the most recently visited tile */
			p = previous.remove(previous.size() - 1);
			
			/* Some tile checks:
			 * - If this tile is out of bounds, don't even try! 
			 * - If this tile has actually been visited before, we don't
			 *   worry about it; but first we check if it's a state different
			 *   to what this is. If it's in the goal set and we're in the start
			 *   set, then we could potentially add a wall between the two. 
			 *   (and vice versa)
			 */
			ArrayList<Point> neighbours = new ArrayList<Point>();
			neighbours.add(new Point(p.x + 2, p.y));
			neighbours.add(new Point(p.x - 2, p.y));
			neighbours.add(new Point(p.x, p.y - 2));
			neighbours.add(new Point(p.x, p.y + 2));
			
			ArrayList<Point> possibleNextPoints = new ArrayList<Point>();
			
			for (Point neighbour : neighbours) {
				if (neighbour.x < 0 || neighbour.y < 0 || neighbour.x >= size || neighbour.y >= size) {
					continue;
				} else if (tiles[neighbour.x][neighbour.y] == null) {
					possibleNextPoints.add(neighbour);
				}
			}
			
			if (!possibleNextPoints.isEmpty()) {
				newP = possibleNextPoints.get(rand.nextInt(possibleNextPoints.size()));
				previous.add(p);
				previous.add(newP);
				
				/* the tile between this and the previous point can be fetched
				 * by taking the midpoint of their coordinates.
				 */
				tiles[(newP.x + p.x) / 2][(newP.y + p.y) / 2] = new MazeTile();
			}
			
			/* otherwise, make this a not-wall as well as the wall joining
			 * it to the previous node
			 */
			
			/* aaaand don't forget to update our condition */
			if (tiles[p.x][p.y] == null) {
				unvisitedTiles--;
				tiles[p.x][p.y] = new MazeTile();
			}
		}
		
		/* Phase 3: Fill out all the uninitialised tiles as walls. */
		/* Stop bitching at my British English spelling, Eclipse...*/
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (tiles[i][j] == null) {
					tiles[i][j] = new MazeTile();
					tiles[i][j].setWall(true);
				}
			}
		}

		/* (optional) Phase 4: generate some boosts! */
		BoostGenerator bootGen = new RandomBoostGenerator(size);
		bootGen.generateBoosts(tiles);
	}
	
	/**
	 * This function can be optionally called before generateMaze() is
	 * called. It sets the start tile for the maze. Note x and y must be
	 * even numbers, and between 0 and size - 1, inclusive. size is not
	 * specified here, but in the generateMaze call. (0, 0) refers to the
	 * leftmost uppermost tile. It must be different from the end tile,
	 * if it is specified!
	 * @param x the x coordinate for the start tile.
	 * @param y the y coordinate for the start tile.
	 */
	@Override
	public void setStartTile(int x, int y) {
		assert(x >= 0 && x < size && x % 2 == 0);
		assert(y >= 0 && y < size && y % 2 == 0);
		
		this.startP = new Point(x, y);
	}
	
	/**
	 * This function can be optionally called before generateMaze() is
	 * called. It sets the end tile for the maze. Note x and y must be
	 * even numbers, and between 0 and size - 1, inclusive. size is not
	 * specified here, but in the generateMaze call. (0, 0) refers to the
	 * leftmost uppermost tile. The end tile must be different from the
	 * start tile!
	 * @param x the x coordinate for the end tile.
	 * @param y the y coordinate for the end tile.
	 */
	@Override
	public void setEndTile(int x, int y) {
		assert(x >= 0 && x < size && x % 2 == 0);
		assert(y >= 0 && y < size && y % 2 == 0);
		
		this.goalP = new Point(x, y);
	}
	
	@Override
	public void setDifficulty(int difficulty) {
		assert(difficulty >= 1 && difficulty <= 10);
		
		this.difficulty = difficulty;
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
		
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
		
		public final int x;
		public final int y;
	}
	
	private final int size;
	@SuppressWarnings("unused")
	private int difficulty;
	private Point startP;
	private Point goalP;
}
