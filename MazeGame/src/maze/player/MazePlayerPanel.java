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
	 * @param color Color of player
	 * @param playerID id of player
	 */
	public MazePlayerPanel(Color color, int playerID) {
		this.color = color;
		this.playerID = playerID;
		this.keyImage = new ImageIcon("images/player/player" + playerID + "_keys.png").getImage();
		this.effectKeyImage = new ImageIcon("images/player/player" + playerID + "_buffs.png").getImage();
		this.effects = new LinkedList<Image>();
		this.isFinished = false;
		this.setupGui();
	}

	/**
	 * Sets up the components for the maze player panel.
	 */
	public void setupGui() {
		/* preferred size of a player maze panel is 600 x 220 pixels */
		this.setPreferredSize(new Dimension(600, 220));

		/* we want a border of 10 pixels thickness */
		Border blackline = BorderFactory.createLineBorder(color, 10);

		/* we want largish text for the panel title */
		Font f = new Font("verdana", Font.PLAIN, 40);

		/*
		 * title is just "P1" or "P0" or "P2" depending on the player the panel
		 * is for
		 */
		playerID += 1;
		TitledBorder title = BorderFactory.createTitledBorder(blackline, "P"
				+ playerID);
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

		/* draw the images of the controls to move for players */
		g.drawImage(keyImage, 10, 35, 240, 170, null);

		/*
		 * draw on the player's effects (if they have any) (x, y) represents from
		 * where the buffs should be drawn
		 */
		g.drawImage(effectKeyImage, 220, 35, 400, 70, null);
		int x = 300;
		int y = 110;
		/*
		 * If finished, display "FINISHED" otherwise display the buffs
		 */
		if (isFinished) {
			Image img = new ImageIcon("images/player/finished_transparent.png").getImage();
			g.drawImage(img, x, y, 190, 60, null);
		} else {
			LinkedList<Image> tempBuffs = new LinkedList<Image>(this.effects);
			for (Image img : tempBuffs) {
				g.drawImage(img, x, y, 60, 60, null);
				x += 100;
			}
		}
	}

	/**
	 * Update the buffs for this panel.
	 * 
	 * @param effectQueue
	 *            the new effects to display.
	 */
	
	public void updateEffects(Queue<MazeEffect> effectQueue) {
		this.effects = new LinkedList<Image>();
		for (MazeEffect e: effectQueue) {
			this.effects.add(e.getImage());
		}
		this.repaint();
	}
	
	public void displayFinished() {
		isFinished = true;
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
	
	/* status of player */
	private boolean isFinished;
}
