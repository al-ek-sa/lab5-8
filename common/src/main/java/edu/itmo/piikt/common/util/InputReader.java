package edu.itmo.piikt.common.util;

import edu.itmo.piikt.common.logger.AppLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.experimental.UtilityClass;

/**
 * Utility class for reading console input.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see BufferedReader
 */
@UtilityClass
public class InputReader {
	private static final AppLogger log = new AppLogger(InputReader.class);
	private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
	private static BufferedReader reader = READER;

	private static BufferedReader newReader() {
		return new BufferedReader(new InputStreamReader(System.in));
	}

	/**
	 * The method reads data from the console.
	 *
	 * @return data
	 */
	public static String nextLine() {
		String input = read();
		while (input == null) {
			log.warn("Failed to read input, retrying...");
			reader = newReader();
			input = read();
		}
		return input;
	}

	private static String read() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			log.error("IO error while reading console input: {}", e.getMessage());
			return null;
		}
	}
}
