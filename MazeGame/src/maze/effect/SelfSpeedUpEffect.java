/**
 * 
 */
package maze.effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import maze.Maze;
import maze.MazePlayer;

/**
 * @author oliver
 *
 */
public class SelfSpeedUpEffect implements MazeEffect {
	private static final long DURATION = 10000;
	private static final double SPEED_MODIFIER = 2.0;
	private long endTime;
	private MazePlayer p;
	
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
		g2d.setColor(Color.ORANGE);
		g2d.fill(new Rectangle(x, y, width, height));
	}
}