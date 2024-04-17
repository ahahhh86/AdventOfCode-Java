/*
 * https://adventofcode.com/2016/day/8
 *
 * --- Day 8: Two-Factor Authentication ---
 *
 * You come across a door implementing what you can only assume is an implementation of two-factor authentication
 * after a long game of requirements telephone.
 *
 * To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). Then, it
 * displays a code on a little screen, and you type that code on a keypad. Then, presumably, the door unlocks.
 *
 *
 * Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart and figured
 * out how it works. Now you just have to work out what the screen would have displayed.
 *
 * The magnetic strip on the card you swiped encodes a series of instructions for the screen; these instructions
 * are your puzzle input. The screen is 50 pixels wide and 6 pixels tall, all of which start off, and is
 * capable of three somewhat peculiar operations:
 *
 *     rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide
 * and B tall.
 *     rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels. Pixels
 * that would fall off the right end appear at the left end of the row.
 *     rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels.
 * Pixels that would fall off the bottom appear at the top of the column.
 *
 * For example, here is a simple sequence on a smaller screen:
 *
 *     rect 3x2 creates a small rectangle in the top-left corner:
 *
 *     ###....
 *     ###....
 *     .......
 *
 *     rotate column x=1 by 1 rotates the second column down by one pixel:
 *
 *     #.#....
 *     ###....
 *     .#.....
 *
 *     rotate row y=0 by 4 rotates the top row right by four pixels:
 *
 *     ....#.#
 *     ###....
 *     .#.....
 *
 *     rotate column x=1 by 1 again rotates the second column down by one pixel, causing the bottom pixel
 * to wrap back to the top:
 *
 *     .#..#.#
 *     #.#....
 *     .#.....
 *
 * As you can see, this display technology is extremely powerful, and will soon dominate the tiny-code-displaying-screen
 * market. That's what the advertisement on the back of the display tries to convince you, anyway.
 *
 * There seems to be an intermediate check of the voltage used by the display: after you swipe your card,
 * if the screen did work, how many pixels should be lit?
 *
 * Your puzzle answer was 128.
 * --- Part Two ---
 *
 * You notice that the screen is only capable of displaying capital letters; in the font it uses, each letter
 * is 5 pixels wide and 6 tall.
 *
 * After you swipe your card, what code is the screen trying to display?
 *
 * Your puzzle answer was EOARGPHYAO.
 */

package year2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import aoc.Day00;
import aoc.Grid;



@SuppressWarnings("javadoc")
public class Day08 extends Day00 {
	private static enum Operation {
		RECT,
		ROTATE_ROW,
		ROTATE_COLUMN;
	}

	private static record Instruction(Operation operation, int a, int b) {
		private static Instruction createRect(String str) {
			var buffer = str.split("x", 2);
			return new Instruction(Operation.RECT, Integer.parseInt(buffer[0]), Integer.parseInt(buffer[1]));
		}

		private static Instruction createRotate(String str) {
			var buffer = str.split(" ");
			var op = switch (buffer[0]) {
			case "row" -> Operation.ROTATE_ROW;
			case "column" -> Operation.ROTATE_COLUMN;
			default -> throw new IllegalArgumentException("Unexpected value: Rotate " + buffer[0]);
			};

			var a = Integer.parseInt(buffer[1].substring(2)); // ignore x= or y=
			if (!buffer[2].equals("by")) { throw new IllegalArgumentException("Unexpected value: " + buffer[2]); }
			var b = Integer.parseInt(buffer[3]);
			return new Instruction(op, a, b);
		}

		static Instruction create(String str) {
			var buffer = str.split(" ", 2);
			return switch (buffer[0]) {
			case "rect" -> createRect(buffer[1]);
			case "rotate" -> createRotate(buffer[1]);
			default -> throw new IllegalArgumentException("Unexpected value: " + buffer[0]);
			};
		}

		static List<Instruction> createList(List<String> input) {
			var buffer = new LinkedList<Instruction>();
			input.forEach(line -> buffer.add(Instruction.create(line)));
			return Collections.unmodifiableList(buffer);
		}
	}



	private static class Screen extends Grid<Boolean> {
		private List<Instruction> instructions;

		Screen(int width, int height, List<String> input) {
			super(width, height, false);
			instructions = Instruction.createList(input);
		}

		int countPixel() {
			return Collections.frequency(list(), true);
		}

		private void rect(int a, int b) {
			for (int i = 0; i < a; ++i) {
				for (int j = 0; j < b; ++j) {
					set(i, j, true);
				}
			}
		}

		private void rotateRow(int row, int value) {
			var width = size().x;
			var result = new ArrayList<Boolean>(width);
			for (int i = 0; i < width; ++i) {
				result.add(get(i, row));
			}
			Collections.rotate(result, value);
			for (int i = 0; i < width; ++i) {
				set(i, row, result.get(i));
			}
		}

		private void rotateColumn(int column, int value) {
			var height = size().y;
			var result = new ArrayList<Boolean>(height);
			for (int i = 0; i < height; ++i) {
				result.add(get(column, i));
			}
			Collections.rotate(result, value);
			for (int i = 0; i < height; ++i) {
				set(column, i, result.get(i));
			}
		}

		private void performInstruction(Instruction instruction) {
			switch (instruction.operation) {
			case RECT :
				rect(instruction.a, instruction.b);
				break;
			case ROTATE_ROW :
				rotateRow(instruction.a, instruction.b);
				break;
			case ROTATE_COLUMN :
				rotateColumn(instruction.a, instruction.b);
				break;
			default :
				throw new IllegalArgumentException("Unexpected value: " + instruction.operation);
			}
		}

		public void performInstructions() {
			instructions.forEach(i -> performInstruction(i));
		}

		@Override
		public String toString() {
			class C {
				static final boolean COLORED_OUTPUT = true;
				static final String ON_STRING = COLORED_OUTPUT ? "\u001B[47m  " : "#";
				static final String OFF_STRING = COLORED_OUTPUT ? "\u001B[40m  " : " ";
				static final String DEFAULT_COLOUR = "\u001B[0m";
				static final int LETTER_WIDTH = 5;
			}

			var result = new StringBuilder();
			var i = iterator();

			for (int count = 0; i.hasNext(); ++count) {
				if (count % size().x == 0 && count != 0) { result.append('\n'); }
				if (count % C.LETTER_WIDTH == 0) { result.append(C.OFF_STRING); }

				result.append(i.next() ? C.ON_STRING : C.OFF_STRING);
			}

			if (C.COLORED_OUTPUT) { result.append(C.DEFAULT_COLOUR); }
			return result.toString();
		}
	}

	public Day08() {
		super(2016, 8);
	}

	@Override
	protected void testPuzzle() {
		//@formatter:off
		var input = Arrays.asList(
				"rect 3x2",
				"rotate column x=1 by 1",
				"rotate row y=0 by 4",
				"rotate column x=1 by 1"
		);
		//@formatter:on

		var screen = new Screen(7, 3, input);
		screen.performInstructions();
		io.printTest(screen.countPixel(), 6);
	}

	@Override
	public void solvePuzzle() {
		var screen = new Screen(50, 6, io.readAllLines());
		screen.performInstructions();
		io.printResult(screen.countPixel(), 128);
		System.out.println("\t02 | expected: EOARGPHYAO");
		System.out.println("\n" + screen);
	}

}
