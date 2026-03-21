package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.common.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseArgumentCommand;
import edu.itmo.piikt.common.massage.MessageCommand;
import lombok.NoArgsConstructor;

/**
 * The class implements the command filter_contains_name name : output elements
 * whose name field value contains the specified substring.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 * @see BaseArgumentCommand
 * @see IOProvider
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class FilterContainsNameCommand implements BaseArgumentCommand {
    /**
     * The method outputs all employees with the same name as entered by the user.
     *
     * @param argument
     *            The name entered by the user.
     */
    // todo через map получить toString
    @Override
    public void doExecute(IOProvider io, String argument) {
        var list = HistoryWorker.INSTANCE.getListWorker();
        list.stream().filter(worker -> worker.getName() != null).filter(worker -> worker.getName().equals(argument))
                .forEach(worker -> io.println(worker.toString()));
    }
    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.FILTER_CONTAINS_NAME;
    }
}
