package edu.itmo.piikt.common.command.functionalInterface;

import edu.itmo.piikt.client.io.provider.IOProvider;

/**
 * Functional interface for argument-based commands. Enables command
 * registration using lambda expressions.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
@FunctionalInterface
public interface ArgumentCommand {
    void execute(IOProvider io, String argument);
}
