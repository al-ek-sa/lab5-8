package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.command.algorithms.Graph;
import edu.itmo.piikt.common.io.data.NameIOProvider;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.providerType.IOFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.itmo.piikt.common.logger.AppLogger;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ExecuteScriptCommand {
	private static final AppLogger log = new AppLogger(ExecuteScriptCommand.class);
	private final List<String> name = new ArrayList<>();
	private final Graph graph = new Graph();

	public void execute(IOProvider io, Object... arg) {
		if (arg.length != 1) {
			throw new RuntimeException("execute_script requires filename argument");
		}
		var argument = (String) arg[0];
		try {
			log.info("Executing script: {}", argument);
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
			IOProvider provider = new ScriptProvider(ioProvider, graph, argument, io);
			while (provider.readLine() != null) {
			}
		} catch (FileNotFoundException e) {
			log.error("Script file not found: {}", argument);
			graph.endScript(argument);
		} catch (IOException e) {
			log.error("IO error reading script: {}", e.getMessage());
			graph.endScript(argument);
		} catch (Exception e) {
			log.error("Unexpected error executing script: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private static class ScriptProvider implements IOProvider {
		private boolean flag = false;
		private final IOFile ioFile;
		private final Graph graph;
		private final String name;
		private final IOProvider parentIO;

		ScriptProvider(IOFile ioFile, Graph graph, String name, IOProvider parentIO) {
			this.ioFile = ioFile;
			this.graph = graph;
			this.name = name;
			this.parentIO = parentIO;
		}

		@Override
		public String name() {
			return "";
		}

		@Override
		public void println(String message) {
			parentIO.println(message);
		}

		@Override
		public String readLine() {
			if (flag)
				return null;
			try {
				String line = ioFile.readLine();
				if (line == null) {
					flag = true;
					ioFile.close();
					graph.endScript(name);
					parentIO.println("Скрипт " + name + " завершён");
					return null;
				}
				if (line.isBlank()) {
					return readLine();
				}
				return line;
			} catch (Exception e) {
				flag = true;
				graph.endScript(name);
				parentIO.println("Ошибка чтения скрипта: " + e.getMessage());
				return null;
			}
		}
	}
}
