package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.client.entrance.Registr;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import java.security.SecureRandom;

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
	private String email;
	private int count = 3;
	private static final SecureRandom random = new SecureRandom();
	private String codeUser;
	private int MAX = 100;

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
		email = io.readLine();
		while (isValidEmail(email)) {
			io.println("Некорректный email, повторите ввод");
			email = io.readLine();
		}
		email(network);
		io.println("Введите логин(больше 8 символов)");
		login = io.readLine();
		while (isLongEnough(login, 8)) {
			io.println("Логин должен состоять не менее чем из 8 символов");
			login = io.readLine();
		}
		io.println("Введите пароль (должен быть не менее 8 символов и содержать минимум 1 спецсимвол: * _ .)");
		String password;
		email(network);
		while (true) {
			io.println(
					"Введите новый пароль (должен быть не менее 8 символов и содержать минимум 1 спецсимвол: * _ .)");
			password = io.readLine();
			while (isLongEnough(password, 8) || hasSpecialCharacter(password)) {
				if (isLongEnough(password, 8)) {
					io.println("Пароль должен быть не менее 8 символов");
				} else if (hasSpecialCharacter(password)) {
					io.println("Пароль должен содержать хотя бы один спецсимвол: * _ .");
				}
				password = io.readLine();
			}
			io.println("Введите пароль повторно");
			String passwordTwo = io.readLine();
			if (passwordTwo.equals(password)) {
				io.println("Пароль успешно подтвержден");
				break;
			}
			io.println("Пароли не совпадают, задайте пароли заново");
		}
		ClientCommand clientCommand = ClientCommand.builder().nameCommand("register").email(email).login(login)
				.password(password).build();
		ServerResponse serverResponse = network.send(clientCommand);
		serverResponse.printToConsole();
		if (!serverResponse.execution()) {
			Registr registr = new Registr(io);
			registr.registration(network);
		}
		io.println("Регистрация прошла успешно");
	}

	private void email(Network network) throws Exception {
		if (MAX > 0) {
			MAX = MAX - 1;
			codeUser = generateCode();
			io.println(
					"На Вашу электронную почту направлено письмо с кодом подтверждения, для повторного запроса введите команду"
							+ "(отправить код повторно), если Вы хотите отменить действие регистрации введите \"exit\")");
			ClientCommand clientCommand = ClientCommand.builder().nameCommand("register_email").email(email)
					.data(codeUser).build();
			if (!network.connected()) {
				network.connect();
			}
			ServerResponse serverResponse = network.send(clientCommand);
			serverResponse.printToConsole();
			while (count > 0) {
				count = count - 1;
				io.println("Введите шестизначный код");
				String code = io.readLine();
				if (DamerauLevenshteinDistance.distance(code, "отправить код повторно") <= 3) {
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
						"Попытки ввода исчерпаны, запросите повторный ввод (отправить код повторно) или вернитесь в меню входа(exit)");
				String input = io.readLine().toLowerCase();
				if (DamerauLevenshteinDistance.distance(input, "exit") <= 1) {
					Registr registr = new Registr(io);
					registr.registration(network);
					return;
				} else if (DamerauLevenshteinDistance.distance("отправить код повторно", input) <= 1) {
					email(network);
					return;
				}
			}
		} else {
			Registr registr = new Registr(io);
			registr.registration(network);
		}
	}

	private String generateCode() {
		codeUser = String.format("%06d", random.nextInt(1_000_000));
		return codeUser;
	}
}
