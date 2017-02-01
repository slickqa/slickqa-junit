package com.slickqa.junit.annotations;

import com.slickqa.client.model.LogEntry;

/**
 * A logging interface for junit tests that want specific logs to go to slick.
 *
 * Rather than connect to a particular logger, this will be an interface for a logger to be used by
 * anything that wants to log to slick.  Note that you could add a bridge for other logger systems.
 */
public interface SlickLogger {
    enum LogLevel {
        TRACE(0),
        DEBUG(10),
        INFO(50),
        WARN(70),
        ERROR(100);

        private final int level;

        LogLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return this.level;
        }
    }

    /**
     * When creating a new logger (for a test), use this log level as the default minimum level.
     */
    LogLevel DEFAULT_MINIMUM_LOG_LEVEL = LogLevel.DEBUG;

    /**
     * Get the current minimum log level.
     * @return the current minimum log level.
     */
    LogLevel getMinimumLogLevel();

    /**
     * Set the new minimum log level for this logger.
     * @param newMiniumum the new minimum log level for this logger.
     */
    void setMinimumLogLevel(LogLevel newMiniumum);

    /**
     * Add a log entry to the list of log entries to upload to slick.
     * @param entry The log entry to add.
     */
    void addLogEntry(LogEntry entry);

    /**
     * See if a log level is enabled.
     * @param level The level to check.
     * @return True if level is at or above the minimum level, False otherwise.
     */
    boolean isLevelEnabled(LogLevel level);


    /**
     * Force a flush of any buffered logs.  If these are going to slick this will force an upload to slick.
     */
    void flushLogs();

    /**
     * Log a message at the specified level.
     *
     * @param level The level to log at.
     * @param msg The message to log.
     */
    void log(LogLevel level, String msg);

    /**
     * Log a message at the specified level.
     * Use java.text.MessageFormat to format the message with any arguments.
     *
     * @param level The level to log at.
     * @param format The formatted message to log.
     * @param arg An argument for the formatted message.
     */
    void log(LogLevel level, String format, Object arg);

    /**
     * Log a message at the specified level.
     * Use java.text.MessageFormat to format the message with any arguments.
     *
     * @param level The level to log at.
     * @param format The formatted message to log.
     * @param arg1 An argument for the formatted message.
     * @param arg2 Another argument for the formatted message.
     */
    void log(LogLevel level, String format, Object arg1, Object arg2);

    /**
     * Log a message at the specified level.
     * Use java.text.MessageFormat to format the message with any arguments.
     *
     * @param level The level to log at.
     * @param format The formatted message to log.
     * @param args Arguments for the formatted message.
     */
    void log(LogLevel level, String format, Object... args);

    /**
     * Log a message with a throwable (exception) at the specified level.
     * @param level The level to log at.
     * @param msg The message to log.
     * @param t The throwable with stack trace to add to the log entry.
     */
    void log(LogLevel level, String msg, Throwable t);

    /**
     * Is TRACE above the minimum log level?
     *
     * @return True if Trace is at or above the minimum log level, false otherwise.
     */
    boolean isTraceEnabled();

    /**
     * Log a message at the TRACE level.
     *
     * @param msg the message string to be logged
     */
    void trace(String msg);

    /**
     * Log a message at the TRACE level according to the specified format
     * and argument.
     *
     * @param format the format string
     * @param arg the argument
     */
    void trace(String format, Object arg);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param arg1 an argument for the formatted message
     * @param arg2 another argument for the formatted message
     */
    void trace(String format, Object arg1, Object arg2);

    /**
     * Log a message at the TRACE level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param args a list of 3 or more arguments for the formatted message
     */
    void trace(String format, Object... args);

    /**
     * Log an exception (throwable) at the TRACE level with an
     * accompanying message.
     *
     * @param msg the message
     * @param t the exception (throwable) to add to the LogEntry
     */
    void trace(String msg, Throwable t);

    /**
     * Is DEBUG above the minimum log level?
     *
     * @return True if Debug is at or above the minimum log level, false otherwise.
     */
    boolean isDebugEnabled();

    /**
     * Log a message at the DEBUG level.
     *
     * @param msg the message string to be logged
     */
    void debug(String msg);

    /**
     * Log a message at the DEBUG level according to the specified format
     * and argument.
     *
     * @param format the format string
     * @param arg the argument
     */
    void debug(String format, Object arg);

    /**
     * Log a message at the DEBUG level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param arg1 an argument for the formatted message
     * @param arg2 another argument for the formatted message
     */
    void debug(String format, Object arg1, Object arg2);

    /**
     * Log a message at the DEBUG level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param args a list of 3 or more arguments for the formatted message
     */
    void debug(String format, Object... args);

    /**
     * Log an exception (throwable) at the DEBUG level with an
     * accompanying message.
     *
     * @param msg the message
     * @param t the exception (throwable) to add to the LogEntry
     */
    void debug(String msg, Throwable t);

    /**
     * Is INFO above the minimum log level?
     *
     * @return True if Info is at or above the minimum log level, false otherwise.
     */
    boolean isInfoEnabled();

    /**
     * Log a message at the INFO level.
     *
     * @param msg the message string to be logged
     */
    void info(String msg);

    /**
     * Log a message at the INFO level according to the specified format
     * and argument.
     *
     * @param format the format string
     * @param arg the argument
     */
    void info(String format, Object arg);

    /**
     * Log a message at the INFO level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param arg1 an argument for the formatted message
     * @param arg2 another argument for the formatted message
     */
    void info(String format, Object arg1, Object arg2);

    /**
     * Log a message at the INFO level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param args a list of 3 or more arguments for the formatted message
     */
    void info(String format, Object... args);

    /**
     * Log an exception (throwable) at the INFO level with an
     * accompanying message.
     *
     * @param msg the message
     * @param t the exception (throwable) to add to the LogEntry
     */
    void info(String msg, Throwable t);

    /**
     * Is WARN above the minimum log level?
     *
     * @return True if Warn is at or above the minimum log level, false otherwise.
     */
    boolean isWarnEnabled();

    /**
     * Log a message at the WARN level.
     *
     * @param msg the message string to be logged
     */
    void warn(String msg);

    /**
     * Log a message at the WARN level according to the specified format
     * and argument.
     *
     * @param format the format string
     * @param arg the argument
     */
    void warn(String format, Object arg);

    /**
     * Log a message at the WARN level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param arg1 an argument for the formatted message
     * @param arg2 another argument for the formatted message
     */
    void warn(String format, Object arg1, Object arg2);

    /**
     * Log a message at the WARN level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param args a list of 3 or more arguments for the formatted message
     */
    void warn(String format, Object... args);

    /**
     * Log an exception (throwable) at the WARN level with an
     * accompanying message.
     *
     * @param msg the message
     * @param t the exception (throwable) to add to the LogEntry
     */
    void warn(String msg, Throwable t);

    /**
     * Is ERROR above the minimum log level?
     *
     * @return True if Error is at or above the minimum log level, false otherwise.
     */
    boolean isErrorEnabled();

    /**
     * Log a message at the ERROR level.
     *
     * @param msg the message string to be logged
     */
    void error(String msg);

    /**
     * Log a message at the ERROR level according to the specified format
     * and argument.
     *
     * @param format the format string
     * @param arg the argument
     */
    void error(String format, Object arg);

    /**
     * Log a message at the ERROR level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param arg1 an argument for the formatted message
     * @param arg2 another argument for the formatted message
     */
    void error(String format, Object arg1, Object arg2);

    /**
     * Log a message at the ERROR level according to the specified format
     * and arguments.
     *
     * @param format the format string
     * @param args a list of 3 or more arguments for the formatted message
     */
    void error(String format, Object... args);

    /**
     * Log an exception (throwable) at the ERROR level with an
     * accompanying message.
     *
     * @param msg the message
     * @param t the exception (throwable) to add to the LogEntry
     */
    void error(String msg, Throwable t);

}
