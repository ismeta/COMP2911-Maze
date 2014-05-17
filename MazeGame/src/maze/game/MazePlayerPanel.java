package maze.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MazePlayerPanel extends JPanel {
	private static final long serialVersionUID = 2639646226913308598L;
	private MazePlayer player;

	/**
	 * @param player
	 */
	public MazePlayerPanel(MazePlayer player) {
		this.player = player;
		
		/* GUI */
		this.setPreferredSize(new Dimension(100, 100));
	}
	
	/**
	 * @return the player
	 */
	public MazePlayer getPlayer() {
		return player;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
	}
	
}
