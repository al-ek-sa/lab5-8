package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.saveManager.CSVParser;
import lombok.NoArgsConstructor;

import java.util.logging.Logger;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 5.1
 */
@NoArgsConstructor
public final class ExitCommand {
    private static final Logger logger = Logger.getLogger(ExitCommand.class.getName());
        public ServerResponse execute() {
            CSVParser csvParser = new CSVParser();
            csvParser.saveCollection();
            logger.info(LoggerCommand.EXIT.getLogMessage());
            return ServerResponse.successfulCompletion("EXIT");
    }
}