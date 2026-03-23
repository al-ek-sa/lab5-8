package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.manager.ValidationCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.NoArgsConstructor;

/**
 * The class implements the command exit : terminate the program (without saving
 * to a file).
 *
 * @author Lishyk Aliaksandra
 * @version 4.0
 */
@NoArgsConstructor
public final class ExitCommand {
    public void execute() {
        ValidationCommand.INSTANCE.setFlag(false);
    }
}
