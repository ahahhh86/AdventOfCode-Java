package aoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class LetterStatistic {
	public static class LetterData implements Comparable<LetterData> { // TODO own public class for letter statistics
		public Character letter = '\0';
		public Integer count = 1;

		public LetterData(LetterData l) {
			letter = l.letter.charValue();
			count = l.count.intValue();
		}

		public LetterData(char letter, int count) {
			this.letter = letter;
			this.count = count;
		}

		public LetterData(char letter) {
			this.letter = letter;
		}

		@Override
		public int compareTo(LetterData o) {
			// sort: higher count -> lower count, a -> z
			return (count == o.count) ? letter.compareTo(o.letter) : o.count.compareTo(count);
		}

		// For Debugging
		@Override
		public String toString() {
			return "['" + letter + "': " + count + "]";
		}
	}

	final String str;
	List<LetterData> statistics;

	public LetterStatistic(String s) {
		str = s;
		statistics = new ArrayList<>(str.length());
		countLetters();
		statistics.sort(LetterData::compareTo);
	}

	public List<LetterData> getStatistics() {
		return Collections.unmodifiableList(statistics);
	}

	public LetterData getFirst() {
		return new LetterData(statistics.getFirst());
	}

	public LetterData getLast() {
		return new LetterData(statistics.getLast());
	}

	public LetterData get(int i) {
		return new LetterData(statistics.get(i));
	}

	private int getIndex(char c) {
		for (int i = 0; i < statistics.size(); ++i) {
			if (statistics.get(i).letter == c) { return i; }
		}
		return -1;
	}

	private void countLetters() {
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			var found = getIndex(c);

			if (found < 0) {
				statistics.add(new LetterStatistic.LetterData(c));
			} else {
				++statistics.get(found).count;
			}
		}
	}

	public void remove(char c) {
		statistics.removeIf(i -> i.letter == c);
	}
}
