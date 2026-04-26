package edu.itmo.piikt.client.commands;

import edu.itmo.piikt.client.io.provider.IOProvider;
@FunctionalInterface
public interface CommandVoid extends Command {
	void execute(IOProvider ioProvider, Object... argument);
}
