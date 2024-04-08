package aoc;

/**
 * Provides a simple class with two numbers to store position data.
 */
public class Position {
	public int x;
	public int y;

	/**
	 * Creates Point with given parameters
	 * 
	 * @param x
	 *            X position
	 * @param y
	 *            Y position
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * multiplies the positions by the specified value
	 * 
	 * @param value
	 *            multiplicator
	 * @return this
	 */
	public Position scale(int value) {
		x *= value;
		y *= value;
		return this;
	}

	/**
	 * Adds the Position of another point
	 * 
	 * @param point
	 *            is added
	 * @return this
	 */
	public Position add(Position point) {
		x += point.x;
		y += point.y;
		return this;
	}

	/**
	 * returns the sum of both position values
	 * 
	 * @return x + y
	 */
	public int sum() {
		return x + y;
	}

	@Override
	public String toString() {
		return x + "|" + y + ": " + sum();
	}
}
