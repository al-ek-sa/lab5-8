package edu.itmo.piikt.client.io.providerType;

import edu.itmo.piikt.client.io.provider.IOProvider;
import edu.itmo.piikt.client.io.data.NameIOProvider;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A class that inherits from the IOProvider interface implements data output to
 * the console and reading from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 2.1
 */
public class IOFile implements IOProvider {
    private final BufferedReader reader;
    private final Queue<String> dataQueue = new ArrayDeque<>();
    public IOFile(String nameFile) throws IOException {
        this.reader = new BufferedReader(new FileReader(nameFile));
    }

    @Override
    public void printError(String message) {
        System.out.println(message);
    }

    @Override
    public void printException(String message) {
        System.out.println(message);
    }

    @Override
    public void println(String message) {
    }

    /**
     * The method reads data from a script. The data is read character by character
     * and converted into words.
     *
     * @throws IOException
     *             If file system errors occurred.
     * @return command
     */
    @Override
    public String readLine() {
        String queued = dataQueue.poll();
        if (queued != null) {
            return queued;
        }
        final String commandLine;
        try {
            commandLine = reader.readLine();
            if (commandLine == null) {
                return null;
            }
            return parseAndQueue(commandLine);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage());
        }
    }

    @Override
    public void printField(String message, String messageFiled) {
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
        if (data.startsWith("{") && data.endsWith("}")) {
            data = data.substring(1, data.length() - 1);
        }

        String[] arguments = data.split(";");
        for (String argument : arguments) {
            String dataEnd = argument.substring(1, argument.length() - 1);
            dataQueue.add(dataEnd);
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
        try{
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
