package edu.itmo.piikt.server.command.model;

import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.models.Organization;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class CountByOrganizationCommand implements CommandType {
	private static final AppLogger logger = new AppLogger(CountByOrganizationCommand.class);

	@Override
	public ServerResponse execute(ClientCommand clientCommand) {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("Command unavailable, please try again later");
			}
			logger.info("Executing COUNT_BY_ORGANIZATION command");

			String searchData = clientCommand.argumentCommand();
			logger.info("Received search data: {}", searchData);

			if (searchData == null || searchData.isEmpty()) {
				return ServerResponse.error("No search data provided");
			}

			String[] parts = searchData.split(":");
			if (parts.length != 3) {
				return ServerResponse.error("Invalid search data format");
			}

			String turnoverStr = parts[0];
			String typeIdStr = parts[1];
			String address = parts[2];

			int turnover;
			try {
				turnover = Integer.parseInt(turnoverStr);
			} catch (NumberFormatException e) {
				return ServerResponse.error("Annual turnover must be a number");
			}

			int typeId;
			try {
				typeId = Integer.parseInt(typeIdStr);
			} catch (NumberFormatException e) {
				return ServerResponse.error("Organization type must be a number");
			}

			final int finalTurnover = turnover;
			final int finalTypeId = typeId;
			final String finalAddress = address;

			var listWorker = HistoryWorker.INSTANCE.getListWorker();
			List<String> workersList = listWorker.stream().filter(worker -> worker.getOrganization() != null)
					.filter(worker -> {
						Organization org = worker.getOrganization();

						if (org.getAnnualTurnover() != finalTurnover) {
							return false;
						}

						if (org.getType() == null) {
							return false;
						}
						if (org.getType().getId() != finalTypeId) {
							return false;
						}

						if (finalAddress != null && !finalAddress.isEmpty()) {
							if (org.getOfficialAddress() == null) {
								return false;
							}
							String orgStreet = org.getOfficialAddress().getStreet();
							return orgStreet != null && orgStreet.equals(finalAddress);
						}

						return true;
					}).map(Worker::toString).collect(Collectors.toList());

			logger.info("Found workers count: {}", workersList.size());

			String result = String.join("###", workersList);
			return ServerResponse.successfulCompletion(result);

		} catch (Exception e) {
			logger.error("Error executing COUNT_BY_ORGANIZATION: {}", e);
			return ServerResponse.error("Internal server error: " + e.getMessage());
		}
	}
}
