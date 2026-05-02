package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.client.entrance.Registr;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

/**
 * Login request handler for the client side. Collects login and password from
 * user input and creates a login command.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
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
		io.println("Вход в аккаунт");
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
			io.println("Введите логин (не менее 8 символов)");
			login = io.readLine();
			while (!isLongEnough(login, 8)) {
				io.println("Логин должен быть не менее 8 символов");
				login = io.readLine();
			}

			io.println("Введите пароль (должен быть не менее 8 символов и содержать минимум 1 спецсимвол: * _ .)");
			String password = io.readLine();
			while (!isLongEnough(password, 8) || !hasSpecialCharacter(password)) {
				if (!isLongEnough(password, 8)) {
					io.println("Пароль должен быть не менее 8 символов");
				} else if (!hasSpecialCharacter(password)) {
					io.println("Пароль должен содержать хотя бы один спецсимвол: * _ .");
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
				io.println("Ошибка входа. Попробуйте снова (введите login), либо вернитесь в меню регистрации (exit).");
				String string = io.readLine().toLowerCase();
				if (DamerauLevenshteinDistance.distance(string, "exit") <= 1) {
					Registr registr = new Registr(io);
					registr.registration(network);
					return;
				}
				execute(network);
			}
		}
	}
}
