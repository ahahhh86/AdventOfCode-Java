package year2016;

import aoc.Date;
import aoc.IO;
import aoc.Point;



public class Day01 {
	private static class Instruction {
		private static enum Direction {
			RIGHT,
			LEFT;
		}

		public Direction dir;
		public int length;

		public Instruction(String in) {
			char first = in.charAt(0);
			switch (first) {
			case 'R' :
				dir = Direction.RIGHT;
				break;
			case 'L' :
				dir = Direction.LEFT;
				break;
			default :
				throw new IllegalArgumentException("Unexpected value: " + first);
			}

			length = Integer.parseInt(in.substring(1));
		}
	}

	private static enum Compass {
		NORTH {
			@Override
			Point getDirection() {
				return new Point(1, 0);
			}
		},
		EAST {
			@Override
			Point getDirection() {
				return new Point(0, 1);
			}
		},
		SOUTH {
			@Override
			Point getDirection() {
				return new Point(-1, 0);
			}
		},
		WEST {
			@Override
			Point getDirection() {
				return new Point(0, -1);
			}
		};

		Point getDirection() {
			return new Point(0, 0);
		}

		Compass turn(Instruction.Direction d) {
			switch (this) {
			case NORTH :
				return (d == Instruction.Direction.RIGHT) ? EAST : WEST;
			case EAST :
				return (d == Instruction.Direction.RIGHT) ? SOUTH : NORTH;
			case SOUTH :
				return (d == Instruction.Direction.RIGHT) ? WEST : EAST;
			case WEST :
				return (d == Instruction.Direction.RIGHT) ? NORTH : SOUTH;

			default :
				throw new IllegalArgumentException("Unexpected value: " + name());
			}
		}
	}

	Point pos = new Point(0, 0);
	Compass facing = Compass.NORTH;

	private void walk(Instruction i) {
		facing = facing.turn(i.dir);
		Point newPos = facing.getDirection().scale(i.length);
		pos.add(newPos);
	}

	public static void main(String[] args) {
		IO io = new IO(new Date(Date.Year.YEAR2016, Date.Day.DAY01));
		String[] input = io.readFile().getFirst().split(", ");
		Instruction[] dir = new Instruction[input.length];

		for (int i = 0; i < input.length; ++i) {
			dir[i] = new Day01.Instruction(input[i]);
		}

		Day01 d = new Day01();
		for (Instruction i : dir) {
			d.walk(i);
		}
		System.out.println(d.pos);
	}

}
