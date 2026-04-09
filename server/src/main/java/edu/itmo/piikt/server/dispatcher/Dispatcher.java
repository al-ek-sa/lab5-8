package edu.itmo.piikt.server.dispatcher;

import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.command.modelCommand.*;

import java.util.EnumMap;
import java.util.function.Function;

public class Dispatcher {
    private static final AppLogger logger = new AppLogger(Dispatcher.class);
    private final EnumMap<Commands, Function<ClientCommand, ServerResponse>> enumMap = new EnumMap<>(Commands.class);

    public Dispatcher() {
        enumMap.put(Commands.ADD, com -> new AddCommand().execute(com));
        enumMap.put(Commands.UPDATE, com -> new UpdateIdCommand().execute(com));
        enumMap.put(Commands.REMOVE_BY_ID, com -> new RemoveByIdCommand().execute(com));
        enumMap.put(Commands.REMOVE_LOWER, com -> new RemoveLowerCommand().execute(com));
        enumMap.put(Commands.FILTER_CONTAINS_NAME, com -> new FilterContainsNameCommand().execute(com));
        enumMap.put(Commands.COUNT_BY_ORGANIZATION, com -> new CountByOrganizationCommand().execute(com));
        enumMap.put(Commands.INFO, com -> new InfoCommand().execute());
        enumMap.put(Commands.SHOW, com -> new ShowCommand().execute());
        enumMap.put(Commands.HEAD, com -> new HeadCommand().execute());
        enumMap.put(Commands.CLEAR, com -> new ClearCommand().execute());
        enumMap.put(Commands.HELP, com -> new HelpCommand().execute());
        enumMap.put(Commands.HELP_ENTERING_COMMAND, com -> new HelpEnteringCommand().execute());
        enumMap.put(Commands.PRINT_FIELD_DESCENDING_END_DATE,
                com -> new PrintFieldDescendingEndDataCommand().execute());
        enumMap.put(Commands.EXIT, com -> new ExitCommand().execute());
    }

    //todo
    public ServerResponse dispatcher(ClientCommand command) {
        try (Context ignored = Context.newId()) {
            String commandName = command.getNameCommand();
            logger.debug("Dispatching command: {}", commandName);

            Commands commands = Commands.nameCommands(commandName);
            if (commands == null) {
                logger.warn("Unknown command: {}", commandName);
                return ServerResponse.error("Unknown command: " + commandName);
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