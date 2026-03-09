package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseSimpleCommand;
import edu.itmo.piikt.massage.MessageCommand;
import edu.itmo.piikt.models.Organization;
import edu.itmo.piikt.validation.modelValidation.ValidationOrganization;

/**
 * The class implements the command count_by_organization organization : output
 * the number of elements whose organization field value is equal to the
 * specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 */
public final class CountByOrganizationCommand implements BaseSimpleCommand {
    public CountByOrganizationCommand() {
    }

    /**
     * The method outputs the number of elements whose Organization parameter is
     * equal to what the user enters.
     */
    @Override
    public void doExecute(IOProvider io) {
        ValidationOrganization validationOrganization = new ValidationOrganization(io);
        Organization organization = validationOrganization.organization(io);
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        long size = listWorker.stream().filter(worker -> worker.getOrganization() != null)
                .filter(worker -> worker.getOrganization().equals(organization)).count();
        io.printlnInt((int) size);
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.COUNT_BY_ORGANIZATION;
    }
}
