package edu.itmo.piikt.managers;

import edu.itmo.piikt.io.IOProvider;

public interface BaseArgumentCommand extends BaseCommand {
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
    void doExecute(IOProvider io, String argument);
    default void onException() {
    }
}
