import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Random;


public class MazePlayer {

	private int id;
	private int posX;
	private int posY;
	private Color color;
	private double speed;
	
	
	/**
	 * @param id
	 * @param color
	 * @param speed
	 */
	public MazePlayer(int id, Color color, double speed) {
		this.id = id;
		this.posX = 0;
		this.posY = 0;
		this.color = color;
		this.speed = speed;
	}
	
	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	public void draw(Graphics2D g2d, int width, int height) {		
		g2d.setColor(color);
		g2d.fill(new Ellipse2D.Double(posX, posY, width, height));
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
	public int getPosX() {
		return posX;
	}

	/**
	 * @param posX the posX to set
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * @param posY the posY to set
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}	
}
