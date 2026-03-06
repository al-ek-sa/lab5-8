package edu.itmo.piikt.managers;

import edu.itmo.piikt.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.reader.HistorySave;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
    public void validation(IOProvider io, Logger logger) {
        while (flag) {
            try {
                String nameCommands = io.readLine();
                HistorySave.getInstance().saveCollection(io);

                if (nameCommands == null || nameCommands.isBlank() || "null".equalsIgnoreCase(nameCommands.trim())) {
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

                var argumentKey = factory.getArgumentMap().keySet();
                var simpleKey = factory.getCommandsMap().keySet();


                String[] parts = parts2.toArray(new String[0]);

                if (parts.length == 1) {
                    argumentKey.forEach(com2 ->{
                        if (DamerauLevenshteinDistance.distance(parts[0], com2) <= 1) {
                            io.printException("The command (" + com2 + ") must contain arguments");
                        }
                    });

                    simpleKey.forEach(com1 ->{
                        if (DamerauLevenshteinDistance.distance(parts[0], com1) <= 1) {
                            parts[0] = com1;
                        }
                    });
                    SimpleCommand command = factory.getCommand(parts[0]);
                    if (command != null) {
                    command.execute(io);}
                    else
                    if (input.equals("historyAll")) {
                        historyCommands.printHistory();
                    } else {
                        io.printException("The command was entered incorrectly");
                    }
                } else if (parts.length == 2) {
                    String argument = parts[1];

                    argumentKey.forEach(com1 -> {
                        if (DamerauLevenshteinDistance.distance(parts[0], com1) <= 1) {
                            io.printException("The command (" + com1 + ") must not contain arguments");
                        }
                    });

                    simpleKey.forEach(com2 -> {
                        if (DamerauLevenshteinDistance.distance(parts[0], com2) <= 1) {
                            parts[0] = com2;
                        }
                    });
                    ArgumentCommand argumentCommand = factory.getArgumentCommand(parts[0]);
                    if (argumentCommand != null) {
                        if (argument.trim().isEmpty()) {
                            io.printException("The command must contain arguments");
                        } else {
                            argumentCommand.execute(io, argument);
                        }
                    }
                } else {
                    io.printException("The command was entered incorrectly");
                }
            } catch (RuntimeException e) {
                io.printException("Unexpected error: " + e.getMessage());
            }
        }
    }
}
