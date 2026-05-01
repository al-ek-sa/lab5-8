package edu.itmo.piikt.client.entrance;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.sc.ClientCommand;
import lombok.Data;

/**
 * Handles user registration and authentication flow.
 * Manages the initial authentication menu with fuzzy command matching.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
public class Registr {
	private int count = 3;
	private final IOProvider io;
	private final Command command;
	private String type;

	public Registr(IOProvider io) {
		this.io = io;
		this.command = new Command(io);
	}

	/**
	 * Returns the user login for the currently selected authentication method.
	 *
	 * @return user login string
	 */
	public String user() {
		return command.user(type);
	}

	/**
	 * Processes the authentication command selection.
	 * Reads user input and uses fuzzy matching to identify the intended command.
	 * Supports login, register, and reset_password commands with typo tolerance.
	 *
	 * @return ClientCommand for the selected authentication method, or null if command not recognized
	 */
	public ClientCommand registration() {
		type = io.readLine();
		if (DamerauLevenshteinDistance.distance(type.toLowerCase(), "login") <= 1) {
			return command.executeCommand("login");
		} else if (DamerauLevenshteinDistance.distance(type.toLowerCase(), "register") <= 1) {
			return command.executeCommand("register");

		} else if (DamerauLevenshteinDistance.distance(type.toLowerCase(), "reset_password") <= 1) {
			return command.executeCommand("reset_password");
		}

		return null;
	}
}
