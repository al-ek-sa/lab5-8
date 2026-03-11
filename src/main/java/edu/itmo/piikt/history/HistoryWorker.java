package edu.itmo.piikt.history;

import edu.itmo.piikt.models.*;
import java.util.*;

/**
 * A class for storing a collection with registered employees. The class is a
 * singleton.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 */
public enum HistoryWorker {
    INSTANCE;
    private Date data;
    private LinkedList<Worker> listWorker = new LinkedList<>();
    HistoryWorker() {
        this.data = new Date();
    }

    public LinkedList<Worker> getListWorker() {
        return listWorker;
    }

    public void add(Worker worker) {
        listWorker.add(worker);
    }

    public void clear() {
        listWorker.clear();
    }

    public Date getData() {
        return data;
    }
}
