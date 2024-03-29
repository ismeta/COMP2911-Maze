package maze.effect;

import java.awt.Graphics2D;
import java.awt.Image;

import maze.game.MazeGamePanel;
import maze.player.MazePlayer;

public interface MazeEffect {
	/**
	 * What the boost will do upon activation Note it must set the endTime
	 * 
	 * @param m
	 *            the Maze game
	 * @param p
	 *            the player who activated it
	 */
	public void activate(MazeGamePanel m, MazePlayer p);

	/**
	 * @return when the boost, upon activation, should deactivate (in
	 *         milliseconds)
	 */
	public long getEndTime();

	/**
	 * @param add
	 *            the amount of time in milliseconds to add
	 */
	public void addEndTime(long add);

	/**
	 * What the boost will do upon deactivation
	 * 
	 * @param m
	 *            the Maze game
	 */
	public void deactivate(MazeGamePanel m);

	/**
	 * Draws the boost
	 * 
	 * @param g2d
	 *            the graphics class to use to draw
	 * @param x
	 *            xCoord
	 * @param y
	 *            yCoord
	 * @param width
	 *            width to draw
	 * @param height
	 *            height to draw
	 */
	public void draw(Graphics2D g2d, int x, int y, int width, int height);

	/**
	 * @return the image of the effect
	 */
	public Image getImage();
}
