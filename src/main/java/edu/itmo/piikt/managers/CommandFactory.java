package edu.itmo.piikt.managers;

import java.util.HashMap;
import java.util.Map;

import edu.itmo.piikt.commands.*;
import edu.itmo.piikt.io.IOProvider;

/**
 * A factory class for storing registered commands.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class CommandFactory {
    private Map<String, Commands> commands = new HashMap<>();
    private Map<String, ArgumentCommand> argumentCommands = new HashMap<>();
    private IOProvider io;
    public CommandFactory(IOProvider io) {
        HistoryCommands history = HistoryCommands.getInstance();
        this.io = io;

        writeCommand(new AddCommand(io));
        writeCommand(new HelpCommand(io));
        writeCommand(new InfoCommand(io));
        writeCommand(new ShowCommand(io));
        writeArgumentCommand(new UpdateIdCommand(io));
        writeArgumentCommand(new RemoveByIdCommand(io));
        writeCommand(new ClearCommand(io));
        writeCommand(new SaveCommand(io));
        writeArgumentCommand(new ExecuteScriptCommand(io));
        writeCommand(new ExitCommand(io));
        writeCommand(new HeadCommand(io));
        writeArgumentCommand(new RemoveLowerCommander(io));
        writeCommand(new HistoryCommand(io));
        writeCommand(new CountByOrganizationCommand(io));
        writeArgumentCommand(new FilterContainsNameCommand(io));
        writeCommand(new PrintFieldDescendingEndDataCommand(io));
        writeCommand(new HelpEnteringCommand(io));
    }

    /**
     *A method for registering commands without arguments.
     *
     * @param command The parameter accepts command objects that are subclasses of the Command class.
     */
    private void writeCommand(Commands command){
        commands.put(command.getName(), command);
    }

    /**
     * A method for registering commands with an argument.
     *
     * @param argumentCommand The parameter accepts command objects that are subclasses of the ArgumentCommand class.
     */

    private void writeArgumentCommand(ArgumentCommand argumentCommand){
        argumentCommands.put(argumentCommand.getName(), argumentCommand);
    }

    public ArgumentCommand getArgumentCommand(String name){
        return argumentCommands.get(name);
    }

    public Commands getCommand(String name){
        return  commands.get(name);
    }
}
