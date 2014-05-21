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
	 * @return the effect
	 */
	public MazeEffect getEffect() {
		return effect;
	}

	/**
	 * @param effect the effect to set
	 */
	public void setEffect(MazeEffect effect) {
		this.effect = effect;
	}

	/**
	 * @return the isGoal
	 */
	public boolean isGoal() {
		return isGoal;
	}

	/**
	 * @param isGoal the isGoal to set
	 */
	public void setGoal(boolean isGoal) {
		this.isGoal = isGoal;
	}

	/**
	 * @return the isWall
	 */
	public boolean isWall() {
		return isWall;
	}

	/**
	 * @param isWall the isWall to set
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
	 * @param isStart the isStart to set
	 */
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		if (this.isGoal) {
			g2d.setPaint(Color.CYAN);
			g2d.fill(new Rectangle(x, y, width, height));
		}
		// draw effect on top
		if (effect != null) {
			// draw boost if required
			effect.draw(g2d, x, y, width, height);
		}
		
	}
	
	private boolean isWall;
	private boolean isGoal;
	private boolean isStart;
	
	/* effect on this tile */
	private MazeEffect effect;
}
