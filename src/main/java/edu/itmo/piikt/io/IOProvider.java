package edu.itmo.piikt.io;

/**
 * An interface that is required for outputting data to the console, as well as implementing data reading.
 * When outputting, the data is provided in color.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public interface IOProvider {
    public static final String ANSI_ORANGE_256 = "\u001B[38;5;216m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[38;5;210m";
    public static final String ANSI_GREEN = "\u001B[38;5;157m";
    public static final String ANSI_YELLOW = "\u001B[38;5;229m";
    public static final String ANSI_TURQUOISE_LIGHT = "\u001B[38;5;86m";
    public static final String ANSI_LAVENDER_LIGHT = "\u001B[38;5;189m";
    public static final String ANSI_BRIGHT_PINK = "\u001B[38;5;205m";
    public static final String ANSI_BRIGHT_BLUE = "\u001B[1;35m";
    public static final String ANSI_PINK_225 = "\u001B[38;5;225m";

    default void printeDesign(){
        System.out.println(ANSI_BRIGHT_PINK + (("-").repeat(210)) + ANSI_BRIGHT_BLUE);
    }
    void print(String message);
    void println(String message);
    void printException(String message);
    void printError(String message);
    void printField (String message, String messageField);
    String readLine();
    String name();
    default void printlnInt(Integer message){
        System.out.println(ANSI_GREEN + message + ANSI_BRIGHT_BLUE);
    }
    default void printlnCommand(String message){
        System.out.println(ANSI_PINK_225 + message + ANSI_PINK_225);
    }
}
