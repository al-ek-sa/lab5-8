package edu.itmo.piikt.common.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class Config {

    public static void configureFromArgs(String[] args) {
        String logLevel = "INFO";
        String logFile = null;
        String logOutput = "both";

        for (int i = 0; i < args.length; i++) {
            if ("--log-level".equals(args[i]) && i + 1 < args.length) {
                logLevel = args[i + 1].toUpperCase();
            } else if ("--log-file".equals(args[i]) && i + 1 < args.length) {
                logFile = args[i + 1];
            } else if ("--log-output".equals(args[i]) && i + 1 < args.length) {
                logOutput = args[i + 1].toLowerCase();
            }
        }

        System.setProperty("LOG_LEVEL", logLevel);
        System.setProperty("LOG_OUTPUT", logOutput);

        if (logFile != null && !logFile.isEmpty()) {
            System.setProperty("LOG_FILE", logFile);
        }

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.toLevel(logLevel, Level.INFO));
    }
}