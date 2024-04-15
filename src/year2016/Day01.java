/*
 * https://adventofcode.com/2016/day/1
 *
 * --- Day 1: No Time for a Taxicab ---
 *
 * Santa's sleigh uses a very high-precision clock to guide its movements, and the clock's oscillator is
 * regulated by stars. Unfortunately, the stars have been stolen... by the Easter Bunny. To save Christmas,
 * Santa needs you to retrieve all fifty stars by December 25th.
 *
 * Collect stars by solving puzzles. Two puzzles will be made available on each day in the Advent calendar;
 * the second puzzle is unlocked when you complete the first. Each puzzle grants one star. Good luck!
 *
 * You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately, is as close
 * as you can get - the instructions on the Easter Bunny Recruiting Document the Elves intercepted start
 * here, and nobody had time to work them out further.
 *
 * The Document indicates that you should start at the given coordinates (where you just landed) and face
 * North. Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees, then walk forward
 * the given number of blocks, ending at a new intersection.
 *
 * There's no time to follow such ridiculous instructions on foot, though, so you take a moment and work
 * out the destination. Given that you can only walk on the street grid of the city, how far is the shortest
 * path to the destination?
 *
 * For example:
 *     Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
 *     R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
 *     R5, L5, R5, R3 leaves you 12 blocks away.
 *
 * How many blocks away is Easter Bunny HQ?
 *
 * Your puzzle answer was 236.
 *
 *
 * --- Part Two ---
 *
 * Then, you notice the instructions continue on the back of the Recruiting Document. Easter Bunny HQ is
 * actually at the first location you visit twice.
 *
 * For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks
 * away, due East.
 *
 * How many blocks away is the first location you visit twice?
 *
 * Your puzzle answer was 182.
*/

package year2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aoc.Day00;
import aoc.Position;



@SuppressWarnings("javadoc")
public class Day01 extends Day00 {
	private static enum Turn {
		RIGHT,
		LEFT;

		static Turn fromChar(char c) {
			return switch (c) {
			case 'R' -> RIGHT;
			case 'L' -> LEFT;
			default -> throw new IllegalArgumentException("Unexpected value: " + c);
			};
		}
	}

	private static record Instruction(Turn turn, int length) {
		Instruction(String in) {
			this(Turn.fromChar(in.charAt(0)), Integer.parseInt(in.substring(1)));
		}
	}

	private static enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST;

		Position getVector() {
			return switch (this) {
			case NORTH -> new Position(1, 0);
			case EAST -> new Position(0, 1);
			case SOUTH -> new Position(-1, 0);
			case WEST -> new Position(0, -1);
			default -> throw new IllegalArgumentException("Unexpected value: " + name());
			};
		}

		Direction turn(Turn turn) {
			return switch (this) {
			case NORTH -> (turn == Turn.RIGHT) ? EAST : WEST;
			case EAST -> (turn == Turn.RIGHT) ? SOUTH : NORTH;
			case SOUTH -> (turn == Turn.RIGHT) ? WEST : EAST;
			case WEST -> (turn == Turn.RIGHT) ? NORTH : SOUTH;
			default -> throw new IllegalArgumentException("Unexpected value: " + name());
			};
		}
	}

	private static class City {
		private static final Direction START_DIRECTION = Direction.NORTH;
		private static final Position START_POS = new Position(0, 0);
		private List<Instruction> instructions;

		City(List<String> input) {
			instructions = new ArrayList<>(input.size());
			input.forEach(i -> instructions.add(new Instruction(i)));
		}

		private interface Part0 {
			Position walk();
		}

		private class Part1 implements Part0 {
			private Position pos = new Position(START_POS);
			private Direction direction = START_DIRECTION;

			private void walkOnce(Instruction i) {
				direction = direction.turn(i.turn);
				var newPos = direction.getVector().scale(i.length);
				pos.add(newPos);
			}

			@Override
			public Position walk() {
				instructions.forEach(i -> walkOnce(i));
				return new Position(pos);
			}

		}

		private class Part2 implements Part0 {
			private static final Position INVALID_POS = new Position(Integer.MIN_VALUE);

			List<Position> positions = new ArrayList<>(Arrays.asList(START_POS));
			private Direction direction = START_DIRECTION;

			private Position walkOnce(Instruction instruction) {
				direction = direction.turn(instruction.turn);
				for (int i = 0; i < instruction.length; ++i) {
					var newPos = new Position(positions.getLast());
					newPos.add(direction.getVector());

					if (positions.contains(newPos)) { return newPos; }
					positions.add(newPos);
				}

				return new Position(INVALID_POS);
			}

			@Override
			public Position walk() {
				for (Instruction i : instructions) {
					var pos = walkOnce(i);
					if (!pos.equals(INVALID_POS)) { return pos; }
				}

				throw new RuntimeException("Headquarter not found!"); // TODO better exception
			}

		}

		private static int getShortestRoute(Part0 p) {
			var pos = p.walk();
			return Math.abs(pos.x) + Math.abs(pos.y);
		}

		int getShortestRoutePart1() {
			return getShortestRoute(new Part1());
		}

		int getShortestRoutePart2() {
			return getShortestRoute(new Part2());
		}
	}

	public Day01() {
		super(2016, 1);
	}

	@Override
	protected void testPuzzle() {
		var c = new City(Arrays.asList("R2", "L3"));
		io.printTest(c.getShortestRoutePart1(), 5);

		c = new City(Arrays.asList("R2", "R2", "R2"));
		io.printTest(c.getShortestRoutePart1(), 2);

		c = new City(Arrays.asList("R5", "L5", "R5", "R3"));
		io.printTest(c.getShortestRoutePart1(), 12);

		c = new City(Arrays.asList("R8", "R4", "R4", "R8"));
		io.printTest(c.getShortestRoutePart2(), 4);
	}

	@Override
	public void solvePuzzle() {
		var c = new City(io.readAllLines(", "));
		io.printResult(c.getShortestRoutePart1(), 236);
		io.printResult(c.getShortestRoutePart2(), 182);
	}
}
