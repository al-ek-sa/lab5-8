package edu.itmo.piikt.server.command.interfaces;

import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

/**
 * Functional interface for commands that accept a client command as an
 * argument. Represents a command that processes a client request and returns a
 * server response.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@FunctionalInterface
public interface CommandType {
	/**
	 * Executes the command based on the client's request. Processes the provided
	 * client command and returns a corresponding server response.
	 *
	 * @param clientCommand
	 *            client request containing command name and data
	 * @return ServerResponse containing the result of command execution
	 */
	ServerResponse execute(ClientCommand clientCommand);
}
