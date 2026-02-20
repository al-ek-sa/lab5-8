package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOFile;
import edu.itmo.piikt.io.IOProvider;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class ExecuteScriptCommand extends ArgumentCommand {
    private IOProvider io;
    public ExecuteScriptCommand(IOProvider io){
        super("execute_script");
        this.io = io;
    }

    @Override
    public void execute(String argument) {
        try{
            IOFile script = new IOFile(argument);
            CommandFactory newFactory = new CommandFactory(script);
            ValidationCommand scriptValidation = new ValidationCommand(script);
            scriptValidation.validation();
        } catch (IOException e) {
            io.printError("ошибка в чтении скрипта");
        }
    }
}
