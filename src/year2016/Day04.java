/*
 * https://adventofcode.com/2016/day/4
 *
 *--- Day 4: Security Through Obscurity ---
 *
 * Finally, you come across an information kiosk with a list of rooms. Of course, the list is encrypted
 * and full of decoy data, but the instructions to decode the list are barely hidden nearby. Better remove
 * the decoy data first.
 *
 * Each room consists of an encrypted name (lowercase letters separated by dashes) followed by a dash, a
 * sector ID, and a checksum in square brackets.
 *
 * A room is real (not a decoy) if the checksum is the five most common letters in the encrypted name, in
 * order, with ties broken by alphabetization. For example:
 *     aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3), and then
 *     a tie between x, y, and z, which are listed alphabetically.
 *
 *     a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each),
 *     the first five are listed alphabetically.
 *
 *     not-a-real-room-404[oarel] is a real room.
 *     totally-real-room-200[decoy] is not.
 *
 * Of the real rooms from the list above, the sum of their sector IDs is 1514.
 *
 * What is the sum of the sector IDs of the real rooms?
 *
 * Your puzzle answer was 278221.
 *
 *
 * --- Part Two ---
 *
 * With all the decoy data out of the way, it's time to decrypt this list and get moving.
 *
 * The room names are encrypted by a state-of-the-art shift cipher, which is nearly unbreakable without
 * the right software. However, the information kiosk designers at Easter Bunny HQ were not expecting to
 * deal with a master cryptographer like yourself.
 *
 * To decrypt a room name, rotate each letter forward through the alphabet a number of times equal to the
 * room's sector ID. A becomes B, B becomes C, Z becomes A, and so on. Dashes become spaces.
 *
 * For example, the real name for qzmt-zixmtkozy-ivhz-343 is very encrypted name.
 *
 * What is the sector ID of the room where North Pole objects are stored?
 *
 * Your puzzle answer was 267.
 */

package year2016;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import aoc.CharStatistic;
import aoc.Day00;



@SuppressWarnings("javadoc")
public class Day04 extends Day00 {
	private static final int CHECKSUM_LENGTH = 5;

	private static record Room(String name, int sectorId, String checkSum) {
		Room {
			if (!name.matches("[\\-a-z]+")) { throw new IllegalArgumentException("name has to be in lower case or '-'"); }

			if (!checkSum.matches("[a-z]+")) { throw new IllegalArgumentException("checkSum has to be in lower case"); }

			if (checkSum.length() != CHECKSUM_LENGTH) {
				throw new IllegalArgumentException("The length of the checkSum has to be " + CHECKSUM_LENGTH);
			}
		}

		boolean isDecoy() {
			var letters = new CharStatistic(name);
			letters.remove('-');

			var checkSumBuffer = new StringBuilder(CHECKSUM_LENGTH);
			for (int i = 0; i < CHECKSUM_LENGTH; ++i) {
				checkSumBuffer.append(letters.get(i).chr());
			}

			return !checkSumBuffer.toString().equals(checkSum);
		}

		static Room create(String str) {
			var buffer = str.split("-(?=\\d)"); // split at "-[number]", but keep the number
			var name = buffer[0];
			buffer = buffer[1].split("[\\[\\]]"); // split at "[", also removes trailing "]"
			return new Room(name, Integer.parseInt(buffer[0]), buffer[1]);
		}

		static List<Room> createList(List<String> list) {
			var buffer = new LinkedList<Room>();
			list.forEach(str -> {
				var room = Room.create(str);
				if (!room.isDecoy()) { buffer.add(room); }
			});
			return Collections.unmodifiableList(buffer);
		}
	}

	public static class RoomList {
		final static int LETTER_COUNT = 'z' - 'a' + 1;

		private List<Room> rooms;

		RoomList(List<String> input) {
			rooms = Room.createList(input);
		}

		int sumSectorIds() {
			return rooms.stream().mapToInt(Room::sectorId).sum();
		}

		private static char shiftChar(char c, int by) {
			if (c == '-') { return ' '; }
			char chr = (char) (c + by);
			if (chr > 'z') { chr -= LETTER_COUNT; }
			return chr;
		}

		private static String decryptRoom(Room r) {
			var shiftBy = r.sectorId() % LETTER_COUNT;
			var result = new StringBuilder(r.name());

			for (int i = 0; i < result.length(); ++i) {
				result.setCharAt(i, shiftChar(result.charAt(i), shiftBy));
			}

			return result.toString();
		}

		// To find out, what "North Pole objects" actually means ("northpole object storage")
		void printDecrypted() {
			rooms.forEach(i -> System.out.println(decryptRoom(i)));
		}

		int findRoom(String ROOM_NAME) {
			for (var i : rooms) {
				// @formatter:off
				if (i.name().length() == ROOM_NAME.length() // faster to check first, if possible match is there
				&& decryptRoom(i).equals(ROOM_NAME))
					{ return i.sectorId(); }
				// @formatter:on
			}

			throw new RuntimeException("Room not found");
		}
	}



	public Day04() {
		super(2016, 4);
	}

	@Override
	protected void testPuzzle() {
		// @formatter:off
		var input = Arrays.asList(
				"aaaaa-bbb-z-y-x-123[abxyz]",
				"a-b-c-d-e-f-g-h-987[abcde]",
				"not-a-real-room-404[oarel]",
				"totally-real-room-200[decoy]",
				"qzmt-zixmtkozy-ivhz-343[zimth]"
		);
		// @formatter:on
		final String findStr = "very encrypted name";

		io.printTest(Room.create(input.get(0)).isDecoy(), false);
		io.printTest(Room.create(input.get(1)).isDecoy(), false);
		io.printTest(Room.create(input.get(2)).isDecoy(), false);
		io.printTest(Room.create(input.get(3)).isDecoy(), true);

		var rooms = new RoomList(input);
		io.printTest(rooms.sumSectorIds(), 1857);

		io.printTest(RoomList.decryptRoom(Room.create(input.get(4))), findStr);
		io.printTest(rooms.findRoom(findStr), 343);
	}

	@Override
	public void solvePuzzle() {
		final String findStr = "northpole object storage";
		var rooms = new RoomList(io.readAllLines());
		io.printResult(rooms.sumSectorIds(), 278221);
		io.printResult(rooms.findRoom(findStr), 267);
	}

}
