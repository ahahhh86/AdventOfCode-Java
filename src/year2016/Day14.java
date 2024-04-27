/*
 * https://adventofcode.com/2016/day/14
 * 
 * 
 */

package year2016;

import java.util.HexFormat;

import aoc.Day00;
import aoc.MD5Generator;



@SuppressWarnings("javadoc")
public class Day14 extends Day00 {
	@SuppressWarnings("unused")
	private static class MD5GeneratorStretch extends MD5Generator {
		private final int stretchCount;

		public MD5GeneratorStretch(String salt, int stretchCount) {
			super(salt);
			this.stretchCount = stretchCount;
		}

		@Override
		public String createHashHex(int index) {
			var buffer = super.createHashHex(index);
			for (int i = 0; i < stretchCount; ++i) {
				buffer = HexFormat.of().formatHex(createHash(buffer));
			}
			return buffer;
		}
	}



	private static class Keys {
		private static final char INVALID_KEY = '\0';

		private MD5Generator hashGenerator;

		Keys(MD5Generator generator) {
			hashGenerator = generator;
		}

		private static char has3InARow(String hash) {
			final int SAME_CHARACTER_COUNT = 3;
			if (hash.length() < SAME_CHARACTER_COUNT) { return INVALID_KEY; }

			final var loopEnd = hash.length() - SAME_CHARACTER_COUNT;
			for (int i = 0; i < loopEnd; ++i) {
				char chr = hash.charAt(i);
				if (chr == hash.charAt(i + 1) && chr == hash.charAt(i + 2)) { return chr; }
			}

			return INVALID_KEY;
		}

		private static boolean has5InARow(String hash, char value) {
			final int SAME_CHARACTER_COUNT = 5;
			if (hash.length() < SAME_CHARACTER_COUNT) { return false; }

			final var loopEnd = hash.length() - SAME_CHARACTER_COUNT;
			for (int i = 0; i < loopEnd; ++i) {
				boolean found = true;
				for (int j = 0; j < SAME_CHARACTER_COUNT; ++j) {
					if (value != hash.charAt(i + j)) {
						found = false;
						break;
					}
				}
				if (found) { return true; }
			}

			return false;
		}

		private boolean find5InARow(int startIndex, char tripleChar) {
			var endLoop = startIndex + 1000;

			for (int i = startIndex; i < endLoop; ++i) {
				var hash = hashGenerator.createHashHex(i);
				if (has5InARow(hash, tripleChar)) { return true; }
			}

			return false;
		}

		private boolean isKey(int index) {
			var hash = hashGenerator.createHashHex(index);
			var tripleChar = has3InARow(hash);
			if (tripleChar == INVALID_KEY) { return false; }
			return find5InARow(index + 1, tripleChar);
		}

		int generateKeys() {
			final int KEYS_NEEDED = 64;

			int index = 0;
			for (int i = 1; i < KEYS_NEEDED; ++i, ++index) {
				while (!isKey(index)) {
					++index;
				}
			}
			return index - 1;
		}
	}



	public Day14() {
		super(2016, 14);
	}

	@Override
	protected void testPuzzle() {
		var keys = new Keys(new MD5Generator("abc"));
		io.printTest(keys.generateKeys(), 22728);

		// keys = new Keys(new MD5GeneratorStretch("abc", 2016));
		// io.printTest(keys.generateKeys(), 22551);
	}

	@Override
	protected void solvePuzzle() {
		var input = io.readAllLines().getFirst();
		Keys keys = new Keys(new MD5Generator(input));
		io.printResult(keys.generateKeys(), 23890);

		// keys = new Keys(new MD5GeneratorStretch(input, 2016));//TODO too slow
		// io.printResult(keys.generateKeys(), 23890);
	}

}
