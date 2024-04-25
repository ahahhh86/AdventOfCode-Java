/*
 * https://adventofcode.com/2016/day/12
 * 
 * --- Day 12: Leonardo's Monorail ---
 *
 * You finally reach the top floor of this building: a garden with a slanted glass ceiling. Looks like there
 * are no more stars to be had.
 *
 * While sitting on a nearby bench amidst some tiger lilies, you manage to decrypt some of the files you
 * extracted from the servers downstairs.
 *
 * According to these documents, Easter Bunny HQ isn't just this building - it's a collection of buildings
 * in the nearby area. They're all connected by a local monorail, and there's another building not far from
 * here! Unfortunately, being night, the monorail is currently not operating.
 *
 * You remotely connect to the monorail control systems and discover that the boot sequence expects a password.
 * The password-checking logic (your puzzle input) is easy to extract, but the code it uses is strange:
 * it's assembunny code designed for the new computer you just assembled. You'll have to execute the code
 * and get the password.
 *
 * The assembunny code you've extracted operates on four registers (a, b, c, and d) that start at 0 and
 * can hold any integer. However, it seems to make use of only a few instructions:
 *     cpy x y copies x (either an integer or the value of a register) into register y.
 *     inc x increases the value of register x by one.
 *     dec x decreases the value of register x by one.
 *     jnz x y jumps to an instruction y away (positive means forward; negative means backward), but only if x is not zero.
 *
 * The jnz instruction moves relative to itself: an offset of -1 would continue at the previous instruction,
 * while an offset of 2 would skip over the next instruction.
 *
 * For example:
 *     cpy 41 a
 *     inc a
 *     inc a
 *     dec a
 *     jnz a 2
 *     dec a
 *
 * The above code would set register a to 41, increase its value by 2, decrease its value by 1, and then
 * skip the last dec a (because a is not zero, so the jnz a 2 skips it), leaving register a at 42. When
 * you move past the last instruction, the program halts.
 *
 * After executing the assembunny code in your puzzle input, what value is left in register a?
 *
 * Your puzzle answer was 318009.
 *
 *
 * --- Part Two ---
 *
 * As you head down the fire escape to the monorail, you notice it didn't start; register c needs to be
 * initialized to the position of the ignition key.
 *
 * If you instead initialize register c to be 1, what value is now left in register a?
 *
 * Your puzzle answer was 9227663.
 */

package year2016;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import aoc.Day00;



@SuppressWarnings("javadoc")
public class Day12 extends Day00 {
	private static enum Action {
		COPY,
		INC,
		DEC,
		JUMP_NOT_ZERO;

		static Action fromString(String str) {
			return switch (str) {
			case "cpy" -> COPY;
			case "inc" -> INC;
			case "dec" -> DEC;
			case "jnz" -> JUMP_NOT_ZERO;
			default -> throw new IllegalArgumentException("Unexpected value: " + str);
			};
		}
	}

	private static record Instruction(Action action, String x, String y) {
		static final String REGISTER = "[a-d]";
		static final String NUMBER = "[-0-9]+";

		Instruction {
			if (!x.matches(REGISTER) && !x.matches(NUMBER)) {
				throw new IllegalArgumentException("Unexpected value for x: " + x);
			}

			if (y != "" && !y.matches(REGISTER) && !y.matches(NUMBER)) {
				throw new IllegalArgumentException("Unexpected value for y: " + y);
			}
		}

		static Instruction create(String str) {
			var buffer = str.split(" ");
			var action = Action.fromString(buffer[0]);
			var x = buffer[1];
			var y = (action == Action.COPY || action == Action.JUMP_NOT_ZERO) ? buffer[2] : "";
			return new Instruction(action, x, y);
		}

		static List<Instruction> create(List<String> input) {
			var result = new LinkedList<Instruction>();
			input.forEach(str -> result.add(create(str)));
			return result;
		}
	}



	private static class Assembunny {
		List<Integer> registers = Arrays.asList(0, 0, 0, 0);
		List<Instruction> instructions;

		Assembunny(List<String> input) {
			instructions = Instruction.create(input);
		}

		Assembunny(List<String> input, int c) {
			this(input);
			registers.set(registerNameToNumber("c"), c);
		}

		private static int registerNameToNumber(String name) {
			if (name.length() != 1) { throw new IllegalArgumentException("Unexpected value length: " + name); }
			var c = name.charAt(0);

			return switch (c) {
			case 'a', 'b', 'c', 'd' -> c - 'a';
			default -> throw new IllegalArgumentException("Unexpected value: " + name);
			};
		}

		private int getRegisterOrNumber(String name) {
			if (name.matches(Instruction.REGISTER)) { return registers.get(registerNameToNumber(name)); }
			return Integer.parseInt(name);
		}

		private int performInstruction(int index) {
			var inst = instructions.get(index);

			switch (inst.action) {
			case COPY :
				registers.set(registerNameToNumber(inst.y), getRegisterOrNumber(inst.x));
				break;
			case DEC :
				var registerDec = registerNameToNumber(inst.x);
				registers.set(registerDec, registers.get(registerDec) - 1);
				break;
			case INC :
				var registerInc = registerNameToNumber(inst.x);
				registers.set(registerInc, registers.get(registerInc) + 1);
				break;
			case JUMP_NOT_ZERO :
				if (getRegisterOrNumber(inst.x) != 0) { return index + Integer.parseInt(inst.y); }
				break;
			default :
				throw new IllegalArgumentException("Unexpected value: " + inst.action);
			}

			return index + 1;
		}

		int performInstructions() {
			for (int i = 0; i < instructions.size(); i = performInstruction(i)) {
				// do nothing, all done in line above
			}
			return registers.get(0);
		}
	}



	public Day12() {
		super(2016, 12);
	}

	@Override
	protected void testPuzzle() {
		//@formatter:off
		var input = Arrays.asList(
				"cpy 41 a",
				"inc a",
				"inc a",
				"dec a",
				"jnz a 2",
				"dec a"
		);
		//@formatter:on
		var instructions = new Assembunny(input);
		io.printTest(instructions.performInstructions(), 42);
	}

	@Override
	protected void solvePuzzle() {
		var instructions = new Assembunny(io.readAllLines());
		io.printTest(instructions.performInstructions(), 318009);

		instructions = new Assembunny(io.readAllLines(), 1);
		io.printTest(instructions.performInstructions(), 9227663);
	}

}
