package maze.player;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

import maze.effect.MazeEffect;
import maze.game.MazeBasePanel;
import maze.game.MazeGameEffectPanel;

/**
 * Representation of a player within the game.
 */
public class MazePlayer {
	/**
	 * @param id
	 *            the id of the player
	 */
	public MazePlayer(int id, MazePlayerPanel mazePlayerPanel) {
		this.id = id;
		this.posX = 0;
		this.posY = 0;
		this.direction = MazePlayerDirection.RIGHT;
		this.speedModifier = 1.0;
		this.effectQueue = new LinkedList<MazeEffect>();
		this.mazePlayerPanel = mazePlayerPanel;
		this.ranking = UNRANKED;

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
	public MazePlayerDirection getDirection() {
		return direction;
	}

	/**
	 * @param dirX the dirX to set
	 */
	public void setDirection(MazePlayerDirection direction) {
		this.direction = direction;
	}

	/**
	 * activate the next MazeEffect in the queue
	 * 
	 * @param m
	 *            the maze the player is in
	 */
	public void activateNextMazeEffect(MazeBasePanel m) {
		if (!this.effectQueue.isEmpty()) {
			MazeEffect ef = this.effectQueue.remove();
			ef.activate(m.getMazeGamePanel(), this);
			if (ef.getEndTime() >= System.currentTimeMillis()) {
				m.getActivatedEffects().add(ef);
			}
			m.getMazeGamePanel().setEffectDisplay(new MazeGameEffectPanel(this, ef));
			this.mazePlayerPanel.updateBuffs(this.effectQueue);
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
		
		/* Gui - Draw effect to buffs display */
		this.mazePlayerPanel.updateBuffs(this.effectQueue);
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
		draw(g2d, width, height, true);
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
	 * @param inGame whether this is the in game drawing
	 */
	public void draw(Graphics2D g2d, int width, int height, boolean inGame) {
		/* clone car graphics instance */
		Graphics2D g = (Graphics2D) g2d.create();
		
		/* rotate based on where we are going */
		if (inGame) {
			Double rotateFactor = Math.PI / 2;			
			g.rotate(rotateFactor * this.direction.ordinal(), ((int) this.posX) + width/2, ((int) this.posY) + height/2);
		}
		
		/* draw as long as player is unranked */
		if (!inGame || this.ranking == UNRANKED) {
			g.drawImage(this.image, (int) this.posX, (int) this.posY, width, height, null);
		}
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

	/**
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @return the ranking
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	/* use this to denote a player's ranking if they haven't finished */
	private static final int UNRANKED = 0;
	
	/* maximum number of effects that a player can hold at one time */
	public static final int MAX_EFFECTS = 2;
	
	private int id;
	
	/* player's current position (in pixels) on the board */
	private double posX;
	private double posY;
	
	/* which direction the player is facing */
	private MazePlayerDirection direction;
	
	/* their speed multiplier: 1 represents a normal speed, higher represents
	 * a faster speed and lower represents a lower speed */
	private double speedModifier;
	
	/* the effects that a player is in possession of - should not
	 * have more than MAX_EFFECTS effects in the queue at one time */
	private Queue<MazeEffect> effectQueue;
	
	/* the image to display for this player */
	private BufferedImage image;
	
	private MazePlayerPanel mazePlayerPanel;
	
	/* what position they finished in - or UNRANKED if they haven't finished */
	private int ranking;
}
