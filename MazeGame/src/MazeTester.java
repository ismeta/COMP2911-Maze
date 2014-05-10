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
		frame.add(new MazeJPanel(33));
		frame.pack();
		frame.setVisible(true);
	}

}
