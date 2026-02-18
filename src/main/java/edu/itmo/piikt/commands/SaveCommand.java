package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.reader.CSVParser;

public class SaveCommand extends Commands {
    private IOProvider io;
    public SaveCommand(IOProvider io){
        super("save");
        this.io = io;
    }
    @Override
    public void execute() {
        CSVParser csvParser = new CSVParser(io);
        csvParser.saveCollection();
    }
}
