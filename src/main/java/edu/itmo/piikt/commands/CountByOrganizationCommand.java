package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

public class CountByOrganizationCommand extends Commands {
    private IOProvider io;
    public  CountByOrganizationCommand(IOProvider io){
        super("count_by_organization");
        this.io =io;
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            //Введите все значения для организации
            io.println("Enter all values for Organization");
            io.printeDesign();
            HistoryWorker.getInstance(io).countByOrganization();
            io.printeDesign();
            io.println("Number of elements displayed successfully");
            io.printeDesign();
        } catch (RuntimeException e){
            io.printeDesign();
            //ошибка выполнения, элементы не выведены
            io.printError("Execution error, elements not displayed");
            io.printeDesign();
        }
    }
}
