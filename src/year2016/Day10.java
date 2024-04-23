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

// TODO make nicer / refactoring

package year2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aoc.Day00;



@SuppressWarnings("javadoc")
public class Day10 extends Day00 {
	static final int NO_CHIP = -1;

	private static class Chip {
		int value = NO_CHIP;
		int reciver = NO_CHIP;
		boolean output = false;


		Chip() {
		}

		Chip(String output, String reciver) {
			this.reciver = Integer.parseInt(reciver);
			this.output = output.equals("output");
		}

		@Override
		public String toString() {
			return "" + value + ": " + (output ? "o" : "b") + reciver;
		}
	}

	private static class Bot {
		private Chip lowChip = new Chip();
		private Chip highChip = new Chip();
		private final int number;

		Bot(int number) {
			this.number = number;
			if (number < 0) { throw new IllegalArgumentException("Unexpected value: " + number); }
		}

		Bot(int number, int chipValue) {
			this(number);
			giveChip(chipValue);
		}

		Bot(int number, Chip lowChipReciver, Chip highChipReciver) {
			this(number);
			setReciver(lowChipReciver, highChipReciver);
		}

		int getNumber() {
			return number;
		}

		void giveChip(int chipValue) {
			if (proceed()) { throw new RuntimeException("Bot already has two chips"); }
			if (chipValue <= NO_CHIP) { throw new IllegalArgumentException("Unexpected value: " + chipValue); }

			if (lowChip.value > NO_CHIP) {
				if (lowChip.value < chipValue) {
					highChip.value = chipValue;
				} else {
					highChip.value = lowChip.value;
					lowChip.value = chipValue;
				}
			} else {// TODO remove ?
				if (highChip.value > chipValue) {
					lowChip.value = chipValue;
				} else {
					lowChip.value = highChip.value;
					highChip.value = chipValue;
				}
			}
		}

		void removeChips() {
			lowChip.value = NO_CHIP;
			highChip.value = NO_CHIP;
		}

		void setReciver(Chip lowChipReciver, Chip highChipReciver) {
			lowChip.output = lowChipReciver.output;
			lowChip.reciver = lowChipReciver.reciver;
			highChip.output = highChipReciver.output;
			highChip.reciver = highChipReciver.reciver;
		}

		boolean proceed() {
			return lowChip.value > NO_CHIP && highChip.value > NO_CHIP;
		}

		@Override
		public String toString() {
			return String.format("#%d: %s, %s", number, lowChip, highChip);
		}
	}

	private static class BotList {
		private final int lowCompare;
		private final int highCompare;

		int[] output = {NO_CHIP, NO_CHIP, NO_CHIP};
		private List<Bot> bots = new ArrayList<>();

		private int findBot(int number) {
			for (int i = 0; i < bots.size(); ++i) {
				if (bots.get(i).getNumber() == number) { return i; }
			}
			return -1;
		}

		private void addValue(String str) {
			// example: value 5 goes to bot 2
			final var buffer = str.split(" ");
			final int botNr = Integer.parseInt(buffer[5]);
			final int value = Integer.parseInt(buffer[1]);
			final int botFound = findBot(botNr);

			if (botFound < 0) {
				bots.add(new Bot(botNr, value));
			} else {
				bots.get(botFound).giveChip(value);
			}
		}

		private void addInstruction(String str) {
			// example: bot 2 gives low to bot 1 and high to bot 0
			final var buffer = str.split(" ");
			final int botNr = Integer.parseInt(buffer[1]);
			final var lowValue = new Chip(buffer[5], buffer[6]);
			final var highValue = new Chip(buffer[10], buffer[11]);
			final int botFound = findBot(botNr);

			if (botFound < 0) {
				bots.add(new Bot(botNr, lowValue, highValue));
			} else {
				bots.get(botFound).setReciver(lowValue, highValue);
			}
		}

		BotList(List<String> input, int compare1, int compare2) {
			input.forEach(i -> {
				if (i.startsWith("value ")) {
					addValue(i);
				} else if (i.startsWith("bot ")) {
					addInstruction(i);
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

		void setOutput(Chip c) {
			int index = c.reciver;
			if (index >= output.length) { return; }
			output[index] = c.value;
		}

		void processBot(Bot bot) {
			var lowBot = bots.get(findBot(bot.lowChip.reciver));
			if (!bot.lowChip.output) {
				lowBot.giveChip(bot.lowChip.value);
			} else {
				setOutput(bot.lowChip);
			}

			var highBot = bots.get(findBot(bot.highChip.reciver));
			if (!bot.highChip.output) {
				highBot.giveChip(bot.highChip.value);
			} else {
				setOutput(bot.highChip);
			}

			bot.removeChips();
		}

		int compare() {
			while (true) {
				boolean changed = false;

				for (var bot : bots) {
					if (bot.proceed()) {
						changed = true;
						if (bot.lowChip.value == lowCompare && bot.highChip.value == highCompare) { return bot.number; }
						processBot(bot);
					}
				}
				if (!changed) { throw new RuntimeException("The loop is infinite"); }
			}
		}

		int getOutput() {
			while (output[0] == NO_CHIP || output[1] == NO_CHIP || output[2] == NO_CHIP) {
				boolean changed = false;

				for (var bot : bots) {
					if (bot.proceed()) {
						changed = true;

						processBot(bot);
					}
				}
				if (!changed) { throw new RuntimeException("The loop is infinite"); }
			}
			return output[0] * output[1] * output[2];
		}

		public void print() {
			bots.forEach(i -> System.out.println(i));
			System.out.print("[");
			for (var i : output) {
				System.out.print(i + ",");
			}
			System.out.println("]");
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
		io.printTest(bots.compare(), 2);
		io.printTest(bots.getOutput(), 5 * 2 * 3);
	}

	@Override
	protected void solvePuzzle() {
		var bots = new BotList(io.readAllLines(), 61, 17);
		io.printResult(bots.compare(), 147);
		io.printResult(bots.getOutput(), 55637);
	}

}
