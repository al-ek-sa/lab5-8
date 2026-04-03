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
        IOFile ioProvider = new IOFile(argument);

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
            String line;
            int number = 0;

            while ((line = ioProvider.readLine()) != null) {
                number++;
                if (line.isBlank()) {
                    continue;
                }

                IOProvider commandIO = getIoProvider(io, argument, line);

                ValidationCommand.INSTANCE.validation(commandIO);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ioProvider.close();
            graph.endScript(argument);
        }
    }

    private static IOProvider getIoProvider(IOProvider io, String argument, String finalLine) {
        return new IOProvider() {
            private boolean read = false;

            @Override
            public String readLine() {
                if (!read) {
                    read = true;
                    return finalLine;
                }
                return null;
            }

            @Override
            public void println(String message) {
                io.println(message);
            }

            @Override
            public String name() {
                return "script:" + argument;
            }
        };
    }
}