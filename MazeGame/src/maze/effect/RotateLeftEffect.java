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
 * @author oliver
 *
 */
public class RotateLeftEffect implements MazeEffect {
	private static final String EFFECT_IMAGE = "images/sprites/rotateleft.png";
	private Image image;
	
	public RotateLeftEffect() {
		try {
			this.image = ImageIO.read(new File(EFFECT_IMAGE));
		} catch (IOException e) {
			throw new RuntimeException(EFFECT_IMAGE + " missing!");
		}
	}
	
	@Override
	public void activate(MazeGamePanel m, MazePlayer p) {
		m.rotateLeft();

	}

	@Override
	public long getEndTime() {
		return 0;
	}
	
	@Override
	public void addEndTime(long add) {
		// not affected
	}

	@Override
	public void deactivate(MazeGamePanel m) {
		// nothing
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.drawImage(this.image, x, y, width, height, null);
	}
	
	public Image getImage() {
		return this.image;
	}

}
