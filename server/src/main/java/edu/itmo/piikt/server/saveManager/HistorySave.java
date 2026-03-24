package edu.itmo.piikt.server.saveManager;

import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class saves the history of all entered commands to a file, and also reads
 * commands from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see PrintWriter
 * @see Scanner
 */
public class HistorySave {
    private String fileName;
    Logger logger = Logger.getLogger(HistorySave.class.getName());

    public HistorySave() {
        this.fileName = System.getenv("HISTORY_FILE");
        if (this.fileName == null || this.fileName.isEmpty()) {
            this.fileName = "history.txt";
        }
    }

    /**
     * The method saves all entered commands to a file.
     *
     * @throws Exception
     *             If file system errors occurred.
     */
    /**public void saveCollection() {
        var commands = HistoryCommands.INSTANCE.getLinkedList();

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            commands.forEach(writer::println);
        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
        }
    }

    /**
     * The method reads data about entered commands from a file and writes them to
     * the HistoryCommands collection.
     *
     * @throws Exception
     *             If file system errors occurred.
     */
    /**public void readFile() {
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                HistoryCommands.INSTANCE.add(line);
            }

        } catch (Exception e) {
            logger.log(Level.INFO, e.getMessage());
        }
    }*/
}
