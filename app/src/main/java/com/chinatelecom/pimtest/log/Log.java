package com.chinatelecom.pimtest.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author owensun
 * @since 04/09/2014
 */
public class Log {

    public int level = 2;

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    private Logger logger;

    private Log(Class cls) {
        this.logger = LoggerFactory.getLogger(cls.getSimpleName());
    }

    public static Log build(Class cls) {
        return new Log(cls);
    }

    public void exception(Throwable throwable) {
        if (DEBUG >= level) {
            throwable.printStackTrace();
        }
    }

    public void trace(String format, Object... params) {
        if (VERBOSE >= level) {
            logger.trace(String.format(format, params));
        }
    }

    public void debug(String message) {
        if (DEBUG >= level) {
            logger.debug(message);
        }
    }

    public void debug(String format, Object... params) {
        if (DEBUG >= level) {
            logger.debug(String.format(format, params));
        }
    }

    public void info(String format, Object... params) {
        if (INFO >= level) {
            logger.info(String.format(format, params));
        }
    }

    public void warn(String format, Object... params) {
        if (WARN >= level) {
            logger.warn(String.format(format, params));
        }
    }

    public void error(String format, Object... params) {
        if (ERROR >= level) {
            logger.error(String.format(format, params));
        }
    }

    public Log setLevel(int level) {
        this.level = level;
        return this;
    }
}
