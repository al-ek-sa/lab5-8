package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.client.entrance.Registr;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import java.security.SecureRandom;

/**
 * Password reset request handler for the client side. Collects login and email
 * from user input and creates a password reset command.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class ResetPasswordRequest implements Request {
	private final IOProvider io;
	private String login;
	private String email;
	private int count = 3;
	private static final SecureRandom random = new SecureRandom();
	private String codeUser;
	private int MAX = 100;

	public ResetPasswordRequest(IOProvider io) {
		this.io = io;
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
	 * Displays the description of the password reset operation. Shows prompt
	 * message for password recovery.
	 */
	@Override
	public void getDescription() {
		io.println("Password Recovery");
	}

	/**
	 * Executes the password reset request. Prompts the user for login and email,
	 * then creates a command for the server.
	 */
	@Override
	public void execute(Network network) throws Exception {
		boolean flag = true;
		while (flag) {
			io.println("Enter email address");
			email = io.readLine();
			while (isValidEmail(email)) {
				io.println("Invalid email");
				email = io.readLine();
			}
			io.println("Enter login (at least 8 characters)");
			login = io.readLine();
			while (isLongEnough(login, 8)) {
				io.println("Login must be at least 8 characters");
				login = io.readLine();
			}
			ClientCommand clientCommand = ClientCommand.builder().nameCommand("reset_password").login(login)
					.email(email).build();
			ServerResponse serverResponse = network.send(clientCommand);
			serverResponse.printToConsole();
			if (serverResponse.execution()) {
				flag = false;
			} else {
				io.println("You can continue password recovery or exit to the registration menu (exit)");
				String string = io.readLine().toLowerCase();
				if (DamerauLevenshteinDistance.distance(string, "exit") <= 1) {
					Registr registr = new Registr(io);
					registr.registration(network);
				}
			}
		}
		String password;
		email(network);
		while (true) {
			io.println(
					"Enter new password (must be at least 8 characters and contain at least 1 special character: * _ .)");
			password = io.readLine();
			while (isLongEnough(password, 8) || hasSpecialCharacter(password)) {
				if (isLongEnough(password, 8)) {
					io.println("Password must be at least 8 characters");
				} else if (hasSpecialCharacter(password)) {
					io.println("Password must contain at least one special character: * _ .");
				}
				password = io.readLine();
			}
			io.println("Re-enter password");
			String passwordTwo = io.readLine();
			if (passwordTwo.equals(password)) {
				io.println("Password confirmed successfully");
				break;
			}
			io.println("Passwords do not match, please re-enter");
		}
		ClientCommand clientCommand = ClientCommand.builder().nameCommand("reset_password").login(login).email(email)
				.password(password).build();
		ServerResponse serverResponse = network.send(clientCommand);
		if (!serverResponse.execution()) {
			execute(network);
		}
		io.println("Password reset successfully");
	}

	/**
	 * Handles email verification process.
	 * Sends verification code to user's email and validates input.
	 *
	 * @param network network client for sending command
	 * @throws Exception if I/O error occurs
	 */
	private void email(Network network) throws Exception {
		if (MAX > 0) {
			MAX = MAX - 1;
			codeUser = generateCode();
			io.println(
					"A confirmation code has been sent to your email address. To request a new code, enter 'resend code'. To cancel, enter 'exit'.");
			ClientCommand clientCommand = ClientCommand.builder().nameCommand("register_email").email(email)
					.data(codeUser).build();
			if (!network.connected()) {
				network.connect();
			}
			ServerResponse serverResponse = network.send(clientCommand);
			serverResponse.printToConsole();
			while (count > 0) {
				count = count - 1;
				io.println("Enter the 6-digit code");
				String code = io.readLine();
				if (DamerauLevenshteinDistance.distance(code, "resend code") <= 2) {
					count = 3;
					email(network);
				}
				if (code.equals("exit")) {
					Registr registr = new Registr(io);
					registr.registration(network);
				} else if (code.equals(codeUser)) {
					return;
				}
			}
			count = 3;
			while (true) {
				io.println(
						"Attempts exhausted. Enter 'resend code' to request a new code, or 'exit' to return to the login menu.");
				String input = io.readLine().toLowerCase();
				if (DamerauLevenshteinDistance.distance(input, "exit") <= 1) {
					Registr registr = new Registr(io);
					registr.registration(network);
					return;
				} else if (DamerauLevenshteinDistance.distance("resend code", input) <= 1) {
					email(network);
					return;
				}
			}
		} else {
			Registr registr = new Registr(io);
			registr.registration(network);
		}
	}

	/**
	 * Generates a random 6-digit verification code.
	 *
	 * @return 6-digit code as string
	 */
	private String generateCode() {
		codeUser = String.format("%06d", random.nextInt(1_000_000));
		return codeUser;
	}
}
