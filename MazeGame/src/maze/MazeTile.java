package maze;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import maze.effect.MazePlayerEffect;


public class MazeTile {
	private boolean isWall;
	private boolean isGoal;
	/* effect stored on maze */
	private MazePlayerEffect effect;
	
	public MazeTile() {
		this.isWall = false;
		this.isGoal = false;
		this.effect = null;
	}

	/**
	 * @return the effect
	 */
	public MazePlayerEffect getEffect() {
		return effect;
	}

	/**
	 * @param effect the effect to set
	 */
	public void setEffect(MazePlayerEffect effect) {
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
	
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		// Random r = new Random(); //  new Color(r.nextInt(0xFD), r.nextInt(0xFD), r.nextInt(0xFD))
		// draw tile
		g2d.setPaint(this.isWall ? Color.BLACK : Color.white);
		g2d.fill(new Rectangle(x, y, width, height));
		// draw effect on top
		if (effect != null) {
			// draw boost if required
			effect.draw(g2d, x, y, width, height);
		}
	}
}
