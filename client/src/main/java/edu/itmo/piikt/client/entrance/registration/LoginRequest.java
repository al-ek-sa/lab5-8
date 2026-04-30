package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.server_client.ClientCommand;

public record LoginRequest(IOProvider io) implements Request {


	@Override
	public void getDescription() {
		io.println("Вход в аккаунт");
	}

	@Override
	public ClientCommand execute() {
		String login = io.readLine();
		String password = io.readLine();
		return ClientCommand.builder().nameCommand("login").login(login)
				.password(password).build();
	}
}
