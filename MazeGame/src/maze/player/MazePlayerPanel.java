package maze.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import maze.effect.MazeEffect;

public class MazePlayerPanel extends JPanel {
	/**
	 * @param player the player this panel belongs to
	 */
	public MazePlayerPanel(Color color, int playerID) {
		this.color = color;
		this.playerID = playerID;
		this.keyImage = new ImageIcon("images/player/player" + playerID + "_keys.png").getImage();
		this.buffKeyImage = new ImageIcon("images/player/player" + playerID + "_buffs.png").getImage();
		this.buffs = new LinkedList<Image>();
		
		/* Maze dimensions */
		Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenWidth = (int) resolution.getWidth();
		this.screenHeight = (int) resolution.getHeight();
		
		this.setupGui();
	}
	
	/**
	 * @author davina
	 */
	public void setupGui() {
		// SIze
		this.setPreferredSize(new Dimension(this.screenWidth/2 - 200, this.screenHeight/5));
		// Border
		Border blackline = BorderFactory.createLineBorder(color, 10);
		Font f = new Font("verdana",Font.PLAIN,40);
		// Title
		int playerNum = playerID + 1;
		TitledBorder title = BorderFactory.createTitledBorder(blackline, "P" + playerNum);
		title.setTitlePosition(TitledBorder.DEFAULT_POSITION);
		title.setTitleJustification(TitledBorder.CENTER);
		title.setTitleFont(f);
		title.setTitleColor(color);
		// Background
		this.setBackground(new Color(45, 45, 45));
		this.setBorder(title);
	}
	
	/**
	 * redraw the inside of the panel. (buffs, key controls)
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(keyImage, 10, 35, this.screenWidth/7, this.screenHeight/7, null);
		g.drawImage(buffKeyImage, 220, 35, 400, 70, null);
		
		int x = 300;
		int y = 110;
		for (Image img: this.buffs) {
			g.drawImage(img, x, y, this.screenHeight/18, this.screenHeight/18, null);
			x += 100;
		}
	}
	
	/**
	 * Update the buffs for this panel.
	 * @param effectQueue the new effects to display.
	 */
	public void updateBuffs(Queue<MazeEffect> effectQueue) {
		this.buffs = new LinkedList<Image>();
		for (MazeEffect e: effectQueue) {
			this.buffs.add(e.getImage());
		}
		this.repaint();
	}
	
	private static final long serialVersionUID = 2639646226913308598L;

	/* colour of this panel */
	private Color color;
	
	/* id of the player this panel displays information for */
	private int playerID;
	
	/* image of the keyboard controls for this user */
	private Image keyImage;
	
	/* image of the key used to activate buffs for this user */
	private Image buffKeyImage;
	
	/* images of the buffs being displayed */
	private LinkedList<Image> buffs;
	
	/* screen dimensions for proportional sizing of components */
	private int screenWidth;
	private int screenHeight;
}
