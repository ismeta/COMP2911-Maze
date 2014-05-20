/**
 * 
 */
package maze.effect;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import maze.game.MazeGamePanel;
import maze.player.MazePlayer;

/**
 * Effect which increases the speed of the invoking player.
 * @author Oliver
 */
public class SelfSpeedUpEffect implements MazeEffect {
	
	public SelfSpeedUpEffect() {
		try {
			this.image = ImageIO.read(new File(EFFECT_IMAGE));
		} catch (IOException e) {
			throw new RuntimeException(EFFECT_IMAGE + " missing!");
		}
	}
	
	@Override
	public void activate(MazeGamePanel m, MazePlayer p) {
		this.p = p;
		this.endTime = System.currentTimeMillis() + DURATION;
		p.setSpeedModifier(p.getSpeedModifier() * SPEED_MODIFIER);
	}

	@Override
	public long getEndTime() {
		return endTime;
	}
	
	@Override
	public void addEndTime(long add) {
		this.endTime += add;
	}

	@Override
	public void deactivate(MazeGamePanel m) {
		p.setSpeedModifier(p.getSpeedModifier() / SPEED_MODIFIER);
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.drawImage(this.image, x, y, width, height, null);
	}
	
	public Image getImage() {
		return this.image;
	}

	private static final String EFFECT_IMAGE = "images/sprites/speedup.png";
	
	/* duration in milliseconds of the effect */
	private static final long DURATION = 10000;
	
	/* multiplier for the current player's speed when effect is activated */
	private static final double SPEED_MODIFIER = 2.0;
	
	private long endTime;
	private MazePlayer p;
	private Image image;
}
