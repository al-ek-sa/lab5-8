package edu.itmo.piikt.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IOFile implements  IOProvider {
    private BufferedInputStream reading;
    public IOFile(String nameFile) throws IOException {
        FileInputStream file = new FileInputStream(nameFile);
        this.reading = new BufferedInputStream(file);
    }

    @Override
    public void print(String message) {
        System.out.print(ANSI_ORANGE_256 + message + ANSI_RESET);
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
    int byteRead;

    @Override
    public String readLine() {
        return "help";
    }
}
