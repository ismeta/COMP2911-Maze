package maze.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import maze.GUI;
import maze.player.MazePlayer;

/**
 * Maze Score Panel displays score board
 * 
 * @author davina
 * 
 */
public class MazeScorePanel extends JPanel {

	/**
	 * @param panelWidth
	 */
	public MazeScorePanel(int panelWidth, GUI frameGui) {
		super(true);
		/* GUI */
		this.setPreferredSize(new Dimension(panelWidth, panelWidth));
		this.setMaximumSize(new Dimension(panelWidth, panelWidth));
		this.setFocusable(true);
		this.setVisible(true);
		this.frameGui = frameGui;
	}

	/**
	 * Set up score panel
	 * 
	 * @param players
	 */
	public void setup(MazePlayer[] players) {
		/* Background and Layout */
		this.setLayout(new GridBagLayout());
		this.setBackground(new Color(45, 45, 45));

		/* Border */
		Font f = new Font("verdana", Font.PLAIN, 40);
		Border line = BorderFactory.createLineBorder(Color.YELLOW, 10);
		TitledBorder title = BorderFactory.createTitledBorder(line,
				"GAME OVER!");
		title.setTitlePosition(TitledBorder.DEFAULT_POSITION);
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitleFont(f);
		title.setTitleColor(Color.WHITE);
		this.setBorder(title);

		/* Score Board Header */
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel header = new JLabel("SCORE BOARD");
		header.setFont(new Font("verdana", Font.PLAIN, 50));
		header.setForeground(Color.YELLOW);
		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1;
		this.add(header, gbc);

		/* Sort Players by Ranking */
		Arrays.sort(players, new Comparator<MazePlayer>() {
			public int compare(MazePlayer p1, MazePlayer p2) {
				return p1.getRanking() - p2.getRanking();
			}
		});

		String[] positions = { "images/gui/trophy_first.png",
							   "images/gui/trophy_second.png",
							   "images/gui/trophy_third.png" };

		/* Results Table */
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		int i = 0;
		for (i = 0; i < players.length; i++) {
			// Position
			Image trophy = new ImageIcon(positions[i]).getImage();
			JLabel ranking = new JLabel(new ImageIcon(getResized(trophy, 130,
					70)));
			ranking.setFont(new Font("verdana", Font.PLAIN, 40));
			ranking.setForeground(Color.WHITE);
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.gridx = 0;
			gbc.gridy = 2 + i;
			this.add(ranking, gbc);
			// Player Image
			JLabel playerImage = new JLabel();
			Image img = new ImageIcon(players[i].getImage()).getImage();
			playerImage.setIcon(new ImageIcon(getResized(img, 50, 50)));
			gbc.gridx = 1;
			this.add(playerImage, gbc);
			// Player Number
			JLabel playerID = new JLabel("Player "
					+ Integer.toString(players[i].getId() + 1));
			playerID.setFont(new Font("verdana", Font.PLAIN, 40));
			playerID.setForeground(Color.WHITE);
			gbc.gridx = 2;
			this.add(playerID, gbc);
			// TODO: display actual time
			JLabel time = new JLabel("Time");
			time.setFont(new Font("verdana", Font.PLAIN, 40));
			time.setForeground(Color.WHITE);
			gbc.gridx = 3;
			this.add(time, gbc);
		}

		/* Play again button */
		// TODO: do we want to give play again option
		ImageIcon ico = new ImageIcon("images/gui/maze_playagain.png");
		JButton restart = new JButton(ico);
		restart.setBorderPainted(false);
		restart.setContentAreaFilled(false);
		gbc.gridwidth = 4;
		gbc.gridx = 0;
		gbc.gridy = 2 + i;
		this.add(restart, gbc);
		
		restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            	// Go back to play options page
            	frameGui.displayPlayOptionsPage();
            }
        });
	}

	/**
	 * Resize image
	 * 
	 * @param srcImg
	 * @param w
	 * @param h
	 * @return
	 */
	private Image getResized(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	private static final long serialVersionUID = 7399404361523168615L;
	
	private GUI frameGui;
}
