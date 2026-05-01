package edu.itmo.piikt.client.entrance.registration;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.server_client.ClientCommand;

public record ResetPasswordRequest(IOProvider io) implements Request {

	@Override
	public void getDescription() {
		io.println("Восстановление пароля");
	}

	@Override
	public ClientCommand execute() {
		io.println("Введите логин");
		String login = io.readLine();
		io.println("Введите электронную почту");
		String email = io.readLine();
		return ClientCommand.builder().nameCommand("reset_password").login(login).email(email).build();
	}
}
