package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.command.server.EmailSender;
import lombok.Data;

@Data
public class SendResetCode implements CommandType {
	private static final AppLogger logger = new AppLogger(SendResetCode.class);

	@Override
	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			String login = command.login();
			String email = command.email();
			String code = command.data() != null ? command.data().toString() : null;
			logger.info("Sending reset code for login: {}, email: {}", login, email);
			if (code == null || code.isEmpty()) {
				return ServerResponse.error("Код не получен");
			}
			ServerResponse emailResponse = EmailSender.sendVerificationCode(email, code);
			if (emailResponse.execution()) {
				logger.info("Reset code sent successfully to: {}", email);
				return ServerResponse.successfulCompletion("Код подтверждения отправлен на вашу почту");
			} else {
				logger.error("Failed to send reset code to: {}", email);
				return ServerResponse.error("Не удалось отправить код на почту");
			}
		} catch (Exception e) {
			logger.error("Error sending reset code: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error");
		}
	}
}
