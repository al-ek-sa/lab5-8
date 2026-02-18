package edu.itmo.piikt;
import edu.itmo.piikt.commands.HelpCommand;
import edu.itmo.piikt.commands.HistoryCommands;
import edu.itmo.piikt.commands.ValidationCommand;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.io.IOScanner;
import edu.itmo.piikt.reader.CSVParser;
import edu.itmo.piikt.reader.InputReader;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        IOProvider io = new IOScanner();
        CSVParser csvParser = new CSVParser(io);
        csvParser.readFile();
        HelpCommand help = new HelpCommand(io);
        help.execute();
        ValidationCommand validationCommand = new ValidationCommand(io);
        validationCommand.validation();
    }
}