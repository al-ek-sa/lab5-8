package edu.itmo.piikt.command.functionalInterface;

import edu.itmo.piikt.io.provider.IOProvider;

/**
 * An abstract class that is the common parent for commands with arguments.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
@FunctionalInterface
public interface ArgumentCommand {
    void execute(IOProvider io, String argument);
}
