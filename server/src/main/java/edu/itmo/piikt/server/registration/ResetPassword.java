package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.Data;

/**
 * Command handler for password reset. Processes password reset requests and
 * updates user passwords in the database.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
public class ResetPassword implements CommandType {
	private static final AppLogger logger = new AppLogger(ResetPassword.class);
	private BD bd = new BD();

	/**
	 * Executes the password reset command. Updates the user's password after
	 * verifying login and email.
	 *
	 * @param command
	 *            client command containing login, email, and new password
	 * @return ServerResponse indicating success or failure of the password reset
	 */
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
			if (command.getPassword() == null || newPassword.isEmpty()) {
				return bd.selectUser(command.getLogin(), command.getEmail());
			}
			return bd.newPassword(email, login, newPassword);
		} catch (Exception e) {
			logger.error("Unexpected error during password reset for login {}: {}", command.getLogin(), e.getMessage(),
					e);
			return ServerResponse.error("Internal server error");
		}
	}
}
