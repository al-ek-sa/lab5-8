package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Command {
	private static final AppLogger logger = new AppLogger(Command.class);
	private Map<String, Function<ClientCommand, ServerResponse>> map = new HashMap<>();

	public Command() {
		map.put("login", com -> new Login().execute(com));
		map.put("register", com -> new Register().execute(com));
		map.put("reset_password", com -> new ResetPassword().execute(com));
	}

	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			Function<ClientCommand, ServerResponse> input = map.get(command.getNameCommand());
			if (input == null) {
				return ServerResponse.error("Command not implemented");
			}
			return input.apply(command);
		} catch (Exception e) {
			// todo
			return null;
		}
	}

}
