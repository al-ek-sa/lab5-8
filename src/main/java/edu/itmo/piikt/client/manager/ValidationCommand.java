package edu.itmo.piikt.client.manager;

import edu.itmo.piikt.server.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.server.command.factory.CommandFactory;
import edu.itmo.piikt.common.command.functionalInterface.ArgumentCommand;
import edu.itmo.piikt.common.command.functionalInterface.SimpleCommand;
import edu.itmo.piikt.server.history.HistoryCommands;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.server.saveManager.HistorySave;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The main class of the program. The class determines which command was called.
 *
 * @author Lishyk Aliaksandra
 * @version 1.2
 */
@Getter
public enum ValidationCommand {
    INSTANCE;
    private CommandFactory factory;
    private boolean flag;

    ValidationCommand() {
        this.factory = new CommandFactory();
        this.flag = true;
    }
    // todo
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     * The method selects from the registered commands the command that the user
     * entered. When entering, the user can make a mistake once.
     *
     * <p>
     * Attention! The command handles only single-word commands without arguments
     * and single-word commands with one argument.
     */
    // todo
    public void validation(IOProvider io) {
        while (flag) {
            try {
                String nameCommands = io.readLine();
                HistorySave historySave = new HistorySave();
                historySave.saveCollection(io);

                if (nameCommands == null || nameCommands.isBlank() || "null".equalsIgnoreCase(nameCommands.trim())) {
                    continue;
                }

                String input = nameCommands.trim();
                HistoryCommands.INSTANCE.add(input);

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
                    argumentKey.forEach(com2 -> {
                        if (DamerauLevenshteinDistance.distance(parts[0], com2) <= 1) {
                            io.printException("The command (" + com2 + ") must contain arguments");
                        }
                    });

                    simpleKey.forEach(com1 -> {
                        if (DamerauLevenshteinDistance.distance(parts[0], com1) <= 1) {
                            parts[0] = com1;
                        }
                    });
                    SimpleCommand command = factory.getCommand(parts[0]);
                    if (command != null) {
                        command.execute(io);
                    } else if (input.equals("historyAll")) {
                        HistoryCommands.INSTANCE.printHistory();
                    } else {
                        io.printException("The command was entered incorrectly");
                    }
                } else if (parts.length == 2) {
                    String argument = parts[1];

                    simpleKey.forEach(com1 -> {
                        if (DamerauLevenshteinDistance.distance(parts[0], com1) <= 1) {
                            io.printException("The command (" + com1 + ") must not contain arguments");
                        }
                    });

                    argumentKey.forEach(com2 -> {
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
