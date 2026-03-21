package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.NoArgsConstructor;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 */
@NoArgsConstructor
public final class ExitCommand {
    public ServerResponse execute() {
        return ServerResponse.successfulCompletion("EXIT");
    }
}
