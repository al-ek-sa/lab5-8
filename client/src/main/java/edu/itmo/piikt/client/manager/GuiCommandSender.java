package edu.itmo.piikt.client.manager;

import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import lombok.Setter;

public enum GuiCommandSender {
	INSTANCE;

	private Network network;
	@Setter
	private String user;

	public ServerResponse sendCommand(ClientCommand command) {
		if (network == null) {
			network = new Network();
			network.connect();
		}

		try {
			ServerResponse response = network.send(command);
			return response;
		} catch (Exception e) {
			return ServerResponse.error("Сервер недоступен");
		}
	}
}
