package maze.effect;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import maze.game.MazeGamePanel;
import maze.player.MazePlayer;

/**
 * Effect which rotates the maze to the right by 90 degrees
 */
public class RotateRightEffect implements MazeEffect {
	public RotateRightEffect() {
		try {
			this.image = ImageIO.read(new File(EFFECT_IMAGE));
		} catch (IOException e) {
			throw new RuntimeException(EFFECT_IMAGE + " missing!");
		}
	}
	
	@Override
	public void activate(MazeGamePanel m, MazePlayer p) {
		m.rotateRight();
	}

	@Override
	public long getEndTime() {
		return 0;
	}
	
	@Override
	public void addEndTime(long add) {
		/* not affected */
	}
	
	@Override
	public void deactivate(MazeGamePanel m) {
		/* do nothing */
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.drawImage(this.image, x, y, width, height, null);
	}
	
	/**
	 * @return the image for this effect
	 */
	public Image getImage() {
		return this.image;
	}
	
	private static final String EFFECT_IMAGE = "images/sprites/rotateright.png";
	
	private Image image;
}
