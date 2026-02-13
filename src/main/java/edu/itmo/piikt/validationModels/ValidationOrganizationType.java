package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.reader.InputReader;

public class ValidationOrganizationType {
    private InputReader scanner;
    public ValidationOrganizationType(){
        this.scanner = InputReader.getInstance();
    }

    public OrganizationType validationOrganizationType(){
        String organizationTypeConsole = scanner.nextLine();
        for (OrganizationType nameObject : OrganizationType.values()){
            if(nameObject.name().equals(organizationTypeConsole)){
                return nameObject;
            }
        }
        return validationOrganizationType();
    }
}
