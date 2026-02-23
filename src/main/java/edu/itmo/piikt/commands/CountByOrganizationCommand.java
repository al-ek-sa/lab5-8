package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.validationModels.ValidationOrganization;

public class CountByOrganizationCommand extends Commands {
    private IOProvider io;
    private ValidationOrganization organization;
    public  CountByOrganizationCommand(IOProvider io){
        super("count_by_organization");
        this.io =io;
        this.organization = new ValidationOrganization(io);
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            //Введите все значения для организации
            io.printlnCommand("Enter all values for Organization");
            io.printeDesign();
            HistoryWorker.getInstance(io).countByOrganization(organization.organization());
            io.printeDesign();
            io.printlnCommand("Number of elements displayed successfully");
            io.printeDesign();
        } catch (RuntimeException e){
            io.printeDesign();
            //ошибка выполнения, элементы не выведены
            io.printException("Execution error, elements not displayed");
            io.printeDesign();
        }
    }
}
