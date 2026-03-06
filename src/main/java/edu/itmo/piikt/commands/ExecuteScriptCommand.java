package edu.itmo.piikt.commands;

import edu.itmo.piikt.exception.ExceptionScript;
import edu.itmo.piikt.io.IOFile;
import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.ValidationCommand;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class implements the command execute_script file_name : read and execute
 * a script from the specified file. The script contains commands in the same
 * format as the user enters them in interactive mode.
 *
 * @author Lishyk Aliaksandra
 * @version 2.0
 */
public class ExecuteScriptCommand {
    private static final List<String> name = new ArrayList<>();
    Logger logger = Logger.getLogger(ExecuteScriptCommand.class.getName());

    public ExecuteScriptCommand() {
    }

    public void execute(IOProvider io, String argument) {
        try {
            if (io.name().equals("File")) {
                logger.log(Level.INFO, "Start of script reading");
                for (String nameFile : name) {
                    if (nameFile.equals(argument)) {
                        throw new ExceptionScript();
                    }
                }
                name.add(argument);
                IOFile script = new IOFile(argument);
                ValidationCommand.getInstance().validation(script);
                logger.log(Level.INFO, "Script successfully read and executed");
            }

            if (io.name().equals("Console")) {
                name.clear();
                io.printlnCommand("Start of script reading");
                for (String nameFile : name) {
                    if (nameFile.equals(argument)) {
                        io.printException("Error in file:" + name.getLast());
                        throw new ExceptionScript();
                    }
                }
                name.add(argument);
                IOFile script = new IOFile(argument);
                ValidationCommand.getInstance().validation(script);
                name.clear();
                io.printlnCommand("Script successfully read and executed");
            }
        } catch (ExceptionScript e) {
            io.printError(e.getMessage() + argument + ")");
        } catch (IOException e) {
            io.printException("Error, script not read");
        } catch (Exception e) {
            io.printError(e.getMessage());
        }
    }
}
