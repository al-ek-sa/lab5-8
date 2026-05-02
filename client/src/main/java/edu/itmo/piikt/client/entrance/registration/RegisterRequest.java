package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

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
	 */
	@Override
	public void execute(Network network) throws Exception {
		io.println("Введите электронную почту");
		String email = io.readLine();
		while (isValidEmail(email)) {
			io.println("Некорректный email, повторите ввод");
			email = io.readLine();
		}
		io.println("Введите логин(больше 8 символов)");
		login = io.readLine();
		while (isLongEnough(login, 8)) {
			io.println("Логин должен состоять не менее чем из 8 символов");
			login = io.readLine();
		}
		io.println("Введите пароль (должен быть не менее 8 символов и содержать минимум 1 спецсимвол: * _ .)");
		String password = io.readLine();
		while (isLongEnough(password, 8) || hasSpecialCharacter(password)) {
			if (isLongEnough(password, 8)) {
				io.println("Пароль должен быть не менее 8 символов");
			} else if (hasSpecialCharacter(password)) {
				io.println("Пароль должен содержать хотя бы один спецсимвол: * _ .");
			}
			password = io.readLine();
		}
		ClientCommand clientCommand = ClientCommand.builder().nameCommand("register").email(email).login(login)
				.password(password).build();
		ServerResponse serverResponse = network.send(clientCommand);
		serverResponse.printToConsole();
	}
}
