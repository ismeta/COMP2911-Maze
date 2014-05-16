package maze;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import maze.effect.MazeEffect;


public class MazeTile {
	private boolean isWall;
	private boolean isGoal;
	private Image image;
	private boolean flipX;
	private boolean flipY;
	/* effect stored on maze */
	private MazeEffect effect;
	
	public MazeTile() {
		this.isWall = false;
		this.isGoal = false;
		this.effect = null;
		this.flipX = false;
		this.flipY = false;
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
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public void setRotation(boolean flipX, boolean flipY) {
		this.flipX = flipX;
		this.flipY = flipY;
	}
	
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D) g.create();
		if (this.isGoal) {
			g2d.setPaint(Color.CYAN);
			g2d.fill(new Rectangle(x, y, width, height));
		} else if (this.image == null){
			/*Random r = (new Random());
			Color c = new Color(r.nextInt(0xFD), r.nextInt(0xFD), r.nextInt(0xFD));*/ 
			// draw tile
			g2d.setPaint(this.isWall ? Color.BLACK : Color.GRAY);
			g2d.fill(new Rectangle(x, y, width, height));
		} else {
			if (flipX && !flipY) {
				g2d.drawImage(this.image, x + width, y, -width, height, null);
			} else if (flipX && flipY) {
				g2d.drawImage(this.image, x + width, y + height, -width, -height, null);
			} else if (!flipX && flipY) {
				g2d.drawImage(this.image, x, y + height, width, -height, null);
			} else {
				g2d.drawImage(this.image, x, y, width, height, null);
			}
		}
		// draw effect on top
		if (effect != null) {
			// draw boost if required
			effect.draw(g2d, x, y, width, height);
		}
		
	}
}
