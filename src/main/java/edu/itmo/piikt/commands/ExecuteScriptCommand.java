package edu.itmo.piikt.commands;

import edu.itmo.piikt.io.IOFile;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;
import edu.itmo.piikt.managers.CommandFactory;
import edu.itmo.piikt.managers.ValidationCommand;

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
            io.printeDesign();
            //начало чтения скрипта
            io.println("Start of script reading");
            io.printeDesign();
            IOFile script = new IOFile(argument);
            CommandFactory newFactory = new CommandFactory(script);
            ValidationCommand scriptValidation = new ValidationCommand(script);
            scriptValidation.validation();
            io.printeDesign();
            //скрипт успешно прочтен и выполнен
            io.println("Script successfully read and executed");
            io.printeDesign();
        } catch (IOException e) {
            io.printeDesign();
            //Ошибка, скрипт не прочтен
            io.printError("Error, script not read");
            io.printeDesign();
        }
    }
}
