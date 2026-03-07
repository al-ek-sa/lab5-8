package edu.itmo.piikt.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utility class for reading console input.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 */
public class InputReader {
    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private static BufferedReader reader = READER;

    private InputReader() {
        throw new UnsupportedOperationException("Utility class");
    }

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
            reader = newReader();
            input = read();
        }
        return input;
    }

    private static String read() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}
