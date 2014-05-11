import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;


public class MazePlayer {

	private int id;
	private double posX;
	private double posY;
	private Color color;
	
	/**
	 * @param id
	 * @param color
	 * @param size
	 */
	public MazePlayer(int id, Color color) {
		this.id = id;
		this.posX = 0;
		this.posY = 0;
		this.color = color;
	}
	
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	public void draw(Graphics2D g2d, int width, int height) {		
		g2d.setColor(color);
		g2d.fill(new Ellipse2D.Double(posX,  posY, width, height));
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the posX
	 */
	public double getPosX() {
		return posX;
	}

	/**
	 * @param posX the posX to set
	 */
	public void setPosX(double posX) {
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public double getPosY() {
		return posY;
	}

	/**
	 * @param posY the posY to set
	 */
	public void setPosY(double posY) {
		this.posY = posY;
	}	
}
