/*
 * --- Day 2: Bathroom Security ---
 * You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in such a rush that 
 * you forgot to use the bathroom! Fancy office buildings like this one usually have keypad locks on their 
 * bathrooms, so you search the front desk for the code.
 * 
 * "In order to improve security," the document you find says, "bathroom codes will no longer be written 
 * down. Instead, please memorize and follow the procedure below to access the bathrooms."
 * 
 * The document goes on to explain that each button to be pressed can be found by starting on the previous 
 * button and moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and R moves 
 * right. Each line of instructions corresponds to one button, starting at the previous button (or, for 
 * the first line, the "5" button); press whatever button you're on at the end of each line. If a move doesn't 
 * lead to a button, ignore it.
 * 
 * You can't hold it much longer, so you decide to figure out the code as you walk to the bathroom. You 
 * picture a keypad like this:
 * 
 *   1 2 3
 *   4 5 6
 *   7 8 9
 * 
 * Suppose your instructions are:
 * 
 *   ULL
 *   RRDDD
 *   LURDL
 *   UUUUD
 * 
 *     You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"), so the 
 * first button is 1.
 *     Starting from the previous button ("1"), you move right twice (to "3") and then down three times 
 * (stopping at "9" after two moves and ignoring the third), ending up with 9.
 *     Continuing from "9", you move left, up, right, down, and left, ending with 8.
 *     Finally, you move up four times (stopping at "2"), then down once, ending with 5.
 * 
 * So, in this example, the bathroom code is 1985.
 * 
 * Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom 
 * code?
 * 
 * Your puzzle answer was 24862.
 * 
 * 
 * --- Part Two ---
 * You finally arrive at the bathroom (it's a several minute walk from the lobby so visitors can behold 
 * the many fancy conference rooms and water coolers on this floor) and go to punch in the code. Much to 
 * your bladder's dismay, the keypad is not at all like you imagined it. Instead, you are confronted with 
 * the result of hundreds of man-hours of bathroom-keypad-design meetings:
 * 
 *     1
 *   2 3 4
 * 5 6 7 8 9
 *   A B C
 *     D
 * 
 * You still start at "5" and stop when you're at an edge, but given the same instructions as above, the 
 * outcome is very different:
 * 
 *     You start at "5" and don't move at all (up and left are both edges), ending at 5.
 *     Continuing from "5", you move right twice and down three times (through "6", "7", "B", "D", "D"), 
 * ending at D.
 *     Then, from "D", you move five more times (through "D", "B", "C", "C", "B"), ending at B.
 *     Finally, after five more moves, you end at 3.
 * 
 * So, given the actual keypad layout, the code would be 5DB3.
 * 
 * Using the same instructions in your puzzle input, what is the correct bathroom code?
 * 
 * Your puzzle answer was 46C91.
 */

package year2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aoc.Date;
import aoc.Day00;



public class Day02 extends Day00 {
	private static enum Direction {
		UP,
		LEFT,
		DOWN,
		RIGHT;

		static Direction fromChar(char c) {
			return switch (c) {
			case 'U' -> Direction.UP;
			case 'L' -> Direction.LEFT;
			case 'D' -> Direction.DOWN;
			case 'R' -> Direction.RIGHT;
			default -> throw new IllegalArgumentException("Unexpected value: " + c);
			};
		}

		// For Debugging
		// @Override
		// public String toString() {
		// return switch (this) {
		// case UP -> "^";
		// case LEFT -> "<";
		// case DOWN -> "v";
		// case RIGHT -> ">";
		// default -> throw new IllegalArgumentException("Unexpected value: " + this);
		// };
		// }
	}

	private static interface NumPad {
		public NumPad move(Direction d);
	}

	private static enum NumPadSquare implements NumPad {
		NUM1,
		NUM2,
		NUM3,

		NUM4,
		NUM5,
		NUM6,

		NUM7,
		NUM8,
		NUM9;

		private static final int LAST_CHAR = 3;

