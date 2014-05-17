package maze.game;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import maze.effect.MazeEffect;


public class MazeTile {
	private boolean isWall;
	private boolean isGoal;
	/* effect stored on maze */
	private MazeEffect effect;
	
	public MazeTile() {
		this.isWall = false;
		this.isGoal = false;
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
	
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g.create();
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
}
