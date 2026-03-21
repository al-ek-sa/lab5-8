package edu.itmo.piikt.client.command;

import edu.itmo.piikt.server.command.exception.ExceptionScript;
import edu.itmo.piikt.client.io.providerType.IOFile;
import edu.itmo.piikt.client.provider.IOProvider;
import edu.itmo.piikt.client.io.data.NameIOProvider;
import edu.itmo.piikt.client.manager.ValidationCommand;
import lombok.NoArgsConstructor;
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
 * @version 3.0
 * @see Logger
 * @see IOProvider
 * @see ValidationCommand
 */
@NoArgsConstructor
public final class ExecuteScriptCommand {
    private final List<String> name = new ArrayList<>();
    Logger logger = Logger.getLogger(ExecuteScriptCommand.class.getName());
    public void execute(IOProvider io, String argument) {
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
            ValidationCommand.INSTANCE.validation(script);
        } catch (ExceptionScript e) {
            logger.log(Level.SEVERE, e.getMessage() + argument);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error, script not read");
        }
    }
}
