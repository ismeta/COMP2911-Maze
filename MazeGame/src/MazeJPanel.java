import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

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
	private ArrayList<ArrayList<MazeTile>> tiles;

	/**
	 * Generates a new MazeJPanel 
	 * @param size size of the maze
	 */
	public MazeJPanel(int size) {
		// double buffered JPanel
		super(true);
		// generate tiles;
		this.tiles = new ArrayList<ArrayList<MazeTile>>();
		for (int i = 0; i  < size; i++) {
			ArrayList<MazeTile> tileRow = new ArrayList<MazeTile>();
			for (int j = 0; j < size; j++) {
				Random r = new Random();
				boolean v = r.nextBoolean();
				System.out.println(v);
				tileRow.add(new MazeTile(v));
			}
			this.tiles.add(tileRow);
		}
		// preferred size
		this.setPreferredSize(new Dimension(400, 400));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < this.tiles.size(); i++) {
			ArrayList<MazeTile> tileRow = this.tiles.get(i);
			for (int j = 0; j < tileRow.size(); j++) {
				int width = this.getWidth() / tileRow.size();
				int height = this.getHeight() / this.tiles.size();
				tileRow.get(j).draw(g2d, i * width, j * height, width, height);
			}
		}
	}
}