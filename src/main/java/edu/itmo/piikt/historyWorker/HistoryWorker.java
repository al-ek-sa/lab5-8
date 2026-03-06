package edu.itmo.piikt.historyWorker;

import edu.itmo.piikt.exception.*;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.*;
import edu.itmo.piikt.validationModels.ValidationOrganization;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for storing a collection with registered employees. The class is a
 * singleton.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class HistoryWorker {
    private static HistoryWorker instance;
    private Date data;
    private ValidationOrganization organization;
    LinkedList<Worker> listWorker = new LinkedList<>();
    Logger logger = Logger.getLogger(HistoryWorker.class.getName());

    private HistoryWorker() {
        this.data = new Date();
        this.organization = new ValidationOrganization();
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

    public void remove(Worker worker) {
        listWorker.remove(worker);
    }

    public void clear() {
        listWorker.clear();
    }

    /** The method outputs the data of the first element in the collection. */
    public void peekFirst(IOProvider io) {
        if (listWorker.isEmpty()) {
            logger.log(Level.INFO, "No registered employees found");
            return;
        }

        io.println(listWorker.getFirst().toString());
        logger.log(Level.INFO, "Element displayed on the screen");
    }

    /** The method outputs data of all registered employees. */
    public void printHistoryWorker(IOProvider io) {
        if (listWorker.isEmpty()) {
            logger.log(Level.INFO, "No registered employees found");
            return;
        }
        listWorker.forEach(worker -> io.println(worker.toString()));
    }

    /**
     * The method outputs all employees with the same name as entered by the user.
     *
     * @param nameConsole
     *            The name entered by the user.
     */
    public void printName(String nameConsole, IOProvider io) {
        listWorker.stream().filter(worker -> worker.getName() != null)
                .filter(worker -> worker.getName().equals(nameConsole))
                .forEach(worker -> io.println(worker.toString()));
        logger.log(Level.INFO, "All users with the entered name have been displayed");
    }

    /** The method outputs data about the collection. */
    public void infoLiat(IOProvider io) {
        io.println("Collection type: " + listWorker.getClass() + "\nIdentification time: " + data
                + "\nNumber of elements: " + listWorker.size());
    }

    /**
     * The method removes from the collection the element whose id is specified by
     * the user.
     *
     * @param idConsole
     *            The argument passed is the argument entered by the user.
     */
    public void removeId(String idConsole) {
        try {
            listWorker.removeIf(worker -> worker.getId().equals(idConsole));
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "Invalid input");
        }
    }

    /**
     * The method replaces the element whose id is equal to the id specified by the
     * user.
     *
     * @throws RuntimeException
     *             If the user entered the id in an incorrect format.
     * @throws ExceptionBigIntegerMAX_INTEGER
     *             If the user entered a number that exceeds the upper limit of the
     *             Integer type.
     * @throws ExceptionId
     *             If the user entered id <= 0 or if the number exceeded the lower
     *             limit of the Integer type.
     * @param argument
     *            The element's id is passed.
     * @param worker
     *            A new employee object.
     */
    public void update(String argument, Worker worker) {
        try {
            // TODO
            UUID input = UUID.fromString(argument);
            logger.log(Level.INFO, "Start of data update");
            listWorker.removeIf(worker1 -> worker1.getId().equals(argument));
            add(worker);
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "The string contains symbols, please try again");
        }
    }

    /**
     * The method outputs the number of elements whose Organization parameter is
     * equal to what the user enters.
     *
     * @param organization
     *            A new Organization type object created by the user is passed as a
     *            parameter.
     */
    public void countByOrganization(Organization organization, IOProvider io) {
        // todo
        long size = listWorker.stream().filter(worker -> worker.getOrganization() != null)
                .filter(worker -> worker.getOrganization().equals(organization)).count();
        io.printlnInt((int) size);
    }

    /**
     * The method removes elements from the collection with an id less than the one
     * specified by the user.
     *
     * @throws ExceptionId
     *             If the entered id value is less than 0 or exceeds the lower bound
     *             of the Integer type.
     * @throws ExceptionBigIntegerMAX_INTEGER
     *             If the entered value exceeds the upper bound of the Integer type.
     * @throws RuntimeException
     *             If the user entered the id incorrectly.
     * @param argument
     *            The argument passed is the argument entered by the user.
     */
    public void removeLower(String argument) {
        try {
            UUID input = UUID.fromString(argument);
            logger.log(Level.INFO, "Deletion of items started");
            listWorker.removeIf(worker -> {
                UUID workerUuid = UUID.fromString(worker.getId());
                return workerUuid.compareTo(input) < 0;
            });
            logger.log(Level.INFO, "Items successfully deleted");
            // todo
        } catch (IllegalArgumentException e) {
            logger.log(Level.INFO, "Invalid UUID format");
        }
    }

    /**
     * The method sorts employees by endDate; if the data matches, employees are
     * sorted by id.
     */
    public void sort(IOProvider io) {
        if (listWorker.isEmpty()) {
            logger.log(Level.INFO, "No registered employees found");
            return;
        }
        LinkedList<Worker> sortedList = new LinkedList<>(listWorker);
        sortedList.sort((worker1, worker2) -> {
            if (worker1.getEndDate() == null && worker2.getEndDate() == null) {
                return worker1.getId().compareTo(worker2.getId());
            }
            if (worker1.getEndDate() == null)
                return 1;
            if (worker2.getEndDate() == null)
                return -1;
            return worker1.getEndDate().compareTo(worker2.getEndDate());
        });

        Collections.reverse(sortedList);
        sortedList.forEach(worker -> io.println(worker.toString()));
        // todo
    }

    /**
     * The method returns the id of the last employee in the list.
     *
     * @return id
     */
    public String tailWorked() {
        if (listWorker.isEmpty()) {
            return "_______";
        } else {
            return listWorker.getLast().getId();
        }
    }

    /**
     * A method that checks if there are elements in the collection with the same id
     * as the id entered by the user.
     */
    public void idMatches(String argument) {
        boolean found = listWorker.stream().anyMatch(worker -> worker.getId().equals(argument));
        if (!found) {
            logger.log(Level.INFO, "No employee with this ID");
        }
    }
}
