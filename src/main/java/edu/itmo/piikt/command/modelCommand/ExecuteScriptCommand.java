package edu.itmo.piikt.command.modelCommand;

import edu.itmo.piikt.command.exception.ExceptionScript;
import edu.itmo.piikt.io.providerType.IOFile;
import edu.itmo.piikt.io.provider.IOProvider;
import edu.itmo.piikt.command.base.BaseArgumentCommand;
import edu.itmo.piikt.massage.MessageCommand;
import edu.itmo.piikt.io.data.NameIOProvider;
import edu.itmo.piikt.manager.ValidationCommand;
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
 * @version 2.2
 * @see BaseArgumentCommand
 * @see Logger
 * @see IOProvider
 * @see ValidationCommand
 */
public final class ExecuteScriptCommand implements BaseArgumentCommand {
    private final List<String> name = new ArrayList<>();
    Logger logger = Logger.getLogger(ExecuteScriptCommand.class.getName());

    public ExecuteScriptCommand() {
    }
    @Override
    public void doExecute(IOProvider io, String argument) {
        try {
            if (io.name().equals(NameIOProvider.CONSOLE.getName())) {
                name.clear();
            }
            name.forEach(nameFile -> {
                if (nameFile.equals(argument)) {
                    throw new ExceptionScript();
                }
            });
            name.add(argument);
            IOFile script = new IOFile(argument);
            ValidationCommand.INSTANCE.validation(script, logger);
        } catch (ExceptionScript e) {
            logger.log(Level.SEVERE, e.getMessage() + argument);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error, script not read");
        }
    }

    @Override
    public MessageCommand getMessageCommand() {
        return MessageCommand.EXECUTE_SCRIPT;
    }
}
