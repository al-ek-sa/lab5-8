package edu.itmo.piikt.client.io.providerType;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.client.io.data.NameIOProvider;
import edu.itmo.piikt.client.util.InputReader;
import lombok.NoArgsConstructor;

/**
 * A class that inherits from the IOProvider interface implements data output to
 * the console and reading from the console.
 *
 * @author Lishyk Aliaksandra
 * @version 1.1
 */
@NoArgsConstructor
public class IOConsole implements IOProvider {
    @Override
    public String readLine() {
        return InputReader.nextLine();
    }

    @Override
    public void printError(String message) {
        System.out.println(message);
    }

    @Override
    public void printException(String message) {
        System.out.println(message);
    }

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public void printField(String message, String messageFiled) {
        System.out.println(message+ " " + messageFiled);
    }

    @Override
    public String name() {
        return NameIOProvider.CONSOLE.getName();
    }
}
