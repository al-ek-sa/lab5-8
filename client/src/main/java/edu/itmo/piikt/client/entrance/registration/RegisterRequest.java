package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;

/**
 * Registration request handler for the client side. Collects email, login, and
 * password from user input and creates a registration command.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class RegisterRequest implements Request {
	private final IOProvider io;
	private String login;

	public RegisterRequest(IOProvider io) {
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
	 * Displays the description of the registration operation. Shows prompt message
	 * for user registration.
	 */
	@Override
	public void getDescription() {
		io.println("Регистрация пользователя");
	}

	/**
	 * Executes the registration request. Prompts the user for email, login, and
	 * password, then creates a command for the server.
	 *
	 * @return ClientCommand configured for registration operation
	 */
	@Override
	public ClientCommand execute() {
		io.println("Введите электронную почту");
		String email = io.readLine();
		io.println("Введите логин");
		login = io.readLine();
		io.println("Введите пароль");
		String password = io.readLine();
		return ClientCommand.builder().nameCommand("register").email(email).login(login).password(password).build();
	}
}
