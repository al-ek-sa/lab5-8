package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.algorithms.Graph;
import edu.itmo.piikt.common.io.providerType.IOFile;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.data.NameIOProvider;
import edu.itmo.piikt.client.manager.ValidationCommand;
import lombok.NoArgsConstructor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public final class ExecuteScriptCommand {
    private final List<String> name = new ArrayList<>();
    private final Graph graph = new Graph();

    public void execute(IOProvider io, String argument) throws IOException {

        try {
            if (io.name().equals(NameIOProvider.CONSOLE.getName())) {
                name.clear();
            }

            if (graph.copy(argument)) {
                return;
            }

            graph.start(argument);
            List<String> list = graph.getList();

            if (list.size() > 1) {
                String beforeScript = list.get(list.size() - 2);
                graph.addScript(beforeScript, argument);
            }

            IOFile ioProvider = new IOFile(argument);

            ValidationCommand.INSTANCE.pushProvider(ioProvider);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}