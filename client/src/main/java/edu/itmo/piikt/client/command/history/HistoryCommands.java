package edu.itmo.piikt.client.command.history;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import java.util.LinkedList;

/**
 * Stores the last commands entered by the user in a LinkedList.
 * New commands are added to the beginning
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public enum HistoryCommands {
/** Singleton instance*/
	INSTANCE;

	private static final AppLogger logger = new AppLogger(HistoryCommands.class);
	/** Storage for command history. New commands are added to the beginning*/
	private final LinkedList<String> listCommands = new LinkedList<>();

	/**
	 * Adds a command to the history.
	 * @param command command string to add
	 */
	public void add(String command) {
		try (Context ignored = Context.newId()) {
			logger.debug("Adding command to history: {}", command);
			listCommands.addFirst(command);
			logger.debug("History size: {}", listCommands.size());
		} catch (Exception e) {
			logger.error("Error adding command to history: {}", e);
		}
	}

	/**
	 * Returns the full command history list
	 * @return LinkedList with command history
	 */
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
