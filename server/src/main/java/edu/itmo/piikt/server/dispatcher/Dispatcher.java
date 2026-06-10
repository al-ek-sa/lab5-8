package edu.itmo.piikt.server.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.model.*;
import edu.itmo.piikt.server.manager.CollectionManager;
import edu.itmo.piikt.server.manager.Websocket;

import java.util.EnumMap;
import java.util.function.Function;

public class Dispatcher {
	private static final AppLogger logger = new AppLogger(Dispatcher.class);
	private final EnumMap<Commands, Function<ClientCommand, ServerResponse>> enumMap = new EnumMap<>(Commands.class);
	private final ObjectMapper objectMapper;
	private Websocket wsServer;
	private CollectionManager collectionManager;

	private final AddCommand addCommand = new AddCommand();
	private final UpdateIdCommand updateCommand = new UpdateIdCommand();
	private final RemoveByIdCommand removeCommand = new RemoveByIdCommand();
	private final ClearCommand clearCommand = new ClearCommand();
	private final RemoveLowerCommand removeLowerCommand = new RemoveLowerCommand();

	public Dispatcher() {
		objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();

		enumMap.put(Commands.ADD, addCommand::execute);
		enumMap.put(Commands.UPDATE, updateCommand::execute);
		enumMap.put(Commands.REMOVE_BY_ID, removeCommand::execute);
		enumMap.put(Commands.REMOVE_LOWER, removeLowerCommand::execute);
		enumMap.put(Commands.CLEAR, clearCommand::execute);
		enumMap.put(Commands.COUNT_BY_ORGANIZATION, com -> new CountByOrganizationCommand().execute(com));
		enumMap.put(Commands.INFO, com -> new InfoCommand().execute());
		enumMap.put(Commands.SHOW, com -> new ShowCommand().execute());
		enumMap.put(Commands.HEAD, com -> new HeadCommand().execute());
		enumMap.put(Commands.HELP, com -> new HelpCommand().execute());
	}

	public void setWebSocketServer(Websocket wsServer) {
		this.wsServer = wsServer;

		addCommand.setWebSocketServer(wsServer);
		updateCommand.setWebSocketServer(wsServer);
		removeCommand.setWebSocketServer(wsServer);
		clearCommand.setWebSocketServer(wsServer);
		removeLowerCommand.setWebSocketServer(wsServer);
	}

	public void setCollectionManager(CollectionManager collectionManager) {
		this.collectionManager = collectionManager;

		addCommand.setCollectionManager(collectionManager);
		updateCommand.setCollectionManager(collectionManager);
		removeCommand.setCollectionManager(collectionManager);
		clearCommand.setCollectionManager(collectionManager);
		removeLowerCommand.setCollectionManager(collectionManager);
	}

	public ServerResponse dispatcher(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			String commandName = command.nameCommand();
			logger.debug("Dispatching command: {}", commandName);
			Commands commands = Commands.nameCommands(commandName);
			if (commands == null) {
				logger.warn("Unknown command: {}", commandName);
				return null;
			}

			Function<ClientCommand, ServerResponse> input = enumMap.get(commands);
			if (input == null) {
				logger.error("Command {} not implemented in dispatcher", commandName);
				return ServerResponse.error("Command not implemented");
			}

			logger.info("Executing command: {}", commandName);
			ServerResponse response = input.apply(command);
			logger.debug("Command {} completed: success={}", commandName, response.execution());
			return response;
		} catch (Exception e) {
			logger.error("Error dispatching command: {}", e);
			return ServerResponse.error("Internal server error");
		}
	}
}
