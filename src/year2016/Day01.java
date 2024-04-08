/*
 * https://adventofcode.com/2016/day/1
 *
 * --- Day 1: No Time for a Taxicab ---
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
 *   Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
 *   R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
 *   R5, L5, R5, R3 leaves you 12 blocks away.
 *
 * How many blocks away is Easter Bunny HQ?
 *
 * Your puzzle answer was 236.
 *
 *
 * --- Part Two ---
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

import aoc.Date;
import aoc.IO;
import aoc.Position;



public class Day01 {
	private static enum Turn {
		RIGHT,
		LEFT;

		static Turn fromChar(char c) {
			return switch (c) {
			case 'R'	-> Turn.RIGHT;
			case 'L'	-> Turn.LEFT;
			default		-> throw new IllegalArgumentException("Unexpected value: " + c);
			};
		}
	}

	private static class Instruction {
		Turn turn;
		int length;

		Instruction(String in) {
			turn = Turn.fromChar(in.charAt(0));
			length = Integer.parseInt(in.substring(1));
		}
	}

	private static enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST;

		Position getVector() {
			return switch (this) {
			case NORTH	-> new Position(1, 0);
			case EAST	-> new Position(0, 1);
			case SOUTH	-> new Position(-1, 0);
			case WEST	-> new Position(0, -1);
			default		-> throw new IllegalArgumentException("Unexpected value: " + name());
			};
		}

		Direction turn(Turn turn) {
			return switch (this) {
			case NORTH	-> (turn == Turn.RIGHT) ? EAST : WEST;
			case EAST	-> (turn == Turn.RIGHT) ? SOUTH : NORTH;
			case SOUTH	-> (turn == Turn.RIGHT) ? WEST : EAST;
			case WEST	-> (turn == Turn.RIGHT) ? NORTH : SOUTH;
			default		-> throw new IllegalArgumentException("Unexpected value: " + name());
			};
		}
	}

	private static class City {
		List<Instruction> instructions;
		Position position = new Position(0, 0);
		Direction direction = Direction.NORTH;

		City(List<String> input) {
			instructions = new ArrayList<>(input.size());
			input.forEach(i -> instructions.add(new Instruction(i)));
		}

		void walk(Instruction i) {
			direction = direction.turn(i.turn);
			Position newPos = direction.getVector().scale(i.length);
			position.add(newPos);
		}

		int getShortestPath() {
			instructions.forEach(i -> walk(i));
			return Math.abs(position.x) + Math.abs(position.y);
		}
	}

	IO io;
	List<String> input;

	Day01() {
		io = new IO(new Date(Date.Year.YEAR2016, Date.Day.DAY01));
		input = io.readOneLine(", ");

	}

	public void testPuzzle() {
		io.startTests();

		City c = new City(Arrays.asList("R2", "L3"));
		io.printTest(c.getShortestPath(), 5);

		c = new City(Arrays.asList("R2", "R2", "R2"));
		io.printTest(c.getShortestPath(), 2);

		c = new City(Arrays.asList("R5", "L5", "R5", "R3"));
		io.printTest(c.getShortestPath(), 12);

		io.showTestResults();
	}

	public void solvePuzzle() {
		City c = new City(input);
		io.printResult(c.getShortestPath(), 236);
	}

	public static void main(String[] args) {
		Day01 d = new Day01();
		d.testPuzzle();
		d.solvePuzzle();
	}

}
