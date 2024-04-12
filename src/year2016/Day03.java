/*
 * https://adventofcode.com/2016/day/3
 * 
 * --- Day 3: Squares With Three Sides ---
 *
 * Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that
 * makes up this part of Easter Bunny HQ. This must be a graphic design department; the walls are covered
 * in specifications for triangles.
 *
 * Or are they?
 *
 * The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these
 * aren't triangles. You can't help but mark the impossible ones.
 *
 * In a valid triangle, the sum of any two sides must be larger than the remaining side. For example, the
 * "triangle" given above is impossible, because 5 + 10 is not larger than 25.
 *
 * In your puzzle input, how many of the listed triangles are possible?
 *
 * Your puzzle answer was 983.
 *
 *
 * --- Part Two ---
 *
 * Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified
 * in groups of three vertically. Each set of three numbers in a column specifies a triangle. Rows are unrelated.
 *
 * For example, given the following specification, numbers with the same hundreds digit would be part of
 * the same triangle:
 *     101 301 501
 *     102 302 502
 *     103 303 503
 *     201 401 601
 *     202 402 602
 *     203 403 603
 *
 * In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?
 *
 * Your puzzle answer was 1836.
 */

package year2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aoc.Date;
import aoc.Day00;



public class Day03 extends Day00 {
	private static final int SIDE_COUNT = 3;

	private static List<Integer> getLine(String in) {
		var strList = new ArrayList<>(Arrays.asList(in.trim().split(" +")));
		if (strList.size() != SIDE_COUNT) { throw new IllegalArgumentException("Triangle only works with 3 sides"); }

		var result = new ArrayList<Integer>(SIDE_COUNT);
		strList.forEach(i -> result.add(Integer.parseInt(i)));
		return result;
	}

	private static record Triangle(List<Integer> sides) {
		Triangle(int a, int b, int c) {
			this(Arrays.asList(a, b, c));
		}

		Triangle(String in) {
			this(getLine(in));
		}

		boolean isValid() {
			// @formatter:off
			return (sides.get(0) < sides.get(1) + sides.get(2))
					&& (sides.get(1) < sides.get(0) + sides.get(2))
					&& (sides.get(2) < sides.get(0) + sides.get(1));
			// @formatter:on
		}

		// For Debugging
		// @Override
		// public String toString() {
		// return String.format("[%d|%d|%d]", sides.get(0), sides.get(1), sides.get(2));
		// }
	}

	private static List<Triangle> readFileHorizontal(List<String> input) {
		var result = new ArrayList<Triangle>(input.size());
		input.forEach(i -> {
			result.add(new Triangle(i));
		});
		return result;
	}

	private static List<List<Integer>> transpose(List<List<Integer>> matrix) {
		if (matrix.size() != matrix.get(0).size()) { throw new IllegalArgumentException("Invalid matrix size."); }

		var loopEnd = matrix.size();
		var result = new ArrayList<List<Integer>>(loopEnd);

		for (int i = 0; i < loopEnd; ++i) {
			var buffer = new ArrayList<Integer>(loopEnd);
			for (int j = 0; j < loopEnd; ++j) {
				buffer.add(matrix.get(j).get(i));
			}
			result.add(buffer);
		}

		return result;
	}

	private static List<Triangle> readFileVertical(List<String> input) {
		if (input.size() % SIDE_COUNT != 0) { throw new IllegalArgumentException("Invalid input size."); }

		var result = new ArrayList<Triangle>(input.size());
		var loopEnd = input.size() - SIDE_COUNT + 1;

		for (int i = 0; i < loopEnd; i += SIDE_COUNT) {
			//@formatter:off
			var buffer = Arrays.asList(
					getLine(input.get(i)),
					getLine(input.get(i + 1)),
					getLine(input.get(i + 2))
			);
			//@formatter:on
			buffer = transpose(buffer);

			for (var line : buffer) {
				result.add(new Triangle(line));
			}
		}

		return result;
	}

	private static int countValid(List<Triangle> triangles) {
		var result = 0;
		for (var i : triangles) {
			if (i.isValid()) { ++result; }
		}
		return result;
	}

	public Day03() {
		super(Date.Year.YEAR2016, Date.Day.DAY03);
	}

	@Override
	public void performTests() {
		var tri = new Triangle(5, 10, 25);
		io.printTest(tri.isValid(), false);

		tri = new Triangle(10, 5, 25);
		io.printTest(tri.isValid(), false);

		tri = new Triangle(25, 10, 5);
		io.printTest(tri.isValid(), false);

		tri = new Triangle(3, 3, 3);
		io.printTest(tri.isValid(), true);

		tri = new Triangle(1, 5, 5);
		io.printTest(tri.isValid(), true);
	}

	@Override
	public void solvePuzzle() {
		var input = io.readAllLines();
		var triangles = readFileHorizontal(input);
		io.printResult(countValid(triangles), 983);

		triangles = readFileVertical(input);
		io.printResult(countValid(triangles), 1836);
	}

}
