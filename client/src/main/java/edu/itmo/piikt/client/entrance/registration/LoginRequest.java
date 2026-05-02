package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;

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
	 * Executes the login request. Prompts the user for login and password, then
	 * creates a command for the server.
	 *
	 * @return ClientCommand configured for login operation
	 */
	@Override
	public ClientCommand execute() {
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
		return ClientCommand.builder().nameCommand("login").login(login).password(password).build();
	}
}
