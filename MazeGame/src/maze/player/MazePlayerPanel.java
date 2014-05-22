package maze.player;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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
	/**
	 * @param player the player this panel belongs to
	 */
	public MazePlayerPanel(Color color, int playerID) {
		this.color = color;
		this.playerID = playerID;
		this.keyImage = new ImageIcon("images/player/player" + playerID + "_keys.png").getImage();
		this.effectKeyImage = new ImageIcon("images/player/player" + playerID + "_effects.png").getImage();
		this.effects = new LinkedList<Image>();
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
		
		/* set background colour to gray */
		Color darkGray = new Color(45, 45, 45);
		this.setBackground(darkGray);
		this.setBorder(title);
	}
	
	/**
	 * redraw the inside of the panel. (effects, key controls)
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(keyImage, 10, 35, 240, 170, null);
		g.drawImage(effectKeyImage, 220, 35, 400, 70, null);
		
		int x = 300;
		int y = 110;
		LinkedList<Image> tempeffects = new LinkedList<Image>(this.effects);
		for (Image img : tempeffects) {
			g.drawImage(img, x, y, 60, 60, null);
			x += 100;
		}
	}
	
	/**
	 * Update the effects for this panel.
	 * @param effectQueue the new effects to display.
	 */
	public void updateEffects(Queue<MazeEffect> effectQueue) {
		this.effects = new LinkedList<Image>();
		for (MazeEffect e: effectQueue) {
			this.effects.add(e.getImage());
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
	
	/* image of the key used to activate effects for this user */
	private Image effectKeyImage;
	
	/* images of the effects being displayed */
	private LinkedList<Image> effects;
}
