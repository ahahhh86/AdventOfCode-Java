/*
 * https://adventofcode.com/2016/day/10
 *
 * --- Day 10: Balance Bots ---
 *
 * You come upon a factory in which many robots are zooming around handing small microchips to each other.
 *
 * Upon closer examination, you notice that each bot only proceeds when it has two microchips, and once
 * it does, it gives each one to a different bot or puts it in a marked "output" bin. Sometimes, bots take
 * microchips from "input" bins, too.
 *
 * Inspecting one of the microchips, it seems like they each contain a single number; the bots must use
 * some logic to decide what to do with each chip. You access the local control computer and download the
 * bots' instructions (your puzzle input).
 *
 * Some of the instructions specify that a specific-valued microchip should be given to a specific bot;
 * the rest of the instructions indicate what a given bot should do with its lower-value or higher-value
 * chip.
 *
 * For example, consider the following instructions:
 *     value 5 goes to bot 2
 *     bot 2 gives low to bot 1 and high to bot 0
 *     value 3 goes to bot 1
 *     bot 1 gives low to output 1 and high to bot 0
 *     bot 0 gives low to output 2 and high to output 0
 *     value 2 goes to bot 2
 *
 *     Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a value-2 chip and a value-5 chip.
 *
 *     Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and its higher one (5) to bot
 *     0.
 *
 *     Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and gives the value-3 chip to
 *     bot 0.
 *
 *     Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
 *
 * In the end, output bin 0 contains a value-5 microchip, output bin 1 contains a value-2 microchip, and
 * output bin 2 contains a value-3 microchip. In this configuration, bot number 2 is responsible for comparing
 * value-5 microchips with value-2 microchips.
 *
 * Based on your instructions, what is the number of the bot that is responsible for comparing value-61
 * microchips with value-17 microchips?
 *
 * Your puzzle answer was 147.
 *
 *
 * --- Part Two ---
 *
 * What do you get if you multiply together the values of one chip in each of outputs 0, 1, and 2?
 *
 * Your puzzle answer was 55637.
 */

package year2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aoc.Day00;



@SuppressWarnings("javadoc")
public class Day10 extends Day00 {
	static final int INVALID = -1;

	private static class Chip {
		int value = INVALID;
		final int reciver;
		final boolean toOutput;

		Chip(boolean output, int reciver) {
			this.reciver = reciver;
			this.toOutput = output;
		}

		Chip(String output, String reciver) {
			this(output.equals("output"), Integer.parseInt(reciver));
		}
	}

	private static class Bot {
		private Chip lowChip;
		private Chip highChip;

		Bot(Chip lowChipReciver, Chip highChipReciver) {
			lowChip = new Chip(lowChipReciver.toOutput, lowChipReciver.reciver);
			highChip = new Chip(highChipReciver.toOutput, highChipReciver.reciver);
		}

		boolean proceed() {
			return lowChip.value > INVALID && highChip.value > INVALID;
		}

		void giveChip(int chipValue) {
			if (proceed()) { throw new RuntimeException("Bot already has two chips"); }
			if (chipValue <= INVALID) { throw new IllegalArgumentException("Unexpected value: " + chipValue); }

			if (highChip.value > chipValue) {
				lowChip.value = chipValue;
			} else {
				lowChip.value = highChip.value;
				highChip.value = chipValue;
			}
		}

		void removeChips() {
			lowChip.value = INVALID;
			highChip.value = INVALID;
		}
	}



	private static class BotList {
		private final int lowCompare;
		private final int highCompare;

		private int[] output = {INVALID, INVALID, INVALID};
		private int part1Number = INVALID;
		private List<Bot> bots = new ArrayList<>();

		BotList(List<String> input, int compare1, int compare2) {
			input.sort((o1, o2) -> compareInput(o1, o2));

			input.forEach(i -> {
				if (i.startsWith("bot ")) {
					addInstruction(i);
				} else if (i.startsWith("value ")) {
					addValue(i);
				} else {
					throw new IllegalArgumentException("Unexpected value: " + i);
				}
			});

			if (compare1 < compare2) {
				lowCompare = compare1;
				highCompare = compare2;
			} else {
				lowCompare = compare2;
				highCompare = compare1;
			}
		}

