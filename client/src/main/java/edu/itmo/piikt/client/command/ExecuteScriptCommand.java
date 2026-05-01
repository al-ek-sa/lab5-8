package edu.itmo.piikt.client.command;

import edu.itmo.piikt.client.algorithms.Graph;
import edu.itmo.piikt.client.commands.CommandVoid;
import edu.itmo.piikt.client.manager.ValidationCommand;
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

/**
 * Command for executing script files
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@NoArgsConstructor
public final class ExecuteScriptCommand implements CommandVoid {
	private static final AppLogger log = new AppLogger(ExecuteScriptCommand.class);
	/** List of executed scripts to prevent recursion */
	private final List<String> name = new ArrayList<>();
	/** Graph for cycle detection in script calls */
	private final Graph graph = new Graph();

	/**
	 * Executes a script from the specified file
	 *
	 * @param io
	 *            input/output provider
	 */
	@Override
	public void execute(IOProvider io, Object... arg) {
		if (arg.length != 1) {
			throw new RuntimeException();
		}
		var argument = (String) arg[0];
		try {
			log.info("Executing script: {}", argument);
			// Clear history for console commands
			if (io.name().equals(NameIOProvider.CONSOLE.getName())) {
				name.clear();
			}

			// Check for cycle before execution
			if (graph.copy(argument)) {
				return;
			}
			// Start script execution in graph
			graph.start(argument);
			List<String> list = graph.getList();

			// Add dependency edge if parent script exists
			if (list.size() > 1) {
				String beforeScript = list.get(list.size() - 2);
				graph.addScript(beforeScript, argument);
			}

			// Create provider for script file and switch context
			IOFile ioProvider = new IOFile(argument);
			IOProvider provider = new ScriptProvider(ioProvider, graph, argument);

			ValidationCommand.INSTANCE.pushProvider(provider);
			log.debug("Script provider pushed for: {}", argument);
		} catch (FileNotFoundException e) {
			log.error("Script file not found: {}", argument);
			io.println("Ошибка: файл скрипта '" + argument + "' не найден");
			graph.endScript(argument);
		} catch (IOException e) {
			log.error("IO error reading script: {}", e.getMessage());
			io.println("Ошибка при чтении файла скрипта '" + argument + "': " + e.getMessage());
			graph.endScript(argument);

		} catch (Exception e) {
			log.error("Unexpected error executing script: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * IOProvider implementation for reading commands from a script file.
	 */
	private static class ScriptProvider implements IOProvider {
		/** Flag indicating end of file */
		private boolean flag = false;
		private final IOFile ioFile;
		private final Graph graph;
		private final String name;

		ScriptProvider(IOFile ioFile, Graph graph, String name) {
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
			if (flag)
				return null;
			try {
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
			} catch (Exception e) {
				flag = true;
				graph.endScript(name);
				return null;
			}
		}
	}
}
