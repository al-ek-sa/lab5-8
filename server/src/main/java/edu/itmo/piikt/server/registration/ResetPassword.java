package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.commands.CommandType;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.Data;

@Data
public class ResetPassword implements CommandType {
	private static final AppLogger logger = new AppLogger(ResetPassword.class);
	private BD bd = new BD();

	@Override
	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			String login = command.getLogin();
			String email = command.getEmail();
			String newPassword = command.getPassword();

			logger.info("Processing reset password request for login: {}, email: {}", login, email);

			if (!BDConnect.INSTANCE.isConnected()) {
				logger.warn("Database unavailable for reset password request - login: {}", login);
				return ServerResponse.error("Service temporarily unavailable, please try again later");
			}

			logger.debug("Executing password reset in database for login: {}", login);
			ServerResponse response = bd.newPassword(email, login, newPassword);

			if (response.execution()) {
				logger.info("Password reset successful for login: {}", login);
			} else {
				logger.warn("Password reset failed for login: {}", login);
			}

			return response;

		} catch (Exception e) {
			logger.error("Unexpected error during password reset for login {}: {}", command.getLogin(), e.getMessage(),
					e);
			return ServerResponse.error("Internal server error");
		}
	}
}
