package maze;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.LinkedList;
import java.util.Queue;

import maze.effect.MazeEffect;


public class MazePlayer {
	public static final int MAX_EFFECTS = 2;
	private int id;
	private double posX;
	private double posY;
	private Color color;
	private double speedModifier;
	private Queue<MazeEffect> effectQueue;
	
	/**
	 * @param id the id of the player
	 * @param color the color to draw the player
	 */
	public MazePlayer(int id, Color color) {
		this.id = id;
		this.posX = 0;
		this.posY = 0;
		this.color = color;
		this.speedModifier = 1.0;
		this.effectQueue = new LinkedList<MazeEffect>();
	}
	
	/**
	 * activate the next MazeEffect in the queue
	 * @param m the maze the player is in
	 */
	public void activateNextMazeEffect(Maze m) {
		if (!this.effectQueue.isEmpty()) {
			MazeEffect ef = this.effectQueue.remove();
			ef.activate(m, this);
			if (ef.getEndTime() >= System.currentTimeMillis()) {
				m.getActivatedEffects().add(ef);
			}
		}
	}
	
	/**
	 * Adds to a player's hand a MazeEffect
	 * @param mf the MazeEffect to add
	 */
	public void addMazeEffect(MazeEffect mf) {
		if (this.effectQueue.size() < MAX_EFFECTS) {
			this.effectQueue.add(mf);
		}
	}
	
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
		/**
	 * @return the speedModifier
	 */
	public double getSpeedModifier() {
		return speedModifier;
	}

	/**
	 * @param speedModifier the speedModifier to set
	 */
	public void setSpeedModifier(double speedModifier) {
		this.speedModifier = speedModifier;
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
