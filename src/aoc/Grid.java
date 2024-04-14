package aoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;



public class Grid<E> implements Iterable<E> {
	private final int width;
	private final int height;
	private List<E> list;

	public Grid(int width, int height, E o) {
		this.width = width;
		this.height = height;
		list = new ArrayList<>(Collections.nCopies(width * height, o));
	}

	public Grid(Position size, E o) {
		this(size.x, size.y, o);
	}

	private int from2Dto1D(int x, int y) {
		return x + width * y;
	}

	public E get(int x, int y) {
		return list.get(from2Dto1D(x, y));
	}

	public E get(Position pos) {
		return get(pos.x, pos.y);
	}

	public E set(int x, int y, E element) {
		return list.set(from2Dto1D(x, y), element);
	}

	public E set(Position pos, E element) {
		return set(pos.x, pos.y, element);
	}

	public void swap(Position a, Position b) {
		Collections.swap(list, from2Dto1D(a.x, a.y), from2Dto1D(b.x, b.y));
	}

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
