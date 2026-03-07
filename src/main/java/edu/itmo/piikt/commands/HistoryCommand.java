package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseSimpleCommand;
import edu.itmo.piikt.managers.HistoryCommands;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command history : output the last 14 commands
 * (without their arguments).
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class HistoryCommand implements BaseSimpleCommand {
    Logger logger = Logger.getLogger(HistoryCommand.class.getName());

    public HistoryCommand() {
    }

    @Override
    public void doExecute(IOProvider io) {
        var history = HistoryCommands.INSTANCE.getLinkedList();
        history.stream().limit(14).forEach(io::println);
    }

    @Override
    public void before() {
        logger.log(Level.INFO, "Displaying the last 14 commands");
    }

    @Override
    public void after() {
        logger.log(Level.INFO, "Commands displayed successfully");
    }

    @Override
    public void onError(RuntimeException e) {
        logger.log(Level.SEVERE, "Command not executed");
    }
}
