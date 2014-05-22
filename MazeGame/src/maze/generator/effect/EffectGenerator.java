package maze.generator.effect;

import maze.game.MazeTile;

public interface EffectGenerator {
	/**
	 * Given a maze, it generates boosts within the maze.
	 * 
	 * @param tiles
	 *            the maze to add boosts to.
	 */
	public void generateEffects(MazeTile[][] tiles);
}
