package edu.itmo.piikt.command.base;

import edu.itmo.piikt.io.provider.IOProvider;

/**
 * Defines the contract for commands that accept arguments.
 *
 * @author Lishik Aliaksandra
 * @version 1.0
 * @see BaseCommand
 * @see IOProvider
 */
public interface BaseArgumentCommand extends BaseCommand {
    /**
     *
     * @param io
     *            the input/output provider
     * @param argument
     */
    default void execute(IOProvider io, String argument) {
        io.printeDesign();
        try {
            before();
            io.printeDesign();
            doExecute(io, argument);
            io.printeDesign();
            after();
        } catch (IllegalArgumentException e) {
            io.printeDesign();
            onException();
            io.printeDesign();
            throw e;
        } catch (RuntimeException e) {
            io.printeDesign();
            onError(e);
            io.printeDesign();
            throw e;
        } finally {
            io.printeDesign();
        }
    }

    /**
     * Contains the core business logic of the command.
     *
     * @param io
     *            the input/output provider
     * @param argument
     */
    void doExecute(IOProvider io, String argument);
    default void onException() {
    }
}
