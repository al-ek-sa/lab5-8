package edu.itmo.piikt.client.entrance;

import edu.itmo.piikt.client.entrance.registration.LoginRequest;
import edu.itmo.piikt.client.entrance.registration.RegisterRequest;
import edu.itmo.piikt.client.entrance.registration.Request;
import edu.itmo.piikt.client.entrance.registration.ResetPasswordRequest;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Command router for client-side authentication. Manages login, registration,
 * and password reset requests.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
public class Command {
	private Map<String, Request> command = new HashMap<>();
	private final IOProvider io;
	public Command(IOProvider io) {
		this.io = io;
		command.put("login", new LoginRequest(io));
		command.put("register", new RegisterRequest(io));
		command.put("reset_password", new ResetPasswordRequest(io));
	}

	/**
	 * Executes an authentication command by its name. Displays the command
	 * description and returns the built client command.
	 *
	 * @param commandName
	 *            name of the command to execute (login, register, reset_password)
	 */
	public void executeCommand(String commandName, Network network) throws Exception {
		Request request = command.get(commandName);
		request.getDescription();
		request.execute(network);
	}

	/**
	 * Displays the authentication menu. Shows available options for entering the
	 * system.
	 */
	public void show() {
		io.println("""
				Выберите способ входа и введите соответствующую команду:\s
				> регистрация (register)\
				\s
				> вход в аккаунт (login)\s
				> восстановление пароля (reset_password)""");
	}

	/**
	 * Returns the login name associated with the specified command.
	 *
	 * @param commandName
	 *            name of the command to get the login from
	 * @return user login string
	 */
	public String user(String commandName) {
		return command.get(commandName).user();
	}
}
