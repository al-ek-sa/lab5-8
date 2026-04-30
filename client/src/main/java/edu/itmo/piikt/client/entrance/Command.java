package edu.itmo.piikt.client.entrance;

import edu.itmo.piikt.client.entrance.registration.LoginRequest;
import edu.itmo.piikt.client.entrance.registration.RegisterRequest;
import edu.itmo.piikt.client.entrance.registration.Request;
import edu.itmo.piikt.client.entrance.registration.ResetPasswordRequest;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.server_client.ClientCommand;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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

	public ClientCommand executeCommand(String commandName) {
		Request request = command.get(commandName);
		request.getDescription();
		return request.execute();
	}

	public void show() {
		io.println("Выберите один из вариантов входа и введите команду");

	}
}
