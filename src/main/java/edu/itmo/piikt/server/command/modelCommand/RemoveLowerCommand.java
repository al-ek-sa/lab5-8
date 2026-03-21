package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.history.HistoryWorker;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The class implements the command remove_lower {element} : remove from the
 * collection all elements that are lower than the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 4.0
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class RemoveLowerCommand {
    public ServerResponse execute(ClientCommand clientCommand) {
        String argument = clientCommand.getArgumentCommand();
        if (argument == null || argument.trim().isEmpty()) {
            return ServerResponse.error("Дата не введена");
        }
        LocalDate date;
        try {
            date = LocalDate.parse(argument.trim());
        } catch (DateTimeParseException e) {
            return ServerResponse.error("Неверный формат даты");
        }
        var listWorker = HistoryWorker.INSTANCE.getListWorker();
        listWorker.removeIf(worker -> worker.getStartDate().isAfter(date));
        return ServerResponse.successfulCompletion("REMOVE LOWER");
    }
}
