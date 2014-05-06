
public interface Maze {
	/**
	 * Given a point on the maze map, determines whether it is
	 * a wall or not.
	 * @param row
	 * @param column
	 * @return if the specified location is a wall.
	 */
	public boolean isWall(int row, int column);
}
