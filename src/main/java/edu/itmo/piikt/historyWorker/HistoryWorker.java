package edu.itmo.piikt.historyWorker;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Address;
import edu.itmo.piikt.models.Organization;
import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.models.Worker;
import edu.itmo.piikt.reader.InputReader;
import edu.itmo.piikt.validationModels.GeneratorId;
import edu.itmo.piikt.validationModels.ValidationOrganization;
import edu.itmo.piikt.validationModels.ValidationWorker;

import java.util.*;

public class HistoryWorker {
    private IOProvider io;
    private static HistoryWorker instance;
    private Date data;
    private ValidationWorker worker;
    private ValidationOrganization organization;
    LinkedList<Worker> listWorker = new LinkedList<>();

    private HistoryWorker(IOProvider io) {
        this.io = io;
        this.data = new Date();
        this.worker = new ValidationWorker(io);
        this.organization = new ValidationOrganization(io);
    }

    public static HistoryWorker getInstance(IOProvider io) {
        if (instance == null) {
            instance = new HistoryWorker(io);
        }
        return instance;
    }

    public void add(Worker worker) {
        listWorker.add(worker);
    }

    public void remove(Worker worker) {
        listWorker.remove(worker);
    }

    public void clear() {
        listWorker.clear();
    }

    public void peekFirst() {
        Iterator<Worker> iterator = listWorker.iterator();
        for (int i = 1; i <= 1 && iterator.hasNext(); i++) {
            Worker worker1 = iterator.next();
            System.out.println(worker1);
        }
    }

    public void printHistoryWorker() {
        Iterator<Worker> iterator = listWorker.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            System.out.println(worker.toString());
        }
    }

    public void printName(String nameConsole) {
        Iterator<Worker> iterator = listWorker.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            String name = worker.getName();
            if (nameConsole.equals(name)) {
                System.out.println(worker.toString());
            }
        }
    }

    public void infoLiat() {
        System.out.println("Тип коллекции: " + listWorker.getClass() + "\nВремя идентификации: "
                + data + "\nКолличество эллиментов: " + listWorker.size());
    }

    public void removeId(int idConsole) {
        Iterator<Worker> iterator = listWorker.iterator();
        try {
            while (iterator.hasNext()) {
                Worker worker = iterator.next();
                int id = worker.getId();
                if (idConsole == id) {
                    iterator.remove();
                    break;
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Invalid input");
        }
    }

    public void update(String argument) {
        try {
            int idNewStart = listWorker.getLast().getId() + 1;
            LinkedList<Worker> work = new LinkedList<>();
            Iterator<Worker> iterator = listWorker.iterator();
            String input = argument;
            int idConsole = Integer.parseInt(input);
            if ((idNewStart - 1) >= idConsole){
                while (iterator.hasNext()) {
                    Worker workerObject = iterator.next();
                    int id = workerObject.getId();
                    if (id > idConsole) {
                        work.add(workerObject);
                        iterator.remove();
                    }
                    if (id == idConsole) {
                        iterator.remove();
                    }
                }
                GeneratorId.getInstance(io).setStartId(idConsole);
                listWorker.add(worker.worker());
                listWorker.addAll(work);
                GeneratorId.getInstance(io).setStartId(idNewStart);}
        } catch (RuntimeException e){
            io.printError("индекс не найден");
        }




        /**
        Worker lastElement = listWorker.getLast();
        int idNewStart = lastElement.getId() + 1;
        Iterator<Worker> iterator = listWorker.iterator();
        int idConsole = InputReader.getInstance().nextInt();
        InputReader.getInstance().nextLine();
        while (iterator.hasNext()) {
            Worker workerObject = iterator.next();
            int id = workerObject.getId();
            if (idConsole == id) {
                iterator.remove();
                break;
            }
        }
        GeneratorId.getInstance(io).setStartId(idConsole);
        add(worker.worker());
        GeneratorId.getInstance(io).setStartId(idNewStart);*/
    }

    public void countByOrganization() {
        ArrayList<Worker> organizationArrayList = new ArrayList<>();
        Organization input = organization.organization();
        Iterator<Worker> iterator = listWorker.iterator();

        if (iterator.hasNext()){
            Worker worker = iterator.next();
            if (worker.getOrganization().equals(input)){
                organizationArrayList.add(worker);
            }
        }
        int size = organizationArrayList.size();
        System.out.println(size);
        organizationArrayList.clear();
    }

    public void removeLower(String argument) {
        System.out.println("Enter the id of the element to remove: ");
        Iterator<Worker> iterator = listWorker.iterator();
        try {
            int idConsole = Integer.parseInt(argument);
            while(iterator.hasNext()){
                Worker worker = iterator.next();
                if(idConsole > worker.getId()){
                    iterator.remove();
                }
            }
        } catch (RuntimeException e) {
            System.out.println("Invalid input");
        }
    }

    public void sort(){
        LinkedList<Worker> sortedList = new LinkedList<>(listWorker);
        sortedList.sort(null);
        Iterator<Worker> iterator = sortedList.descendingIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());}
    }

    public LinkedList<Worker> getListWorker() {
        return listWorker;
    }

    public int tailWorked() {
        if (listWorker.isEmpty()) {
            return 0;
        } else{
        return  listWorker.getLast().getId();}
    }
}