package edu.itmo.piikt.io;

import edu.itmo.piikt.reader.InputReader;

public class IOScanner implements IOProvider{
    private InputReader scanner;
    public static final String ANSI_ORANGE_256 = "\u001B[38;5;216m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[38;5;210m";
    public static final String ANSI_GREEN = "\u001B[38;5;157m";
    public static final String ANSI_YELLOW = "\u001B[38;5;229m";
    public IOScanner(){
        this.scanner = InputReader.getInstance();
    }

    @Override
    public void print(String message) {
        System.out.print(ANSI_ORANGE_256 + message + ANSI_RESET);
    }

    @Override
    public String readLIne() {
        return scanner.nextLine();
    }

    @Override
    public void printError(String message) {
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    @Override
    public void printException(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }

    @Override
    public void println(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }
}
