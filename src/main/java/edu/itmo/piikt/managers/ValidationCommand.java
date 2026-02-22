package edu.itmo.piikt.managers;

import edu.itmo.piikt.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.reader.HistorySave;

import java.util.Arrays;
import java.util.List;

public class ValidationCommand {
    private HistoryCommands historyCommands;
    private CommandFactory factory;
    private IOProvider io;
    private static final List<String> oneWord = Arrays.asList("save", "help", "show", "info", "add",
            "clear", "count_by_organization", "exit", "head", "history", "print_field_descending_end_date");

    private static final List<String> twoWords = Arrays.asList("remove_by_id", "execute_script",
            "filter_contains_name", "remove_lower");

    private static final List<String> threeWords = Arrays.asList("update id");

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
                for (String com1 : oneWord){
                    if (DamerauLevenshteinDistance.distance(input, com1) <= 1){
                        input = com1;
                    }
                }

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

                for(String com2 : twoWords) {
                    if (DamerauLevenshteinDistance.distance(commandName, com2) <=1){
                        commandName =com2;
                    }
                }

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

                for (String com3 : threeWords){
                    if (DamerauLevenshteinDistance.distance(nameCommand, com3) <= 1){
                        nameCommand = com3;
                    }
                }
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
