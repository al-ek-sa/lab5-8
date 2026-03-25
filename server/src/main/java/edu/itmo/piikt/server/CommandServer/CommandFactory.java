package edu.itmo.piikt.server.CommandServer;

import edu.itmo.piikt.common.util.InputReader;
import edu.itmo.piikt.common.algorithms.DamerauLevenshteinDistance;

public class CommandFactory {
    private final SaveCommand saveCommand = new SaveCommand();
    public void execute(String command) {
            if (DamerauLevenshteinDistance.distance(command, saveCommand.getName()) <= 1) {
                saveCommand.execute();

        }
    }
}
