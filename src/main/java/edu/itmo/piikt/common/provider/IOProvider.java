package edu.itmo.piikt.common.provider;

/**
 * An interface that is required for outputting data to the console, as well as
 * implementing data reading. When outputting, the data is provided in color.
 *
 * @author Lishyk Aliaksandra
 * @version 1.1
 */
public interface IOProvider {
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[38;5;210m";
    String ANSI_GREEN = "\u001B[38;5;157m";
    String ANSI_YELLOW = "\u001B[38;5;229m";
    String ANSI_TURQUOISE_LIGHT = "\u001B[38;5;86m";
    String ANSI_LAVENDER_LIGHT = "\u001B[38;5;189m";
    String ANSI_BRIGHT_PINK = "\u001B[38;5;205m";
    String ANSI_BRIGHT_BLUE = "\u001B[1;35m";
    String ANSI_PINK_225 = "\u001B[38;5;225m";

    default void printDesign() {
        System.out.println(ANSI_BRIGHT_PINK + (("-").repeat(160)) + ANSI_BRIGHT_BLUE);
    }

    void println(String message);

    void printException(String message);

    void printError(String message);

    void printField(String message, String messageField);

    String readLine();

    String name();

    default void printlnInt(Integer message) {
        System.out.println(ANSI_GREEN + message + ANSI_BRIGHT_BLUE);
    }

    default void printlnCommand(String message) {
        System.out.println(ANSI_PINK_225 + message + ANSI_BRIGHT_BLUE);
    }
}
