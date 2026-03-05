package edu.itmo.piikt.managers;

import java.util.*;

import edu.itmo.piikt.commands.*;
import edu.itmo.piikt.io.IOProvider;

/**
 * A factory class for storing registered commands.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */

public class CommandFactory {
    private Map<String, SimpleCommand> commands = new HashMap<>();
    private Map<String, ArgumentCommand> argumentCommands = new HashMap<>();
    public CommandFactory() {
        simpleCommand(Commands.ADD, new AddCommand()::execute);
        simpleCommand(Commands.CLEAR, new ClearCommand()::execute);
        simpleCommand(Commands.EXIT, new ExitCommand()::execute);
        simpleCommand(Commands.HEAD, new HeadCommand()::execute);
        simpleCommand(Commands.COUNT_BY_ORGANIZATION, new CountByOrganizationCommand()::execute);
        simpleCommand(Commands.HELP_ENTERING_COMMAND, new HelpEnteringCommand()::execute);
        simpleCommand(Commands.HELP, new HelpCommand()::execute);
        simpleCommand(Commands.HISTORY, new HistoryCommand()::execute);
        simpleCommand(Commands.SAVE, new SaveCommand()::execute);
        simpleCommand(Commands.INFO, new InfoCommand()::execute);
        simpleCommand(Commands.SHOW, new ShowCommand()::execute);
        argumentCommand(Commands.FILTER_CONTAINS_NAME, (i, arg) -> new FilterContainsNameCommand().execute(i, arg));
        argumentCommand(Commands.REMOVE_BY_ID, (i, arg) -> new RemoveByIdCommand().execute(i,arg));
        argumentCommand(Commands.UPDATE, (i, arg) -> new UpdateIdCommand().execute(i, arg));
        argumentCommand(Commands.REMOVE_LOWER, (i, arg) -> new RemoveLowerCommander().execute(i, arg));
        argumentCommand(Commands.EXECUTE_SCRIPT, (i, arg) -> new ExecuteScriptCommand().execute(i, arg));
        simpleCommand(Commands.PRINT_FIELD_DESCENDING_END_DATE, new PrintFieldDescendingEndDataCommand()::execute);
    }

    private void simpleCommand (Commands com, SimpleCommand command) {
        commands.put(com.getName(), command);
    }

    private void argumentCommand (Commands com, ArgumentCommand argumentCommand) {
        argumentCommands.put(com.getName(),argumentCommand);
    }

    public ArgumentCommand getArgumentCommand(String name){
        return argumentCommands.get(name);
    }

    public SimpleCommand getCommand(String name){
        return  commands.get(name);
    }

    public Map<String, ArgumentCommand> getArgumentMap(){
        return argumentCommands;
    }

    public Map<String, SimpleCommand> getCommandsMap(){
        return commands;
    }
}
