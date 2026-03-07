package edu.itmo.piikt;

import edu.itmo.piikt.commands.HelpCommand;
import edu.itmo.piikt.commands.HelpEnteringCommand;
import edu.itmo.piikt.io.IOConsole;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ValidationCommand;
import edu.itmo.piikt.reader.CSVParser;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        /**
         * Signal.handle(new Signal("INT"), SignalHandler.SIG_IGN); Signal.handle(new
         * Signal("TSTP"), SignalHandler.SIG_IGN);
         */
        Logger logger = Logger.getLogger(Main.class.getName());
        IOProvider io = new IOConsole();
        // HistorySave.getInstance().readFile();
        CSVParser csvParser = new CSVParser();
        csvParser.readFile(io);
        HelpCommand help = new HelpCommand();
        help.execute(io);
        HelpEnteringCommand helpEnteringCommand = new HelpEnteringCommand();
        helpEnteringCommand.execute(io);
        ValidationCommand validationCommand = ValidationCommand.INSTANCE;
        validationCommand.validation(io, logger);
    }
}
