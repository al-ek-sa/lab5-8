package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.data.Organization;
import edu.itmo.piikt.client.data.OrganizationServer;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationCommand {
    private Network network;
    private Organization organization = new Organization();
    public ServerResponse execute(IOProvider io) {
        try {
            OrganizationData organizationData = organization.build(io);
            ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.COUNT_BY_ORGANIZATION.getName())
                    .data(organizationData).build();
            ServerResponse serverResponse = network.send(clientCommand);
            ServerResponse server = organization(serverResponse, io);
            return server;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ServerResponse organization(ServerResponse serverResponse, IOProvider io) {
        var organizationServer = new OrganizationServer(io);
        var server = serverResponse;
        while (true) {
            try {
                if (server.isExecution()) {
                    return server;
                } else {
                    OrganizationData data = organizationServer.build(server);
                    ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.COUNT_BY_ORGANIZATION.getName())
                            .data(data).build();
                    server = network.send(clientCommand);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
