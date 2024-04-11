package aoc;

public abstract class Day00 {
	protected IO io;

	public Day00(Date.Year year, Date.Day day) {
		io = new IO(new Date(year, day));
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
		Day00 d = new year2016.Day05();
		d.testPuzzle();
		d.solvePuzzle();
	}
}
