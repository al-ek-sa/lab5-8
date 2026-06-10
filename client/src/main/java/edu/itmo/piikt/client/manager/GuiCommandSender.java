package edu.itmo.piikt.client.manager;

import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import lombok.Setter;

public enum GuiCommandSender {
	INSTANCE;

	private Network network;
	@Setter
	private String user;

	public void setNetwork(Network network) {
		this.network = network;
	}

	public ServerResponse sendCommand(String command, String argument, Object data) {
		try {
			if (network == null || !network.connected()) {
				network = new Network();
				network.connect();
			}

			ClientCommand clientCommand = ClientCommand.builder().nameCommand(command).user(user)
					.argumentCommand(argument).data(data).build();

			return network.send(clientCommand);
		} catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.error("Ошибка: " + e.getMessage());
		}
	}

	public ServerResponse sendCommand(ClientCommand command) {
		try {
			if (network == null || !network.connected()) {
				network = new Network();
				network.connect();
			}
			return network.send(command);
		} catch (Exception e) {
			e.printStackTrace();
			return ServerResponse.error("Ошибка: " + e.getMessage());
		}
	}
}
