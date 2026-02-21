package edu.itmo.piikt;
import edu.itmo.piikt.commands.HelpCommand;
import edu.itmo.piikt.managers.ValidationCommand;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.io.IOScanner;
import edu.itmo.piikt.reader.CSVParser;
import edu.itmo.piikt.reader.HistorySave;

public class Main {
    public static void main(String[] args) {
        IOProvider io = new IOScanner();
        HistorySave.getInstance().readFile();
        CSVParser csvParser = new CSVParser(io);
        csvParser.readFile();
        HelpCommand help = new HelpCommand(io);
        help.execute();
        ValidationCommand validationCommand = new ValidationCommand(io);
        validationCommand.validation();
    }
}