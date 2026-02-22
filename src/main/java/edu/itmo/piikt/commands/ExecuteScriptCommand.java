package edu.itmo.piikt.commands;

import edu.itmo.piikt.exception.ExceptionScript;
import edu.itmo.piikt.io.IOFile;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ArgumentCommand;
import edu.itmo.piikt.managers.CommandFactory;
import edu.itmo.piikt.managers.ValidationCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecuteScriptCommand extends ArgumentCommand {
    private IOProvider io;
    private static final List<String> name = new ArrayList<>();
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
            for (String nameFile :name){
                if (nameFile.equals(argument)){
                    io.printError("Error in file:" + name.getLast());
                    name.clear();
                    throw new ExceptionScript();
                }
            }
            name.add(argument);
            IOFile script = new IOFile(argument);
            ValidationCommand scriptValidation = new ValidationCommand(script);
            scriptValidation.validation();
            name.clear();
            io.printeDesign();
            //скрипт успешно прочтен и выполнен
            io.println("Script successfully read and executed");
            io.printeDesign();
        }catch (ExceptionScript e){
            io.printError(e.getMessage() + argument + ")");
        } catch (IOException e) {
            io.printeDesign();
            //Ошибка, скрипт не прочтен
            io.printError("Error, script not read");
            io.printeDesign();
        } catch (Exception e) {
            io.printError(e.getMessage());
        }
    }
}
