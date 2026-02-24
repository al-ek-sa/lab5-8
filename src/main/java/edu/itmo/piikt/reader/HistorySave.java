package edu.itmo.piikt.reader;

import edu.itmo.piikt.io.IOProvider;
import edu.itmo.piikt.managers.HistoryCommands;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * The class saves the history of all entered commands to a file, and also
 * reads commands from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */

public class HistorySave {
        private IOProvider io;
        private String fileName;
        private static HistorySave instance;
        private HistorySave() {
            this.fileName = System.getenv("HISTORY_FILE");
            if (this.fileName == null || this.fileName.isEmpty()) {
                this.fileName = "history.txt";
            }
        }

        public static HistorySave getInstance(){
            if (instance == null){
                instance = new HistorySave();
            }
            return instance;}


    /**
     *The method saves all entered commands to a file.
     * @throws Exception If file system errors occurred.
     */
    public void saveCollection() {
            List<String> commands = HistoryCommands.getInstance().getLinkedList();

            try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
                for(String command : commands) {
                    writer.println(command);
                }

            } catch (Exception e) {
                io.printError(e.getMessage());
            }
        }

    /**
     *The method reads data about entered commands from a file and writes them to the HistoryCommands collection.
     * @throws Exception If file system errors occurred.
     */

        public void readFile(){
            try (Scanner scanner = new Scanner(new File(fileName))){
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    HistoryCommands.getInstance().add(line);}

            } catch (Exception e) {
                io.printError(e.getMessage());
            }
    }
}
