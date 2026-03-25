package edu.itmo.piikt.client.command;

import edu.itmo.piikt.common.io.providerType.IOFile;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.data.NameIOProvider;
import edu.itmo.piikt.client.manager.ValidationCommand;
import lombok.NoArgsConstructor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The class implements the command execute_script file_name : read and execute
 * a script from the specified file. The script contains commands in the same
 * format as the user enters them in interactive mode.
 *
 * @author Lishyk Aliaksandra
 * @version 3.0
 * @see IOProvider
 * @see ValidationCommand
 */
@NoArgsConstructor
public final class ExecuteScriptCommand {
    private final List<String> name = new ArrayList<>();
    public void execute(IOProvider io, String argument) {
        try {
            if (io.name().equals(NameIOProvider.CONSOLE.getName())) {
                name.clear();
            }
            name.forEach(nameFile -> {
                if (nameFile.equals(argument)) {
                    io.println("error");
                }
            });
            name.add(argument);
            IOFile script = new IOFile(argument);
            String input;
            while((input = script.readLine()) != null) {
                if (input.isBlank()) {
                    continue;
                }
                ValidationCommand.INSTANCE.validation(script);
            }
            script.close();
        } catch (IOException e) {
            io.println("Error, script not read");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
