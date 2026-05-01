package edu.itmo.piikt.server.command.interfaces;

import edu.itmo.piikt.common.sc.ServerResponse;

/**
 * Functional interface for simple commands that take no arguments.
 * Represents a command that can be executed without any input parameters.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@FunctionalInterface
public interface CommandSimple {
	/**
	 * Executes the command and returns a server response.
	 * This method contains the core logic of the command.
	 *
	 * @return ServerResponse containing the result of command execution
	 */
	ServerResponse execute();
}
