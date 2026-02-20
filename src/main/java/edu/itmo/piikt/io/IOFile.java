package edu.itmo.piikt.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class IOFile implements  IOProvider {
    private BufferedInputStream reading;
    public IOFile(String nameFile) throws IOException {
        this.reading = new BufferedInputStream(new FileInputStream(nameFile));
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
        try {
            StringBuilder line = new StringBuilder();
            int byteFile;

            while ((byteFile = reading.read()) != -1){
                if (byteFile == '\n'){
                    break;
                }

                line.append((char) byteFile);
            }

            String command = line.toString();

            if (byteFile == -1){
                return  null;
            }
            return command;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void printField(String message, String messageFiled) {
        System.out.println(ANSI_TURQUOISE_LIGHT + message + ANSI_RESET + " " + ANSI_LAVENDER_LIGHT + messageFiled + ANSI_RESET);
    }
}
