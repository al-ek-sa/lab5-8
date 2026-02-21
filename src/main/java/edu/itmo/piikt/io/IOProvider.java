package edu.itmo.piikt.io;

public interface IOProvider {
    public static final String ANSI_ORANGE_256 = "\u001B[38;5;216m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[38;5;210m";
    public static final String ANSI_GREEN = "\u001B[38;5;157m";
    public static final String ANSI_YELLOW = "\u001B[38;5;229m";
    public static final String ANSI_TURQUOISE_LIGHT = "\u001B[38;5;86m";
    public static final String ANSI_LAVENDER_LIGHT = "\u001B[38;5;189m";
    public static final String ANSI_BRIGHT_PINK = "\u001B[38;5;205m";

    default void printeDesign(){
        System.out.println(ANSI_BRIGHT_PINK + (("-").repeat(150)) + ANSI_RESET);
    }
    void print(String message);
    void println(String message);
    void printException(String message);
    void printError(String message);
    void printField (String message, String messageField);
    String readLine();
    String name();
}
