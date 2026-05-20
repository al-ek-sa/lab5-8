package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import edu.itmo.piikt.server.command.server.EmailSender;
import lombok.Data;

@Data
public class RegisterEmail implements CommandType {
	private static final AppLogger logger = new AppLogger(Register.class);

	@Override
	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			String email = command.getEmail();
			String code = command.getData().toString();
			return EmailSender.sendVerificationCode(email, code);
		} catch (Exception e) {
			return ServerResponse.error("Internal server error");
		}
	}
}
