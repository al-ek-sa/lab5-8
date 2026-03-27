package edu.itmo.piikt.server.saveManager;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import java.io.*;
import java.util.Scanner;

/**
 * The class saves the history of all entered commands to a file, and also reads
 * commands from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.1
 * @see PrintWriter
 * @see Scanner
 */
public class HistorySave {
    private static final AppLogger logger = new AppLogger(HistorySave.class);
    private String fileName;

    public HistorySave() {
        this.fileName = System.getenv("HISTORY_FILE");
        if (this.fileName == null || this.fileName.isEmpty()) {
            this.fileName = "history.txt";
        }
        logger.info("HistorySave initialized, file: {}", fileName);
    }
}