		// sort: bot 1, bot 2, ..., value 1, ...
		private static int compareInput(String c1, String c2) {
			var buffer1 = c1.split(" ", 3);
			var buffer2 = c2.split(" ", 3);
			if (buffer1[0].equals(buffer2[0])) {
				Integer i1 = Integer.parseInt(buffer1[1]);
				Integer i2 = Integer.parseInt(buffer2[1]);
				return i1.compareTo(i2);
			}
			return buffer1[0].compareTo(buffer2[0]);
		}

		private void addInstruction(String str) {
			// example: bot 2 gives low to bot 1 and high to bot 0
			var buffer = str.split(" ");
			int botNr = Integer.parseInt(buffer[1]);
			if (botNr != bots.size()) {
				throw new IllegalArgumentException("Unexpected value: " + botNr + ". There is a bot missing");
			}
			var lowRecieve = new Chip(buffer[5], buffer[6]);
			var highRecieve = new Chip(buffer[10], buffer[11]);
			bots.add(new Bot(lowRecieve, highRecieve));
		}

		private void addValue(String str) {
			// example: value 5 goes to bot 2
			var buffer = str.split(" ");
			int botNr = Integer.parseInt(buffer[5]);
			int chipValue = Integer.parseInt(buffer[1]);
			bots.get(botNr).giveChip(chipValue);
		}

		private void setOutput(Chip c) {
			int index = c.reciver;
			if (index >= output.length) { return; }
			output[index] = c.value;
		}

		private void processBot(Bot bot) {
			var lowBot = bots.get(bot.lowChip.reciver);
			if (!bot.lowChip.toOutput) {
				lowBot.giveChip(bot.lowChip.value);
			} else {
				setOutput(bot.lowChip);
			}

			var highBot = bots.get(bot.highChip.reciver);
			if (!bot.highChip.toOutput) {
				highBot.giveChip(bot.highChip.value);
			} else {
				setOutput(bot.highChip);
			}

			bot.removeChips();
		}

		private boolean hasNoOutput() {
			for (var i : output) {
				if (i == INVALID) { return true; }
			}
			return false;
		}

		void process() {
			while (part1Number == INVALID || hasNoOutput()) {
				boolean changed = false;

				for (int i = 0; i < bots.size(); ++i) {// TODO check only the bots that received a chip
					var bot = bots.get(i);

					if (bot.proceed()) {
						changed = true;
						if (part1Number == INVALID && bot.lowChip.value == lowCompare && bot.highChip.value == highCompare) {
							part1Number = i;
						}
						processBot(bot);
					}
				}

				if (!changed) { throw new RuntimeException("The loop is infinite"); }
			}
		}

		int getOutput() {
			int result = 1;
			for (var i : output) {
				result *= i;
			}
			return result;
		}
	}



	public Day10() {
		super(2016, 10);
	}

	@Override
	protected void testPuzzle() {
		//@formatter:off
		var bots = new BotList(Arrays.asList(
				"value 5 goes to bot 2",
				"bot 2 gives low to bot 1 and high to bot 0",
				"value 3 goes to bot 1",
				"bot 1 gives low to output 1 and high to bot 0",
				"bot 0 gives low to output 2 and high to output 0",
				"value 2 goes to bot 2"
		 ), 5 , 2);
		//@formatter:on
		bots.process();
		io.printTest(bots.part1Number, 2);
		io.printTest(bots.getOutput(), 5 * 2 * 3);
	}

	@Override
	protected void solvePuzzle() {
		var bots = new BotList(io.readAllLines(), 61, 17);
		bots.process();
		io.printResult(bots.part1Number, 147);
		io.printResult(bots.getOutput(), 55637);
	}

}
