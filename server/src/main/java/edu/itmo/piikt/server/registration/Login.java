package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command handler for user login.
 * Processes authentication requests, validates credentials against the database.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class Login implements CommandType {
	private static final AppLogger logger = new AppLogger(Login.class);
	private BD bd = new BD();

	/**
	 * Executes the login command.
	 * Authenticates the user using provided login and password.
	 *
	 * @param command client command containing login and password
	 * @return ServerResponse with authentication result (includes user ID on success)
	 */
	@Override
	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			String login = command.getLogin();
			String password = command.getPassword();

			logger.info("Processing login request for user: {}", login);

			if (!BDConnect.INSTANCE.isConnected()) {
				logger.warn("Database unavailable for login request from user: {}", login);
				return ServerResponse.error("Service temporarily unavailable, please try again later");
			}

			logger.debug("Authenticating user in database: {}", login);

			return bd.login(login, password);

		} catch (Exception e) {
			logger.error("Unexpected error during login for user {}: {}", command.getLogin(), e.getMessage(), e);
			return ServerResponse.error("Internal server error");
		}
	}
}
