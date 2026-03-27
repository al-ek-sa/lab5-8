package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.data.Worker;
import edu.itmo.piikt.client.data.WorkerServer;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddCommand {
    private static final AppLogger logger = new AppLogger(AddCommand.class);
    private Network network;
    private Worker worker = new Worker();

    public ServerResponse execute(IOProvider io) {
        try (Context context = Context.newId()) {
            logger.info("ADD command started");
            WorkerData workerData = worker.build(io);
            ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.ADD.getName())
                    .data(workerData).build();
            ServerResponse serverResponse = network.send(clientCommand);
            return add(serverResponse, io);
        } catch (Exception e) {
            logger.error("ADD command failed: {}", e);
            throw new RuntimeException(e);
        }
    }

    private ServerResponse add(ServerResponse serverResponse, IOProvider io) {
        var workerServer = new WorkerServer(io);
        var server = serverResponse;
        while (true) {
            try (Context context = Context.newId()) {
                if (server.isExecution()) {
                    logger.info("ADD command completed");
                    return server;
                } else {
                    logger.warn("Validation error, requesting correction");
                    var data = workerServer.build(server);
                    ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.ADD.getName())
                            .data(data).build();
                    server = network.send(clientCommand);
                }
            } catch (Exception e) {
                logger.error("Error in add retry: {}", e);
                throw new RuntimeException(e);
            }
        }
    }
}