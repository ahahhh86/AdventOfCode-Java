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
	 * Solves the puzzle. Each sub class has to implement own method.
	 */
	protected abstract void solvePuzzle();

	/**
	 * Solves a defined puzzle
	 * 
	 * @param year
	 *          year of the puzzle
	 * @param day
	 *          day of the puzzle
	 */
	public static void solvePuzzle(int year, int day) {
		try {
			@SuppressWarnings("deprecation") // TODO find not deprecated method
			Day00 d = (Day00) Class.forName(String.format("year%4d.Day%02d", year, day)).newInstance();
			if (IO.DEBUG_MODE) {
				d.io.startTests();
				d.testPuzzle();
				d.io.showTestResults();
			}
			d.solvePuzzle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Solves the specified puzzle.
	 * 
	 * @param args
	 *          atm no meaning
	 */
	public static void main(String[] args) {
		solvePuzzle(2016, 10);

		// for (int i = 1; i < 9; ++i) {
		// solvePuzzle(2016, i);
		// System.out.print("\n\n\n");
		// }
	}
}
