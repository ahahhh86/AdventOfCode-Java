package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;



public class IO {
	public static final boolean DEBUG_MODE = true;

	private static final String DEFAULT_COLOUR = "\u001B[39m";
	private static final String TEST_COLOUR = "\u001B[33m";
	private static final String PASS_COLOUR = "\u001B[32m";
	private static final String FAIL_COLOUR = "\u001B[31m";

	private static final String testFormat = "\t%02d | %8s | %24s | %24s | %10s%n";

	// private Date date;
	private long time;
	private final String filename;
	private int partCount = 0;
	private int testCount = 0;
	private int failCount = 0;

	public IO(Date date) {
		// this.date = date;
		time = System.currentTimeMillis();
		filename = "input/" + date.year.toFilename() + date.day.toFilename();
		System.out.println(date + ": ");
	}

	private long elapsedTime() {
		var oldTime = time;
		time = System.currentTimeMillis();
		return (time - oldTime);
	}

	public List<String> readAllLines() {

		try {
			return Files.readAllLines(Paths.get(filename));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(); // TODO better exception
		}
	}

	public List<String> readOneLine(String delimiter) {
		try {
			String str = Files.readAllLines(Paths.get(filename)).getFirst();
			return Arrays.asList(str.split(delimiter));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(); // TODO better exception
		}
	}



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

	public void startTests() {
		if (!DEBUG_MODE) { return; }

		final String testForm = testFormat.replace("02d", "2s");// .replace("8d", "8s");

		time = System.currentTimeMillis();

		System.out.println(TEST_COLOUR + "Testing puzzle: ");
		System.out.printf(testForm, "#", "success", "result", "expected", "time");
	}

	public void printTest(Object result, Object expected) {
		if (!DEBUG_MODE) { return; }

		++testCount;
		boolean success = result.equals(expected);
		if (!success) { ++failCount; }
		var elapsed = NumberFormat.getInstance().format(elapsedTime());
		System.out.printf(testFormat, testCount, success, result, expected, elapsed);
	}

	public void showTestResults() {
		if (!DEBUG_MODE) { return; }

		if (failCount == 0) {
			System.out.println(PASS_COLOUR + "Tests passed: success :)" + DEFAULT_COLOUR + '\n');
		} else {
			System.out.println(FAIL_COLOUR + failCount + " Tests failed" + DEFAULT_COLOUR + '\n');
		}
	}
}
