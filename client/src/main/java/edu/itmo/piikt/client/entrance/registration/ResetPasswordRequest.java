package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;

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
		io.println("Восстановление пароля");
	}

	/**
	 * Executes the password reset request. Prompts the user for login and email,
	 * then creates a command for the server.
	 *
	 * @return ClientCommand configured for password reset operation
	 */
	@Override
	public ClientCommand execute() {
		io.println("Введите электронную почту");
		String email = io.readLine();
		while (!isValidEmail(email)) {
			io.println("Некорректный email. Пример: user@example.com");
			email = io.readLine();
		}
		io.println("Введите логин (не менее 8 символов)");
		login = io.readLine();
		while (!isLongEnough(login, 8)) {
			io.println("Логин должен быть не менее 8 символов");
			login = io.readLine();
		}
		return ClientCommand.builder().nameCommand("reset_password").login(login).email(email).build();
	}
}
