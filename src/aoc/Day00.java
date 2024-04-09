package aoc;

public abstract class Day00 {
	protected IO io;

	public Day00(Date date) {
		io = new IO(date);
	}


	public abstract void performTests();

	public final void testPuzzle() {
		if (!IO.DEBUG_MODE) { return; }
		io.startTests();
		performTests();
		io.showTestResults();
	}

	public abstract void solvePuzzle();

	public static void main(String[] args) {
		Day00 d = new year2016.Day02();
		d.testPuzzle();
		d.solvePuzzle();
	}
}
