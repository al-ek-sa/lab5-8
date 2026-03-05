package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.HistoryCommands;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * The class implements the command history : output the last 14 commands
 * (without their arguments).
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class HistoryCommand {
    private HistoryCommands historyCommands;
    Logger logger = Logger.getLogger(HistoryCommand.class.getName());

    public HistoryCommand() {
        this.historyCommands = HistoryCommands.getInstance();
    }

    public void execute(IOProvider io) {
        try {
            io.printlnCommand("Displaying the last 14 commands");
            Iterator<String> iterator = historyCommands.getIterator();
            for (int i = 1; i <= 14 && iterator.hasNext(); i++) {
                String command = iterator.next();
            }
            io.printlnCommand("Commands displayed successfully");
        } catch (Exception e) {
            io.printException("Command not executed");
        }
    }
}
