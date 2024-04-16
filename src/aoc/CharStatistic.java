package aoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;



/**
 * Creates a statistic about the characters of a string (how often a letter is used). The list of used characters is
 * then sorted from most used to least used.
 */
public class CharStatistic {
	/**
	 * remembers how often a character was used
	 */
	public static class CharData implements Comparable<CharData> {
		private Character chr;
		private Long count = 1L;

		/**
		 * (deep) copy constructor
		 * 
		 * @param source
		 *          copy from
		 */
		public CharData(CharData source) {
			chr = source.chr.charValue();
			count = source.count.longValue();
		}

		/**
		 * constructor
		 * 
		 * @param chr
		 *          character
		 * @param count
		 *          number of times chr is present
		 */
		// not yet needed
		public CharData(char chr, long count) {
			this.chr = chr;
			this.count = count;
		}

		/**
		 * @return character
		 */
		public char chr() {
			return chr;
		}

		/**
		 * @return how often the character is used
		 */
		// public long count() {
		// return count;
		// }

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

	private void countLetters() {
		String uniqueChrs = string.chars().distinct().mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining());
		for (int i = 0; i < uniqueChrs.length(); ++i) {
			var chr = uniqueChrs.charAt(i);
			var count = string.chars().filter(c -> chr == c).count();
			statistics.add(new CharData(chr, count));
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
		statistics.removeIf(i -> i.chr() == letter);
	}
}
