package edu.itmo.piikt.server.command.modelCommand;

import edu.itmo.piikt.server.command.exception.ExceptionScript;
import edu.itmo.piikt.client.io.providerType.IOFile;
import edu.itmo.piikt.common.provider.IOProvider;
import edu.itmo.piikt.common.command.base.BaseArgumentCommand;
import edu.itmo.piikt.common.massage.MessageCommand;
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
 * @version 2.2
 * @see BaseArgumentCommand
 * @see Logger
 * @see IOProvider
 * @see ValidationCommand
 */
@NoArgsConstructor
public final class ExecuteScriptCommand implements BaseArgumentCommand {
    private final List<String> name = new ArrayList<>();
    Logger logger = Logger.getLogger(ExecuteScriptCommand.class.getName());
    // todo пересмотреть рекурсию (хвостовая приведет к переполнению стека)
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
            ValidationCommand.INSTANCE.validation(script);
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
