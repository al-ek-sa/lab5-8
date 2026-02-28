package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command print_field_descending_end_date : output the endDate field values of all elements in descending order.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class PrintFieldDescendingEndDataCommand extends Commands {
    private IOProvider io;
    Logger logger = Logger.getLogger(PrintFieldDescendingEndDataCommand.class.getName());
    public PrintFieldDescendingEndDataCommand(IOProvider io) {
        super("print_field_descending_end_date");
        this.io = io;
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            //сортировка по дате увольнения началась
            logger.log(Level.INFO,"Sorting by date of dismissal started");
            io.printeDesign();
            HistoryWorker.getInstance(io).sort();
        } catch (Exception e) {
            io.printeDesign();
            //не удалось произвести сортировку
            io.printException("Failed to sort");
            io.printeDesign();
        }
    }
}
