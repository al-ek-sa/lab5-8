package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.models.OrganizationType;
import edu.itmo.piikt.reader.InputReader;

public class ValidationOrganizationType {
    private InputReader scanner;
    public ValidationOrganizationType(){
        this.scanner = InputReader.getInstance();
    }

    public OrganizationType validationOrganizationType(){
        System.out.println("Выпишите в верхнем регистре один из нескольких вариантов: ");
        for (OrganizationType type : OrganizationType.values()){
            System.out.println(type);
        }
        String organizationTypeConsole = scanner.nextLine();
        for (OrganizationType nameObject : OrganizationType.values()){
            if(nameObject.name().equals(organizationTypeConsole)){
                return nameObject;
            }
        }
        return validationOrganizationType();
    }
}
