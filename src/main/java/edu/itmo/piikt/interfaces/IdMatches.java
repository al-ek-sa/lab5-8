package edu.itmo.piikt.interfaces;

import edu.itmo.piikt.historyWorker.HistoryWorker;

import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface IdMatches {
    /**
     * A method that checks if there are elements in the collection with the same id
     * as the id entered by the user.
     */

    default void idMatches(String argument, Logger logger) {
        var listWorker = HistoryWorker.INSTANCE.getListWorker();

        boolean found = listWorker.stream().anyMatch(worker -> worker.getId().equals(argument));
        if (!found) {
            logger.log(Level.INFO, "No employee with this ID");
        }
    }
}
