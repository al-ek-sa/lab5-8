package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.commands.CommandType;
import lombok.Data;

@Data
public class ResetPassword implements CommandType {
	private static final AppLogger logger = new AppLogger(ResetPassword.class);
	private BD bd = new BD();

	@Override
	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			return bd.newPassword(command.getEmail(), command.getLogin(), command.getPassword());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
