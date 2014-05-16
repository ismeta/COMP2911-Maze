/**
 * 
 */
package maze.effect;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import maze.Maze;
import maze.MazePlayer;

/**
 * @author oliver
 *
 */
public class SelfSpeedUpEffect implements MazeEffect {
	private static final String EFFECT_IMAGE = "images/sprites/speedup.png";
	private static final long DURATION = 10000;
	private static final double SPEED_MODIFIER = 2.0;
	private long endTime;
	private MazePlayer p;
	private Image image;
	
	public SelfSpeedUpEffect() {
		try {
			this.image = ImageIO.read(new File(EFFECT_IMAGE));
		} catch (IOException e) {
			throw new RuntimeException(EFFECT_IMAGE + " missing!");
		}
	}
	
	@Override
	public void activate(Maze m, MazePlayer p) {
		this.p = p;
		this.endTime = System.currentTimeMillis() + DURATION;
		p.setSpeedModifier(p.getSpeedModifier() * SPEED_MODIFIER);
	}

	@Override
	public long getEndTime() {
		return endTime;
	}

	@Override
	public void deactivate(Maze m) {
		p.setSpeedModifier(p.getSpeedModifier() / SPEED_MODIFIER);
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.drawImage(this.image, x, y, width, height, null);
	}
}
