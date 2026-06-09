package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.command.interfaces.CommandType;
import lombok.Data;

@Data
public class CheckUser implements CommandType {
	private static final AppLogger logger = new AppLogger(CheckUser.class);
	private BD bd = new BD();

	@Override
	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			String login = command.login();
			String email = command.email();
			logger.info("Checking user existence: login={}, email={}", login, email);
			if (login == null || login.trim().isEmpty()) {
				return ServerResponse.error("Логин не может быть пустым");
			}
			if (email == null || email.trim().isEmpty()) {
				return ServerResponse.error("Email не может быть пустым");
			}
			return bd.selectUser(login, email);
		} catch (Exception e) {
			logger.error("Error checking user: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error");
		}
	}
}
