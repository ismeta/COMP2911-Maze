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
		try {
			this.image = ImageIO.read(new File(IMAGE_FILE));
		} catch (IOException e) {
			throw new RuntimeException(IMAGE_FILE + " missing!");
		}
		this.winningPlayers = new ArrayList<MazePlayer>();
	}

	@Override
	public void activate(MazeGamePanel m, MazePlayer p) {
		this.endTime = System.currentTimeMillis() + EFFECT_DURATION;

		setWinningPlayers(m, p);
		
		/* Slow down all the players who are winning! */
		for (MazePlayer player : this.winningPlayers) {
			/* We don't slow down ourselves :P */
			if (player.equals(p)) {
				continue;
			}
			player.setSpeedModifier(player.getSpeedModifier() * SPEED_MODIFIER);
		}
	}
	
	/**
	 * Determine the winning players (i.e. the players who are closest to the
	 * finishing tile) and set the variable accordingly.
	 * @param m
	 * @param p the player who activated this effect.
	 */
	private void setWinningPlayers (MazeGamePanel m, MazePlayer currentPlayer) {
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
		
		unvisitedTiles.add(new BFSState(goalX, goalY, 0));
		
		/* we set this to true if we've finishing finding players close
		 * to the finish - we could prematurely finish as we don't really
		 * want to explore the entire maze.	 */
		boolean done = false;
		
		BFSState current;
		int closestDistance = m.getMazeSize() * m.getMazeSize();
		int[] location = new int[2];
		
		/* check whether we've visited tiles before */
		boolean[][] seen = new boolean[m.getMazeSize()][m.getMazeSize()];
		
		while (!unvisitedTiles.isEmpty() && !done) {
			/* pop off the next closest tile */
			current = unvisitedTiles.remove(0);
			
			/* if we've already found the closest player, then we will have
			 * found the closest player distance to the goal tile. After that,
			 * we only want to check tiles of equal distance from the goal state
			 * and add any players on those - if we've exceeded the closest distance
			 * then there are no more players which are equally close, so we quit. */
			if (current.distance > closestDistance) {
				break;
			}
			
			/* check that we're within the right bounds */
			if (current.x < 0 || current.y < 0 
					|| current.x >= m.getMazeSize() || current.y >= m.getMazeSize()) {
				continue;
			}
			
			/* skip this if we've already seen it before */
			if (seen[current.x][current.y]) {
				continue;
			}
			
			for (MazePlayer p : m.getMazePlayers()) {
				/* we don't want to slow down the current player, so we skip them */
				if (p.equals(currentPlayer)) {
					continue;
				}
				
				/* get the player's x, y coordinates into the location array */
				m.getPlayerTileLocation(location, p);
				
				/* check if there is a player on this tile and if so,
				 * then we add them to the winning players list. We also set
				 * the minimum distance so that we can determine any other players
				 * equally close to the end. */
				if (current.x == location[0] && current.y == location[1]) {
					winningPlayers.add(p);
					closestDistance = current.distance;
				}
			}
			
			/* we've seen this tile now */
			seen[current.x][current.y] = true;
			
			/* Now we add all the tiles around it to the queue. */
			int newDistance = current.distance + 1;
			unvisitedTiles.add(
					new BFSState(current.x + 1, current.y, newDistance));
			unvisitedTiles.add(
					new BFSState(current.x - 1, current.y, newDistance));
			unvisitedTiles.add(
					new BFSState(current.x, current.y + 1, newDistance));
			unvisitedTiles.add(
					new BFSState(current.x, current.y - 1, newDistance));
		}
	}

	/* (non-Javadoc)
	 * @see maze.effect.MazeEffect#getEndTime()
	 */
	@Override
	public long getEndTime() {
		return this.endTime;
	}
	
	@Override
	public void addEndTime(long add) {
		this.endTime += add;
	}
	
	/* (non-Javadoc)
	 * @see maze.effect.MazeEffect#deactivate(maze.game.MazeGamePanel)
	 */
	@Override
	public void deactivate(MazeGamePanel m) {
		/* undo our changes to whoever our winning players were 8) */
		for (MazePlayer p : winningPlayers) {
			p.setSpeedModifier(p.getSpeedModifier() / SPEED_MODIFIER);
		}
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

	private static final String IMAGE_FILE = "images/sprites/equalise.png";
	private static final long EFFECT_DURATION = 10000;
	private static final double SPEED_MODIFIER = 0.5;
	
	private long endTime;
	private ArrayList<MazePlayer> winningPlayers;
	private Image image;
}
