package edu.itmo.piikt.commands;

import edu.itmo.piikt.historyWorker.HistoryWorker;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;

/**
 * The class implements the command print_field_descending_end_date : output the endDate field values of all elements in descending order.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class PrintFieldDescendingEndDataCommand extends Commands {
    private IOProvider io;
    public PrintFieldDescendingEndDataCommand(IOProvider io) {
        super("print_field_descending_end_date");
        this.io = io;
    }

    @Override
    public void execute() {
        try {
            io.printeDesign();
            //сортировка по дате увольнения началась
            io.printlnCommand("Sorting by date of dismissal started");
            io.printeDesign();
            HistoryWorker.getInstance(io).sort();
            io.printeDesign();
            //сортировка успешно окончена
            io.printlnCommand("Sorting completed successfully");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //не удалось произвести сортировку
            io.printException("Failed to sort");
            io.printeDesign();
        }
    }
}
