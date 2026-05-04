package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.client.entrance.Registr;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

/**
 * Client-side login request handler.
 * Prompts user for login and password, sends command to server,
 * and retries indefinitely on failure.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class LoginRequest implements Request {

	private final IOProvider io;
	private String login;
	public LoginRequest(IOProvider io) {
		this.io = io;
	}

	/**
	 * Displays the description of the login operation. Shows prompt message for
	 * account login.
	 */
	@Override
	public void getDescription() {
		io.println("Account login");
	}

	/**
	 * Returns the stored login value.
	 *
	 * @return user login string
	 */
	public String user() {
		return login;
	}

	/**
	 * Executes login: prompts for login and password, validates them, sends command
	 * to server and repeats input on failure.
	 *
	 * @param network
	 *            network client for sending command
	 * @throws Exception
	 *             if I/O error occurs
	 */
	@Override
	public void execute(Network network) throws Exception {
		boolean success = false;

		while (!success) {
			io.println("Enter login (at least 8 characters)");
			login = io.readLine();
			while (isLongEnough(login, 8)) {
				io.println("Login must be at least 8 characters");
				login = io.readLine();
			}

			io.println("Enter password (at least 8 characters and must contain at least 1 special character: * _ .)");
			String password = io.readLine();
			while (isLongEnough(password, 8) || hasSpecialCharacter(password)) {
				if (isLongEnough(password, 8)) {
					io.println("Password must be at least 8 characters");
				} else if (hasSpecialCharacter(password)) {
					io.println("Password must contain at least one special character: * _ .");
				}
				password = io.readLine();
			}

			ClientCommand clientCommand = ClientCommand.builder().nameCommand("login").login(login).password(password)
					.build();

			ServerResponse serverResponse = network.send(clientCommand);
			serverResponse.printToConsole();

			if (serverResponse.execution()) {
				success = true;
			} else {
				io.println("Login failed. Please try again (type 'login'), or return to the registration menu (type 'exit').");
				String string = io.readLine().toLowerCase();
				if (DamerauLevenshteinDistance.distance(string, "exit") <= 1) {
					Registr registr = new Registr(io);
					registr.registration(network);
					return;
				}
				execute(network);
			}
		}
		io.println("Login successful");
	}
}
