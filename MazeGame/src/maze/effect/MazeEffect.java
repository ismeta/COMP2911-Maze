package maze.effect;
import java.awt.Graphics2D;

import maze.Maze;
import maze.MazePlayer;



public interface MazeEffect {
	/**
	 * What the boost will do upon activation
	 * Note it must set the endTime
	 * @param m the Maze game
	 * @param p the player who activated it
	 */
	public void activate(Maze m, MazePlayer p);
	
	/**
	 * @return when the boost, upon activation, will deactivate
	 */
	public long getEndTime();
	
	/**
	 * What the boost will do upon deactivation
	 * @param m the Maze game
	 */
	public void deactivate(Maze m);
	
	/**
	 * Draws the boost
	 * @param g2d the graphics class to use to draw
	 * @param x xCoord
	 * @param y yCoord
	 * @param width width to draw
	 * @param height height to draw
	 */
	public void draw(Graphics2D g2d, int x, int y, int width, int height);
}
