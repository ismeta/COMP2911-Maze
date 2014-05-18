package maze.effect;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import maze.game.MazeGamePanel;
import maze.game.MazePlayer;

public class RotateRightEffect implements MazeEffect {
	private static final String EFFECT_IMAGE = "images/sprites/rotateright.png";
	private Image image;
	
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
	public void deactivate(MazeGamePanel m) {
		// do nothing
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.drawImage(this.image, x, y, width, height, null);
	}
	public Image getImage() {
		return this.image;
	}
}
