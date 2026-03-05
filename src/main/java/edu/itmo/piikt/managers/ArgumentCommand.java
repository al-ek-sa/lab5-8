package edu.itmo.piikt.managers;

import edu.itmo.piikt.io.IOProvider;

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
