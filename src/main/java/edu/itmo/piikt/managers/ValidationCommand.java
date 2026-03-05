package edu.itmo.piikt.managers;

import edu.itmo.piikt.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.reader.HistorySave;
import java.util.ArrayList;
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
    private boolean flag;
    private static ValidationCommand instance;

    private ValidationCommand() {
        this.historyCommands = HistoryCommands.getInstance();
        this.factory = new CommandFactory();
        this.flag = true;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public static ValidationCommand getInstance() {
        if (instance == null) {
            instance = new ValidationCommand();
        }
        return instance;
    }

    /**
     * The method selects from the registered commands the command that the user
     * entered. When entering, the user can make a mistake once.
     *
     * <p>
     * Attention! The command handles only single-word commands without arguments
     * and single-word commands with one argument.
     */
    public void validation(IOProvider io) {
        while (flag) {
            String nameCommands = io.readLine();
            HistorySave.getInstance().saveCollection();

            if (nameCommands == null) {
                break;
            }

            String input = nameCommands.trim();
            historyCommands.add(input);

            String[] parts1 = input.split("\\s+");

            List<String> parts2 = new ArrayList<>();
            for (String element : parts1) {
                if (!element.equals("null")) {
                    parts2.add(element);
                }
            }

            String[] parts = parts2.toArray(new String[0]);

            if (parts.length == 1) {

                for (String com2 : factory.getArgumentMap().keySet()) {
                    if (DamerauLevenshteinDistance.distance(parts[0], com2) <= 1) {
                        io.printeDesign();
                        io.printException("The command (" + com2 + ") must contain arguments");
                    }
                }

                for (String com1 : factory.getCommandsMap().keySet()) {
                    if (DamerauLevenshteinDistance.distance(parts[0], com1) <= 1) {
                        parts[0] = com1;
                    }
                }

                SimpleCommand command = factory.getCommand(parts[0]);
                if (command != null) {
                    command.execute(io);
                } else if (input.equals("historyAll")) {
                    historyCommands.printHistory();
                } else {
                    io.printeDesign();
                    io.printException("The command was entered incorrectly");
                    io.printeDesign();
                }
            } else if (parts.length == 2) {
                String commandName = parts[0];
                String argument = parts[1];

                for (String com1 : factory.getCommandsMap().keySet()) {
                    if (DamerauLevenshteinDistance.distance(commandName, com1) <= 1) {
                        io.printeDesign();
                        io.printException("The command (" + com1 + ") must not contain arguments");
                        io.printeDesign();
                    }
                }

                for (String com2 : factory.getArgumentMap().keySet()) {
                    if (DamerauLevenshteinDistance.distance(commandName, com2) <= 1) {
                        commandName = com2;
                    }
                }

                ArgumentCommand argumentCommand = factory.getArgumentCommand(commandName);
                if (argumentCommand != null) {
                    if (argument.trim().isEmpty()) {
                        io.printeDesign();
                        // Команда должна содержать аргументы
                        io.printException("The command must contain arguments");
                        io.printeDesign();
                    } else {
                        argumentCommand.execute(io, argument);
                    }
                }
            } else {
                io.printeDesign();
                // Команда введена неверно
                io.printException("The command was entered incorrectly");
                io.printeDesign();
            }
        }
    }
}
