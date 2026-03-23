package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.saveManager.CSVParser;
import lombok.NoArgsConstructor;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 5.0
 */
@NoArgsConstructor
public final class ExitCommand {
        public ServerResponse execute() {
            CSVParser csvParser = new CSVParser();
            csvParser.saveCollection();
            return ServerResponse.successfulCompletion("EXIT");
    }
}