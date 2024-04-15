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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aoc.CharStatistic;
import aoc.Day00;



@SuppressWarnings("javadoc")
public class Day06 extends Day00 {
	private static class RepeatingMessage {
		List<String> input;

		RepeatingMessage(List<String> in) {
			input = in;

			for (var i : input) {
				if (i.length() != input.getFirst().length()) {
					throw new IllegalArgumentException("the size of the messages does not match");
				}

				for (int j = 0; j < i.length(); ++j) {
					if (!Character.isLowerCase(i.charAt(j))) {
						throw new IllegalArgumentException("lower case characters expected");
					}
				}
			}
		}

		String[] getMessage() {
			var inputList = new ArrayList<String>(input.get(0).length());
			for (int i = 0; i < input.get(0).length(); ++i) {
				var buffer = new StringBuilder(input.size());
				for (var j : input) {
					buffer.append(j.charAt(i));
				}
				inputList.add(buffer.toString());
			}

			var result = new StringBuilder(input.size());
			var modifiedResult = new StringBuilder(input.size());

			for (var i : inputList) {
				var buffer = new CharStatistic(i);
				result.append(buffer.getFirst().getChar());
				modifiedResult.append(buffer.getLast().getChar());
			}

			return new String[]{result.toString(), modifiedResult.toString()};
		}
	}

	public Day06() {
		super(2016, 6);
	}

	@Override
	protected void testPuzzle() {
		//@formatter:off
		var input = new ArrayList<>(Arrays.asList(
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

		var msg = new RepeatingMessage(input);
		var x = msg.getMessage();
		io.printTest(x[0], "easter");
		io.printTest(x[1], "advent");
	}

	@Override
	public void solvePuzzle() {
		var msg = new RepeatingMessage(io.readAllLines());
		var x = msg.getMessage();
		io.printResult(x[0], "xdkzukcf");
		io.printResult(x[1], "cevsgyvd");
	}

}
