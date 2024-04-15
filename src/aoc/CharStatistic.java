package aoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * Creates a statistic about the characters of a string (how often a letter is used). The list of used characters is
 * then sorted from most used to least used.
 */
public class CharStatistic {
	/**
	 * remembers how often a character was used
	 */
	public static class CharData implements Comparable<CharData> {
		final Character chr;
		Integer count = 1;

		/**
		 * (deep) copy constructor
		 * 
		 * @param source
		 *          copy from
		 */
		public CharData(CharData source) {
			chr = source.chr.charValue();
			count = source.count.intValue();
		}

		// not yet needed
		// public LetterData(char letter, int count) {
		// this.letter = letter;
		// this.count = count;
		// }

		/**
		 * constructor
		 * 
		 * @param chr
		 *          letter to remember
		 */
		public CharData(char chr) {
			this.chr = chr;
		}

		/**
		 * @return character
		 */
		public Character getChar() {
			return chr;
		}

		/**
		 * @return how often the character is used
		 */
		public Integer getCount() {
			return count;
		}

		@Override
		public int compareTo(CharData o) {
			// sort: higher count -> lower count, a -> z
			return (count.equals(o.count)) ? chr.compareTo(o.chr) : o.count.compareTo(count);
		}

		// For Debugging
		@Override
		public String toString() {
			return "['" + chr + "': " + count + "]";
		}
	}

	private final String string;
	private List<CharData> statistics;

	/**
	 * constructor
	 * 
	 * @param str
	 *          String to be analysed
	 */
	public CharStatistic(String str) {
		string = str;
		statistics = new ArrayList<>(string.length());
		countLetters();
		statistics.sort(CharData::compareTo);
	}

	private int getIndex(char chr) {
		for (int i = 0; i < statistics.size(); ++i) {
			if (statistics.get(i).chr == chr) { return i; }
		}
		return -1;
	}

	private void countLetters() {
		for (int i = 0; i < string.length(); ++i) {
			char c = string.charAt(i);
			var found = getIndex(c);

			if (found < 0) {
				statistics.add(new CharStatistic.CharData(c));
			} else {
				++statistics.get(found).count;
			}
		}
	}

	/**
	 * returns the whole unmodifiable statistic
	 * 
	 * @return the statistic
	 */
	public List<CharData> getStatistics() {
		return Collections.unmodifiableList(statistics);
	}

	/**
	 * If you only need the letter that is used most
	 * 
	 * @return the first element of the statistic
	 */
	public CharData getFirst() {
		return new CharData(statistics.getFirst());
	}

	/**
	 * If you only need the letter that is used least
	 * 
	 * @return the last element of the statistic
	 */
	public CharData getLast() {
		return new CharData(statistics.getLast());
	}

	/**
	 * returns the element at an index of the statistic
	 * 
	 * @param index
	 *          index of the element
	 * 
	 * @return an element of the statistic
	 */
	public CharData get(int index) {
		return new CharData(statistics.get(index));
	}

	/**
	 * removes a specific letter from the statistic
	 * 
	 * @param letter
	 *          letter to be removed
	 */
	public void remove(char letter) {
		statistics.removeIf(i -> i.chr == letter);
	}
}
