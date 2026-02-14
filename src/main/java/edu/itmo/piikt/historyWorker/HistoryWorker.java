package edu.itmo.piikt.historyWorker;

import edu.itmo.piikt.models.Worker;

import java.util.LinkedList;
import java.util.Iterator;

public class HistoryWorker {
    private static HistoryWorker instance;
    LinkedList<Worker> listWorker = new LinkedList<>();
    private HistoryWorker(){}

    public static HistoryWorker getInstance() {
        if (instance == null) {
            instance = new HistoryWorker();
        } return instance;
    }

    public void add(Worker worker){
        listWorker.add(worker);
    }

    public void remove(Worker worker) {
        listWorker.remove(worker);
    }

    public void clear() {
        listWorker.clear();
    }

    public void printHistoryWorker(){
        Iterator<Worker> iterator = listWorker.iterator();
        while (iterator.hasNext()){
            Worker worker = iterator.next();
            System.out.println(worker.toString());
        }
    }
}