package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.WorkerObject.BuilderOrganization;
import edu.itmo.piikt.server.WorkerObject.OrganizationBuilder;
import edu.itmo.piikt.server.WorkerObject.ValidationError;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.models.Organization;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.logging.Logger;

/**
 * The class implements the command count_by_organization organization : output
 * the number of elements whose organization field value is equal to the
 * specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class CountByOrganizationCommand {
    private final BuilderOrganization builderOrganization = new BuilderOrganization();
    private final OrganizationBuilder organizationBuilder = new OrganizationBuilder();
    private static final Logger logger= Logger.getLogger(CountByOrganizationCommand.class.getName());
    /**
     * The method outputs the number of elements whose Organization parameter is
     * equal to what the user enters.
     */
    public ServerResponse execute(ClientCommand clientCommand) {
        OrganizationData organizationData = (OrganizationData) clientCommand.getData();
        Object result = builderOrganization.data(organizationData);
        if (result instanceof OrganizationData) {
            Organization organization = organizationBuilder.organizationBuilder(organizationData);
            var listWorker = HistoryWorker.INSTANCE.getListWorker();
            long size = listWorker.stream().filter(worker -> worker.getOrganization() != null)
                    .filter(worker -> worker.getOrganization().equals(organization)).count();
            logger.info(LoggerCommand.COUNT_BY_ORGANIZATION.getLogMessage());
            return ServerResponse.successfulCompletion("COUNT_BY_ORGANIZATION: ", List.of(String.valueOf(size)));
        } else if (result instanceof ValidationError) {
            ValidationError validationError = (ValidationError) result;
            logger.info(LoggerCommand.COUNT_BY_ORGANIZATION.getLogMessage());
            return ServerResponse.error("данные введены неверно ", validationError.getErrors(), validationError.getData());
        }
        logger.info(LoggerCommand.COUNT_BY_ORGANIZATION.getLogMessage());
        return ServerResponse.error("Неизвестная ошибка");
    }
}
