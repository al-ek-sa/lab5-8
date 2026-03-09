package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.BaseArgumentCommand;
import edu.itmo.piikt.managers.MessageCommand;

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
