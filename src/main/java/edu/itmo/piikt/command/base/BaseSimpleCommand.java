package edu.itmo.piikt.command.base;

import edu.itmo.piikt.io.provider.IOProvider;

/**
 * Defines the contract for commands without arguments.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see IOProvider
 * @see BaseCommand
 */
public interface BaseSimpleCommand extends BaseCommand {
    default void execute(IOProvider io) {
        io.printDesign();
        try {
            before();
            io.printDesign();
            doExecute(io);
            io.printDesign();
            after();
        } catch (RuntimeException e) {
            io.printDesign();
            onError(e);
            io.printDesign();
            throw e;
        } finally {
            io.printDesign();
        }
    }
    void doExecute(IOProvider io);
}
