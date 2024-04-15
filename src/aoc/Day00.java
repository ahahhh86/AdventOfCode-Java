package aoc;

/**
 * Super class for each puzzle (Day##).
 */
public abstract class Day00 {
	protected IO io;

	/**
	 * Constructor
	 * 
	 * @param year
	 *          year of the puzzle
	 * @param day
	 *          day of the puzzle
	 */
	public Day00(int year, int day) {
		io = new IO(year, day);
	}


	/**
	 * Test the puzzle. Each puzzle has some hints, if your code is working. Each sub class has to implement own method.
	 */
	protected abstract void testPuzzle();

	/**
	 * Starts the tests, if in debug mode
	 */
	public final void performTests() {
		if (!IO.DEBUG_MODE) { return; }
		io.startTests();
		testPuzzle();
		io.showTestResults();
	}

	/**
	 * Solves the puzzle. Each sub class has to implement own method.
	 */
	public abstract void solvePuzzle();

	/**
	 * Solves the specified puzzle.
	 * 
	 * @param args
	 *          atm no meaning
	 */
	// TODO: solve all puzzles or use args to determine which puzzle to solve
	public static void main(String[] args) {
		Day00 d = new year2016.Day08();
		d.performTests();
		d.solvePuzzle();
	}
}
