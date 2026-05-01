package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.commands.CommandType;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.Data;

@Data
public class Register implements CommandType {
	private static final AppLogger logger = new AppLogger(Register.class);
	private BD bd = new BD();

	@Override
	public ServerResponse execute(ClientCommand command) {
		try (Context ignored = Context.newId()) {
			if (!BDConnect.INSTANCE.isConnected()) {
				return ServerResponse.error("сервер временно не доступен");
			}
			return bd.newUser(command.getEmail(), command.getLogin(), command.getPassword());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
