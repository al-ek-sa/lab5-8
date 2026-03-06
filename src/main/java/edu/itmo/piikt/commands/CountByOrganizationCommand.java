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
 * @version 1.0
 */
public class CountByOrganizationCommand {
    Logger logger = Logger.getLogger(CountByOrganizationCommand.class.getName());

    public CountByOrganizationCommand() {
    }

    public void execute(IOProvider io) {
        try {
            ValidationOrganization organization = new ValidationOrganization();
            logger.log(Level.INFO, "Enter all values for Organization");
            HistoryWorker.getInstance().countByOrganization(organization.organization(io), io);
            logger.log(Level.INFO, "Number of elements displayed successfully");
        } catch (RuntimeException e) {
            logger.log(Level.INFO, "Execution error, elements not displayed");
        }
    }
}
