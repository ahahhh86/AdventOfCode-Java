/*
 * https://adventofcode.com/2016/day/7
 *
 * --- Day 7: Internet Protocol Version 7 ---
 *
 * While snooping around the local network of EBHQ, you compile a list of IP addresses (they're IPv7, of
 * course; IPv6 is much too limited). You'd like to figure out which IPs support TLS (transport-layer snooping).
 *
 * An IP supports TLS if it has an Autonomous Bridge Bypass Annotation, or ABBA. An ABBA is any four-character
 * sequence which consists of a pair of two different characters followed by the reverse of that pair, such
 * as xyyx or abba. However, the IP also must not have an ABBA within any hypernet sequences, which are
 * contained by square brackets.
 *
 * For example:
 *     abba[mnop]qrst supports TLS (abba outside square brackets).
 *     abcd[bddb]xyyx does not support TLS (bddb is within square brackets, even though xyyx is outside square brackets).
 *     aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
 *     ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even though it's within a larger string).
 *
 * How many IPs in your puzzle input support TLS?
 *
 * Your puzzle answer was 110.
 *
 *
 * --- Part Two ---
 *
 * You would also like to know which IPs support SSL (super-secret listening).
 *
 * An IP supports SSL if it has an Area-Broadcast Accessor, or ABA, anywhere in the supernet sequences (outside
 * any square bracketed sections), and a corresponding Byte Allocation Block, or BAB, anywhere in the hypernet
 * sequences. An ABA is any three-character sequence which consists of the same character twice with a different
 * character between them, such as xyx or aba. A corresponding BAB is the same characters but in reversed
 * positions: yxy and bab, respectively.
 *
 * For example:
 *     aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
 *
 *     xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
 *
 *     aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet; the aaa sequence is
 *     not related, because the interior character must be different).
 *
 *     zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb, even though
 *     zaz and zbz overlap).
 *
 * How many IPs in your puzzle input support SSL?
 *
 * Your puzzle answer was 242.
 */

package year2016;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import aoc.Day00;



@SuppressWarnings("javadoc")
public class Day07 extends Day00 {
	private static record IpAddress(List<String> outsideBrackets, List<String> insideBrackets) {
		private static boolean containsABBA(String str) {
			final int ABBA_LENGTH = 4;
			if (str.length() < ABBA_LENGTH) { return false; }

			int loopEnd = str.length() - ABBA_LENGTH + 1;
			for (int i = 0; i < loopEnd; ++i) {
				//@formatter:off
				if ( str.charAt(i)     == str.charAt(i + 3)
					&& str.charAt(i + 1) == str.charAt(i + 2)
					&& str.charAt(i)     != str.charAt(i + 1)) {
						return true;
				}
				//@formatter:on
			}
			return false;
		}

		boolean hasTLS() {
			for (var str : insideBrackets) {
				if (containsABBA(str)) { return false; }
			}
			for (var str : outsideBrackets) {
				if (containsABBA(str)) { return true; }
			}
			return false;
		}



		private List<String> findABA() {// returns List of BAB
			final int ABA_LENGTH = 3;
			var result = new LinkedList<String>();

			for (var str : outsideBrackets) {
				if (str.length() < ABA_LENGTH) { continue; }
				int loopEnd = str.length() - ABA_LENGTH + 1;
				for (int i = 0; i < loopEnd; ++i) {
					var a = str.charAt(i);
					var b = str.charAt(i + 1);
					if (a == str.charAt(i + 2) && a != b) { result.add("" + b + a + b); }
				}
			}

			return result;
		}

		private boolean containsBAB(String bab) {
			for (var str : insideBrackets) {
				if (str.indexOf(bab) >= 0) { return true; }
			}
			return false;
		}

		boolean hasSSL() {
			var babList = findABA();
			for (var bab : babList) {
				if (containsBAB(bab)) { return true; }
			}
			return false;
		}



		static IpAddress create(String ipAdress) {
			var outsideBrackets = new LinkedList<String>();
			var insideBrackets = new LinkedList<String>();
			var splitAddress = Arrays.asList(ipAdress.split("[\\[\\]]"));
			boolean outside = true;

			for (var addressPart : splitAddress) {
				if (outside) {
					outsideBrackets.add(addressPart);
				} else {
					insideBrackets.add(addressPart);
				}
				outside = !outside;
			}

			return new IpAddress(Collections.unmodifiableList(outsideBrackets), Collections.unmodifiableList(insideBrackets));
		}
	}



	private static class AddressList {
		private List<IpAddress> list;

		AddressList(List<String> input) {
			list = new LinkedList<>();
			input.forEach(str -> list.add(IpAddress.create(str)));
		}

		long countTLS() {
			return list.stream().filter(i -> i.hasTLS()).count();
		}

		long countSSL() {
			return list.stream().filter(i -> i.hasSSL()).count();
		}
	}



	public Day07() {
		super(2016, 7);
	}

	@Override
	protected void testPuzzle() {
		//@formatter:off
		var input1 = Arrays.asList(new String[]{
				"abba[mnop]qrst",
				"abcd[bddb]xyyx",
				"aaaa[qwer]tyui",
				"ioxxoj[asdfgh]zxcvbn"
			});
		var input2 = Arrays.asList(new String[]{
				"aba[bab]xyz",
				"xyx[xyx]xyx",
				"aaa[kek]eke",
				"zazbz[bzb]cdb"
			});
		//@formatter:on

		var test = IpAddress.create(input1.get(0));
		io.printTest(test.hasTLS(), true);

		test = IpAddress.create(input1.get(1));
		io.printTest(test.hasTLS(), false);

		test = IpAddress.create(input1.get(2));
		io.printTest(test.hasTLS(), false);

		test = IpAddress.create(input1.get(3));
		io.printTest(test.hasTLS(), true);

		var tests = new AddressList(input1);
		io.printTest(tests.countTLS(), 2L);


		test = IpAddress.create(input2.get(0));
		io.printTest(test.hasSSL(), true);

		test = IpAddress.create(input2.get(1));
		io.printTest(test.hasSSL(), false);

		test = IpAddress.create(input2.get(2));
		io.printTest(test.hasSSL(), true);

		test = IpAddress.create(input2.get(3));
		io.printTest(test.hasSSL(), true);

		tests = new AddressList(input2);
		io.printTest(tests.countSSL(), 3L);
	}

	@Override
	public void solvePuzzle() {
		var adresses = new AddressList(io.readAllLines());
		io.printResult(adresses.countTLS(), 110L);
		io.printResult(adresses.countSSL(), 242L);
	}

}
