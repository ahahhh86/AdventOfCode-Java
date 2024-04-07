package aoc;

public class UnitTester {
	private static final String TEST_COLOUR = "\u001B[33m";
	private static final String PASS_COLOUR = "\u001B[32m";
	private static final String FAIL_COLOUR = "\u001B[31m";
	private static final String DEFAULT_COLOUR = "\u001B[39m";

	private static final String testFormat = "\t%02d | %8s | %16s | %16s | %8d%n";

	private int testCount = 0;
	private int failCount = 0;
	private long startTime;

	public UnitTester() {
		final String testForm = testFormat.replace("02d", "2s").replace("8d", "8s");

		startTime = System.currentTimeMillis();

		System.out.println(TEST_COLOUR + "Testing puzzle: ");
		System.out.printf(testForm, "#", "success", "comment", "exprected", "output", "time");
	}

	private long elapsedTime() {
		return (System.currentTimeMillis() - startTime);
	}

	public void printTest(Object test1, Object test2) {
		++testCount;
		boolean success = test1.equals(test2);
		if (!success) { ++failCount; }
		System.out.printf(testFormat, testCount, success, test1, test2, elapsedTime());
	}

	public void showResult() {
		if (failCount == 0) {
			System.out.println(PASS_COLOUR + "Tests passed: success :)" + DEFAULT_COLOUR);
		} else {
			System.out.println(FAIL_COLOUR + failCount + " Tests failed" + DEFAULT_COLOUR);
		}
	}

}
