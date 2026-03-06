package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.validationModels.ValidationOrganization;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command count_by_organization organization : output
 * the number of elements whose organization field value is equal to the
 * specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class CountByOrganizationCommand {
    Logger logger = Logger.getLogger(CountByOrganizationCommand.class.getName());

    public CountByOrganizationCommand() {
    }

    /**
     * The method outputs the number of elements whose Organization parameter is
     * equal to what the user enters.
     */

    public void execute(IOProvider io) {
        try {
            ValidationOrganization organization = new ValidationOrganization();
            logger.log(Level.INFO, "Enter all values for Organization");
            var listWorker = HistoryWorker.getInstance().getListWorker();
            // todo
            long size = listWorker.stream().filter(worker -> worker.getOrganization() != null)
                    .filter(worker -> worker.getOrganization().equals(organization)).count();
            io.printlnInt((int) size);
            logger.log(Level.INFO, "Number of elements displayed successfully");
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "Execution error, elements not displayed");
        }
    }
}
