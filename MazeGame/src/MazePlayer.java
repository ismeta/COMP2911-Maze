import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public class MazePlayer {

	private int id;
	private int posX;
	private int posY;
	
	/**
	 * @param id
	 */
	public MazePlayer(int id) {
		this.id = id;
		this.posX = 0;
		this.posY = 0;
	}
	
	public void draw(Graphics2D g2d, int width, int height) {
		switch (id) {
			case 0:
				g2d.setPaint(Color.red);
				break;
			case 1:
				g2d.setPaint(Color.green);
				break;
			default:
				g2d.setPaint(Color.blue);
				break;
		}
		g2d.fill(new Ellipse2D.Double(posX, posY, width, height));
	}
	
}
