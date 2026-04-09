package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.algorithms.Graph;
import edu.itmo.piikt.common.io.providerType.IOFile;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.data.NameIOProvider;
import edu.itmo.piikt.client.manager.ValidationCommand;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Data
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
            IOProvider provider = new ScriptProvider(ioProvider, graph, argument);

            ValidationCommand.INSTANCE.pushProvider(provider);
        } catch (RuntimeException ex) {
            io.println("ошибка выполнения");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static  class ScriptProvider implements IOProvider{
        private boolean flag = false;
        private IOFile ioFile;
        private Graph graph;
        private String name;

        ScriptProvider(IOFile ioFile, Graph graph, String name){
            this.ioFile = ioFile;
            this.graph = graph;
            this.name = name;
        }

        @Override
        public String name() {
            return "";
        }

        @Override
        public void println(String message) {
            ioFile.println(message);
        }

        @Override
        public String readLine() {
            if (flag) return null;
            try{
                String line = ioFile.readLine();
                if (line == null) {
                    flag = true;
                    ioFile.close();
                    graph.endScript(name);
                    return null;
                }
                if (line.isBlank()) {
                    return readLine();
                }
                return line;
            } catch (RuntimeException e) {
                flag = true;
                graph.endScript(name);
                return null;
            }
        }
    }
}