package edu.itmo.piikt.interfaces;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.io.provider.IOProvider;

/**
 * Interface implementing a default method for checking the absence of workers with the given ID.
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public interface IdMatches {
    /**
     * Checks if a worker with the specified ID exists in the collection and notifies the user if not.
     */
    default void idMatches(String argument, IOProvider io) {
        var listWorker = HistoryWorker.INSTANCE.getListWorker();

        boolean found = listWorker.stream().anyMatch(worker -> worker.getId().equals(argument));
        if (!found) {
            io.println("No employee with this ID");
        }
    }
}
