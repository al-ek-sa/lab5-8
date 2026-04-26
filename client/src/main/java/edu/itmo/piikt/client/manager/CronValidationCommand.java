package edu.itmo.piikt.client.manager;

import edu.itmo.piikt.client.command.AddCommand;
import edu.itmo.piikt.client.command.ExecuteScriptCommand;
import edu.itmo.piikt.client.command.UpdateCommand;
import edu.itmo.piikt.client.command.history.HistoryCommand;
import edu.itmo.piikt.client.command.history.HistoryCommands;
import edu.itmo.piikt.client.data.Organization;
import edu.itmo.piikt.client.data.Worker;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command validator for cron mode
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public enum CronValidationCommand {
	INSTANCE;

	private static final AppLogger logger = new AppLogger(CronValidationCommand.class);
	final Worker worker = new Worker();
	private Network network;
	private UpdateCommand updateCommand;
	private AddCommand addCommand;
	final HistoryCommand historyCommand = new HistoryCommand();
	final Organization organization = new Organization();
	final ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand();
	final List<String> argumentCommand = Arrays.stream(Commands.values()).filter((Commands::getArgument))
			.map(Commands::getName).collect(Collectors.toList());
	final List<String> baseCommand = Arrays.stream(Commands.values()).filter(com -> !com.getArgument())
			.map(Commands::getName).collect(Collectors.toList());

	public void setNetwork(Network network) {
		this.network = network;
		this.addCommand = new AddCommand(network, worker);
		this.updateCommand = new UpdateCommand(network, addCommand);
	}
	/**
	 * Executes a single command and exits
	 *
	 * @param provider
	 *            input/output provider
	 * @param nameCommand
	 *            name command
	 */
	public void validation(IOProvider provider, String nameCommand) {
		logger.info("Starting cron validation");
		try {
			if (nameCommand == null || nameCommand.isBlank()) {
				return;
			}
			logger.debug("User input: {}", nameCommand);
			HistoryCommands.INSTANCE.add(nameCommand);
			String command = nameCommand.trim();
			String[] input = command.split("\\s+");
			String element = input[0];
			// Command without arguments
			if (input.length == 1) {
				for (String com1 : baseCommand) {
					// Typo correction
					if (DamerauLevenshteinDistance.distance(com1, element) <= 1) {
						if (com1.equals(Commands.HISTORY.getName())) {
							historyCommand.execute(provider);
							return;
						}
						if (com1.equals(Commands.ADD.getName())) {
							logger.debug("Executing ADD command");
							var server = addCommand.execute(provider);
							server.printToConsole();
							return;
						}
						if (com1.equals(Commands.COUNT_BY_ORGANIZATION.getName())) {
							logger.debug("Executing COUNT_BY_ORGANIZATION command");
							OrganizationData organizationData = organization.build(provider);
							ClientCommand clientCommand = ClientCommand.builder()
									.nameCommand(Commands.COUNT_BY_ORGANIZATION.getName()).data(organizationData)
									.build();
							ServerResponse serverResponse = network.send(clientCommand);
							serverResponse.printToConsole();
							return;
						}
						ClientCommand clientCommand = ClientCommand.builder().nameCommand(com1).build();
						ServerResponse serverResponse = network.send(clientCommand);
						serverResponse.printToConsole();
					}
				}
			}
			// Command with argument
			if (input.length == 2) {
				String argument = input[1];
				for (String com2 : argumentCommand) {
					if (DamerauLevenshteinDistance.distance(com2, element) <= 1) {
						if (com2.equals(Commands.EXECUTE_SCRIPT.getName())) {
							logger.debug("Executing script: {}", argument);
							executeScriptCommand.execute(provider, argument);
							return;
						}
						if (com2.equals(Commands.UPDATE.getName())) {
							logger.debug("Executing UPDATE command for id: {}", argument);
							updateCommand.execute(provider, com2, argument).printToConsole();
							return;
						}
						ClientCommand clientCommand = ClientCommand.builder().nameCommand(com2)
								.argumentCommand(argument).build();
						ServerResponse serverResponse = network.send(clientCommand);
						serverResponse.printToConsole();
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error in cron validation: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
