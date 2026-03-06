package edu.itmo.piikt;

import edu.itmo.piikt.commands.HelpCommand;
import edu.itmo.piikt.commands.HelpEnteringCommand;
import edu.itmo.piikt.io.IOConsole;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ValidationCommand;
import edu.itmo.piikt.reader.CSVParser;
import java.util.logging.Logger;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class Main {
    public static void main(String[] args) {
        Signal.handle(new Signal("INT"), new SignalHandler() {
            public void handle(Signal sig) {
            }
        });
        Signal.handle(new Signal("TSTP"), new SignalHandler() {
            public void handle(Signal sig) {
            }
        });
        Logger logger = Logger.getLogger(Main.class.getName());
        IOProvider io = new IOConsole();
        // HistorySave.getInstance().readFile();
        CSVParser csvParser = new CSVParser();
        csvParser.readFile(io);
        HelpCommand help = new HelpCommand();
        help.execute(io);
        HelpEnteringCommand helpEnteringCommand = new HelpEnteringCommand();
        helpEnteringCommand.execute(io);
        ValidationCommand validationCommand = ValidationCommand.getInstance();
        validationCommand.validation(io);
    }
}
