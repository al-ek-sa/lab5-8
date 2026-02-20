package edu.itmo.piikt.io;

import edu.itmo.piikt.reader.InputReader;

public class IOScanner implements IOProvider{
    private InputReader scanner;
    public IOScanner(){
        this.scanner = InputReader.getInstance();
    }

    @Override
    public void print(String message) {
        System.out.print(ANSI_ORANGE_256 + message + ANSI_RESET);
    }

    @Override
    public String readLine() {
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

    @Override
    public void printField(String message, String messageFiled) {
        System.out.println(ANSI_TURQUOISE_LIGHT + message + ANSI_RESET + " " + ANSI_LAVENDER_LIGHT + messageFiled + ANSI_RESET);
    }

    @Override
    public String name() {
        return "Console";
    }
}
