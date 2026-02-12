package edu.itmo.piikt.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {
    private Map<String, Commands> commands = new HashMap<>();

    public CommandFactory() {
        HistoryCommands history = HistoryCommands.getInstance();

        writeCommand(new AddCommand());
        writeCommand(new HelpCommand());
        writeCommand(new InfoCommand());
        writeCommand(new ShowCommand());
        writeCommand(new UpdateIdCommand());
        writeCommand(new RemoveByIdCommand());
        writeCommand(new ClearCommand());
        writeCommand(new SaveCommand());
        writeCommand(new ExecuteScriptCommand());
        writeCommand(new ExitCommand());
        writeCommand(new HeadCommand());
        writeCommand(new RemoveLowerCommander());
        writeCommand(new HistoryCommand());
        writeCommand(new CountByOrganizationCommand());
        writeCommand(new FilterContainsNameCommand());
        writeCommand(new PrintFieldDescendingEndDataCommand());
    }

    private void writeCommand(Commands command){
        commands.put(command.getName(), command);
    }

    public Commands getCommand(String name){
        return  commands.get(name);
    }
}
