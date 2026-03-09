package edu.itmo.piikt.command.functionalInterface;

import edu.itmo.piikt.io.provider.IOProvider;

@FunctionalInterface
public interface SimpleCommand {
    void execute(IOProvider io);
}
