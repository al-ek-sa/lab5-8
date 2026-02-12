package edu.itmo.piikt.commands;

import java.util.LinkedList;
import edu.itmo.piikt.reader.InputReader;

public class ValidationCommand {
    private HistoryCommands historyCommands;
    private CommandFactory factory;

    public ValidationCommand() {
        this.historyCommands = HistoryCommands.getInstance();
        this.factory = new CommandFactory();
    }

    public void validation(){
        while (true){
            String nameCommands = InputReader.getInstance().nextLine();
            String input = nameCommands.trim().split("\\s+")[0];
            historyCommands.add(input);

            Commands command = factory.getCommand(input);
            if (command != null){
                command.execute();
            } else if (input.equals("historyAll")) {
                historyCommands.printHistory();
            } else {
                System.out.println("Команда введена неверно");
            }
        }
    }


}
