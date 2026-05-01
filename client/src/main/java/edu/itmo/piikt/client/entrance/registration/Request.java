package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.sc.ClientCommand;

/**
 * Interface for client-side authentication requests. Defines the contract for
 * login, registration, and password reset operations.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public interface Request {
	/**
	 * Executes the request and creates a client command for the server. Collects
	 * necessary data from user input and builds a command object.
	 *
	 * @return ClientCommand ready to be sent to the server
	 */
	ClientCommand execute();
	/**
	 * Displays the description of the request operation. Shows a prompt message to
	 * guide the user through the authentication process.
	 */
	void getDescription();
	/**
	 * Returns the stored login value for the current request. Used to identify the
	 * user making the request.
	 *
	 * @return user login string
	 */
	String user();
}
