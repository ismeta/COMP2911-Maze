package maze;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class MazeTile {
	private boolean isWall;
	private boolean isGoal;
	
	public MazeTile() {
		this.isWall = false;
		this.isGoal = false;
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
		g2d.setPaint(this.isWall ? Color.BLACK : Color.white);
		g2d.fill(new Rectangle(x, y, width, height));
	}
}
