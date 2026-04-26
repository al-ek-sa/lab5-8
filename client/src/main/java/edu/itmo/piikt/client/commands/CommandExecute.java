package edu.itmo.piikt.client.commands;

import edu.itmo.piikt.client.io.provider.IOProvider;

@FunctionalInterface
public interface CommandExecute<T> extends Command {
	T execute(IOProvider ioProvider, Object... argument);
}
