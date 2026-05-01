package edu.itmo.piikt.server.command.interfaces;

import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;

@FunctionalInterface
public interface CommandType {
	ServerResponse execute(ClientCommand clientCommand);
}
