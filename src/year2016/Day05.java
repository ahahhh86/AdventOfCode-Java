/*
 * https://adventofcode.com/2016/day/4
 * 
 * 
 */

package year2016;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import aoc.Date;
import aoc.Day00;



public class Day05 extends Day00 {
	public Day05() {
		super(Date.Year.YEAR2016, Date.Day.DAY05);
	}

	private static String getMD5(String in) {
		try {
			byte[] buffer = in.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			buffer = md.digest(buffer);

			if (buffer[0] == 0 && buffer[1] == 0 && (buffer[2] & 0xF0) == 0) { return Integer.toHexString(buffer[2]); }

		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String findPassword(String doorId) {
		final int PASSWORD_LENGTH = 8;
		var result = new StringBuilder(PASSWORD_LENGTH);
		Integer index = -1;

		for (int i = 0; i < PASSWORD_LENGTH; ++i) {
			while (true) {
				++index;
				var buffer = getMD5(doorId + index);
				if (buffer == "") { continue; }

				result.append(buffer);
				break;
			}
		}

		return result.toString();
	}

	@Override
	public void performTests() {
		final String DOOR_ID = "abc";
		io.printTest(getMD5(DOOR_ID + 3231929), "1");
		io.printTest(getMD5(DOOR_ID + 5017308), "8");
		io.printTest(getMD5(DOOR_ID + 5278568), "f");
		io.printTest(findPassword(DOOR_ID), "18f47a30");
	}

	@Override
	public void solvePuzzle() {
		var input = io.readAllLines().getFirst();
		io.printResult(findPassword(input), "1a3099aa");
	}

}
