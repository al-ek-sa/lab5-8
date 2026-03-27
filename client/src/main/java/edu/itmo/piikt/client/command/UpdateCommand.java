package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommand {
    private Network network;
    private AddCommand addCommand;
    public ServerResponse update(IOProvider io, String command, String argument) {
        try {
            ClientCommand clientCommand = ClientCommand.builder().nameCommand(command).argumentCommand(argument).build();
            ServerResponse serverResponse = network.send(clientCommand);
            if (serverResponse.isExecution()) {
                ServerResponse server = addCommand.execute(io);
                return server;
            }
            return serverResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
