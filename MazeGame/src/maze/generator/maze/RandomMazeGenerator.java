package maze.generator.maze;

import java.util.ArrayList;
import java.util.Random;

import maze.game.MazeTile;
import maze.generator.boost.BoostGenerator;
import maze.generator.boost.RandomBoostGenerator;

/**
 * LIMITATIONS:
 * 
 * A limitation of this class is that the size must be odd, and the provided
 * 2D array of tiles must all be nulls.
 * 
 * ALGORITHM
 * 
 * tl;dr Randomly generated. (Neither BFS or DFS; the degree of BFS/DFS
 * at each iteration is randomly determined.)
 * 
 * This implementation of MazeGenerator employs the below strategy:
 * 
 * - Separates the tiles into three sets:
 * -- start tiles, which are connected to the beginning tile *only*
 * -- end tiles, which are connected to the goal tile *only*
 * -- unvisited tiles, which are not connected to any other tile
 * - In our implementation, we only need to really keep a list of tiles that we 
 *   want to investigate. This keeps memory required down.
 * 
 * - We pick a random tile that we want to investigate, and check if it's been
 *   visited. If not, then we add it to the relevant set. We then add all its 
 *   surrounding tiles to the list of tiles to investigate.
 *   
 * - We also keep track of the set that the tiles belong to. A tile is added to 
 *   the same set that the tile before it belongs to. If a tile is explored that
 *   already exists, then we check if it is in a different set from the tile
 *   before it. If so, we add it to the list of possible walls to remove for 
 *   the next step:
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
public class RandomMazeGenerator implements MazeGenerator {
	public RandomMazeGenerator(int size) {
		assert(size % 2 == 1 && size > 2);
		this.size   = size;
		this.startP = new Point(0, 0);
		this.goalP  = new Point(size - 1, size - 1);
		this.difficulty = 10;
	}
	
	@Override
	public void generateMaze(MazeTile[][] tiles) {
		/* some checks: make sure we aren't handed lame, and that
		 * the start/end tiles aren't the same!
		 */
		assert(tiles != null);
		assert(startP.x != goalP.x || startP.y != goalP.y);
		
		/* Keeps track of which set each tile belongs to. */
		boolean[][] inStartSet = new boolean[size][size];
		inStartSet[startP.x][startP.y] = true;
		inStartSet[goalP.x][goalP.y]   = false;
		
		/* There are several ways we can keep track of what 
		 * tile to visit. Here, we simply keep a list of tiles
		 * we can visit and randomly pick from it (checking if it's
		 * been visited already). Tiles are added to this list
		 * after a tile is explored. It's not efficient to clean up
		 * this list.
		 */
		ArrayList<PointState> tilesToVisit = new ArrayList<PointState>();

		/* Only allow one path from the end (i.e. can't go on after reaching
		 * the end tile
		 */
		Random rand = new Random();
		
		Point[] possibleEndPaths = { 
				new Point(goalP.x - 2, goalP.y),
				new Point(goalP.x + 2, goalP.y),
				new Point(goalP.x, goalP.y + 2),
				new Point(goalP.x, goalP.y - 2)
		};
		
		/* randomly add a legal tile to the list of tiles to visit */
		while (true) {
			Point next = possibleEndPaths[rand.nextInt(possibleEndPaths.length)];
			if (next.x < 0 || next.y < 0 || next.x >= size || next.y >= size) {
				continue;
			} else {
				tilesToVisit.add(new PointState(next, goalP));
				break;
			}
		}
		
		Point[] possibleStartPaths = { 
				new Point(startP.x - 2, startP.y),
				new Point(startP.x + 2, startP.y),
				new Point(startP.x, startP.y + 2),
				new Point(startP.x, startP.y - 2)
		};
		
		/* randomly add a legal tile to the list of tiles to visit - 
		 * check that it's not close to the other tile in the list
		 */
		while (true) {
			Point next = possibleStartPaths[rand.nextInt(possibleStartPaths.length)];
			if (next.x < 0 || next.y < 0 || next.x >= size || next.y >= size) {
				continue;
			} else if (tilesToVisit.get(0).current.x == next.x 
					&& tilesToVisit.get(0).current.y == next.y) {
				continue;
			} else {
				tilesToVisit.add(new PointState(next, startP));
				break;
			}
		}
		
		
		/* Don't forget to initialise our start/end tiles :P */
		tiles[startP.x][startP.y] = new MazeTile();
		tiles[startP.x][startP.y].setStart(true);
		tiles[goalP.x][goalP.y]   = new MazeTile();
		tiles[goalP.x][goalP.y].setGoal(true);
		
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
		
		/* Phase 1: randomly visit tiles until they have all been visited. */
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
			 * it to the previous node */
			tiles[p.x][p.y] = new MazeTile();
			
			/* the tile between this and the previous point can be fetched
			 * by taking the midpoint of their coordinates.	 */
			tiles[(prev.x + p.x) / 2][(prev.y + p.y) / 2] = new MazeTile();
			
			/* update which set this tile belongs to (just whatever the previous
			 * tile belonged to) */
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
		 * n is a number of our choosing. */
		Point wallToRemove;
		
		/* This is a naive measure of difficulty - we should change this later */
		int wallsToRemove = 11 - difficulty;
		
		while (wallsToRemove > 0 && boundaries.size() > 0) {
			/* remove a random wall */
			wallToRemove = boundaries.remove(rand.nextInt(boundaries.size()));
			
			tiles[wallToRemove.x][wallToRemove.y] = new MazeTile();
			wallsToRemove--;
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
	
	private final int size;
	private int difficulty;
	private Point startP;
	private Point goalP;
}
