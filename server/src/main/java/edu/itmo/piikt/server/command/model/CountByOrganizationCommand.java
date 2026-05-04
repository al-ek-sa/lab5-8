package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.validation.object.BuilderOrganization;
import edu.itmo.piikt.server.validation.object.OrganizationBuilder;
import edu.itmo.piikt.server.validation.object.ValidationError;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The class implements the command count_by_organization organization : output
 * the number of elements whose organization field value is equal to the
 * specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 3.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class CountByOrganizationCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(CountByOrganizationCommand.class);
	private final BuilderOrganization builderOrganization = new BuilderOrganization();
	private final OrganizationBuilder organizationBuilder = new OrganizationBuilder();

	/**
	 * Executes the COUNT_BY_ORGANIZATION command
	 *
	 * @param clientCommand
	 *            command containing OrganizationData
	 * @return with count result or error
	 */
	@Override
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("         return ServerResponse.error(\"Command unavailable, please try again later");
			}
			logger.info("Executing COUNT_BY_ORGANIZATION command");
			OrganizationData organizationData = (OrganizationData) clientCommand.getData();
			logger.debug("Organization data: turnover={}, type={}, street={}", organizationData.getAnnualTurnover(),
					organizationData.getType().getId(), organizationData.getOfficialAddress().getStreet());
			// Validate data
			Object result = builderOrganization.data(organizationData);
			if (result instanceof OrganizationData) {
				Organization organization = organizationBuilder.organizationBuilder(organizationData);
				var listWorker = HistoryWorker.INSTANCE.getListWorker();
				long size = listWorker.stream().filter(worker -> worker.getOrganization() != null)
						.filter(worker -> worker.getOrganization().equals(organization)).count();
				logger.info("Count result: {}", size);
				return ServerResponse.successfulCompletion("COUNT_BY_ORGANIZATION: ", List.of(String.valueOf(size)));
			} else if (result instanceof ValidationError(List<edu.itmo.piikt.common.data.MessageExceptionValidation>errors,Object data)) {
				logger.warn("Validation failed: {} errors", errors.size());
				return ServerResponse.error("Invalid data entered", errors, data);
			}
			logger.error("Unknown result type from builder");
			return ServerResponse.error("Internal server error while processing COUNT_BY_ORGANIZATION command");
		} catch (Exception e) {
			logger.error("Error executing COUNT_BY_ORGANIZATION: {}", e);
			throw new RuntimeException(e);
		}
	}
}
