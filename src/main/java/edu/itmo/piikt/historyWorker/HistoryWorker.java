package edu.itmo.piikt.historyWorker;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.*;
import edu.itmo.piikt.reader.InputReader;
import edu.itmo.piikt.validationModels.GeneratorId;
import edu.itmo.piikt.validationModels.ValidationOrganization;
import edu.itmo.piikt.validationModels.ValidationWorker;

import java.math.BigInteger;
import java.util.*;

public class HistoryWorker {
    private IOProvider io;
    private static HistoryWorker instance;
    private Date data;
    private ValidationOrganization organization;
    LinkedList<Worker> listWorker = new LinkedList<>();

    private HistoryWorker(IOProvider io) {
        this.io = io;
        this.data = new Date();
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
        if (listWorker.isEmpty()){
            //не найдено зарегистрированных работников
            io.println("No registered employees found");
        }

        Iterator<Worker> iterator = listWorker.iterator();
        for (int i = 1; i <= 1 && iterator.hasNext(); i++) {
            Worker worker1 = iterator.next();
            io.println(worker1.toString());
        }
    }

    public void printHistoryWorker() {
        if (listWorker.isEmpty()){
            //не найдено зарегистрированных работников
            io.println("No registered employees found");
        }
        Iterator<Worker> iterator = listWorker.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            io.println(worker.toString());
        }
    }

    public void printName(String nameConsole) {
        boolean flag = false;
        for (Worker work : listWorker){
            if (work.getName().equals(nameConsole)){
                flag = true;
            }
        }

        if (flag == false) {
            //работников с таким именем не найдено
            io.println("No employees found with that name");
        } else {
            flag = false;
        }

        Iterator<Worker> iterator = listWorker.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            String name = worker.getName();
            if (nameConsole.equals(name)) {
                io.println(worker.toString());
            }
        }
    }

    public void infoLiat() {
        io.println("Collection type: " + listWorker.getClass() + "\nIdentification time: "
                + data + "\nNumber of elements: " + listWorker.size());
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
            io.printException("Invalid input");
        }
    }

    public void update(String argument, ValidationWorker worker) {
        try {
            int idNewStart = listWorker.getLast().getId() + 1;
            LinkedList<Worker> work = new LinkedList<>();
            Iterator<Worker> iterator = listWorker.iterator();

            BigInteger bigInteger = new BigInteger(argument);

            if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new ExceptionBigIntegerMAX_INTEGER();
            }

            if (bigInteger.compareTo(BigInteger.valueOf(0)) < 0) {
                throw new ExceptionId();
            }

            int idConsole = Integer.parseInt(argument);
            if ((idNewStart - 1) >= idConsole){
                while (iterator.hasNext()) {
                    Worker workerObject = iterator.next();
                    int id = workerObject.getId();
                    if (id > idConsole) {
                        work.add(workerObject);
                        iterator.remove();
                    } else if (id == idConsole) {
                        iterator.remove();
                        GeneratorId.getInstance(io).setStartId(idConsole);
                        work.add(worker.worker());
                    }
                }
                listWorker.addAll(work);
                GeneratorId.getInstance(io).setStartId(idNewStart);
            }
        } catch (ExceptionNull e) {
            io.printError(e.getMessage());
        } catch (ExceptionBigIntegerMAX_INTEGER e){
            io.printError(e.getMessage());
        } catch (ExceptionId e) {
            io.printError(e.getMessage());
        } catch (RuntimeException e) {
            io.printException("The string contains symbols, please try again");
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

    public void countByOrganization(Organization organization) {
        ArrayList<Worker> organizationArrayList = new ArrayList<>();
        for (Worker worker1 : listWorker){
            if (worker1.getOrganization() != null && worker1.getOrganization().equals(organization)){
                organizationArrayList.add(worker1);
            }
        }

        io.printeDesign();
        io.printlnInt(organizationArrayList.size());
        organizationArrayList.clear();
    }

    public void removeLower(String argument) {
        Iterator<Worker> iterator = listWorker.iterator();
        try {
            BigInteger bigInteger = new BigInteger(argument);

            if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new ExceptionBigIntegerMAX_INTEGER();
            }

            if (bigInteger.compareTo(BigInteger.valueOf(0)) <= 0) {
                throw new ExceptionId();
            }

            int idConsole = Integer.parseInt(argument);
            Worker tip = new Worker();
            tip.setId(idConsole);
            while(iterator.hasNext()){
                Worker worker = iterator.next();
                if(worker.compareTo(tip) < 0){
                    iterator.remove();
                }
            }
        } catch (ExceptionBigIntegerMAX_INTEGER e){
            io.printError(e.getMessage());
        } catch (ExceptionId e) {
            io.printError(e.getMessage());
        } catch (RuntimeException e) {
            io.printException("The string contains symbols, please try again");
        }
    }

    public void sort(){
        if (listWorker.isEmpty()){
            //не найдено зарегистрированных работников
            io.println("No registered employees found");
        }
        LinkedList<Worker> sortedList = new LinkedList<>(listWorker);

        Comparator<Worker> workerDate = new Comparator<Worker>() {
            @Override
            public int compare(Worker worker, Worker worker1) {
                if (worker.getEndDate() != null && worker1.getEndDate() != null) {
                    return worker.getEndDate().compareTo(worker1.getEndDate());
                } else if (worker1.getEndDate() == null) {
                    return 1;
                } else if (worker.getEndDate() == null) {
                    return -1;
                } else
                    return Integer.compare(worker.getId(), worker1.getId());
            }
        };

        sortedList.sort(workerDate);
        Iterator<Worker> iterator = sortedList.descendingIterator();
        while (iterator.hasNext()) {
            io.println(iterator.next().toString());
        }
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

    public void idMatches(String argument) {
        LinkedList<Worker> workId = new LinkedList<>();
        try {
            BigInteger bigInteger = new BigInteger(argument);

            if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new ExceptionBigIntegerMAX_INTEGER();
            }

            if (bigInteger.compareTo(BigInteger.valueOf(0)) < 0) {
                throw new ExceptionId();
            }

            int idConsole = Integer.parseInt(argument);

                for (Worker worker : listWorker){
                    if (idConsole == worker.getId()){
                        workId.add(worker);
                    }
                }


            if (workId.isEmpty()){
                io.printException("No employee with this ID");

            } else {
                workId.clear();
            }

        } catch (ExceptionNull ignored) {
            //ignored
        } catch (ExceptionBigIntegerMAX_INTEGER ignored){
            //ignored
        } catch (ExceptionId ignored) {
            //ignored
        } catch (RuntimeException ignored) {
            //ignored
        }
    }
}