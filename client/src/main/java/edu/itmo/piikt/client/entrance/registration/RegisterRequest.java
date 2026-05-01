package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.server_client.ClientCommand;

public record RegisterRequest(IOProvider io) implements Request {

	@Override
	public void getDescription() {
		io.println("Регистрация пользователя");
	}

	@Override
	public ClientCommand execute() {
		io.println("Введите электронную почту");
		String email = io.readLine();
		io.println("Введите логин");
		String login = io.readLine();
		io.println("Введите пароль");
		String password = io.readLine();
		return ClientCommand.builder().nameCommand("register").email(email).login(login).password(password).build();
	}
}
