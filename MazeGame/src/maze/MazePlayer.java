package maze;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

import maze.effect.MazeEffect;

public class MazePlayer {
	public static final int MAX_EFFECTS = 2;
	private int id;
	private double posX;
	private double posY;
	private int dirX;
	private int dirY;
	private Color color;
	private double speedModifier;
	private Queue<MazeEffect> effectQueue;
	private BufferedImage image;

	/**
	 * @param id
	 *            the id of the player
	 * @param color
	 *            the color to draw the player
	 */
	public MazePlayer(int id, Color color) {
		this.id = id;
		this.posX = 0;
		this.posY = 0;
		this.dirX = 0;
		this.dirY = 0;
		this.color = color;
		this.speedModifier = 1.0;
		this.effectQueue = new LinkedList<MazeEffect>();

		// generate transparent image
		try {
			this.image = ImageIO.read(new File(String.format(
					"images/sprites/player%d.png", id)));
		} catch (IOException e) {
			throw new RuntimeException("player image missing!");
		}
		
	}
	
	/**
	 * @return the dirX
	 */
	public int getDirX() {
		return dirX;
	}

	/**
	 * @param dirX the dirX to set
	 */
	public void setDirX(int dirX) {
		this.dirX = dirX;
	}

	/**
	 * @return the dirY
	 */
	public int getDirY() {
		return dirY;
	}

	/**
	 * @param dirY the dirY to set
	 */
	public void setDirY(int dirY) {
		this.dirY = dirY;
	}

	/**
	 * activate the next MazeEffect in the queue
	 * 
	 * @param m
	 *            the maze the player is in
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
	 * 
	 * @param mf
	 *            the MazeEffect to add
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
	 * @param speedModifier
	 *            the speedModifier to set
	 */
	public void setSpeedModifier(double speedModifier) {
		this.speedModifier = speedModifier;
	}

	/**
	 * draws the player
	 * 
	 * @param g2d
	 *            what graphics class we use to draw
	 * @param width
	 *            width of player
	 * @param height
	 *            height of player
	 */
	public void draw(Graphics2D g2d, int width, int height) {
		// clone car graphics instance
		Graphics2D g = (Graphics2D) g2d.create();
		// pls tranparency
		g.setComposite(AlphaComposite.Src);
		// rotate based on where we are going
		g.rotate(Math.toRadians(dirX * 90 + (dirY == 0 ? 0 : (dirY + 1) * 90)), (int) this.posX + width/2, (int) this.posY + height/2);		
		g.drawImage(this.image, (int) this.posX, (int) this.posY, width, height, null);
		g.dispose();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
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
	 * @param posX
	 *            the posX to set
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
	 * @param posY
	 *            the posY to set
	 */
	public void setPosY(double posY) {
		this.posY = posY;
	}
}
