package edu.itmo.piikt.command.base;

import edu.itmo.piikt.io.provider.IOProvider;

public interface BaseSimpleCommand extends BaseCommand {
    default void execute(IOProvider io) {
        io.printeDesign();
        try {
            before();
            io.printeDesign();
            doExecute(io);
            io.printeDesign();
            after();
        } catch (RuntimeException e) {
            io.printeDesign();
            onError(e);
            io.printeDesign();
            throw e;
        } finally {
            io.printeDesign();
        }
    }
    void doExecute(IOProvider io);
}
