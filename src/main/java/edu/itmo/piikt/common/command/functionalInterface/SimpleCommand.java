package edu.itmo.piikt.common.command.functionalInterface;

import edu.itmo.piikt.client.io.provider.IOProvider;

/**
 * Functional interface for simple commands (without arguments). Enables command
 * registration using lambda expressions.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
@FunctionalInterface
public interface SimpleCommand {
    void execute(IOProvider io);
}
