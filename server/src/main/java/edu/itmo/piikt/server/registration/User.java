package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class User {
	private static final AppLogger logger = new AppLogger(User.class);
	private final Map<String, Function<ClientCommand, ServerResponse>> handlers = new HashMap<>();

	public User() {
		handlers.put("login", com -> new Login().execute(com));
		handlers.put("register", com -> new Register().execute(com));
		handlers.put("reset_password", com -> new ResetPassword().execute(com));
		logger.debug("Auth command handlers initialized: {}", handlers.keySet());
	}

	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			String commandName = command.getNameCommand();
			logger.info("Processing auth command: {}", commandName);

			Function<ClientCommand, ServerResponse> handler = handlers.get(commandName);

			if (handler == null) {
				logger.warn("Unknown auth command: {} - available commands: {}", commandName, handlers.keySet());
				return ServerResponse.error("Unknown command: " + commandName);
			}

			logger.debug("Executing auth command: {}", commandName);
			ServerResponse response = handler.apply(command);

			if (response == null) {
				logger.error("Handler returned null for command: {}", commandName);
				return ServerResponse.error("Internal server error - handler returned null");
			}

			if (response.execution()) {
				logger.info("Auth command completed successfully: {}", commandName);
			} else {
				logger.warn("Auth command completed with error: {}", commandName);
			}

			return response;

		} catch (Exception e) {
			logger.error("Unexpected error processing auth command {}: {}", command.getNameCommand(), e.getMessage(),
					e);
			return ServerResponse.error("Internal server error");
		}
	}
}
