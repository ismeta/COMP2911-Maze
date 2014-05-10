import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;


public class MazeTester {
	private static final int TILE_SIZE_ORIGINAL = 22;
	private static final int TILE_SIZE = ((int) (TILE_SIZE_ORIGINAL) / 2) * 2 + 1;
	private static final int FULL_SIZE_MAX = 500;
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		MazeJPanel mp = new MazeJPanel(TILE_SIZE);
		mp.setPreferredSize(new Dimension((int) (FULL_SIZE_MAX / TILE_SIZE) * TILE_SIZE, (int) (FULL_SIZE_MAX / TILE_SIZE) * TILE_SIZE));
		
		frame.add(mp);
		frame.pack();
		frame.setVisible(true);
	}

}
