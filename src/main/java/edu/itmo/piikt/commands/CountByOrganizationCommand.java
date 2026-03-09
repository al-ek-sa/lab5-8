package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.MessageCommand;
import edu.itmo.piikt.models.Organization;
import edu.itmo.piikt.validationModels.ValidationOrganization;

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
