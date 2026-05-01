package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.server_client.ClientCommand;

public class RegisterRequest implements Request {
	private IOProvider io;
	private String login;

	public RegisterRequest(IOProvider io) {
		this.io = io;
	}

	public String user() {
		return login;
	}

	@Override
	public void getDescription() {
		io.println("Регистрация пользователя");
	}

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
