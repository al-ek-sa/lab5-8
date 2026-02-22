package edu.itmo.piikt.managers;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.reader.HistorySave;

public class ValidationCommand {
    private HistoryCommands historyCommands;
    private CommandFactory factory;
    private IOProvider io;

    public ValidationCommand(IOProvider io) {
        this.historyCommands = HistoryCommands.getInstance();
        this.factory = new CommandFactory(io);
        this.io = io;
    }

    public void validation() {
        while (true) {
            String nameCommands = io.readLine();
            HistorySave.getInstance().saveCollection();

            if (nameCommands == null) {
                break;
            }

            String input = nameCommands.trim();
            historyCommands.add(input);


            String[] parts = input.split("\\s+");

            if (parts.length == 1) {
                Commands command = factory.getCommand(input);
                if (command != null) {
                    command.execute();
                } else if (input.equals("historyAll")) {
                    historyCommands.printHistory();
                } else {
                    io.printeDesign();
                    //Команда введена неверно
                    io.printException("The command was entered incorrectly");
                    io.printeDesign();
                }
            } else if (parts.length == 2) {
                String commandName = parts[0];
                String argument = parts[1];

                ArgumentCommand argumentCommand = factory.getArgumentCommand(commandName);
                if (argumentCommand != null) {
                    if (argument.trim().isEmpty()) {
                        io.printeDesign();
                        //Команда должна содержать аргументы
                        io.printException("The command must contain arguments");
                        io.printeDesign();
                    } else {
                        argumentCommand.execute(argument);
                    }
                }
            } else if (parts.length == 3) {
                String nameCommand = (parts[0] + " " + parts[1]);
                String  argument = parts[2];

                ArgumentCommand argumentCommand = factory.getArgumentCommand(nameCommand);
                if (argument != null){
                    if (argument.trim().isEmpty()) {
                        io.printeDesign();
                        //Команда должна содержать аргументы
                        io.printException("The command must contain arguments");
                        io.printeDesign();
                    } else {
                        argumentCommand.execute(argument);
                    }
                }
            }else {
                io.printeDesign();
                //Команда введена неверно
                io.printException("The command was entered incorrectly");
                io.printeDesign();
            }
        }
    }
}
