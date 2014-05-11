package maze.effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import maze.Maze;
import maze.MazePlayer;

public class GlobalSpeedDownEffect implements MazePlayerEffect {
	private static final long DURATION = 10000;
	private static final double SPEED_MODIFIER = 0.75;
	private long endTime;
	private MazePlayer p;
	
	@Override
	public void activate(Maze m, MazePlayer p) {
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
	public void deactivate(Maze m) {
		// speed up other players
		for (MazePlayer mp : m.getPlayers()) {
			if (mp != p) {
				mp.setSpeedModifier(mp.getSpeedModifier() / SPEED_MODIFIER);
			}
		}
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.setColor(Color.PINK);
		g2d.fill(new Rectangle(x, y, width, height));
	}

}
