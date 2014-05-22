package maze.effect;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import maze.game.MazeGamePanel;
import maze.player.MazePlayer;

/**
 * Effect which slows down every player except the player invoking the effect.
 */
public class GlobalSpeedDownEffect implements MazeEffect {
	public GlobalSpeedDownEffect() {
		try {
			this.image = ImageIO.read(new File(EFFECT_IMAGE));
		} catch (IOException e) {
			throw new RuntimeException(EFFECT_IMAGE + " missing!");
		}
	}

	/**
	 * put the effect into action
	 */
	@Override
	public void activate(MazeGamePanel m, MazePlayer p) {
		this.unaffectedPlayer = p;
		this.endTime = System.currentTimeMillis() + DURATION;
		// speed down other players
		for (MazePlayer mp : m.getMazePlayers()) {
			if (mp != p) {
				mp.setSpeedModifier(mp.getSpeedModifier() * SPEED_MODIFIER);
			}
		}
	}

	/**
	 * @return when the effect should end.
	 */
	@Override
	public long getEndTime() {
		return endTime;
	}

	@Override
	public void addEndTime(long add) {
		this.endTime += add;
	}

	/**
	 * Deactivate the effect
	 */
	@Override
	public void deactivate(MazeGamePanel m) {
		/* speed up other players */
		for (MazePlayer mp : m.getMazePlayers()) {
			if (mp != unaffectedPlayer) {
				mp.setSpeedModifier(mp.getSpeedModifier() / SPEED_MODIFIER);
			}
		}
	}

	/**
	 * Draw this effect at the specified coordinates.
	 */
	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.drawImage(this.image, x, y, width, height, null);
	}

	/**
	 * @return image for the effect.
	 */
	public Image getImage() {
		return this.image;
	}

	private static final String EFFECT_IMAGE = "images/sprites/slow.png";

	/* time that the effect should be in place - in milliseconds */
	private static final long DURATION = 10000;

	/* multiplier to multiply the player's speed by for this effect */
	private static final double SPEED_MODIFIER = 0.75;

	private long endTime;
	private MazePlayer unaffectedPlayer;
	private Image image;
}
