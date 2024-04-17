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
	 * 
	 * @param chr
	 *          character
	 * @param count
	 *          number used
	 */
	public static record CharData(Character chr, Long count) implements Comparable<CharData> {
		@Override
		public int compareTo(CharData o) {
			// sort: higher count -> lower count, a -> z
			return (count.equals(o.count)) ? chr.compareTo(o.chr) : o.count.compareTo(count);
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
	public List<CharData> getList() {
		return Collections.unmodifiableList(statistics);
	}

	/**
	 * If you only need the letter that is used most
	 * 
	 * @return the first element of the statistic
	 */
	public CharData getFirst() {
		return statistics.getFirst();
	}

	/**
	 * If you only need the letter that is used least
	 * 
	 * @return the last element of the statistic
	 */
	public CharData getLast() {
		return statistics.getLast();
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
		return statistics.get(index);
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
