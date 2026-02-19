package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.reader.InputReader;

public class ValidationOrganizationType {
    private InputReader scanner;
    private IOProvider io;
    public ValidationOrganizationType(IOProvider io){
        this.scanner = InputReader.getInstance();
        this.io = io;
    }

    public OrganizationType organizationType(){
        while (true){
            //Выберите тип организации (введите ее номер)
            io.printField("Select the organization type", "(enter its number)");
            for (OrganizationType type : OrganizationType.values()) {
                System.out.println("(" + type.getId() +") " + type.name());
            }
            try {
                String idStatus = scanner.nextLine();
                int id = Integer.parseInt(idStatus);
                for (OrganizationType type : OrganizationType.values()) {
                    if (type.getId() == id) {
                        return type;
                    }
                }
            } catch (RuntimeException e) {
                //Либо ничего не введено либо цифры не те либо буквы
                io.printError("Invalid input, please enter the value again");
            }
        }
    }
}
