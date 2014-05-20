/**
 * 
 */
package maze.effect;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import maze.game.MazeTile;
import maze.game.MazeGamePanel;
import maze.game.MazePlayer;

/**
 * @author ness
 *
 */
public class EgalitarianEffect implements MazeEffect {

	public EgalitarianEffect() {
		try{
			this.image = ImageIO.read(new File(IMAGE_FILE));
		} catch (IOException e) {
			throw new RuntimeException(IMAGE_FILE + " missing!");
		}
	}

	@Override
	public void activate(MazeGamePanel m, MazePlayer p) {
		this.endTime = System.currentTimeMillis() + EFFECT_DURATION;

		ArrayList<MazePlayer> winningPlayers = getWinningPlayers(m);
		
		for (MazePlayer player : m.getMazePlayers()) {
			
		}
	}
	
	/**
	 * @param m
	 * @return the players closest (in terms of distance through the maze, not
	 * Manhattan distance) to the finishing tile.
	 */
	private ArrayList<MazePlayer> getWinningPlayers (MazeGamePanel m) {
		MazeTile[][] maze = m.getMazeTiles();
		
		/* First we find out where the goal tile is */
		int goalX = 0, goalY = 0;
		int i = 0, j = 0;
		boolean goalFound = false;
		
		while (i < m.getMazeSize() && !goalFound) {
			j = 0;
			while (j < m.getMazeSize()) {
				if (maze[i][j].isGoal()) {
					goalX = i;
					goalY = j;
					
					goalFound = true;
					break;
				}
				j++;
			}
			i++;
		}
		
		/* We then BFS until we find a player. 
		 * If we find a player, then we check the rest of the positions
		 * equally far from the goal state; if they also contain other players,
		 * then we also want to affect these players. */
		ArrayList<BFSState> unvisitedTiles = new ArrayList<BFSState>();
		ArrayList<MazePlayer> winningPlayers = new ArrayList<MazePlayer>();
		
		unvisitedTiles.add(new BFSState(goalX, goalY, 0));
		
		/* we set this to true if we've finishing finding players close
		 * to the finish - we could prematurely finish as we don't really
		 * want to explore the entire maze.	 */
		boolean done = false;
		BFSState current;
		int closestDistance;
		
		while (!unvisitedTiles.isEmpty() && !done) {
			/* pop off the next closest tile */
			current = unvisitedTiles.remove(0);
			
			for (MazePlayer p : m.getMazePlayers()) {
				/* check if there is a player on this tile */
				if (current.x == p.getPosX() && current.y == p.getPosY()) {
					
				}
			}
		}
		
		return winningPlayers;
	}

	/* (non-Javadoc)
	 * @see maze.effect.MazeEffect#getEndTime()
	 */
	@Override
	public long getEndTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see maze.effect.MazeEffect#deactivate(maze.game.MazeGamePanel)
	 */
	@Override
	public void deactivate(MazeGamePanel m) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see maze.effect.MazeEffect#draw(java.awt.Graphics2D, int, int, int, int)
	 */
	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.drawImage(this.image, x, y, width, height, null);
	}

	/* (non-Javadoc)
	 * @see maze.effect.MazeEffect#getImage()
	 */
	@Override
	public Image getImage() {
		return this.image;
	}
	
	/**
	 * State to help flood fill the board when determining the players
	 * closest to the goal tile.
	 * @author Vanessa
	 */
	private class BFSState {
		/* its (x, y) coordinates on the maze grid */
		public int x, y;
		
		/* its distance in squares from the end */
		public int distance;
		
		public BFSState(int x, int y, int distance) {
			this.x = x;
			this.y = y;
			this.distance = distance;
		}
	}

	private static final String IMAGE_FILE = "images/sprites/egalitarian.png";
	private static final long EFFECT_DURATION = 10000;
	
	private long endTime;
	private Image image;
}
