package edu.itmo.piikt.interfaces;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.io.provider.IOProvider;

public interface IdMatches {
    /**
     * A method that checks if there are elements in the collection with the same id
     * as the id entered by the user.
     */

    default void idMatches(String argument, IOProvider io) {
        var listWorker = HistoryWorker.INSTANCE.getListWorker();

        boolean found = listWorker.stream().anyMatch(worker -> worker.getId().equals(argument));
        if (!found) {
            io.println("No employee with this ID");
        }
    }
}
