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
public class RotateLeftEffect implements MazePlayerEffect {
	
	@Override
	public void activate(Maze m, MazePlayer p) {
		m.rotateLeft();

	}

	@Override
	public long getEndTime() {
		return 0;
	}

	@Override
	public void deactivate(Maze m) {
		// nothing
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.setColor(Color.YELLOW);
		g2d.fill(new Rectangle(x, y, width, height));
	}

}
