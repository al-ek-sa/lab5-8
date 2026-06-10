package edu.itmo.piikt.common.io.providerType;

import edu.itmo.piikt.common.io.data.NameIOProvider;
import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class IOFile implements IOProvider {
	private static final AppLogger logger = new AppLogger(IOFile.class);
	private final BufferedReader reader;
	private final Queue<String> dataQueue = new ArrayDeque<>();

	public IOFile(String nameFile) throws IOException {
		this.reader = new BufferedReader(new FileReader(nameFile));
		try (Context ignored = Context.newId()) {
			logger.info("IOFile opened: {}", nameFile);
		}
	}

	@Override
	public void println(String message) {
		try (Context ignored = Context.newId()) {
			logger.debug("File output (ignored): {}", message);
		}
	}

	@Override
	public String readLine() {
		try (Context ignored = Context.newId()) {
			String queued = dataQueue.poll();
			if (queued != null) {
				logger.debug("Returning queued data: {}", queued);
				return queued;
			}
			final String commandLine;
			try {
				commandLine = reader.readLine();
				if (commandLine == null) {
					logger.debug("End of file reached");
					close();
					return null;
				}
				logger.debug("Read line from file: {}", commandLine);
				return parseAndQueue(commandLine);
			} catch (IOException e) {
				logger.error("Error reading file: {}", e.getMessage());
				throw new RuntimeException("Error reading file: " + e.getMessage());
			}
		}
	}

	@Override
	public String name() {
		return NameIOProvider.FILE.getName();
	}

	private void data(String data) {
		try (Context ignored = Context.newId()) {
			logger.debug("Parsing data: {}", data);
			if (data.startsWith("{") && data.endsWith("}")) {
				data = data.substring(1, data.length() - 1);
			}
			String[] arguments = data.split(";");
			for (String argument : arguments) {
				String dataEnd = argument.trim();
				if (dataEnd.startsWith("\"") && dataEnd.endsWith("\"")) {
					dataEnd = dataEnd.substring(1, dataEnd.length() - 1);
				}
				dataQueue.add(dataEnd);
				logger.debug("Queued data: {}", dataEnd);
			}
		}
	}

	private String parseAndQueue(String line) {
		int brace = line.indexOf('{');
		if (brace < 0)
			return line;
		String left = line.substring(0, brace).trim();
		String right = line.substring(brace);
		if (left.startsWith("add") || left.startsWith("update")) {
			String[] leftParts = left.split("\\s+");
			String commandWithId = leftParts[0];
			String id = leftParts.length > 1 ? leftParts[1] : null;

			if (id != null && left.startsWith("update")) {
				dataQueue.add(id);
				logger.debug("Added update ID to queue: {}", id);
			}
			data(right);
			return commandWithId;
		}

		return line;
	}

	public void close() {
		try (Context ignored = Context.newId()) {
			logger.info("Closing IOFile");
			if (reader != null) {
				reader.close();
				logger.debug("IOFile closed");
			}
		} catch (IOException e) {
			logger.error("Error closing file: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
