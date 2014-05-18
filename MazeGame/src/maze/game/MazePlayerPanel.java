package maze.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import maze.effect.MazeEffect;

public class MazePlayerPanel extends JPanel {
	private static final long serialVersionUID = 2639646226913308598L;

	private Color color;
	private int playerID;
	private Image img;
	private Image buff;
	private LinkedList<Image> buffs;
	
	/**
	 * @param player
	 */
	public MazePlayerPanel(Color color, int playerID) {
		this.color = color;
		this.playerID = playerID;
		this.img = new ImageIcon("images/player/player" + playerID + "_keys.png").getImage();
		this.buff = new ImageIcon("images/player/player" + playerID + "_buffs.png").getImage();
		this.buffs = new LinkedList<Image>();
		this.setupGui();
	}
	
	/**
	 * @author davina
	 */
	public void setupGui() {
		// SIze
		this.setPreferredSize(new Dimension(600, 220));
		// Border
		Border blackline = BorderFactory.createLineBorder(color, 10);
		Font f = new Font("verdana",Font.PLAIN,40);
		// Title
		TitledBorder title = BorderFactory.createTitledBorder(blackline, "P" + playerID);
		title.setTitlePosition(TitledBorder.DEFAULT_POSITION);
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitleFont(f);
		title.setTitleColor(color);
		// Background
		this.setBackground(new Color(45, 45, 45));
		this.setBorder(title);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 10, 35, 240, 170, null);
		g.drawImage(buff, 220, 35, 400, 70, null);
		
		int x = 300;
		int y = 110;
		for (Image img: this.buffs) {
			g.drawImage(img, x, y, 60, 60, null);
			x += 100;
		}
	}
	
	public void updateBuffs(Queue<MazeEffect> effectQueue) {
		this.buffs = new LinkedList<Image>();
		for (MazeEffect e: effectQueue) {
			this.buffs.add(e.getImage());
		}
		this.repaint();
	}
}
