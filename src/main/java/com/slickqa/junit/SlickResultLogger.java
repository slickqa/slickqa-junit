package com.slickqa.junit;

import com.slickqa.client.errors.SlickError;
import com.slickqa.client.model.LogEntry;
import com.slickqa.client.model.Result;
import com.slickqa.junit.annotations.SlickLogger;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Implementation of SlickLogger interface.
 */
public class SlickResultLogger implements SlickLogger {
    public static int BUFFER_SIZE = 10;
    public static int MAX_SECONDS_SINCE_FIRST_ENTRY = 5;
    public static final String LOGGER_NAME = "testcase";
    protected LogLevel minimumLevel;
    protected SlickResultRule slick;
    protected ArrayList<LogEntry> buffer;


    public SlickResultLogger(SlickResultRule slick) {
        this.slick = slick;
        this.minimumLevel = SlickLogger.DEFAULT_MINIMUM_LOG_LEVEL;
        buffer = new ArrayList<>(BUFFER_SIZE);
    }

    protected void uploadLogsIfNecessary() {
        if (slick.isUsingSlick()) {
            if (buffer.size() >= BUFFER_SIZE || (buffer.size() > 0 && (((new Date()).getTime() - buffer.get(0).getEntryTime().getTime()) / 1000) >= MAX_SECONDS_SINCE_FIRST_ENTRY)) {
                flushLogs();
            }
        }
    }

    @Override
    public void flushLogs() {
        if (slick.isUsingSlick() && buffer.size() > 0) {
            Result current = slick.getCurrentResult();
            if (current != null) {
                try {
                    slick.getSlickClient().result(current.getId()).addLogs(buffer);
                } catch (SlickError e) {
                    e.printStackTrace();
                    System.err.println("!! ERROR: Unable to post logs to slick !!");
                } finally {
                    buffer = new ArrayList<>(BUFFER_SIZE);
                }
            }
        }
    }

    @Override
    public LogLevel getMinimumLogLevel() {
        return minimumLevel;
    }

    @Override
    public void setMinimumLogLevel(LogLevel newMiniumum) {
        this.minimumLevel = newMiniumum;
    }

    @Override
    public void addLogEntry(LogEntry entry) {
        if (slick.isUsingSlick()) {
            buffer.add(entry);
            uploadLogsIfNecessary();
        }
        // TODO: print message if configured
    }

    @Override
    public boolean isLevelEnabled(LogLevel level) {
        return level.getLevel() >= getMinimumLogLevel().getLevel();
    }

    private LogEntry getLogEntryFor(LogLevel level, String msg) {
        LogEntry entry = new LogEntry();
        entry.setEntryTime(new Date());
        entry.setLevel(level.toString());
        entry.setLoggerName(LOGGER_NAME);
        entry.setMessage(msg);
        return entry;
    }

    private String getFormattedMessage(String message, Object[] arguments) {
        return MessageFormat.format(message, arguments);

    }

    @Override
    public void log(LogLevel level, String msg) {
        if (isLevelEnabled(level)) {
            addLogEntry(getLogEntryFor(level, msg));
        }
    }

    @Override
    public void log(LogLevel level, String format, Object arg) {
        if (isLevelEnabled(level)) {
            addLogEntry(getLogEntryFor(level, getFormattedMessage(format, new Object[]{arg})));
        }
    }

    @Override
    public void log(LogLevel level, String format, Object arg1, Object arg2) {
        if (isLevelEnabled(level)) {
            addLogEntry(getLogEntryFor(level, getFormattedMessage(format, new Object[]{arg1, arg2})));
        }
    }

    @Override
    public void log(LogLevel level, String format, Object... args) {
        if (isLevelEnabled(level)) {
            addLogEntry(getLogEntryFor(level, getFormattedMessage(format, args)));
        }
    }

    @Override
    public void log(LogLevel level, String msg, Throwable t) {
        if (isLevelEnabled(level)) {
            LogEntry entry = getLogEntryFor(level, msg);
            entry.setExceptionClassName(t.getClass().getName());
            entry.setExceptionMessage(t.getMessage());
            StackTraceElement[] elements = t.getStackTrace();
            ArrayList<String> stackTrace = new ArrayList<>(elements.length);
            for (StackTraceElement element : elements) {
                stackTrace.add(element.toString());
            }

            entry.setExceptionStackTrace(stackTrace);
            addLogEntry(entry);
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return isLevelEnabled(LogLevel.TRACE);
    }

    @Override
    public void trace(String msg) {
        log(LogLevel.TRACE, msg);

    }

    @Override
    public void trace(String format, Object arg) {
        log(LogLevel.TRACE, format, arg);

    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        log(LogLevel.TRACE, format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... args) {
        log(LogLevel.TRACE, format, args);
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(LogLevel.TRACE, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(LogLevel.DEBUG);
    }

    @Override
    public void debug(String msg) {
        log(LogLevel.DEBUG, msg);

    }

    @Override
    public void debug(String format, Object arg) {
        log(LogLevel.DEBUG, format, arg);

    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        log(LogLevel.DEBUG, format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... args) {
        log(LogLevel.DEBUG, format, args);
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(LogLevel.DEBUG, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(LogLevel.INFO);
    }

    @Override
    public void info(String msg) {
        log(LogLevel.INFO, msg);

    }

    @Override
    public void info(String format, Object arg) {
        log(LogLevel.INFO, format, arg);

    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        log(LogLevel.INFO, format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... args) {
        log(LogLevel.INFO, format, args);
    }

    @Override
    public void info(String msg, Throwable t) {
        log(LogLevel.INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(LogLevel.WARN);
    }

    @Override
    public void warn(String msg) {
        log(LogLevel.WARN, msg);

    }

    @Override
    public void warn(String format, Object arg) {
        log(LogLevel.WARN, format, arg);

    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        log(LogLevel.WARN, format, arg1, arg2);
    }

    @Override
    public void warn(String format, Object... args) {
        log(LogLevel.WARN, format, args);
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(LogLevel.WARN, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(LogLevel.ERROR);
    }

    @Override
    public void error(String msg) {
        log(LogLevel.ERROR, msg);

    }

    @Override
    public void error(String format, Object arg) {
        log(LogLevel.ERROR, format, arg);

    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        log(LogLevel.ERROR, format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... args) {
        log(LogLevel.ERROR, format, args);
    }

    @Override
    public void error(String msg, Throwable t) {
        log(LogLevel.ERROR, msg, t);
    }

}

