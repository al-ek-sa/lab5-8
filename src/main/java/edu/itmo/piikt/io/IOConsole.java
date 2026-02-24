package edu.itmo.piikt.io;

import edu.itmo.piikt.reader.InputReader;

/**
 * A class that inherits from the IOProvider interface
 * implements data output to the console and reading from the console.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class IOConsole implements IOProvider{
    private InputReader scanner;
    public IOConsole(){
        this.scanner = InputReader.getInstance();
    }

    @Override
    public void print(String message) {
        System.out.print(ANSI_ORANGE_256 + message + ANSI_BRIGHT_BLUE);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public void printError(String message) {
        System.out.println(ANSI_RED + message + ANSI_BRIGHT_BLUE);
    }

    @Override
    public void printException(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_BRIGHT_BLUE);
    }

    @Override
    public void println(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_BRIGHT_BLUE);
    }

    @Override
    public void printField(String message, String messageFiled) {
        System.out.println(ANSI_TURQUOISE_LIGHT + message + ANSI_RESET + " " + ANSI_LAVENDER_LIGHT + messageFiled + ANSI_BRIGHT_BLUE);
    }

    @Override
    public String name() {
        return "Console";
    }
}
