package maze.effect;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import maze.Maze;
import maze.MazePlayer;

public class RotateRightEffect implements MazeEffect {

	@Override
	public void activate(Maze m, MazePlayer p) {
		m.rotateRight();
	}

	@Override
	public long getEndTime() {
		return 0;
	}

	@Override
	public void deactivate(Maze m) {
		// do nothing
	}

	@Override
	public void draw(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.setColor(Color.MAGENTA);
		g2d.fill(new Rectangle(x, y, width, height));
	}

}
