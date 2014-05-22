package maze.game;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import maze.effect.MazeEffect;
import maze.player.MazePlayer;

/**
 * Popup to be displayed when the user uses an effect.
 */
public class MazeGameEffectPopup {
	/**
	 * @param player which player this effect belongs to
	 * @param effect the effect 
	 */
	public MazeGameEffectPopup(MazePlayer player, MazeEffect effect) {
		this.player = player;
		this.effect = effect;
		this.startTime = System.currentTimeMillis();		
	}

	/**
	 * draw the maze game effect popup at the specified width/height
	 * @param g
	 * @param width width of whatever this popup will be displayed in
	 * @param height height of whatever this popup will be displayed in
	 */
	public void paint(Graphics g, int width, int height) {
		Graphics2D g2d = (Graphics2D) g.create();
		
		/* transparent - fades out over time */
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 
				(MAX_TRANSPARENCY - ((System.currentTimeMillis() - this.startTime) / 
						(float) DURATION) * (MAX_TRANSPARENCY - MIN_TRANSPARENCY))));
		
		int imageWidth = width / PROPORTION;
		int imageHeight = height / PROPORTION;
		
		/* draw effect centrally on screen */
		g2d.setColor(new Color(45, 45, 45));
		g2d.fill(new Rectangle((width) / 2 - imageWidth, (height) / 2 - imageHeight / 2, imageWidth * 2, imageHeight));
		g2d.drawImage(this.player.getImage(), (width) / 2 - imageWidth, (height) / 2 - imageHeight / 2, imageWidth, imageHeight, null);
		g2d.drawImage(this.effect.getImage(), (width) / 2, (height) / 2 - imageHeight / 2, imageWidth, imageHeight, null);
		g2d.dispose();
	}
	
	/**
	 * @return the player
	 */
	public MazePlayer getPlayer() {
		return player;
	}

	/**
	 * @return the effect
	 */
	public MazeEffect getEffect() {
		return effect;
	}

	/**
	 * @return the finishTime
	 */
	public long getFinishTime() {
		return startTime  + DURATION;
	}
	
	/* how long the popup should be displayed (in milliseconds) */
	private static int DURATION = 3000;
	
	/* maximum/minimum transparency of the popup */
	private static float MAX_TRANSPARENCY = 0.6f;
	private static float MIN_TRANSPARENCY = 0f;
	
	/* how much smaller the popup should be than the window it is displayed in */
	private static int PROPORTION = 4;
	
	/* the player using the effect which triggers the popup */
	private MazePlayer player;
	
	/* the effect */
	private MazeEffect effect;
	
	/* when the popup started */
	private long startTime;
}
