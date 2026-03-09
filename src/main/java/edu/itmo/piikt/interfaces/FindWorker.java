package edu.itmo.piikt.interfaces;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;

public interface FindWorker {
    default void findWorker(IOProvider io) {
        var list = HistoryWorker.INSTANCE.getListWorker();
        if (list.isEmpty()) {
            io.println("No registered employees found");
            return;
        }
    }
}
