package edu.itmo.piikt;
import edu.itmo.piikt.commands.HelpCommand;
import edu.itmo.piikt.managers.ValidationCommand;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.io.IOScanner;
import edu.itmo.piikt.reader.CSVParser;
import edu.itmo.piikt.reader.HistorySave;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class Main {
    public static void main(String[] args) {
        Signal.handle(new Signal("INT"), new SignalHandler () {
            public void handle(Signal sig) {}
        });
        Signal.handle(new Signal("TSTP"), new SignalHandler () {
            public void handle(Signal sig) {}
        });
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