package edu.itmo.piikt.managers;

import edu.itmo.piikt.io.IOProvider;

@FunctionalInterface
public interface SimpleCommand {
    void execute(IOProvider io);
}