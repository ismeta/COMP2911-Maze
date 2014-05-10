import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class MazeTile {
	private boolean isWall;
	
	/**
	 * @param isWall
	 */
	public MazeTile(boolean isWall) {
		this.isWall = isWall;
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
		g2d.setPaint(this.isWall ? Color.black : Color.white);
		g2d.fill(new Rectangle(x, y, width, height));
	}
}
