package edu.itmo.piikt.common.command.base;

import edu.itmo.piikt.common.provider.IOProvider;

import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

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
        io.printDesign();
        try {
            before();
            io.printDesign();
            doExecute(io, argument);
            io.printDesign();
            after();
        } catch (DateTimeParseException e) {
            io.printDesign();
            Logger.getLogger("Invalid date format");
            io.printDesign();
            throw e;
        } catch (IllegalArgumentException e) {
            io.printDesign();
            onException();
            io.printDesign();
            throw e;
        } catch (RuntimeException e) {
            io.printDesign();
            onError(e);
            io.printDesign();
            throw e;
        } finally {
            io.printDesign();
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
