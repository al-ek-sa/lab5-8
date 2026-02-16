package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.models.Status;
import edu.itmo.piikt.reader.InputReader;

public class ValidationOrganizationType {
    private InputReader scanner;
    public ValidationOrganizationType(){
        this.scanner = InputReader.getInstance();
    }

    public OrganizationType organizationType(){
        while (true){
            System.out.println("Organization type options:");
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
                System.out.println("Invalid input, please enter the value again");
            }
        }
    }
}
