package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.validationModels.ValidationOrganization;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command  count_by_organization organization : output the number of elements whose organization field value is equal to the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class CountByOrganizationCommand extends Commands {
    private IOProvider io;
    private ValidationOrganization organization;
    Logger logger = Logger.getLogger(CountByOrganizationCommand.class.getName());
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
            logger.log(Level.INFO,"Enter all values for Organization");
            io.printeDesign();
            HistoryWorker.getInstance(io).countByOrganization(organization.organization());
            io.printeDesign();
            logger.log(Level.INFO,"Number of elements displayed successfully");
            io.printeDesign();
        } catch (RuntimeException e){
            io.printeDesign();
            //ошибка выполнения, элементы не выведены
            logger.log(Level.INFO,"Execution error, elements not displayed");
            io.printeDesign();
        }
    }
}
