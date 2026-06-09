package edu.itmo.piikt.server.commands;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;

@FunctionalInterface
public interface CommandType {
	ServerResponse execute(ClientCommand clientCommand);
}
