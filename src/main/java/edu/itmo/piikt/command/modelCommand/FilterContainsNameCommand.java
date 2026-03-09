package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.history.HistoryWorker;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseArgumentCommand;
import edu.itmo.piikt.massage.MessageCommand;

/**
 * The class implements the command filter_contains_name name : output elements
 * whose name field value contains the specified substring.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public final class FilterContainsNameCommand implements BaseArgumentCommand {
    public FilterContainsNameCommand() {
    }
    /**
     * The method outputs all employees with the same name as entered by the user.
     *
     * @param argument
     *            The name entered by the user.
     */
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
