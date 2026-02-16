package edu.itmo.piikt;
import edu.itmo.piikt.commands.HelpCommand;
import edu.itmo.piikt.commands.HistoryCommands;
import edu.itmo.piikt.commands.ValidationCommand;
import edu.itmo.piikt.reader.InputReader;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HelpCommand help = new HelpCommand();
        help.execute();
        ValidationCommand validationCommand = new ValidationCommand();
        validationCommand.validation();
    }
}