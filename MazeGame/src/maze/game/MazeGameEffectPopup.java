package maze.game;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import maze.effect.MazeEffect;
import maze.player.MazePlayer;

public class MazeGameEffectPopup {
	/**
	 * @param player
	 * @param effect
	 * @param finishTime
	 */
	public MazeGameEffectPopup(MazePlayer player, MazeEffect effect) {
		this.player = player;
		this.effect = effect;
		this.startTime = System.currentTimeMillis();		
	}

	public void paint(Graphics g, int width, int height) {
		Graphics2D g2d = (Graphics2D) g.create();
		// transparent - fades out over time
		float maxTransparency = 0.7f;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (maxTransparency - ((System.currentTimeMillis() - this.startTime) / (float) DURATION) * maxTransparency)));
		int imageWidth = width/PROPORTION;
		int imageHeight = height/PROPORTION;
		// draw effect centrally on screen
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
	
	private static int DURATION = 2500;
	private static int PROPORTION = 4;
	private MazePlayer player;
	private MazeEffect effect;
	private long startTime;
	
}
