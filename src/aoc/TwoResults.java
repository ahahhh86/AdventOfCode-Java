package aoc;

/**
 * combines two results in one object (if both parts of the puzzle can be solved inside one function)
 * 
 * @param <T>
 *          type of the result
 * @param part1
 *          result for part 1
 * @param part2
 *          result for part 2
 */
public record TwoResults<T>(T part1, T part2) {
}
