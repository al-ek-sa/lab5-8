package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.reader.CSVParser;

import java.util.logging.Logger;import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command save : save the collection to a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class SaveCommand extends Commands {
    private IOProvider io;
    Logger logger = Logger.getLogger(SaveCommand.class.getName());
    public SaveCommand(IOProvider io){
        super("save");
        this.io = io;
    }
    @Override
    public void execute() {
        try {
            io.printeDesign();
            //сохранение данных в файл началось
            logger.log(Level.INFO,"Saving data to file started");
            io.printeDesign();
            CSVParser csvParser = new CSVParser(io);
            csvParser.saveCollection();

        } catch (Exception e) {
            io.printeDesign();
            //данные сохраннены в файл
            logger.log(Level.INFO,"Data saved to file");
            io.printeDesign();
        }
    }
}
