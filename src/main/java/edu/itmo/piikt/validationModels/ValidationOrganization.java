package edu.itmo.piikt.validationModels;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.models.Address;
import edu.itmo.piikt.models.Organization;
import edu.itmo.piikt.models.OrganizationType;

public class ValidationOrganization {
    private ValidationOrganizationType type;
    private ValidationAddress address;
    private IOProvider io;
    public ValidationOrganization(IOProvider io){
        this.type = new ValidationOrganizationType(io);
        this.address = new ValidationAddress(io);
        this.io = io;
    }

    public Address validationNullAddress(){
        while (true){
            //Введите адрес (поле обязательно к заполнению)
            io.printField("Enter the address", "(required field)");
            Address addressConsole = address.validationAddress();
            if (addressConsole != null ){
                return addressConsole;
                //поле не заполнено, повторите попытку
            } else io.printException("Field is empty, please try again");
        }
    }

    public OrganizationType validationNullOrganizationType(){
        //Выберите тип организации
        io.println("Select the organization type");
        OrganizationType organizationTypeConsole = type.organizationType();
        if (organizationTypeConsole != null ) {
            return organizationTypeConsole;
        } return validationNullOrganizationType();
    }

    public int validationAnnualTurnover(){
        while (true) {
            try{
                //Введите годовой оборот (годовой оборот должен быть целым числом больше 0, поле обязательно к заполнению)
                io.printField("Enter annual turnover", "(annual turnover must be an integer greater than 0. Field is required)");
                String input = io.readLine();
                int annualTurnoverConsole = Integer.parseInt(input);
                if (annualTurnoverConsole > 0) {
                    return annualTurnoverConsole;
                    //Введено неположительное значение, повторите попытку
                } else io.printException("A non-positive value has been entered, please try again");
        } catch (RuntimeException e) {
                io.printError("Invalid input, please enter the value again");
            }}
    }

    public Organization organization(){
        return new Organization(validationAnnualTurnover(), validationNullOrganizationType(), validationNullAddress());
    }
}
