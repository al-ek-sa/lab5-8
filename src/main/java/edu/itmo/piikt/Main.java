package edu.itmo.piikt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        /**
         * Signal.handle(new Signal("INT"), SignalHandler.SIG_IGN); Signal.handle(new
         * Signal("TSTP"), SignalHandler.SIG_IGN);
         */
        /**
         * Logger logger = Logger.getLogger(Main.class.getName()); IOProvider io = new
         * IOConsole(); // HistorySave.getInstance().readFile(); CSVParser csvParser =
         * new CSVParser(); csvParser.readFile(io); HelpCommand help = new
         * HelpCommand(); help.execute(io); HelpEnteringCommand helpEnteringCommand =
         * new HelpEnteringCommand(); helpEnteringCommand.execute(io); ValidationCommand
         * validationCommand = ValidationCommand.INSTANCE;
         * validationCommand.validation(io);
         */
        try {
            try (FileReader fileReader = new FileReader("workers.csv")) {
                BufferedReader bufferedReaderider = new BufferedReader(fileReader);
                System.out.println(bufferedReaderider.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
