package edu.itmo.piikt.server.saveManager;

import edu.itmo.piikt.common.logger.AppLogger;
import java.io.*;

/**
 * The class saves the history of all entered commands to a file, and also reads
 * commands from a file.
 *
 * @author Lishyk Aliaksandra
 * @version 1.1
 */
public class HistorySave {
    private static final AppLogger logger = new AppLogger(HistorySave.class);

    public HistorySave() {
        String fileName = System.getenv("HISTORY_FILE");
        if (fileName == null || fileName.isEmpty()) {
            fileName = "history.txt";
        }
        logger.info("HistorySave initialized, file: {}", fileName);
    }
}