		@Override
		public String toString() {
			return "" + this.name().charAt(LAST_CHAR);
		}

		@Override
		public NumPadSquare move(Direction d) {
			return switch (d) {
			case UP -> switch (this) {
			case NUM1, NUM2, NUM3 -> this;
			default -> values()[ordinal() - 3];
			};

			case LEFT -> switch (this) {
			case NUM1, NUM4, NUM7 -> this;
			default -> values()[ordinal() - 1];
			};

			case DOWN -> switch (this) {
			case NUM7, NUM8, NUM9 -> this;
			default -> values()[ordinal() + 3];
			};

			case RIGHT -> switch (this) {
			case NUM3, NUM6, NUM9 -> this;
			default -> values()[ordinal() + 1];
			};

			default -> throw new IllegalArgumentException("Unexpected value: " + d);
			};
		}
	}

	private static enum NumPadDiamond implements NumPad {
		NUM1,

		NUM2,
		NUM3,
		NUM4,

		NUM5,
		NUM6,
		NUM7,
		NUM8,
		NUM9,

		NUMA,
		NUMB,
		NUMC,

		NUMD;

		private static final int LAST_CHAR = 3;

		@Override
		public String toString() {
			return "" + this.name().charAt(LAST_CHAR);
		}

		@Override
		public NumPadDiamond move(Direction d) {
			return switch (d) {
			case UP -> switch (this) {
			case NUM1, NUM2, NUM4, NUM5, NUM9 -> this;
			case NUM3 -> NUM1;
			case NUMD -> NUMB;
			default -> values()[ordinal() - 4];
			};

			case LEFT -> switch (this) {
			case NUM1, NUM2, NUM5, NUMA, NUMD -> this;
			default -> values()[ordinal() - 1];
			};

			case DOWN -> switch (this) {
			case NUM5, NUM9, NUMA, NUMC, NUMD -> this;
			case NUM1 -> NUM3;
			case NUMB -> NUMD;
			default -> values()[ordinal() + 4];
			};

			case RIGHT -> switch (this) {
			case NUM1, NUM4, NUM9, NUMC, NUMD -> this;
			default -> values()[ordinal() + 1];
			};

			default -> throw new IllegalArgumentException("Unexpected value: " + d);
			};
		}
	}

	private static class BathroomCode {
		private static final NumPadSquare START_POS_SQUARE = NumPadSquare.NUM5;
		private static final NumPadDiamond START_POS_DIAMOND = NumPadDiamond.NUM5;
		List<List<Direction>> instructions;

		BathroomCode(List<String> input) {
			instructions = new ArrayList<>(input.size());
			input.forEach(i -> {
				List<Direction> buffer = new ArrayList<>(i.length());
				for (int j = 0; j < i.length(); ++j) {
					buffer.add(Direction.fromChar(i.charAt(j)));
				}
				instructions.add(buffer);
			});
		}

		private String findCode(NumPad np) {
			StringBuilder result = new StringBuilder("");
			for (List<Direction> i : instructions) {
				for (Direction j : i) {
					np = np.move(j);
				}
				result.append(np);
			}
			return result.toString();
		}

		String findCodeSquare() {
			NumPadSquare np = START_POS_SQUARE;
			return findCode(np);
		}

		String findCodeDiamond() {
			NumPadDiamond np = START_POS_DIAMOND;
			return findCode(np);
		}
	}

	public Day02() {
		super(new Date(Date.Year.YEAR2016, Date.Day.DAY02));
	}

	@Override
	public void performTests() {
		BathroomCode bc = new BathroomCode(Arrays.asList("ULL", "RRDDD", "LURDL", "UUUUD"));
		io.printTest(bc.findCodeSquare(), "1985");
		io.printTest(bc.findCodeDiamond(), "5DB3");

	}

	@Override
	public void solvePuzzle() {
		BathroomCode bc = new BathroomCode(io.readAllLines());
		io.printResult(bc.findCodeSquare(), "24862");
		io.printResult(bc.findCodeDiamond(), "46C91");
	}
}
