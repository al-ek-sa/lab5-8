package edu.itmo.piikt.io.providerType;

import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.io.data.NameIOProvider;
import edu.itmo.piikt.util.InputReader;

/**
 * A class that inherits from the IOProvider interface implements data output to
 * the console and reading from the console.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class IOConsole implements IOProvider {

    public IOConsole() {
    }

    @Override
    public void print(String message) {
        System.out.print(ANSI_ORANGE_256 + message + ANSI_BRIGHT_BLUE);
    }

    @Override
    public String readLine() {
        return InputReader.nextLine();
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
        System.out.println(ANSI_TURQUOISE_LIGHT + message + ANSI_RESET + " " + ANSI_LAVENDER_LIGHT + messageFiled
                + ANSI_BRIGHT_BLUE);
    }

    @Override
    public String name() {
        return NameIOProvider.CONSOLE.getName();
    }
}
