package maze;


import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import maze.game.MazeBasePanel;
import maze.generator.maze.RandomMazeGenerator;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @author davina
 */
public class GUI implements ActionListener {
	/**
	 * Gui
	 * 
	 * @param frame
	 */
	public GUI(JFrame frame) {
		this.frame = frame;
		this.difficulty = 5;
		this.numPlayers = 2;
		this.playMusic();
	}

	/**
	 * @return the cards
	 */
	public JPanel getCards() {
		return cards;
	}

	/**
	 * Generate card for card layout
	 * 
	 * @param pane
	 */
	public void generate(Container pane) {

		difficulty = 1;

		cards = new JPanel(new CardLayout());

		this.initialiseHomeCard();
		this.initialisePlayOptionsCard();
		this.initialiseMazeCard();
		this.initialiseSystemOptionsCard();

		// Display
		pane.add(cards, BorderLayout.CENTER);
		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, "home");
	}

	/**
	 * Home Card
	 */
	public void initialiseHomeCard() {

		// HOME CARD - Default layout is border layout
		homeCard = new ImagePanel();
		Image img = new ImageIcon("images/gui/mazecityb.png").getImage();
		homeCard.setBackground(img);
		homeCard.setLayout(new BorderLayout());

		// Header (empty)
		JLabel label = new JLabel("");
		label.setPreferredSize(new Dimension(200, 220));
		homeCard.add(label, BorderLayout.PAGE_START);

		// Buttons Panel
		JPanel buttonsPanel = new JPanel();
		GridLayout gl = new GridLayout(4, 1);
		gl.setVgap(20);
		buttonsPanel.setOpaque(false);
		buttonsPanel.setLayout(gl);

		// Play
		Icon ico = new ImageIcon("images/gui/green_road_sign_play.png");
		playButton = new JButton(ico);
		playButton.setContentAreaFilled(false);
		playButton.setBorder(BorderFactory.createEmptyBorder());
		playButton.addActionListener(this);
		buttonsPanel.add(playButton);

		// System Options
		ico = new ImageIcon("images/gui/green_road_sign_options.png");
		optionsButton = new JButton(ico);
		optionsButton.setContentAreaFilled(false);
		optionsButton.setBorder(BorderFactory.createEmptyBorder());
		optionsButton.addActionListener(this);
		buttonsPanel.add(optionsButton);

		// Help
		ico = new ImageIcon("images/gui/green_road_sign_help.png");
		helpButton = new JButton(ico);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorder(BorderFactory.createEmptyBorder());
		helpButton.addActionListener(this);
		buttonsPanel.add(helpButton);

		// Exit
		ico = new ImageIcon("images/gui/green_road_sign_exit.png");
		exitButton = new JButton(ico);
		exitButton.setContentAreaFilled(false);
		exitButton.setBorder(BorderFactory.createEmptyBorder());
		exitButton.addActionListener(this);
		buttonsPanel.add(exitButton);

		homeCard.add(buttonsPanel, BorderLayout.CENTER);

		label = new JLabel("");
		label.setPreferredSize(new Dimension(100, 50));
		homeCard.add(label, BorderLayout.PAGE_END);

		// Add home card to cardss
		cards.add(homeCard, "home");
	}

	/**
	 * Player Options Card
	 */
	public void initialisePlayOptionsCard() {
		playOptionsCard = new ImagePanel();
		Image img = new ImageIcon("images/gui/mazecity_playerops3.png")
				.getImage();
		playOptionsCard.setBackground(img);
		playOptionsCard.setLayout(new BorderLayout());

		JLabel label = new JLabel("");
		label.setPreferredSize(new Dimension(200, 500));
		playOptionsCard.add(label, BorderLayout.PAGE_START);

		label = new JLabel("");
		label.setPreferredSize(new Dimension(500, 500));
		playOptionsCard.add(label, BorderLayout.LINE_START);

		label = new JLabel("");
		label.setPreferredSize(new Dimension(500, 400));
		playOptionsCard.add(label, BorderLayout.LINE_END);

		label = new JLabel("");
		label.setPreferredSize(new Dimension(300, 200));
		playOptionsCard.add(label, BorderLayout.PAGE_END);

		// OPTIONS PANEL
		JPanel optionPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		optionPanel.setOpaque(false);

		JLabel play_chooseCity = new JLabel("CHOOSE CITY");
		play_chooseCity.setFont(label.getFont().deriveFont(25f));
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.1;
		gbc.weighty = 0.1;
		optionPanel.add(play_chooseCity, gbc);

		JRadioButton melbourne = new JRadioButton("Melbourne (Easy)", true);
		melbourne.setFont(label.getFont().deriveFont(25f));
		melbourne.setOpaque(false);
		melbourne.putClientProperty("JComponent.sizeVariant", "large");
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		optionPanel.add(melbourne, gbc);
		// Set difficulty
		melbourne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				difficulty = 5;
			}
		});

		JRadioButton sydney = new JRadioButton("Sydney (Hard)");
		sydney.setFont(label.getFont().deriveFont(25f));
		sydney.setOpaque(false);
		gbc.gridx = 1;
		gbc.gridy = 1;
		optionPanel.add(sydney, gbc);
		// Set difficulty
		sydney.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				difficulty = 10;
			}
		});

		ButtonGroup cityGroup = new ButtonGroup();
		cityGroup.add(melbourne);
		cityGroup.add(sydney);

		JLabel play_players = new JLabel("CHOOSE NUMBER OF PLAYERS");
		play_players.setFont(label.getFont().deriveFont(25f));
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 2;
		optionPanel.add(play_players, gbc);

		JRadioButton twoplayer = new JRadioButton("2", true);
		twoplayer.setFont(twoplayer.getFont().deriveFont(25f));
		twoplayer.setOpaque(false);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		optionPanel.add(twoplayer, gbc);
		// Set difficulty
		twoplayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				numPlayers = 2;
			}
		});

		JRadioButton threeplayer = new JRadioButton("3");
		threeplayer.setFont(label.getFont().deriveFont(25f));
		threeplayer.setOpaque(false);
		gbc.gridx = 1;
		gbc.gridy = 3;
		optionPanel.add(threeplayer, gbc);
		// Set difficulty
		threeplayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				numPlayers = 3;
			}
		});

		ButtonGroup playerGroup = new ButtonGroup();
		playerGroup.add(twoplayer);
		playerGroup.add(threeplayer);

		Icon ico = new ImageIcon("images/gui/playoptions_playbutton1.png");
		playSaveButton = new JButton(ico);
		playSaveButton.setBorderPainted(false);
		playSaveButton.setContentAreaFilled(false);
		playSaveButton.addActionListener(this);
		gbc.gridx = 0;
		gbc.gridy = 4;
		optionPanel.add(playSaveButton, gbc);

		ico = new ImageIcon("images/gui/playoptions_backbutton1.png");
		playBackButton = new JButton(ico);
		playBackButton.setBorderPainted(false);
		playBackButton.setContentAreaFilled(false);
		playBackButton.addActionListener(this);
		gbc.gridx = 1;
		gbc.gridy = 4;
		optionPanel.add(playBackButton, gbc);

		playOptionsCard.add(optionPanel);
		cards.add(playOptionsCard, "play");
	}

	/**
	 * Maze Card
	 */
	public void initialiseMazeCard() {
		mazeCard = new MazeBasePanel(this);
		cards.add(mazeCard, "maze");
		cards.setFocusable(true);
	}

	public void initialiseSystemOptionsCard() {
		systemopsCard = new JPanel();
		JLabel system_music = new JLabel("Music:");
		JLabel systemSoundfx = new JLabel("Sound FX:");
		systemSaveButton = new JButton("Save");
		systemSaveButton.addActionListener(this);
		systemBackButton = new JButton("Back");
		systemBackButton.addActionListener(this);

		systemopsCard.add(system_music);
		systemopsCard.add(systemSoundfx);
		systemopsCard.add(systemSaveButton);
		systemopsCard.add(systemBackButton);

		cards.add(systemopsCard, "system");
	}

	/**
	 * Handle actions
	 */
	public void actionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout) (cards.getLayout());

		if (e.getSource() == playButton) {
			cl.show(cards, "play");
		} else if (e.getSource() == optionsButton) {
			cl.show(cards, "system");
		} else if (e.getSource() == helpButton) {
			this.displayHelpWindow();
		} else if (e.getSource() == exitButton) {
			this.dispose();
		} else if (e.getSource() == playSaveButton) {
			mazeCard.setup(TILE_SIZE, numPlayers, new RandomMazeGenerator(TILE_SIZE), this);
			cl.show(cards, "maze");
		} else if (e.getSource() == playBackButton) {
			cl.show(cards, "home");
		} else if (e.getSource() == systemSaveButton
				|| e.getSource() == systemBackButton) {
			cl.show(cards, "home");
		}
	}
	
	/**
	 * Display help window as pop up
	 */
	public void displayHelpWindow() {
		JFrame help = new JFrame("Help");
		help.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		help.setMinimumSize(new Dimension(500, 500));
		help.setVisible(true);
	}
	
	/**
	 * Exit the app
	 */
	public void dispose() {
		this.frame.dispose();
	}
	
	/***
	 * Pump da music!
	 * TODO: Continuous and mute
	 * maybe different music between MazeGameState = UNSETUP and PLAY,PAUSED and SUCCESS
	 * @param filename
	 */
	public void playMusic() {
		String filename = "music/Pamgaea.wav";
		InputStream in = null;
		AudioStream as = null;
		try {
			//create audio data source
			in = new FileInputStream(new File(filename));
		} catch(FileNotFoundException fnfe) {
			System.err.println("The audio file was not found");
		}
		
		try {
			//create audio stream from file stream
			as = new AudioStream(in);
		} catch(IOException ie) {
			System.err.println("Audio stream could not be created");
		}
		AudioPlayer.player.start(as);
	}
	
	private class ImagePanel extends JPanel {		
		private static final long serialVersionUID = 1;
	    Image image;

	    public void setBackground(Image image) {
	        this.image = image;
	    }

	    @Override
	    public void paintComponent(Graphics G) {
	        super.paintComponent(G);
	        G.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
	    }
	}
	
	/* approximate size of each maze tile */
	private static final int TILE_SIZE_ORIGINAL = 20;
	
	/* actual size of each maze tile - required to be an odd number */
	private static final int TILE_SIZE = ((int) (TILE_SIZE_ORIGINAL) / 2) * 2 + 1;

	private JFrame frame;

	private JPanel cards;
	private ImagePanel homeCard;
	private ImagePanel playOptionsCard;
	private JPanel systemopsCard;
	private MazeBasePanel mazeCard;

	private JButton playButton;
	private JButton optionsButton;
	private JButton helpButton;
	private JButton exitButton;
	private JButton playSaveButton;
	private JButton playBackButton;
	private JButton systemSaveButton;
	private JButton systemBackButton;

	private int difficulty;
	private int numPlayers;
}
