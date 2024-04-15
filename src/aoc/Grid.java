package aoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



/**
 * Acts as a two dimensional array. Internally it uses a one dimensional List of E.
 * 
 * @param <E>
 *          type of the array
 */
public class Grid<E> implements Iterable<E> {
	private final int width;
	private final int height;
	private List<E> list;

	/**
	 * Constructor
	 * 
	 * @param width
	 *          width of the two dimensional array
	 * @param height
	 *          height of the two dimensional array
	 * @param o
	 *          default value of the elements
	 */
	public Grid(int width, int height, E o) {
		this.width = width;
		this.height = height;
		list = new ArrayList<>(Collections.nCopies(width * height, o));
	}

	/**
	 * Constructor
	 * 
	 * @param size
	 *          size of the array (x = width, y = height).
	 * @param o
	 *          default value of the elements
	 */
	public Grid(Position size, E o) {
		this(size.x, size.y, o);
	}

	private int from2Dto1D(int x, int y) {
		return x + width * y;
	}

	/**
	 * returns the element at position (x,y) of the array.
	 * 
	 * @param x
	 *          x position
	 * @param y
	 *          y position
	 * @return element of array
	 */
	public E get(int x, int y) {
		return list.get(from2Dto1D(x, y));
	}


	/**
	 * returns the element at a specified position of the array.
	 * 
	 * @param pos
	 *          at position
	 * @return element of array
	 */
	public E get(Position pos) {
		return get(pos.x, pos.y);
	}

	/**
	 * sets the element at position (x,y) of the array.
	 * 
	 * @param x
	 *          x position
	 * @param y
	 *          y position
	 * @param element
	 *          new element
	 * @return old element
	 */
	public E set(int x, int y, E element) {
		return list.set(from2Dto1D(x, y), element);
	}


	/**
	 * sets the element at position (x,y) of the array.
	 * 
	 * @param pos
	 *          at position
	 * @param element
	 *          new element
	 * @return old element
	 */
	public E set(Position pos, E element) {
		return set(pos.x, pos.y, element);
	}

	/**
	 * swaps the elements of the array
	 * 
	 * @param a
	 *          first position
	 * @param b
	 *          second position
	 */
	public void swap(Position a, Position b) {
		Collections.swap(list, from2Dto1D(a.x, a.y), from2Dto1D(b.x, b.y));
	}

	/**
	 * size of the array
	 * 
	 * @return size (x=width, y=height)
	 */
	public Position size() {
		return new Position(width, height);
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public String toString() {
		var result = new StringBuilder("Grid " + width + "x" + height + ":\n");

		for (int i = 0; i < height; ++i) {
			for (int j = 0; j < width; ++j) {
				result.append(get(j, i).toString() + ",");
			}
			result.setCharAt(result.length() - 1, '\n');
		}
		result.deleteCharAt(result.length() - 1);

		return result.toString();
	}
}
