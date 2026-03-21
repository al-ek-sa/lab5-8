package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The class implements the command filter_contains_name name : output elements
 * whose name field value contains the specified substring.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class FilterContainsNameCommand {
    /**
     * The method outputs all employees with the same name as entered by the user.
     *
     */
    public ServerResponse execute(ClientCommand clientCommand) {
        String argument = clientCommand.getArgumentCommand();
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        List<String> list = listWorker.stream().filter(worker -> worker.getName() != null).filter(worker -> worker.getName().contains(argument))
                .map(Worker::toString).collect(Collectors.toList());
        return ServerResponse.successfulCompletion("FILTER NAME", list);
    }
}
