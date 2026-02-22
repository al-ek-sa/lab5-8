package edu.itmo.piikt.managers;

import java.util.HashMap;
import java.util.Map;

import edu.itmo.piikt.commands.*;
import edu.itmo.piikt.io.IOProvider;

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

    private void writeCommand(Commands command){
        commands.put(command.getName(), command);
    }

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
