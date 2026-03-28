package edu.itmo.piikt.common.io.providerType;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.data.NameIOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.util.InputReader;
import lombok.NoArgsConstructor;

/**
 * A class that inherits from the IOProvider interface implements data output to
 * the console and reading from the console.
 *
 * @author Lishyk Aliaksandra
 * @version 1.2
 */
@NoArgsConstructor
public class IOConsole implements IOProvider {
    private static final AppLogger logger = new AppLogger(IOConsole.class);

    @Override
    public String readLine() {
        try (Context ignored = Context.newId()) {
            String input = InputReader.nextLine();
            logger.debug("Console input: {}", input);
            return input;
        }
    }

    @Override
    public void println(String message) {
        try (Context ignored = Context.newId()) {
            logger.debug("Console output: {}", message);
            System.out.println(message);
        }
    }

    @Override
    public String name() {
        return NameIOProvider.CONSOLE.getName();
    }
}