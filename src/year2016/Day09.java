/*
 * https://adventofcode.com/2016/day/9
 *
 * --- Day 9: Explosives in Cyberspace ---
 *
 * Wandering around a secure area, you come across a datalink port to a new part of the network. After briefly
 * scanning it for interesting files, you find one file in particular that catches your attention. It's
 * compressed with an experimental format, but fortunately, the documentation for the format is nearby.
 *
 * The format compresses a sequence of characters. Whitespace is ignored. To indicate that some sequence
 * should be repeated, a marker is added to the file, like (10x2). To decompress this marker, take the subsequent
 * 10 characters and repeat them 2 times. Then, continue reading the file after the repeated data. The marker
 * itself is not included in the decompressed output.
 *
 * If parentheses or other characters appear within the data referenced by a marker, that's okay - treat
 * it like normal data, not a marker, and then resume looking for markers after the decompressed section.
 *
 * For example:
 *     ADVENT contains no markers and decompresses to itself with no changes, resulting in a decompressed
 *     length of 6.
 *
 *     A(1x5)BC repeats only the B a total of 5 times, becoming ABBBBBC for a decompressed length of 7.
 *
 *     (3x3)XYZ becomes XYZXYZXYZ for a decompressed length of 9.
 *
 *     A(2x2)BCD(2x2)EFG doubles the BC and EF, becoming ABCBCDEFEFG for a decompressed length of 11.
 *
 *     (6x1)(1x3)A simply becomes (1x3)A - the (1x3) looks like a marker, but because it's within a data
 *     section of another marker, it is not treated any differently from the A that comes after it. It has a
 *     decompressed length of 6.
 *
 *     X(8x2)(3x3)ABCY becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18), because the decompressed
 *     data from the (8x2) marker (the (3x3)ABC) is skipped and not processed further.
 *
 * What is the decompressed length of the file (your puzzle input)? Don't count whitespace.
 *
 * Your puzzle answer was 123908.
 *
 *
 * --- Part Two ---
 *
 * Apparently, the file actually uses version two of the format.
 *
 * In version two, the only difference is that markers within decompressed data are decompressed. This,
 * the documentation explains, provides much more substantial compression capabilities, allowing many-gigabyte
 * files to be stored in only a few kilobytes.
 *
 * For example:
 *     (3x3)XYZ still becomes XYZXYZXYZ, as the decompressed section contains no markers.
 *
 *     X(8x2)(3x3)ABCY becomes XABCABCABCABCABCABCY, because the decompressed data from the (8x2) marker
 *     is then further decompressed, thus triggering the (3x3) marker twice for a total of six ABC sequences.
 *
 *     (27x12)(20x12)(13x14)(7x10)(1x12)A decompresses into a string of A repeated 241920 times.
 *
 *     (25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN becomes 445 characters long.
 *
 * Unfortunately, the computer you brought probably doesn't have enough memory to actually decompress the
 * file; you'll have to come up with another way to get its decompressed length.
 *
 * What is the decompressed length of the file using this improved format?
 *
 * Your puzzle answer was 10755693147.
 */

// TODO make nicer / refactoring

package year2016;

import aoc.Day00;



@SuppressWarnings("javadoc")
public class Day09 extends Day00 {
	private record CompressionData(int start, long size) {
	}

	private class CompressedFile {
		private final String source;

		CompressedFile(String input) {
			source = input;
			if (!source.matches("[A-Z\\(0-9x\\)]+")) { throw new IllegalArgumentException("Unexpected input value"); }
		}

		private CompressionData decompressPart1(CompressionData d) {
			var start = d.start;
			var size = d.size;

			int posOpen = source.substring(start).indexOf('(');
			if (posOpen < 0) { return new CompressionData(-1, size + source.length() - start); }

			posOpen += start; // set to actual position
			size += posOpen - start;
			++posOpen; // continue after '('
			int posClose = source.substring(posOpen).indexOf(')') + posOpen;
			var marker = source.substring(posOpen, posClose).split("x");
			int repeatedLength = Integer.parseInt(marker[0]);
			int timesRepeated = Integer.parseInt(marker[1]);
			size += repeatedLength * timesRepeated;

			return new CompressionData(posClose + repeatedLength + 1, size);
		}

		public long decompressPart1() {
			for (var i = decompressPart1(new CompressionData(0, 0));; i = decompressPart1(i)) {
				if (i.start < 0) { return i.size; }
			}
		}

		private static long getDecompressedLength(String str) {
			if (!str.contains("x")) { return str.length(); }

			var buffer = str.split("[\\(x\\)]", 4);

			int repeatedLength = Integer.parseInt(buffer[1]);
			int timesRepeated = Integer.parseInt(buffer[2]);
			var x1 = buffer[0].length();
			var x2 = getDecompressedLength(buffer[3].substring(0, repeatedLength));
			var x3 = getDecompressedLength(buffer[3].substring(repeatedLength));

			var decomprLength = x1 + x2 * timesRepeated + x3;

			return decomprLength;
		}

		private CompressionData decompressPart2(CompressionData d) {
			var start = d.start;
			var size = d.size;

			int posOpen = source.substring(start).indexOf('(');
			if (posOpen < 0) { return new CompressionData(-1, size + source.length() - start); }

			posOpen += start; // set to actual position
			size += posOpen - start;
			++posOpen; // continue after '('
			int posClose = source.substring(posOpen).indexOf(')') + posOpen;
			var marker = source.substring(posOpen, posClose).split("x");
			int repeatedLength = Integer.parseInt(marker[0]);
			int timesRepeated = Integer.parseInt(marker[1]);
			size += getDecompressedLength(source.substring(posClose + 1, posClose + repeatedLength + 1)) * timesRepeated;

			return new CompressionData(posClose + repeatedLength + 1, size);
		}

		public long decompressPart2() {
			for (var i = decompressPart2(new CompressionData(0, 0));; i = decompressPart2(i)) {
				if (i.start < 0) { return i.size; }
			}
		}
	}



	public Day09() {
		super(2016, 9);
	}

	@Override
	protected void testPuzzle() {
		var file = new CompressedFile("ADVENT");
		io.printTest(file.decompressPart1(), 6L);

		file = new CompressedFile("A(1x5)BC");
		io.printTest(file.decompressPart1(), 7L);

		file = new CompressedFile("(3x3)XYZ");
		io.printTest(file.decompressPart1(), 9L);

		file = new CompressedFile("A(2x2)BCD(2x2)EFG");
		io.printTest(file.decompressPart1(), 11L);

		file = new CompressedFile("(6x1)(1x3)A");
		io.printTest(file.decompressPart1(), 6L);

		file = new CompressedFile("X(8x2)(3x3)ABCY");
		io.printTest(file.decompressPart1(), 18L);



		System.out.println();
		file = new CompressedFile("(3x3)XYZ");
		io.printTest(file.decompressPart2(), 9L);

		file = new CompressedFile("X(8x2)(3x3)ABCY");
		io.printTest(file.decompressPart2(), 20L);

		file = new CompressedFile("(27x12)(20x12)(13x14)(7x10)(1x12)A");
		io.printTest(file.decompressPart2(), 241920L);

		file = new CompressedFile("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN");
		io.printTest(file.decompressPart2(), 445L);
	}

	@Override
	protected void solvePuzzle() {
		var file = new CompressedFile(io.readAllLines().getFirst());
		io.printResult(file.decompressPart1(), 123908L);
		io.printResult(file.decompressPart2(), 10755693147L);
	}

}
