package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.client.provider.IOProvider;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.server.validation.modelValidation.ValidationOrganization;
import lombok.NoArgsConstructor;

/**
 * The class implements the command count_by_organization organization : output
 * the number of elements whose organization field value is equal to the
 * specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 * @see IOProvider
 * @see ValidationOrganization
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class CountByOrganizationCommand {
    /**
     * The method outputs the number of elements whose Organization parameter is
     * equal to what the user enters.
     */
    public ServerResponse execute() {
        ValidationOrganization validationOrganization = new ValidationOrganization(io);
        Organization organization = validationOrganization.organization(io);
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        long size = listWorker.stream().filter(worker -> worker.getOrganization() != null)
                .filter(worker -> worker.getOrganization().equals(organization)).count();
        io.printlnInt((int) size);
    }
}
