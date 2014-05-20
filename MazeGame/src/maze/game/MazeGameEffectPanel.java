package maze.game;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import maze.effect.MazeEffect;
import maze.player.MazePlayer;

public class MazeGameEffectPanel {
	private static int DURATION = 2500;
	private static int PROPORTION = 4;
	private MazePlayer player;
	private MazeEffect effect;
	private long startTime;
	
	/**
	 * @param player
	 * @param effect
	 * @param finishTime
	 */
	public MazeGameEffectPanel(MazePlayer player, MazeEffect effect) {
		this.player = player;
		this.effect = effect;
		this.startTime = System.currentTimeMillis();		
	}

	public void paint(Graphics g, int width, int height) {
		Graphics2D g2d = (Graphics2D) g.create();
		// transparent - fades out over time
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (0.6f - ((System.currentTimeMillis() - this.startTime) / (float) DURATION) * 0.6f)));
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
}
