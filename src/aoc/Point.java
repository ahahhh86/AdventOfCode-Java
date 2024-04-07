package aoc;

public class Point {
	public int x = 0;
	public int y = 0;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point scale(int value) {
		x *= value;
		y *= value;
		return this;
	}

	public Point add(Point point) {
		x += point.x;
		y += point.y;
		return this;
	}

	@Override
	public String toString() {
		return x + "|" + y + ": " + (x + y);
	}
}
