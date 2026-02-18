package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;

public class ValidationCommand {
    private HistoryCommands historyCommands;
    private CommandFactory factory;
    private IOProvider io;

    public ValidationCommand(IOProvider io) {
        this.historyCommands = HistoryCommands.getInstance();
        this.factory = new CommandFactory(io);
        this.io = io;
    }

    public void validation(){
        while (true){
            String nameCommands = io.readLine();
            String input = nameCommands.trim();
            historyCommands.add(input);

            String[] parts = input.split("\\s+", 2);
            String commandName = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";

            ArgumentCommand argumentCommand = factory.getArgumentCommand(commandName);
            if(argumentCommand != null){
                if(argument == null || argument.trim().isEmpty()){
                    io.printException("Команда должна содержать аргументы");
                } else {
                    argumentCommand.execute(argument);
                }
                continue;
            }

            Commands command = factory.getCommand(input);

            if (command != null){
                command.execute();
            } else if (input.equals("historyAll")) {
                historyCommands.printHistory();
            } else {
                io.printException("Команда введена неверно");
            }
        }
    }


}
