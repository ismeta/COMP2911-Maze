/**
 * 
 */
package maze.generator.effect;

import java.util.Random;

import maze.effect.EgalitarianEffect;
import maze.effect.GlobalSpeedDownEffect;
import maze.effect.MazeEffect;
import maze.effect.RotateLeftEffect;
import maze.effect.RotateRightEffect;
import maze.effect.SelfSpeedUpEffect;
import maze.game.MazeTile;

/**
 * Generates boosts randomly. - Only generates speed boosts at dead ends - Only
 * generates board rotations at non-dead end tiles
 */
public class RandomEffectGenerator implements EffectGenerator {
	public RandomEffectGenerator (int size) {
		this.mazeSize = size;
	}

	/**
	 * Adds boosts to the tiles. - Speed boosts are added randomly to deadends -
	 * Board rotations are added randomly to any tile
	 * 
	 * @param tiles
	 *            The maze to add boosts to.
	 */
	public void generateEffects(MazeTile[][] tiles) {
		Random rand = new Random();

		for (int i = 0; i < mazeSize; i++) {
			for (int j = 0; j < mazeSize; j++) {
				/* don't generate boosts on the start tile */
				if (tiles[i][j].isWall() || tiles[i][j].isGoal() || tiles[i][j].isStart()) {
					continue;
				}
				MazeEffect effect;

				/*
				 * if we have one neighbour then we're at a dead end - so
				 * generate a speed boost otherwise generate one of the other
				 * boosts
				 */
				if (numPathNeighbours(tiles, i, j, mazeSize) == 1) {
					/*
					 * only proceed for a small percentage of cases - there are
					 * 'usableTiles' possible tiles we can put boosts on.
					 */
					if (rand.nextDouble() >= SPEED_BOOST_ADUNDANCE) {
						continue;
					}

					/*
					 * change the integer bound based on the number of possible
					 * effects!
					 */
					int choice = rand.nextInt(5);

					/*
					 * we want more self speed up effects than the others - we
					 * can change this if we want to change the distribution
					 * of boosts
					 */
					switch (choice) {
					case 0:
					case 1:
						effect = new GlobalSpeedDownEffect();
						break;
					case 2:
					case 3:
						effect = new SelfSpeedUpEffect();
						break;
					case 4:
						effect = new EgalitarianEffect();
						break;
					default:
						throw new RuntimeException(
								"random generated too large a number! "
										+ choice);
					}
				} else {
					/*
					 * only proceed for a small percentage of cases - there are
					 * 'usableTiles' possible tiles we can put boosts on.
					 */
					if (rand.nextDouble() >= ROTATE_BOOST_ABUNDANCE) {
						continue;
					}

					/*
					 * change the integer bound based on the number of possible
					 * effects!
					 */
					int choice = rand.nextInt(2);

					switch (choice) {
					case 0:
						effect = new RotateLeftEffect();
						break;
					case 1:
						effect = new RotateRightEffect();
						break;
					default:
						throw new RuntimeException(
								"random generated too large a number! "
										+ choice);
					}
				}

				/* set the effect for the tile! */
				tiles[i][j].setEffect(effect);
			}
		}
	}

	/**
	 * Checks how many neighbours of the specified tile are part of the path
	 * 
	 * @param tiles
	 *            the maze tiles
	 * @param x
	 * @param y
	 */
	private int numPathNeighbours(MazeTile[][] tiles, int x, int y, int size) {
		int neighbours = 0;

		/* check all around ourselves! */
		if (x > 0 && !tiles[x - 1][y].isWall()) {
			neighbours++;
		}
		if (y > 0 && !tiles[x][y - 1].isWall()) {
			neighbours++;
		}
		if (x < size - 1 && !tiles[x + 1][y].isWall()) {
			neighbours++;
		}
		if (y < size - 1 && !tiles[x][y + 1].isWall()) {
			neighbours++;
		}

		return neighbours;
	}

	/*
	 * approximately what percentage of the maze should contain boosts If set to
	 * one, all non-wall and non-start/end tiles will contain boosts. If set to
	 * zero, no tiles will contain any boosts.
	 */
	private static final double SPEED_BOOST_ADUNDANCE = 0.20;
	private static final double ROTATE_BOOST_ABUNDANCE = 0.01;

	/* width/height of the maze (in tiles) */
	private final int mazeSize;
}
