package maze.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import maze.effect.MazeEffect;

/**
 * Represents a tile in the maze.
 */
public class MazeTile {
	public MazeTile() {
		this.isWall = false;
		this.isGoal = false;
		this.isStart = false;
		this.effect = null;
	}

	/**
	 * @return the effect on this tile
	 */
	public MazeEffect getEffect() {
		return effect;
	}

	/**
	 * @param effect
	 *            the effect to set
	 */
	public void setEffect(MazeEffect effect) {
		this.effect = effect;
	}

	/**
	 * @return whether this tile is a goal tile or not
	 */
	public boolean isGoal() {
		return isGoal;
	}

	/**
	 * @param isGoal
	 *            the isGoal to set
	 */
	public void setGoal(boolean isGoal) {
		this.isGoal = isGoal;
	}

	/**
	 * @return whether this tile is a wall or not
	 */
	public boolean isWall() {
		return isWall;
	}

	/**
	 * @param isWall
	 *            the isWall to set
	 */
	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}

	/**
	 * @return the isStart
	 */
	public boolean isStart() {
		return isStart;
	}

	/**
	 * @param isStart
	 *            the isStart to set
	 */
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	/**
	 * Draws this tile at the specified location with the specified height
	 * and width (in pixels).
	 * 
	 * @param g2d
	 * @param x
	 *            the x-coordinate (in pixels) on which to draw this tile
	 * @param y
	 *            the y-coordinate (in pixels) on which to start drawing this
	 *            tile
	 * @param width
	 *            the width of the drawn tile (pixels)
	 * @param height
	 *            the height of the drawn tile (pixels)
	 */
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		/* special graphic if it's a goal tile! */
		if (this.isGoal) {
			g2d.setPaint(Color.CYAN);
			g2d.fill(new Rectangle(x, y, width, height));
		}

		/* draw boost if required */
		if (effect != null) {
			effect.draw(g2d, x, y, width, height);
		}

	}

	private boolean isWall;
	private boolean isGoal;
	private boolean isStart;

	/* effect on this tile */
	private MazeEffect effect;
}
