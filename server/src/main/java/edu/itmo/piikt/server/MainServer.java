package edu.itmo.piikt.server;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Config;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.CommandServer.CommandFactory;
import edu.itmo.piikt.server.CommandServer.SaveCommand;
import edu.itmo.piikt.server.dispatcher.Dispatcher;
import edu.itmo.piikt.server.netWork.NetWork;
import edu.itmo.piikt.server.saveManager.CSVParser;

import java.io.IOException;
import java.net.URL;

public class MainServer {
    private static final AppLogger logger = new AppLogger(MainServer.class);

    public static void main(String[] args) {
        Config.configureFromArgs(args);

        try (Context ignored = Context.newId()) {
            logger.info("Starting server...");
            CSVParser csvParser = new CSVParser();
            csvParser.readFile();
            logger.info("Data loaded from file");
            Dispatcher dispatcher = new Dispatcher();
            NetWork netWork = new NetWork(dispatcher);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try (Context ignored1 = Context.newId()) {
                    logger.info("Shutdown signal received");
                    netWork.stop();
                    csvParser.saveCollection();
                    logger.info("Server stopped");
                }
            }));

            try {
                netWork.start();
            } catch (IOException e) {
                logger.error("Failed to start server: {}", e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            logger.error("Server initialization failed: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}