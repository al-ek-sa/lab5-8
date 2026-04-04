package edu.itmo.piikt.common.io.providerType;

import edu.itmo.piikt.common.io.provider.IOProvider;
import edu.itmo.piikt.common.io.data.NameIOProvider;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A class that inherits from the IOProvider interface implements data output to
 * the console and reading from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 2.2
 */
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

    /**
     * The method reads data from a script. The data is read character by character
     * and converted into words.
     *
     * @return command
     */
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

    /**
     * A method that adds data entered after the command, in curly braces, to a
     * queue.
     *
     * @param data
     *            A string with data is passed as parameters to the method.
     */
    private void data(String data) {
        try (Context ignored = Context.newId()) {
            logger.debug("Parsing data: {}", data);
            if (data.startsWith("{") && data.endsWith("}")) {
                data = data.substring(1, data.length() - 1);
            }
            String[] arguments = data.split(";");
            for (String argument : arguments) {
                String dataEnd = argument.substring(1, argument.length() - 1);
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
        if (!left.startsWith("add"))
            return line;
        String right = line.substring(brace + 1);
        data(right);
        return left;
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