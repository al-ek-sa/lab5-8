package edu.itmo.piikt.client.manager;

import edu.itmo.piikt.client.algorithms.DamerauLevenshteinDistance;
import edu.itmo.piikt.client.command.ExecuteScriptCommand;
import edu.itmo.piikt.client.data.Organization;
import edu.itmo.piikt.client.data.Worker;
import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.common.command.data.Commands;
import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.server_client.ClientCommand;
import lombok.Getter;

import java.util.ArrayList;
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

    /**
     * The method selects from the registered commands the command that the user
     * entered. When entering, the user can make a mistake once.
     *
     * <p>
     * Attention! The command handles only single-word commands without arguments
     * and single-word commands with one argument.
     */
    // todo
    public ClientCommand validation(IOProvider io) {
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
                                    worker.build(io);
                                    //возращает данные
                                }
                                if (com1.equals(Commands.COUNT_BY_ORGANIZATION.getName())) {
                                    //создание организации
                                    //возврат данных
                                }
                                //возращает команду
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
                                //возвращает комманду
                                //возвращает аргумент
                            }
                }}

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
