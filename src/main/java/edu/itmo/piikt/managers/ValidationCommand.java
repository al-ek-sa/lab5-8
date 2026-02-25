package edu.itmo.piikt.managers;

import edu.itmo.piikt.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.reader.HistorySave;
import edu.itmo.piikt.reader.InputReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The main class of the program. The class determines which command was called.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ValidationCommand {
    private HistoryCommands historyCommands;
    private CommandFactory factory;
    private IOProvider io;

    private static final List<String> oneWord = Arrays.asList("save", "help", "show", "info", "add",
            "clear", "count_by_organization", "exit", "head", "history", "print_field_descending_end_date", "help_entering_command");

    private static final List<String> twoWords = Arrays.asList("remove_by_id", "execute_script",
            "filter_contains_name", "remove_lower", "update");

    public ValidationCommand(IOProvider io) {
        this.historyCommands = HistoryCommands.getInstance();
        this.factory = new CommandFactory(io);
        this.io = io;
    }
    /**
     * The method selects from the registered commands the command that the user entered. When entering,
     * the user can make a mistake once.
     *
     * Attention! The command handles only single-word commands without arguments and
     * single-word commands with one argument.
     */

    public void validation() {
        while (true) {
            String nameCommands = io.readLine();
            HistorySave.getInstance().saveCollection();

            if (nameCommands == null) {
                break;
            }

            String input = nameCommands.trim();
            historyCommands.add(input);


            String[] parts1 = input.split("\\s+");

            List<String> parts2 = new ArrayList<>();
            for (String element: parts1){
                if (!element.equals("null")){
                    parts2.add(element);
                }
            }

            String[] parts = parts2.toArray(new String[0]);


            if (parts.length == 1) {

                for(String com2 : twoWords) {
                    if (DamerauLevenshteinDistance.distance(parts[0], com2) <=1){
                        io.printeDesign();
                        io.printException("The command (" + com2 + ") must contain arguments");
                    }
                }


                for (String com1 : oneWord){
                    if (DamerauLevenshteinDistance.distance(parts[0], com1) <= 1){
                        parts[0] = com1;
                    }
                }

                Commands command = factory.getCommand(parts[0]);
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

                for(String com1 : oneWord) {
                    if (DamerauLevenshteinDistance.distance(commandName, com1) <=1){
                        io.printeDesign();
                        io.printException("The command (" + com1 + ") must not contain arguments");
                        io.printeDesign();
                    }
                }

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
            }else {
                io.printeDesign();
                //Команда введена неверно
                io.printException("The command was entered incorrectly");
                io.printeDesign();
            }
        }
    }
}
