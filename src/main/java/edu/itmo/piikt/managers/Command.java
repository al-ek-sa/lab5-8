package edu.itmo.piikt.managers;

import edu.itmo.piikt.io.IOProvider;

@FunctionalInterface
public interface Command {
    void execute(IOProvider io);
}