package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.client.entrance.Registr;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import java.security.SecureRandom;
import java.sql.DriverManager;

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
		io.println("Восстановление пароля");
	}

	/**
	 * Executes the password reset request. Prompts the user for login and email,
	 * then creates a command for the server.
	 */
	@Override
	public void execute(Network network) throws Exception {
		boolean flag = true;
		while (flag) {
			io.println("Введите электронную почту");
			String email = io.readLine();
			while (!isValidEmail(email)) {
				io.println("Некорректный email");
				email = io.readLine();
			}
			io.println("Введите логин (не менее 8 символов)");
			login = io.readLine();
			while (!isLongEnough(login, 8)) {
				io.println("Логин должен быть не менее 8 символов");
				login = io.readLine();
			}
			ClientCommand clientCommand = ClientCommand.builder().nameCommand("reset_password").login(login)
					.email(email).build();
			ServerResponse serverResponse = network.send(clientCommand);
			serverResponse.printToConsole();
			if (serverResponse.execution()) {
				flag = false;
			} else {
				io.println("вы можете продолжить восстановление пароля, либо выйти в меню регистрации (exit)");
				String string = io.readLine().toLowerCase();
				if (DamerauLevenshteinDistance.distance(string, "exit") <= 1) {
					Registr registr = new Registr(io);
					registr.registration(network);
				}
			}
		}
		email(network);
		String password = io.readLine();
		while (!isLongEnough(password, 8) || !hasSpecialCharacter(password)) {
			if (!isLongEnough(password, 8)) {
				io.println("Пароль должен быть не менее 8 символов");
			} else if (!hasSpecialCharacter(password)) {
				io.println("Пароль должен содержать хотя бы один спецсимвол: * _ .");
			}
			password = io.readLine();
		}
		ClientCommand clientCommand = ClientCommand.builder().nameCommand("reset_password").login(login).email(email)
				.password(password).build();
		ServerResponse serverResponse = network.send(clientCommand);
		if (serverResponse.execution()) {
			return;
		} else {
			execute(network);
		}
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
