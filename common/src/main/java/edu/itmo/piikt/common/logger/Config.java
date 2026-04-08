package edu.itmo.piikt.common.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import java.nio.charset.StandardCharsets;
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
		if (logFile != null && !logFile.isEmpty()) {
			System.setProperty("LOG_FILE", logFile);
		}
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
		rootLogger.detachAndStopAllAppenders();
		String pattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [id=%X{id}] %logger{36} - %msg%n";
		if (logOutput.contains("console") || logOutput.contains("both")) {
			ConsoleAppender<ILoggingEvent> console = new ConsoleAppender<>();
			console.setContext(loggerContext);
			PatternLayoutEncoder consoleEncoder = new PatternLayoutEncoder();
			consoleEncoder.setPattern(pattern);
			consoleEncoder.setCharset(StandardCharsets.UTF_8);
			consoleEncoder.setContext(loggerContext);
			consoleEncoder.start();
			console.setEncoder(consoleEncoder);
			console.start();
			rootLogger.addAppender(console);
		}
		if (logOutput.contains("file") || logOutput.contains("both")) {
			String logFilePath = System.getProperty("LOG_FILE", "logs/server.log");
			RollingFileAppender<ILoggingEvent> file = new RollingFileAppender<>();
			file.setContext(loggerContext);
			file.setFile(logFilePath);
			TimeBasedRollingPolicy<ILoggingEvent> rolling = new TimeBasedRollingPolicy<>();
			rolling.setFileNamePattern(logFilePath + ".%d{yyyy-MM-dd}.gz");
			rolling.setMaxHistory(30);
			rolling.setContext(loggerContext);
			rolling.setParent(file);
			rolling.start();
			PatternLayoutEncoder fileEncoder = new PatternLayoutEncoder();
			fileEncoder.setPattern(pattern);
			fileEncoder.setCharset(StandardCharsets.UTF_8);
			fileEncoder.setContext(loggerContext);
			fileEncoder.start();
			file.setEncoder(fileEncoder);
			file.setRollingPolicy(rolling);
			file.start();
			rootLogger.addAppender(file);
			RollingFileAppender<ILoggingEvent> errorFile = new RollingFileAppender<>();
			errorFile.setContext(loggerContext);
			errorFile.setFile(logFilePath + ".error");
			TimeBasedRollingPolicy<ILoggingEvent> errorRolling = new TimeBasedRollingPolicy<>();
			errorRolling.setFileNamePattern(logFilePath + ".error.%d{yyyy-MM-dd}.gz");
			errorRolling.setMaxHistory(30);
			errorRolling.setContext(loggerContext);
			errorRolling.setParent(errorFile);
			errorRolling.start();
			PatternLayoutEncoder errorEncoder = new PatternLayoutEncoder();
			errorEncoder.setPattern(pattern);
			errorEncoder.setCharset(StandardCharsets.UTF_8);
			errorEncoder.setContext(loggerContext);
			errorEncoder.start();
			errorFile.setEncoder(errorEncoder);
			errorFile.setRollingPolicy(errorRolling);
			errorFile.start();
			rootLogger.addAppender(errorFile);
		}
		rootLogger.setLevel(Level.toLevel(logLevel, Level.INFO));
	}
}
