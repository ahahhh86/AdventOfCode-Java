package aoc;

import java.util.Objects;



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
	 * Creates Point where x and y are equal
	 * 
	 * @param p
	 *            position
	 */
	public Position(int p) {
		this(p, p);
	}

	/**
	 * Copy constructor
	 * 
	 * @param p
	 *            original Position
	 */
	public Position(Position p) {
		this(p.x, p.y);
	}

	/**
	 * multiplies the positions by the specified value
	 * 
	 * @param value
	 *            multiplicator
	 * @return {@code this}
	 */
	public Position scale(int value) {
		x *= value;
		y *= value;
		return this;
	}

	/**
	 * Adds both values to {@code this}
	 * 
	 * @param point
	 *            point is added
	 * @return {@code this}
	 */
	public Position add(Position point) {
		x += point.x;
		y += point.y;
		return this;
	}

	/**
	 * checks if {@code this} point to the same position as {@code p}
	 */
	public boolean equals(Position p) {
		return this.x == p.x && this.y == p.y;
	}

	@Override
	/**
	 * converts to a {@code String}
	 */
	public String toString() {
		return x + "|" + y;
	}

	@Override
	/**
	 * creates a hashCode. For use in collections
	 */
	public int hashCode() {
		return Objects.hash(x, y);
	}

	/**
	 * checks if {@ obj} is the same as {@code this}. For use in collections
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		return this.equals((Position) obj);
	}
}
