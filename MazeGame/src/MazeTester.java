import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JFrame;


public class MazeTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setLayout(new GridBagLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		MazeJPanel mp = new MazeJPanel(20);
		mp.setPreferredSize(new Dimension(500, 500));
		
		frame.add(mp);
		frame.pack();
		frame.setVisible(true);
	}

}
