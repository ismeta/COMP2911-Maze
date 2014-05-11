package maze.boost;
import maze.Maze;
import maze.MazePlayer;



public interface MazeBoost {
	/**
	 * What the boost will do upon activation
	 * Note it must set the endTime
	 * @param m the Maze game
	 * @param p the player who activated it
	 */
	public void activate(Maze m, MazePlayer p);
	/**
	 * @return when the boost, upon activation, will deactivate
	 */
	public long endTime();
	/**
	 * What the boost will do upon deactivation
	 * @param m the Maze game
	 */
	public void deactivate(Maze m);
}
