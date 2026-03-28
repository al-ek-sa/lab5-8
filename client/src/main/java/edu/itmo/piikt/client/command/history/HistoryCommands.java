package edu.itmo.piikt.client.command.history;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import java.util.LinkedList;

public enum HistoryCommands {
    INSTANCE;

    private static final AppLogger logger = new AppLogger(HistoryCommands.class);
    private final LinkedList<String> listCommands = new LinkedList<>();

    public void add(String command) {
        try (Context ignored = Context.newId()) {
            logger.debug("Adding command to history: {}", command);
            listCommands.addFirst(command);
            logger.debug("History size: {}", listCommands.size());
        } catch (Exception e) {
            logger.error("Error adding command to history: {}", e);
        }
    }

    public LinkedList<String> getLinkedList() {
        try (Context ignored = Context.newId()) {
            logger.debug("Retrieving history list, size: {}", listCommands.size());
            return listCommands;
        } catch (Exception e) {
            logger.error("Error retrieving history list: {}", e);
            return new LinkedList<>();
        }
    }
}