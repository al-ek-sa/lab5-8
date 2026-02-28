package edu.itmo.piikt.io;

import edu.itmo.piikt.commands.UpdateIdCommand;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A class that inherits from the IOProvider interface implements data output to
 * the console and reading from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class IOFile implements  IOProvider {
    private BufferedInputStream reading;
    private Queue<String> dataQueue = new LinkedList<>();
    private String argument;
    public IOFile(String nameFile) throws IOException {
        this.reading = new BufferedInputStream(new FileInputStream(nameFile));
    }

    @Override
    public void print(String message) {
        System.out.print(ANSI_ORANGE_256 + message + ANSI_BRIGHT_BLUE);
    }

    @Override
    public void printError(String message) {
        System.out.println(ANSI_RED + message + ANSI_BRIGHT_BLUE);
    }

    @Override
    public void printException(String message) {
        System.out.println(ANSI_YELLOW + message + ANSI_BRIGHT_BLUE);
    }

    @Override
    public void println(String message) {
        System.out.println(ANSI_GREEN + message + ANSI_BRIGHT_BLUE);
    }
    int byteRead;


    /**
     *The method reads data from a script. The data is read character by character and converted into words.
     *
     *
     * @throws IOException If file system errors occurred.
     * @return command
     */

    @Override
    public String readLine() {
        try {

            if ( !dataQueue.isEmpty()) {
                return dataQueue.poll();
            }


            StringBuilder line = new StringBuilder();
            int byteFile;

            while ((byteFile = reading.read()) != -1){
                if (byteFile == '\n'){
                    break;
                }

                line.append((char) byteFile);
            }

            String command = line.toString();

            if (byteFile == -1){
                return  null;
            }

            if (command.startsWith("add") && command.contains("{") ){
                int ind = command.indexOf("{") -1;
                String commandArgument = command.substring(0, ind);
                String dataLIne = command.substring(ind +1);
                data(dataLIne);
                return commandArgument;

            }

            return command;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void printField(String message, String messageFiled) {
        System.out.println(ANSI_TURQUOISE_LIGHT + message + ANSI_RESET + " " + ANSI_LAVENDER_LIGHT + messageFiled + ANSI_BRIGHT_BLUE);
    }

    @Override
    public String name() {
        return "File";
    }

    /**
     *A method that adds data entered after the command, in curly braces, to a queue.
     *
     * @param data A string with data is passed as parameters to the method.
     */

    private void data(String data){
        if (data.startsWith("{") && data.endsWith("}")){
            data = data.substring(1,data.length()-1);
        }

        String[] arguments = data.split(";");
        for (String argument: arguments){
            String dataEnd = argument.substring(1, argument.length()-1);
            dataQueue.add(dataEnd);
        }
    }
}
