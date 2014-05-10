import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 * MazeJPanel
 * contains the actual panel controls 
 */

/**
 * @author oliver
 *
 */
public class MazeJPanel extends JPanel {
	/**
	 * serialVersionUID magic
	 */
	private static final long serialVersionUID = 4602880844383443785L;
	private MazeTile[][] tiles;
	private int size;
	private Timer timer;

	/**
	 * Generates a new MazeJPanel 
	 * @param size size of the maze
	 */
	public MazeJPanel(int size) {
		// double buffered JPanel
		super(true);
		// generate tiles;
		this.tiles = new MazeTile[size][size];
		for (int i = 0; i  < size; i++) {
			for (int j = 0; j < size; j++) {
				this.tiles[i][j] = new MazeTile((new Random()).nextBoolean());
			}
		}
		// size
		this.size = size;
		// allocate timer
		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(new MazeJPanelTimer(this), 0, 1000 / 24);
	}
	
	@Override
	public void paint(Graphics g) {
		System.out.println("REPAINT!");
		super.paint(g);
		int width = this.getWidth() / size;
		int height = this.getHeight() / size;
		// lol random
		Random r = new Random();
		Graphics2D g2d = (Graphics2D) g;
		
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				this.tiles[i][j].draw(g2d, i * width, j * height, width, height);
				// i am evil
				if (r.nextInt(1000) > 950) {
					this.tiles[i][j].setWall(!this.tiles[i][j].isWall());
				}
			}
		}
	}
	
	/**
	 * @author oliver
	 * MazeJPanelTimer allows player listeners and repainting
	 */
	private class MazeJPanelTimer extends TimerTask {
		private MazeJPanel mp;
		private long lastTime;
		
		private MazeJPanelTimer(MazeJPanel mp) {
			this.mp = mp;
			this.lastTime = System.currentTimeMillis();
		}
		
		@Override
		public void run() {
			// repaint the maze since there are updates
			this.mp.repaint();
			// time has changed!
			this.lastTime = System.currentTimeMillis();
		}
	}
}