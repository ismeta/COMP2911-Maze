import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;


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
		Random r = new Random();
		g2d.setPaint(this.isWall ? new Color(r.nextInt(0xFD), r.nextInt(0xFD), r.nextInt(0xFD)) : Color.white);
		g2d.fill(new Rectangle(x, y, width, height));
	}
}
