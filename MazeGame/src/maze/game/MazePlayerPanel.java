package maze.game;

import java.awt.BorderLayout;
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
		this.setLayout(new BorderLayout());
		this.add(new JLabel(new ImageIcon(this.player.getImage())), BorderLayout.LINE_START);
		this.add(new JLabel("LOL"), BorderLayout.LINE_END);
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
