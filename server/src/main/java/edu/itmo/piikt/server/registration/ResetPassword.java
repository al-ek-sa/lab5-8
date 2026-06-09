package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import lombok.Data;

@Data
public class ResetPassword implements CommandType {
	private static final AppLogger logger = new AppLogger(ResetPassword.class);
	private BD bd = new BD();

	@Override
	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			String login = command.login();
			String email = command.email();
			String newPassword = command.password();
			logger.info("Processing reset password request for login: {}, email: {}", login, email);
			if (login == null || login.trim().isEmpty()) {
				return ServerResponse.error("Логин не может быть пустым");
			}
			if (email == null || email.trim().isEmpty()) {
				return ServerResponse.error("Email не может быть пустым");
			}
			if (newPassword == null || newPassword.length() < 8) {
				return ServerResponse.error("Пароль должен быть не менее 8 символов");
			}
			ServerResponse response = bd.newPassword(email, login, newPassword);
			if (response.execution()) {
				logger.info("Password reset successful for login: {}", login);
			} else {
				logger.warn("Password reset failed for login: {}", login);
			}
			return response;
		} catch (Exception e) {
			logger.error("Unexpected error during password reset: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error");
		}
	}
}
