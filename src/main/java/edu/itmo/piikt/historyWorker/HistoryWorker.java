package edu.itmo.piikt.historyWorker;

import edu.itmo.piikt.commands.UpdateIdCommand;
import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.*;
import edu.itmo.piikt.reader.InputReader;
import edu.itmo.piikt.validationModels.GeneratorId;
import edu.itmo.piikt.validationModels.ValidationOrganization;
import edu.itmo.piikt.validationModels.ValidationWorker;

import java.math.BigInteger;
import java.util.*;
import java.util.logging.Logger;import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for storing a collection with registered employees.
 * The class is a singleton.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class HistoryWorker {
    private IOProvider io;
    private static HistoryWorker instance;
    private Date data;
    private ValidationOrganization organization;
    LinkedList<Worker> listWorker = new LinkedList<>();
    Logger logger = Logger.getLogger(HistoryWorker.class.getName());
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

    /**
     * The method outputs the data of the first element in the collection.
     */
    public void peekFirst() {
        if (listWorker.isEmpty()){
            //не найдено зарегистрированных работников
            logger.log(Level.INFO,"No registered employees found");
        }

        Iterator<Worker> iterator = listWorker.iterator();
        for (int i = 1; i <= 1 && iterator.hasNext(); i++) {
            Worker worker1 = iterator.next();
            io.println(worker1.toString());
        }
    }

    /**
     * The method outputs data of all registered employees.
     */
    public void printHistoryWorker() {
        if (listWorker.isEmpty()){
            //не найдено зарегистрированных работников
            logger.log(Level.INFO,"No registered employees found");
        }
        Iterator<Worker> iterator = listWorker.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            io.println(worker.toString());
        }
    }

    /**
     *The method outputs all employees with the same name as entered by the user.
     *
     * @param nameConsole The name entered by the user.
     */
    public void printName(String nameConsole) {
        boolean flag = false;
        for (Worker work : listWorker){
            if (work.getName().equals(nameConsole)){
                flag = true;
            }
        }

        if (flag == false) {
            //работников с таким именем не найдено
            logger.log(Level.INFO,"No employees found with that name");
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

    /**
     * The method outputs data about the collection.
     */
    public void infoLiat() {
        io.println("Collection type: " + listWorker.getClass() + "\nIdentification time: "
                + data + "\nNumber of elements: " + listWorker.size());
    }

    /**
     *The method removes from the collection the element whose id is specified by the user.
     *
     * @param idConsole The argument passed is the argument entered by the user.
     */
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
            logger.log(Level.INFO,"Invalid input");
        }
    }

    /**
     * The method replaces the element whose id is equal to the id specified by the user.
     *
     * @throws RuntimeException If the user entered the id in an incorrect format.
     * @throws ExceptionBigIntegerMAX_INTEGER If the user entered a number that exceeds the upper limit of the Integer type.
     * @throws ExceptionId If the user entered id <= 0 or if the number exceeded the lower limit of the Integer type.
     * @param argument The element's id is passed.
     * @param worker A new employee object.
     */
    public void update(String argument, ValidationWorker worker) {
        try {
            // TODO
            int idNewStart = listWorker.getLast().getId() + 1;
            //может бросить исключение
            LinkedList<Worker> work = new LinkedList<>();
            Iterator<Worker> iterator = listWorker.iterator();

            BigInteger bigInteger = new BigInteger(argument);

            if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new ExceptionBigIntegerMAX_INTEGER();
            }

            if (bigInteger.compareTo(BigInteger.valueOf(0)) <=0) {
                throw new ExceptionId();
            }

            int idConsole = Integer.parseInt(argument);
            io.printeDesign();
            //начало обновления данных
            logger.log(Level.INFO,"Start of data update");
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
                        io.printeDesign();
                        //данные успешно обновлены
                        logger.log(Level.INFO,"Data successfully updated");
                        io.printeDesign();
                    }
                }
                listWorker.addAll(work);
                GeneratorId.getInstance(io).setStartId(idNewStart);
            }
        } catch (ExceptionNull e) {
            io.printeDesign();
            logger.log(Level.INFO,e.getMessage());
            io.printeDesign();
        } catch (ExceptionBigIntegerMAX_INTEGER e){
            io.printeDesign();
            logger.log(Level.INFO,e.getMessage());
            io.printeDesign();
        } catch (ExceptionId e) {
            io.printeDesign();
            logger.log(Level.INFO,e.getMessage());
            io.printeDesign();
        } catch (RuntimeException e) {
            io.printeDesign();
            logger.log(Level.INFO,"The string contains symbols, please try again");
            io.printeDesign();
        }
    }

    /**
     *The method outputs the number of elements whose Organization parameter is equal to what the user enters.
     *
     * @param organization A new Organization type object created by the user is passed as a parameter.
     */
    public void countByOrganization(Organization organization) {
        // todo
        //LinkedList
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

    /**
     * The method removes elements from the collection with an id less than the one specified by the user.
     *
     * @throws ExceptionId If the entered id value is less than 0 or exceeds the lower bound of the Integer type.
     * @throws ExceptionBigIntegerMAX_INTEGER If the entered value exceeds the upper bound of the Integer type.
     * @throws RuntimeException If the user entered the id incorrectly.
     * @param argument The argument passed is the argument entered by the user.
     */
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
            io.printeDesign();
            //удаление элементов началось
            logger.log(Level.INFO,"Deletion of items started");
            io.printeDesign();
            Worker tip = new Worker();
            tip.setId(idConsole);
            while(iterator.hasNext()){
                Worker worker = iterator.next();
                if(worker.compareTo(tip) < 0){
                    iterator.remove();
                }
            }
            // todo
            //
        } catch (ExceptionBigIntegerMAX_INTEGER e){
            io.printeDesign();
            logger.log(Level.INFO,e.getMessage());
            io.printeDesign();
        } catch (ExceptionId e) {
            io.printeDesign();
            logger.log(Level.INFO,e.getMessage());
            io.printeDesign();
        } catch (RuntimeException e) {
            io.printeDesign();
            logger.log(Level.INFO,"The string contains symbols, please try again");
            io.printeDesign();
        }
    }

    /**
     * The method sorts employees by endDate; if the data matches, employees are sorted by id.
     */
    public void sort(){
        if (listWorker.isEmpty()){
            //не найдено зарегистрированных работников
            logger.log(Level.INFO,"No registered employees found");
        }
        LinkedList<Worker> sortedList = new LinkedList<>(listWorker);


        // todo
        //вынести с метода
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

    /**
     * The method returns the id of the last employee in the list.
     * @return id
     */
    public int tailWorked() {
        if (listWorker.isEmpty()) {
            return 0;
        } else{
        return  listWorker.getLast().getId();}
    }

    /**
     *A method that checks if there are elements in the collection with the same id
     *  as the id entered by the user.
     *
     * @param argument The employee's id is passed as an argument.
     * Attention! The method contains ignored exceptions, as the command works in
     * symbiosis with other commands that do not ignore similar exceptions.
     */
    public void idMatches(String argument) {
        LinkedList<Worker> workId = new LinkedList<>();
        try {
            BigInteger bigInteger = new BigInteger(argument);

            if (bigInteger.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
                throw new ExceptionBigIntegerMAX_INTEGER();
            }

            if (bigInteger.compareTo(BigInteger.valueOf(0)) <= 0) {
                throw new ExceptionId();
            }

            int idConsole = Integer.parseInt(argument);

                for (Worker worker : listWorker){
                    if (idConsole == worker.getId()){
                        workId.add(worker);
                    }
                }


            if (workId.isEmpty()){
                logger.log(Level.INFO,"No employee with this ID");

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