package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;
import edu.itmo.piikt.validationModels.ValidationWorker;

import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command update id {element} : update the value of the collection element whose id is equal to the specified one.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class UpdateIdCommand extends ArgumentCommand {
    private IOProvider io;
    private ValidationWorker worker;
    Logger logger = Logger.getLogger(UpdateIdCommand.class.getName());
    public UpdateIdCommand(IOProvider io){
        super("update");
        this.io = io;
        this.worker = new ValidationWorker(io);
    }

    @Override
    public void execute(String argument) {
        try {

            HistoryWorker.getInstance(io).idMatches(argument);
            HistoryWorker.getInstance(io).update(argument, worker);
        } catch (RuntimeException e){
            io.printeDesign();
            //Обновление данных прервано
            logger.log(Level.INFO,"Data update interrupted");
            io.printeDesign();
        }
    }
}
