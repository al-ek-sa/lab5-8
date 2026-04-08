package edu.itmo.piikt.common.logger;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger {
    private final Logger logger;
    private final String className;

    public AppLogger(Class<?> name) {
        this.className = name.getSimpleName();
        this.logger = (Logger) LoggerFactory.getLogger(name);
    }

    public void info(String message) {
        if (logger.isInfoEnabled()) {
            logger.info("[{}] {}", className, message);
        }
    }

    public void info(String message, Object... argument) {
        if (logger.isInfoEnabled()) {
            logger.info("[{}] {}", className, format(message, argument));
        }
    }

    public void error(String message) {
        if (logger.isErrorEnabled()) {
            logger.error("[{}] {}", className, message);
        }
    }

    public void error(String message, Object... argument) {
        if (logger.isErrorEnabled()) {
            logger.error("[{}] {}", className, format(message, argument));
        }
    }

    public void error(String message, Throwable throwable) {
        if (logger.isErrorEnabled()) {
            logger.error("[{}] {}", className, message, throwable);
        }
    }

    public void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug("[{}] {}", className, message);
        }
    }

    public void debug(String message, Object... argument) {
        if (logger.isDebugEnabled()) {
            logger.debug("[{}] {}", className, format(message, argument));
        }
    }

    public void warn(String message) {
        if (logger.isWarnEnabled()) {
            logger.warn("[{}] {}", className, message);
        }
    }

    public void warn(String message, Object... argument) {
        if (logger.isWarnEnabled()) {
            logger.warn("[{}] {}", className, format(message, argument));
        }
    }

    private String format(String message, Object... argument) {
        if (argument == null || argument.length == 0) {
            return message;
        }
        String pattern = message.replace("{}", "%s");
        return String.format(pattern, argument);
    }
}