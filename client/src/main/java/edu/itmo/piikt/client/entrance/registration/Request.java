package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.client.network.Network;
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
	 */
	void execute(Network network) throws Exception;
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

	/**
	 * Basic email format validation.
	 *
	 * @param email
	 *            email to validate
	 * @return true if email matches pattern, false otherwise
	 */
	default boolean isValidEmail(String email) {
		if (email == null || email.isBlank())
			return false;
		return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
	}

	/**
	 * Checks if string contains at least one special character: *, _, .
	 *
	 * @param str
	 *            string to check
	 * @return true if special character found, false otherwise
	 */
	default boolean hasSpecialCharacter(String str) {
		if (str == null)
			return false;
		return str.contains("*") || str.contains("_") || str.contains(".");
	}

	/**
	 * Checks if string length is at least minLength.
	 *
	 * @param str
	 *            string to check
	 * @param minLength
	 *            minimum required length
	 * @return true if length >= minLength, false otherwise
	 */
	default boolean isLongEnough(String str, int minLength) {
		if (str == null)
			return false;
		return str.length() >= minLength;
	}
}
