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

		private CompressionData decompressPart1(CompressionData d) { // TODO remove unnecessary operations
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


		// TODO: part 2
		private static long getDecompressedLength(String str) {
			if (!str.contains("x")) { return str.length(); }

			var buffer = str.split("[\\(x\\)]", 4);

			int repeatedLength = Integer.parseInt(buffer[1]);
			int timesRepeated = Integer.parseInt(buffer[2]);
			var x1 = buffer[0].length();
			var x2 = getDecompressedLength(buffer[3]);

			var decomprLength = x1 + x2 * timesRepeated;

			return decomprLength;
		}

		private CompressionData decompressPart2(CompressionData d) { // TODO remove unnecessary operations
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
			for (var i = decompressPart2(new CompressionData(0, 0));; i = decompressPart1(i)) {
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



		System.out.println("\n\n");
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
		io.printResult(file.decompressPart2(), 123908L);
	}

}
