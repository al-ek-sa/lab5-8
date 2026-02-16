package edu.itmo.piikt.historyWorker;

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
    private static HistoryWorker instance;
    private Date data;
    private ValidationWorker worker;
    private ValidationOrganization organization;
    LinkedList<Worker> listWorker = new LinkedList<>();

    private HistoryWorker() {
        this.data = new Date();
        this.worker = new ValidationWorker();
        this.organization = new ValidationOrganization();
    }

    public static HistoryWorker getInstance() {
        if (instance == null) {
            instance = new HistoryWorker();
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

    public void printName() {
        Iterator<Worker> iterator = listWorker.iterator();
        String nameConsole = InputReader.getInstance().nextLine();
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

    public void removeId() {
        Iterator<Worker> iterator = listWorker.iterator();
        try {
            String input = InputReader.getInstance().nextLine();
            int idConsole = Integer.parseInt(input);
            InputReader.getInstance().nextLine();
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

    public void update() {
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
        GeneratorId.getInstance().setStartId(idConsole);
        add(worker.worker());
        GeneratorId.getInstance().setStartId(idNewStart);
    }

    /**public void countByOrganization() {
        Iterator<Worker> iterator = listWorker.iterator();
        LinkedList<Organization> listOrganization = new LinkedList<>();
        class OraganizationConsole{
         private int annualTurnoverConsole; //Значение поля должно быть больше 0
         private OrganizationType typeConsole; //Поле не может быть null
         private Address officialAddressConsole; //Поле не может быть null
            public OraganizationConsole(int annualTurnoverConsole, OrganizationType typeConsole, Address officialAddressConsole){
                this.annualTurnoverConsole = annualTurnoverConsole;
                this.typeConsole =typeConsole;
                this.officialAddressConsole =
            }
         }

        if (iterator.hasNext()) {
            Worker worker = iterator.next();
            int annualTurnoverConsole = InputReader.getInstance().nextInt();
            if (annualTurnoverConsole == worker.getOrganization().getAnnualTurnover()){
                Organization typeConsole =  InputReader.getInstance().nextLine();
                String type = (String) worker.getType
                if (type == worker.getType)
            }

        }
    }*/

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

    public void removeLower() {
        System.out.println("Enter the id of the element to remove: ");
        /**ArrayList<Integer> idWorker = new ArrayList<>();*/
        Iterator<Worker> iterator = listWorker.iterator();
        try {
            String input = InputReader.getInstance().nextLine();
            int idConsole = Integer.parseInt(input);
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
        /**listWorker.sort(Comparator.comparZonedDateTime(worker -> worker.getEndDate()).reversed);*/
    }
}