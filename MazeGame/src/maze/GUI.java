package maze;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import maze.generator.maze.MazeGenerator;
import maze.generator.maze.RandomMazeGenerator;
import maze.generator.maze.RecursiveBacktrackerMazeGenerator;

/**
 * GUI.
 * 
 * DISPLAY 'PAGES':
 * Home, Play Options, System Options, Maze Game.
 * Help (Pop up frame) and Exit (closes application).
 * 
 * GUI FLOW/USER INTERACTION:
 * Listens for when a GUI flow button is pressed e.g. User presses Back.
 * 
 * @author davina
 */
public class GUI implements ActionListener {
	
	/**
	 * @param frame
	 */
	public GUI() {
		/* Gui has a stack of 'pages' and displays the relevant page.
		 * Only one page is visible at a time and is switched with the previous card.
		 */
		this.pages = new JPanel(new CardLayout());
	}
	
	public JPanel getPages() {
		return this.pages;
	}


	/**
	 * Set up default settings.
	 * Generate pages for each page and show home initially.
	 * @param pane
	 */
	public void generate(Container pane) {
		/* Default difficulty and number of players */
		this.numPlayers = TWO_PLAYERS;
		this.difficulty = MazeDifficulty.MELBOURNE;

		/* Initialise Pages */
		this.initialiseHomePage();
		this.initialisePlayOptionsPage();
		this.initialiseSystemOptionsPage();
		this.initialiseMazePage();

		/* Initial display */
		pane.add(pages, BorderLayout.CENTER);
		CardLayout cl = (CardLayout) (pages.getLayout());
		cl.show(pages, "home");
	}

	/**
	 * Home Page
	 */
	public void initialiseHomePage() {
		/* Background and Layout */
		homePage = new ImagePanel();
		Image img = new ImageIcon("images/gui/mazecityb.png").getImage();
		homePage.setBackground(img);
		homePage.setLayout(new BorderLayout());

		/* Empty Header */
		JLabel label = new JLabel("");
		label.setPreferredSize(this.getAdjustedDimension(4));
		homePage.add(label, BorderLayout.PAGE_START);

		/* Buttons panel */
		JPanel buttonsPanel = new JPanel();
		GridLayout gl = new GridLayout(4, 1);
		gl.setVgap(20);
		buttonsPanel.setOpaque(false);
		buttonsPanel.setLayout(gl);

		/* - Play button */
		Icon ico = new ImageIcon("images/gui/green_road_sign_play.png");
		playButton = new JButton(ico);
		playButton.setContentAreaFilled(false);
		playButton.setBorder(BorderFactory.createEmptyBorder());
		playButton.addActionListener(this);
		buttonsPanel.add(playButton);

		/* - System options */
		ico = new ImageIcon("images/gui/green_road_sign_options.png");
		optionsButton = new JButton(ico);
		optionsButton.setContentAreaFilled(false);
		optionsButton.setBorder(BorderFactory.createEmptyBorder());
		optionsButton.addActionListener(this);
		buttonsPanel.add(optionsButton);

		/* - Help */
		ico = new ImageIcon("images/gui/green_road_sign_help.png");
		helpButton = new JButton(ico);
		helpButton.setContentAreaFilled(false);
		helpButton.setBorder(BorderFactory.createEmptyBorder());
		helpButton.addActionListener(this);
		buttonsPanel.add(helpButton);

		/* - Exit */
		ico = new ImageIcon("images/gui/green_road_sign_exit.png");
		exitButton = new JButton(ico);
		exitButton.setContentAreaFilled(false);
		exitButton.setBorder(BorderFactory.createEmptyBorder());
		exitButton.addActionListener(this);
		buttonsPanel.add(exitButton);

		/* All buttons set, add it to home page in the center. */
		homePage.add(buttonsPanel, BorderLayout.CENTER);

		/* Empty Footer */
		label = new JLabel("");
		label.setPreferredSize(this.getAdjustedDimension(18));
		homePage.add(label, BorderLayout.PAGE_END);

		/* Add home page to stack of pages */
		this.pages.add(homePage, "home");
	}

