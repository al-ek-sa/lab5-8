package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.sc.ClientCommand;

public class LoginRequest implements Request {

	private IOProvider io;
	private String login;
	public LoginRequest(IOProvider io) {
		this.io = io;
	}

	@Override
	public void getDescription() {
		io.println("Вход в аккаунт");
	}

	public String user() {
		return login;
	}

	@Override
	public ClientCommand execute() {
		io.println("Введите логин");
		login = io.readLine();
		io.println("введите пароль");
		String password = io.readLine();
		return ClientCommand.builder().nameCommand("login").login(login).password(password).build();
	}
}
