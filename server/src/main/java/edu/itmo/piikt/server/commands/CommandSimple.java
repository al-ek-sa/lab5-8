package edu.itmo.piikt.server.commands;

import edu.itmo.piikt.common.server_client.ServerResponse;

@FunctionalInterface
public interface CommandSimple {
	ServerResponse execute();
}
