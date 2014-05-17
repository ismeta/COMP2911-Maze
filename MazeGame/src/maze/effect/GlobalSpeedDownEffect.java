package maze.effect;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import maze.game.MazeGamePanel;
import maze.game.MazePlayer;

public class GlobalSpeedDownEffect implements MazeEffect {
	private static final String EFFECT_IMAGE = "images/sprites/slow.png";
	
	private static final long DURATION = 10000;
	private static final double SPEED_MODIFIER = 0.75;
	private long endTime;
	private MazePlayer p;
	private Image image;
	
	public GlobalSpeedDownEffect() {
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
		// speed down other players
		for (MazePlayer mp : m.getPlayers()) {
			if (mp != p) {
				mp.setSpeedModifier(mp.getSpeedModifier() * SPEED_MODIFIER);
			}
		}
	}

	@Override
	public long getEndTime() {
		return endTime;
	}

	@Override
	public void deactivate(MazeGamePanel m) {
		// speed up other players
		for (MazePlayer mp : m.getPlayers()) {
			if (mp != p) {
				mp.setSpeedModifier(mp.getSpeedModifier() / SPEED_MODIFIER);
			}
		}
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.drawImage(this.image, x, y, width, height, null);
	}

}
