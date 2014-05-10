import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class MazePlayer {

	private int id;
	private double posX;
	private double posY;
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
		g2d.fill(new Rectangle((int) posX, (int) posY, width, height));
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
