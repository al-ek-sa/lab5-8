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
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * The main class of the program. The class determines which command was called.
 *
 * @author Lishyk Aliaksandra
 * @version 1.3
 */
@Getter
public enum ValidationCommand {
	INSTANCE;

	private static final AppLogger logger = new AppLogger(ValidationCommand.class);
	/** Flag controlling the main command loop */
	private boolean flag;
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
	private final Deque<IOProvider> dequeProvider = new ArrayDeque<>();
	private IOProvider provider;

	ValidationCommand() {
		this.flag = true;
	}

	public void setNetwork(Network network) {
		this.network = network;
		this.addCommand = new AddCommand(network, worker);
		this.updateCommand = new UpdateCommand(network, addCommand);
	}

	/**
	 * Pushes a new IOProvider onto the stack, switching the current input source
	 *
	 * @param io
	 *            new IOProvider to activate
	 */
	public void pushProvider(IOProvider io) {
		if (provider != null) {
			dequeProvider.push(provider);
		}
		provider = io;
	}

	/**
	 * Pops the previous IOProvider from the stack, restoring the previous input
	 * source
	 */
	public void popProvider() {
		if (!dequeProvider.isEmpty())
			provider = dequeProvider.pop();
	}

	/** Gets the next command from the current IOProvider */
	public String nextCommand() {
		if (provider == null)
			return null;
		String line = provider.readLine();
		if (line == null && !dequeProvider.isEmpty()) {
			popProvider();
			return nextCommand();
		}
		return line;
	}

	/**
	 * Continuously reads commands, recognizes them
	 *
	 * @param io
	 *            initial IOProvider
	 */
	public void validation(IOProvider io) {
		provider = io;
		dequeProvider.clear();
		logger.info("Starting command validation loop");
		while (flag) {
			try {
				String nameCommand = nextCommand();
				if (nameCommand == null || nameCommand.isBlank()) {
					continue;
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
								continue;
							}
							if (com1.equals(Commands.EXIT.getName())) {
								logger.info("Exit command received");
								flag = false;
								break;
							}
							if (com1.equals(Commands.ADD.getName())) {
								logger.debug("Executing ADD command");
								var server = addCommand.execute(provider);
								server.printToConsole();
								continue;
							}
							if (com1.equals(Commands.COUNT_BY_ORGANIZATION.getName())) {
								logger.debug("Executing COUNT_BY_ORGANIZATION command");
								OrganizationData organizationData = organization.build(provider);
								ClientCommand clientCommand = ClientCommand.builder()
										.nameCommand(Commands.COUNT_BY_ORGANIZATION.getName()).data(organizationData)
										.build();
								ServerResponse serverResponse = network.send(clientCommand);
								serverResponse.printToConsole();
								continue;
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
								continue;
							}
							if (com2.equals(Commands.UPDATE.getName())) {
								logger.debug("Executing UPDATE command for id: {}", argument);
								updateCommand.update(provider, com2, argument).printToConsole();
								continue;
							}
							ClientCommand clientCommand = ClientCommand.builder().nameCommand(com2)
									.argumentCommand(argument).build();
							ServerResponse serverResponse = network.send(clientCommand);
							serverResponse.printToConsole();
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error in validation loop: {}", e);
				throw new RuntimeException(e);
			}
		}
		logger.info("Command validation loop ended");
	}
}
