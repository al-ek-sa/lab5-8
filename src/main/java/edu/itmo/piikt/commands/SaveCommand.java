package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.Commands;
import edu.itmo.piikt.reader.CSVParser;

public class SaveCommand extends Commands {
    private IOProvider io;
    public SaveCommand(IOProvider io){
        super("save");
        this.io = io;
    }
    @Override
    public void execute() {
        try {
            io.printeDesign();
            //сохранение данных в файл началось
            io.printlnCommand("Saving data to file started");
            io.printeDesign();
            CSVParser csvParser = new CSVParser(io);
            csvParser.saveCollection();
            //данные сохраннены в файл
            io.printlnCommand("Data saved to file");
            io.printeDesign();
        } catch (Exception e) {
            io.printeDesign();
            //данные сохраннены в файл
            io.printException("Data saved to file");
            io.printeDesign();
        }
    }
}
