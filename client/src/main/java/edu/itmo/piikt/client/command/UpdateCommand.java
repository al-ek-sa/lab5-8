package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommand {
    private static final AppLogger logger = new AppLogger(UpdateCommand.class);
    private Network network;
    private AddCommand addCommand;

    public ServerResponse update(IOProvider io, String command, String argument) {
        try (Context context = Context.newId()) {
            logger.info("UPDATE command started: id={}", argument);
            ClientCommand clientCommand = ClientCommand.builder().nameCommand(command).argumentCommand(argument).build();
            ServerResponse serverResponse = network.send(clientCommand);
            logger.debug("Initial response: success={}", serverResponse.isExecution());
            if (serverResponse.isExecution()) {
                logger.info("Fetching current data for update");
                ServerResponse server = addCommand.execute(io);
                return server;
            }
            logger.warn("Update failed: {}", serverResponse.getMessage());
            return serverResponse;
        } catch (Exception e) {
            logger.error("UPDATE command failed: {}", e);
            throw new RuntimeException(e);
        }
    }
}