package edu.itmo.piikt.interfaces;

import edu.itmo.piikt.historyWorker.HistoryWorker;

import java.util.logging.Level;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface FindWorker {
    default void findWorker(Logger logger) {
        var list = HistoryWorker.INSTANCE.getListWorker();
        if (list.isEmpty()) {
            logger.log(Level.INFO, "No registered employees found");
            return;
        }
    }
}
