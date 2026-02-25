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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command execute_script file_name : read and execute a script from the specified file.
 * The script contains commands in the same format as the user enters them in interactive mode.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class ExecuteScriptCommand extends ArgumentCommand {
    private IOProvider io;
    private static final List<String> name = new ArrayList<>();
    Logger logger = Logger.getLogger(ExecuteScriptCommand.class.getName());
    public ExecuteScriptCommand(IOProvider io){
        super("execute_script");
        this.io = io;
    }

    @Override
    public void execute(String argument) {
        try{
            if(io.name().equals("File")) {
                io.printeDesign();
                //начало чтения скрипта
                logger.log(Level.INFO,"Start of script reading");
                io.printeDesign();
                for (String nameFile : name){
                    if (nameFile.equals(argument)){
                        throw new ExceptionScript();
                    }
                }
                name.add(argument);
                IOFile script = new IOFile(argument);
                ValidationCommand scriptValidation = new ValidationCommand(script);
                scriptValidation.validation();
                io.printeDesign();
                //скрипт успешно прочтен и выполнен
                logger.log(Level.INFO,"Script successfully read and executed");
                io.printeDesign();
            }

            if (io.name().equals("Console")){
                name.clear();
                io.printeDesign();
                //начало чтения скрипта
                io.printlnCommand("Start of script reading");
                io.printeDesign();
                for (String nameFile : name){
                    if (nameFile.equals(argument)){
                        io.printException("Error in file:" + name.getLast());
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
                io.printlnCommand("Script successfully read and executed");
                io.printeDesign();
            }
        }catch (ExceptionScript e){
            io.printError(e.getMessage() + argument + ")");
        } catch (IOException e) {
            io.printeDesign();
            //Ошибка, скрипт не прочтен
            io.printException("Error, script not read");
            io.printeDesign();
        } catch (Exception e) {
            io.printError(e.getMessage());
        }
    }
}
