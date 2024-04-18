/*
 * https://adventofcode.com/2016/day/6
 *
 * --- Day 6: Signals and Noise ---
 *
 * Something is jamming your communications with Santa. Fortunately, your signal is only partially jammed,
 * and protocol in situations like this is to switch to a simple repetition code to get the message through.
 *
 *
 * In this model, the same message is sent repeatedly. You've recorded the repeating message signal (your
 * puzzle input), but the data seems quite corrupted - almost too badly to recover. Almost.
 *
 * All you need to do is figure out which character is most frequent for each position. For example, suppose
 * you had recorded the following messages:
 *     eedadn
 *     drvtee
 *     eandsr
 *     raavrd
 *     atevrs
 *     tsrnev
 *     sdttsa
 *     rasrtv
 *     nssdts
 *     ntnada
 *     svetve
 *     tesnvt
 *     vntsnd
 *     vrdear
 *     dvrsen
 *     enarar
 *
 * The most common character in the first column is e; in the second, a; in the third, s, and so on. Combining
 * these characters returns the error-corrected message, easter.
 *
 * Given the recording in your puzzle input, what is the error-corrected version of the message being sent?
 *
 * Your puzzle answer was xdkzukcf.
 *
 *
 * --- Part Two ---
 *
 * Of course, that would be the message - if you hadn't agreed to use a modified repetition code instead.
 *
 * In this modified code, the sender instead transmits what looks like random data, but for each character,
 * the character they actually want to send is slightly less likely than the others. Even after signal-jamming
 * noise, you can look at the letter distributions in each column and choose the least common letter to
 * reconstruct the original message.
 *
 * In the above example, the least common character in the first column is a; in the second, d, and so on.
 * Repeating this process for the remaining characters produces the original message, advent.
 *
 * Given the recording in your puzzle input and this new decoding methodology, what is the original message
 * that Santa is trying to send?
 *
 * Your puzzle answer was cevsgyvd.
 */

package year2016;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import aoc.CharStatistic;
import aoc.Day00;
import aoc.TwoResults;



@SuppressWarnings("javadoc")
public class Day06 extends Day00 {
	private static record RepeatingMessage(List<String> messages) {
		RepeatingMessage {
			final int msgLength = messages.getFirst().length();

			for (var msg : messages) {
				if (msg.length() != msgLength) {
					throw new IllegalArgumentException("the size of the messages does not match");
				}

				if (!msg.matches("[a-z]+")) { throw new IllegalArgumentException("lower case characters expected"); }
			}
		}

		private List<String> createColumns() {
			var result = new LinkedList<String>();

			for (int i = 0; i < messages.get(0).length(); ++i) {
				var buffer = new StringBuilder(messages.size());
				for (var j : messages) {
					buffer.append(j.charAt(i));
				}
				result.add(buffer.toString());
			}

			return result;
		}

		TwoResults<String> correctErrors() {
			var msgColumn = createColumns();
			var part1 = new StringBuilder(messages.size());
			var part2 = new StringBuilder(messages.size());

			msgColumn.forEach(str -> {
				var stat = new CharStatistic(str);

				part1.append(stat.getFirst().chr()); // most common letter
				part2.append(stat.getLast().chr()); // least common letter
			});

			return new TwoResults<>(part1.toString(), part2.toString());
		}
	}



	public Day06() {
		super(2016, 6);
	}

	@Override
	protected void testPuzzle() {
		//@formatter:off
		var input = new LinkedList<>(Arrays.asList(
			"eedadn",
			"drvtee",
			"eandsr",
			"raavrd",
			"atevrs",
			"tsrnev",
			"sdttsa",
			"rasrtv",
			"nssdts",
			"ntnada",
			"svetve",
			"tesnvt",
			"vntsnd",
			"vrdear",
			"dvrsen",
			"enarar"
		));
		//@formatter:on

		var msg = new RepeatingMessage(input).correctErrors();
		io.printTest(msg.part1(), "easter");
		io.printTest(msg.part2(), "advent");
	}

	@Override
	public void solvePuzzle() {
		var msg = new RepeatingMessage(io.readAllLines()).correctErrors();
		io.printResult(msg.part1(), "xdkzukcf");
		io.printResult(msg.part2(), "cevsgyvd");
	}

}
