package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



public class IO {
	private Date date;
	private int partCount = 0;

	public IO(Date puzzleDate) {
		date = puzzleDate;

		System.out.println(date + ": ");
	}

	public List<String> readFile() {
		String fileName = "input/" + date.year.toFilename() + date.day.toFilename();

		try {
			return Files.readAllLines(Paths.get(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void printResult(Object result) {
		++partCount;
		System.out.printf("Part %d: %16s%n", partCount, result);
	}
}
