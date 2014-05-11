import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;


public class MazePlayer {

	private int id;
	private double posX;
	private double posY;
	private Color color;
	
	/**
	 * @param id the id of the player
	 * @param color the color to draw the player
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

	/**
	 * draws the player
	 * @param g2d what graphics class we use to draw
	 * @param width width of player
	 * @param height height of player
	 */
	public void draw(Graphics2D g2d, int width, int height) {
		Stroke previousStroke = g2d.getStroke();
		g2d.setStroke(previousStroke);
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
