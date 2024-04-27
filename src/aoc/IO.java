package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



/**
 * handles the input (*.txt file) and console output of the puzzles
 */
public class IO {
	/**
	 * are we debugging the puzzles (are the tests printed)? atm hardcoded
	 */
	public static final boolean DEBUG_MODE = true;

	private static final String DEFAULT_COLOUR = "\u001B[39m";
	private static final String TEST_COLOUR = "\u001B[33m";
	private static final String PASS_COLOUR = "\u001B[32m";
	private static final String FAIL_COLOUR = "\u001B[31m";

	private static final String testFormat = "\t%02d | %8s | %32s | %32s | %10s%n";

	private long time;
	private final String filename;
	private int partCount = 0;
	private int testCount = 0;
	private int failCount = 0;

	/**
	 * Constructor
	 * 
	 * @param year
	 *          year of the puzzle
	 * @param day
	 *          day of the puzzle
	 */
	public IO(int year, int day) {
		validate(year, day);
		time = System.currentTimeMillis();
		filename = String.format("input/%4d/Day%02d.txt", year, day);
		System.out.printf("Year %4d | Day %02d:%n", year, day);
	}

	private static void validate(int year, int day) {
		assert (0 < day || day < 26) : "day has to be between 1 and 25";
		assert (2014 < year) : "year has to be above 2015";
	}

	private long elapsedTime() {
		var oldTime = time;
		time = System.currentTimeMillis();
		return (time - oldTime);
	}



	/**
	 * Gets the content of the input file for the specified date
	 * 
	 * @return all lines of the file
	 */
	public List<String> readAllLines() {
		try {
			return Files.readAllLines(Paths.get(filename));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not read file :" + filename);
		}
	}

	/**
	 * Gets the content of the input file for the specified date
	 * 
	 * @param delimiter
	 *          delimiter
	 * @return all lines of the file split at the delimiter
	 */
	public List<String> readAllLines(String delimiter) {
		try {
			var strs = Files.readAllLines(Paths.get(filename));
			var result = new LinkedList<String>();
			for (var line : strs) {
				result.addAll(Arrays.asList(line.split(delimiter)));
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not read file :" + filename);
		}
	}



	/**
	 * prints the result of the puzzle. If in debug mode also checks versus an expected value and returns how long the
	 * calculation took
	 * 
	 * @param result
	 *          the result of the puzzle
	 * @param expected
	 *          the expected result of the puzzle
	 */
	public void printResult(Object result, Object expected) {
		++partCount;
		if (DEBUG_MODE) {
			boolean success = result.equals(expected);
			var elapsed = NumberFormat.getInstance().format(elapsedTime());
			System.out.printf(testFormat, partCount, success, result, expected, elapsed);
		} else {
			System.out.printf("\tPart %d: %16s%n", partCount, result);
		}
	}

	/**
	 * Initialises the tests: resets the time, prints the header of the test table and changes colour
	 */
	public void startTests() {
		if (!DEBUG_MODE) { return; }

		final String testForm = testFormat.replace("02d", "2s");// .replace("8d", "8s");

		time = System.currentTimeMillis();

		System.out.println(TEST_COLOUR + "Testing puzzle: ");
		System.out.printf(testForm, "#", "success", "result", "expected", "time");
	}

	/**
	 * if in debug mode: prints the test of the puzzle, checks if it has the expected result and how long the calculations
	 * took
	 * 
	 * @param result
	 *          the result of the test
	 * @param expected
	 *          the expected result of the test
	 */
	public void printTest(Object result, Object expected) {
		if (!DEBUG_MODE) { return; }

		++testCount;
		boolean success = result.equals(expected);
		if (!success) { ++failCount; }
		var elapsed = NumberFormat.getInstance().format(elapsedTime());
		System.out.printf(testFormat, testCount, success, result, expected, elapsed);
	}

	/**
	 * prints the test results and returns to the default colour
	 */
	public void showTestResults() {
		if (!DEBUG_MODE) { return; }

		if (failCount == 0) {
			System.out.println(PASS_COLOUR + "Tests passed: success :)" + DEFAULT_COLOUR + '\n');
		} else {
			System.out.println(FAIL_COLOUR + failCount + " Tests failed" + DEFAULT_COLOUR + '\n');
		}
	}
}
