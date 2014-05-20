package maze.generator.boost;

import maze.game.MazeTile;

public interface BoostGenerator {
	/**
	 * Given a maze, it generates boosts within the maze.
	 * @param tiles the maze to add boosts to.
	 */
	public void generateBoosts(MazeTile[][] tiles);
}
