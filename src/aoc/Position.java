package aoc;

import java.util.Objects;



/**
 * Provides a simple class with two numbers to store position data.
 */
public class Position {
	/**
	 * position of x direction
	 */
	public int x;
	/**
	 * position of y direction
	 */
	public int y;

	/**
	 * constructor
	 * 
	 * @param x
	 *          X position
	 * @param y
	 *          Y position
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * constructor where x and y are equal
	 * 
	 * @param p
	 *          position
	 */
	public Position(int p) {
		this(p, p);
	}

	/**
	 * (deep) copy constructor
	 * 
	 * @param p
	 *          original Position
	 */
	public Position(Position p) {
		this(p.x, p.y);
	}

	/**
	 * multiplies both values (x,y) by the specified value
	 * 
	 * @param value
	 *          multiplicator
	 * @return the result
	 */
	public Position scale(int value) {
		x *= value;
		y *= value;
		return this;
	}

	/**
	 * Adds a two positions
	 * 
	 * @param pos
	 *          position is added
	 * @return the result
	 */
	public Position add(Position pos) {
		x += pos.x;
		y += pos.y;
		return this;
	}

	@Override
	public String toString() {
		return x + "|" + y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}


	/**
	 * specialised equals() method, if both objects are of Position class
	 * 
	 * @param p
	 *          compare to
	 * @return true if values of the positions are equal
	 */
	public boolean equals(Position p) {
		return this.x == p.x && this.y == p.y;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		return this.equals((Position) obj);
	}
}
