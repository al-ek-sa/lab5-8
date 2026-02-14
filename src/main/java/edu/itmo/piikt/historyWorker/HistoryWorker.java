package edu.itmo.piikt.historyWorker;

import edu.itmo.piikt.models.Worker;
import edu.itmo.piikt.reader.InputReader;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Date;

public class HistoryWorker {
    private static HistoryWorker instance;
    private  Date data;
    LinkedList<Worker> listWorker = new LinkedList<>();
    private HistoryWorker(){
        this.data = new Date();
    }

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

    public void peekFirst(){
        Iterator<Worker> iterator = listWorker.iterator();
        for (int i = 1; i <= 1 && iterator.hasNext(); i++){
            Worker worker1 = iterator.next();
            System.out.println(worker1);
        }
    }

    public void printHistoryWorker(){
        Iterator<Worker> iterator = listWorker.iterator();
        while (iterator.hasNext()){
            Worker worker = iterator.next();
            System.out.println(worker.toString());
        }
    }

    public void printName(){
        Iterator<Worker> iterator = listWorker.iterator();
        String nameConsole = InputReader.getInstance().nextLine();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            String name = worker.getName();
            if (nameConsole.equals(name)){
            System.out.println(worker.toString());
        }
    }}

    public void infoLiat(){
        System.out.println("Тип коллекции: " + listWorker.getClass() + "\nВремя идентификации: "
                        + data + "\nКолличество эллиментов: " + listWorker.size());
    }

    public void removeId(){
        Iterator<Worker> iterator = listWorker.iterator();
        int idConsole = InputReader.getInstance().nextInt();
        InputReader.getInstance().nextLine();
        while(iterator.hasNext()){
            Worker worker = iterator.next();
            int id = worker.getId();
            if (idConsole == id){
                iterator.remove();
                break;
            }
        }
    }
}