package edu.itmo.piikt.client.manager;

import edu.itmo.piikt.client.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.client.command.ExecuteScriptCommand;
import edu.itmo.piikt.client.data.Organization;
import edu.itmo.piikt.client.data.Worker;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.client.network.ClientData;
import edu.itmo.piikt.client.network.Network;
import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.data.OrganizationData;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import lombok.Getter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The main class of the program. The class determines which command was called.
 *
 * @author Lishyk Aliaksandra
 * @version 1.2
 */
@Getter
public enum ValidationCommand {
    INSTANCE;
    private boolean flag;
    Worker worker = new Worker();
    private Network network;
    Organization organization = new Organization();
    ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand();
    List<String> argumentCommand = Arrays.stream(Commands.values()).filter((Commands::getArgument)).map(Commands::getName).collect(Collectors.toList());
    List<String> baseCommand = Arrays.stream(Commands.values()).filter(com -> !com.getArgument()).map(Commands::getName).collect(Collectors.toList());

    ValidationCommand() {
        this.flag = true;
    }
    // todo
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    /**
     * The method selects from the registered commands the command that the user
     * entered. When entering, the user can make a mistake once.
     *
     * <p>
     * Attention! The command handles only single-word commands without arguments
     * and single-word commands with one argument.
     */
    // todo
    public void validation(IOProvider io) {
        while (flag) {
            try {
                String nameCommand = io.readLine();
                if (nameCommand == null || nameCommand.isBlank()) {
                    continue;
                }
                String command = nameCommand.trim();
                String[] input = command.split("\\s+");
                String element = input[0];
                    if (input.length == 1) {
                        for (String com1 : baseCommand) {
                            if (DamerauLevenshteinDistance.distance(com1, element) <= 1) {
                                if (com1.equals(Commands.ADD.getName())) {
                                    WorkerData workerData = worker.build(io);
                                    ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.ADD.getName())
                                            .data(workerData).build();
                                    ServerResponse serverResponse = network.send(clientCommand);
                                    serverResponse.printToConsole();
                                }
                                if (com1.equals(Commands.COUNT_BY_ORGANIZATION.getName())) {
                                    OrganizationData organizationData = organization.build(io);
                                    ClientCommand clientCommand = ClientCommand.builder().nameCommand(Commands.COUNT_BY_ORGANIZATION.getName()).data(organizationData)
                                            .build();
                                    ServerResponse serverResponse = network.send(clientCommand);
                                    serverResponse.printToConsole();
                                }
                                ClientCommand clientCommand = ClientCommand.builder()
                                        .nameCommand(com1)
                                        .build();
                                ServerResponse serverResponse = network.send(clientCommand);
                                serverResponse.printToConsole();
                            }
                        }
                    }

                    if (input.length == 2) {
                        String argument = input[1];
                        for (String com2 : argumentCommand) {
                            if (DamerauLevenshteinDistance.distance(com2, element) <= 1) {
                                if (com2.equals(Commands.EXECUTE_SCRIPT.getName())) {
                                    executeScriptCommand.execute(io, argument);
                                }
                                ClientCommand clientCommand = ClientCommand.builder().nameCommand(com2).argumentCommand(argument).build();
                                ServerResponse serverResponse = network.send(clientCommand);
                                serverResponse.printToConsole();
                            }
                }}

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
