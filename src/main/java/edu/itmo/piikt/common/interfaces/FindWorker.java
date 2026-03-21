package edu.itmo.piikt.common.interfaces;

import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.provider.IOProvider;

/**
 * Checks if the worker collection is empty and notifies the user.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see HistoryWorker
 */
public interface FindWorker {
    /**
     * Checks if the worker collection is empty and notifies the user.
     *
     * @param io
     *            the input/ output provider.
     */
    default boolean findWorker(IOProvider io) {
        var list = HistoryWorker.INSTANCE.getListWorker();
        if (list.isEmpty()) {
            io.println("No registered employees found");
            return true;
        }
        return false;
    }
}
