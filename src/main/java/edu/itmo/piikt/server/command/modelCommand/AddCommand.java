package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.common.data.WorkerData;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.server.history.HistoryWorker;
import edu.itmo.piikt.client.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseSimpleCommand;
import edu.itmo.piikt.server.validation.modelValidation.ValidationWorker;
import lombok.NoArgsConstructor;

/**
 * The class implements the command add {element} : add a new element to the
 * collection.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 * @see IOProvider
 * @see HistoryWorker
 */
@NoArgsConstructor
public final class AddCommand {

    public void execute(ClientCommand clientCommand) {
        WorkerData data = (WorkerData) clientCommand.getWorker();
        ValidationWorker worker = new ValidationWorker(io);
        HistoryWorker.INSTANCE.add(worker.worker(io));
    }
/**
    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.ADD;
    }*/
}
