package edu.itmo.piikt.server.history;

import edu.itmo.piikt.common.models.Worker;
import lombok.Getter;

import java.util.*;

/**
 * A class for storing a collection with registered employees. The class is a
 * singleton.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 */
@Getter
public enum HistoryWorker {
    INSTANCE;
    private Date data;
    private LinkedList<Worker> listWorker = new LinkedList<>();
    HistoryWorker() {
        this.data = new Date();
    }

    public void add(Worker worker) {
        listWorker.add(worker);
    }

    public void clear() {
        listWorker.clear();
    }
}
