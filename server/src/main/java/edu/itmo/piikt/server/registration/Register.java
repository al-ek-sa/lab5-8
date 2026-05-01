package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.Data;

/**
 * Command handler for user registration.
 * Processes new user registration requests and creates user accounts in the database.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
public class Register implements CommandType {
	private static final AppLogger logger = new AppLogger(Register.class);
	private BD bd = new BD();

	/**
	 * Executes the registration command.
	 * Creates a new user account with the provided login, email, and password.
	 *
	 * @param command client command containing login, email, and password
	 * @return ServerResponse indicating success or failure of registration
	 */
	@Override
	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			String login = command.getLogin();
			String email = command.getEmail();
			String password = command.getPassword();

			logger.info("Processing registration request for login: {}, email: {}", login, email);

			if (!BDConnect.INSTANCE.isConnected()) {
				logger.warn("Database unavailable for registration request - login: {}", login);
				return ServerResponse.error("Service temporarily unavailable, please try again later");
			}

			logger.debug("Executing user registration in database for login: {}", login);
			ServerResponse response = bd.newUser(email, login, password);

			if (response.execution()) {
				logger.info("Registration successful for login: {}", login);
			} else {
				logger.warn("Registration failed for login: {}", login);
			}

			return response;

		} catch (Exception e) {
			logger.error("Unexpected error during registration for login {}: {}", command.getLogin(), e.getMessage(),
					e);
			return ServerResponse.error("Internal server error");
		}
	}
}
