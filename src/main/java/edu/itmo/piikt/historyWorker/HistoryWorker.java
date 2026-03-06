package edu.itmo.piikt.historyWorker;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.models.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * A class for storing a collection with registered employees. The class is a
 * singleton.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class HistoryWorker {
    private static HistoryWorker instance;
    private Date data;
    LinkedList<Worker> listWorker = new LinkedList<>();
    Logger logger = Logger.getLogger(HistoryWorker.class.getName());

    private HistoryWorker() {
        this.data = new Date();
    }

    public static HistoryWorker getInstance() {
        if (instance == null) {
            instance = new HistoryWorker();
        }
        return instance;
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