	/**
	 * Player Options Page
	 */
	public void initialisePlayOptionsPage() {
		/* Background and Layout */
		playOptionsPage = new ImagePanel();
		Image img = new ImageIcon("images/gui/player_options_bg.png")
				.getImage();
		playOptionsPage.setBackground(img);
		playOptionsPage.setLayout(new BorderLayout());

		/* Empty header */
		JLabel label = new JLabel("");
		label.setPreferredSize(this.getAdjustedDimension(3));
		playOptionsPage.add(label, BorderLayout.PAGE_START);
	
		/* Panel with options */
		JPanel optionPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		optionPanel.setOpaque(false);

		/* - CHOOSE CITY*/
		JLabel cityLabel = new JLabel("CHOOSE CITY (DIFFICULTY)");
		cityLabel.setFont(new Font("verdana", Font.BOLD, 25));
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0.1;
		optionPanel.add(cityLabel, gbc);

		/* -- Melbourne radio button */
		JRadioButton melbourne = new JRadioButton("Melbourne (Easy)", true);
		melbourne.setFont(new Font("verdana", Font.PLAIN, 25));
		melbourne.setOpaque(false);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		optionPanel.add(melbourne, gbc);
		// Set difficulty
		melbourne.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				difficulty = MazeDifficulty.MELBOURNE;
			}
		});

		/* -- Sydney radio button */
		JRadioButton sydney = new JRadioButton("Sydney (Hard)");
		sydney.setFont(new Font("verdana", Font.PLAIN, 25));
		sydney.setOpaque(false);
		gbc.gridx = 1;
		gbc.gridy = 1;
		optionPanel.add(sydney, gbc);
		// Set difficulty
		sydney.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				difficulty = MazeDifficulty.SYDNEY;
			}
		});

		/* Radio buttons are set up so add to button group */
		ButtonGroup cityGroup = new ButtonGroup();
		cityGroup.add(melbourne);
		cityGroup.add(sydney);

		/* CHOOSE NUM PLAYERS */
		JLabel playersLabel = new JLabel("CHOOSE NUMBER OF PLAYERS");
		playersLabel.setFont(new Font("verdana", Font.BOLD, 25));
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 2;
		optionPanel.add(playersLabel, gbc);

		/* - Two player */
		JRadioButton twoplayer = new JRadioButton("2 Players", true);
		twoplayer.setFont(new Font("verdana", Font.PLAIN, 25));
		twoplayer.setOpaque(false);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		optionPanel.add(twoplayer, gbc);
		// Set num players
		twoplayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				numPlayers = TWO_PLAYERS;
			}
		});

		/* - Three players */
		JRadioButton threeplayer = new JRadioButton("3 Players");
		threeplayer.setFont(new Font("verdana", Font.PLAIN, 25));
		threeplayer.setOpaque(false);
		gbc.gridx = 1;
		gbc.gridy = 3;
		optionPanel.add(threeplayer, gbc);
		// Set num players
		threeplayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				numPlayers = THREE_PLAYERS;
			}
		});

		/* Radio buttons are set up so add to button group */
		ButtonGroup playerGroup = new ButtonGroup();
		playerGroup.add(twoplayer);
		playerGroup.add(threeplayer);

		/* Play Button */
		Icon ico = new ImageIcon("images/gui/playoptions_playbutton1.png");
		playSaveButton = new JButton(ico);
		playSaveButton.setBorderPainted(false);
		playSaveButton.setContentAreaFilled(false);
		playSaveButton.addActionListener(this);
		gbc.gridx = 0;
		gbc.gridy = 4;
		optionPanel.add(playSaveButton, gbc);

		/* Back button */
		ico = new ImageIcon("images/gui/playoptions_backbutton1.png");
		playBackButton = new JButton(ico);
		playBackButton.setBorderPainted(false);
		playBackButton.setContentAreaFilled(false);
		playBackButton.addActionListener(this);
		gbc.gridx = 1;
		gbc.gridy = 4;
		optionPanel.add(playBackButton, gbc);

		/* Options GUI set up, add to page */
		playOptionsPage.add(optionPanel);
		
		/* Empty Footer */
		label = new JLabel("");
		label.setPreferredSize(this.getAdjustedDimension(5));
		playOptionsPage.add(label, BorderLayout.PAGE_END);
		
		/* Add options page to stack of pages */
		this.pages.add(playOptionsPage, "play");
	}

	/**
	 * Maze Page - Page with actual maze and game play
	 */
	public void initialiseMazePage() {
		mazePage = new MazeBasePanel(this);
		this.pages.add(mazePage, "maze");
		this.pages.setFocusable(true);
	}

	public void initialiseSystemOptionsPage() {
		systemopsPage = new JPanel();
		JLabel system_music = new JLabel("Music:");
		JLabel systemSoundfx = new JLabel("Sound FX:");
		systemSaveButton = new JButton("Save");
		systemSaveButton.addActionListener(this);
		systemBackButton = new JButton("Back");
		systemBackButton.addActionListener(this);

		systemopsPage.add(system_music);
		systemopsPage.add(systemSoundfx);
		systemopsPage.add(systemSaveButton);
		systemopsPage.add(systemBackButton);

		pages.add(systemopsPage, "system");
	}

	/**
	 * Handle actions
	 */
	public void actionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout) (pages.getLayout());

		if (e.getSource() == playButton) {
			cl.show(pages, "play");
		} else if (e.getSource() == optionsButton) {
			cl.show(pages, "system");
		} else if (e.getSource() == helpButton) {
			this.displayHelpWindow();
		} else if (e.getSource() == exitButton) {
			System.exit(0);
		} else if (e.getSource() == playSaveButton) {
			MazeGenerator generator = null;
			if (this.difficulty.equals(MazeDifficulty.MELBOURNE)) {
				generator = new RecursiveBacktrackerMazeGenerator(TILE_SIZE);
			} else if (this.difficulty.equals(MazeDifficulty.SYDNEY)) {
				generator = new RandomMazeGenerator(TILE_SIZE);
			} else {
				throw new RuntimeException("Unknown difficulty");
			}
			mazePage.setup(TILE_SIZE, numPlayers, generator, this);
			cl.show(pages, "maze");
		} else if (e.getSource() == playBackButton) {
			cl.show(pages, "home");
		} else if (e.getSource() == systemSaveButton
				|| e.getSource() == systemBackButton) {
			cl.show(pages, "home");
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
	
	/**
	 * Make GUI nice for all resolutions
	 * @param divisor
	 * @return adjusted Dimension
	 */
	private Dimension getAdjustedDimension(int divisor) {
		Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		int adjustedWidth = (int) resolution.getWidth()/divisor;
		int adjustedHeight = (int) resolution.getHeight()/divisor;
		return new Dimension(adjustedWidth, adjustedHeight);
	}
	
	/* approximate size of each maze tile */
	private static final int TILE_SIZE_ORIGINAL = 25;
	
	/* actual size of each maze tile - required to be an odd number */
	private static final int TILE_SIZE = ((int) (TILE_SIZE_ORIGINAL) / 2) * 2 + 1;

	/* difficulty level - linked to a city */
	private static final int EASY_MELBOURNE = 5;
	private static final int HARD_SYDNEY = 10;
	
	/* number of players */
	private static final int TWO_PLAYERS = 2;
	private static final int THREE_PLAYERS = 3;
	
	/* Pages */
	private JPanel pages;
	private ImagePanel homePage;
	private ImagePanel playOptionsPage;
	private JPanel systemopsPage;
	private MazeBasePanel mazePage;

	/* Buttons */
	private JButton playButton;
	private JButton optionsButton;
	private JButton helpButton;
	private JButton exitButton;
	private JButton playSaveButton;
	private JButton playBackButton;
	private JButton systemSaveButton;
	private JButton systemBackButton;

	/* Option settings */
	private int numPlayers;
	private MazeDifficulty difficulty;
	
	private enum MazeDifficulty {
		SYDNEY,
		MELBOURNE
	}
}
