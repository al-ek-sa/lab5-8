package edu.itmo.piikt.server.command.interfaces;

import edu.itmo.piikt.common.sc.ServerResponse;

@FunctionalInterface
public interface CommandSimple {
	ServerResponse execute();
}
