/*
 * Copyright 2012 Monits
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.monits.blackberry.commons.logger;

import java.util.Date;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.EventLogger;

import com.monits.blackberry.commons.logger.appender.Appender;
import com.monits.blackberry.commons.utils.StringUtils;

/*
 * Logger.java
 *
 */
public class Logger {

	public static final int LEVEL_DEBUG			= EventLogger.DEBUG_INFO;
	public static final int LEVEL_INFO			= EventLogger.INFORMATION;
	public static final int LEVEL_WARNING		= EventLogger.WARNING;
	public static final int LEVEL_ERROR			= EventLogger.ERROR;
	public static final int LEVEL_SEVERE_ERROR	= EventLogger.SEVERE_ERROR;

	public static final String LOG_PREFIX_DEBUG		= "[D]";
	public static final String LOG_PREFIX_INFO		= "[I]";
	public static final String LOG_PREFIX_WARN		= "[W]";
	public static final String LOG_PREFIX_ERROR		= "[E]";
	public static final String LOG_PREFIX_SEVERE	= "[S]";
	public static final String LOG_DESC_DEBUG		= "[DEBUG]";
	public static final String LOG_DESC_INFO		= "[INFO]";
	public static final String LOG_DESC_WARN		= "[WARNING]";
	public static final String LOG_DESC_ERROR		= "[ERROR]";
	public static final String LOG_DESC_SEVERE		= "[SEVERE]";

	/**
	 * Token that identifies long log level descriptions.
	 * e.g. [DEBUG], [INFO], etc.
	 */
	public static final String LOG_DESC_TOKEN	= "[LD]";//[DEBUG]
	/**
	 * Token that identifies short log level descriptions.
	 * e.g. [D], [I], etc.
	 */
	public static final String LOG_LEV_TOKEN	= "[SD]"; // [D]
	/**
	 * Token that identifies message section.
	 */
	public static final String MESSAGE_TOKEN	= "[MESSAGE]";
	/**
	 * Token that identifies date section.
	 */
	public static final String DATE_TOKEN		= "[DATE:";

	private static final String DEFAULT_FORMAT = "[LD],[DATE:[dd/MM/yyyy HH:mm:ss:SS]],[MESSAGE]";

	private static Vector appenders = new Vector();

	private String loggerName;
	private String format;

	/**
	 * Return a Logger instance.
	 * @param clazz Class to log.
	 * @param format Format of the log messages.
	 * <p><p> The format got 3 section: Date section, log level section and message section.
	 * <p><p> To define a format use {@link Logger#MESSAGE_TOKEN}, {@link Logger#DATE_TOKEN} and {@link Logger#LOG_LEV_TOKEN}.
	 * <p><p> The order of the parameters define the format of the log message.
	 * <p><p> For example:
	 * <p><p> "[SD],[DATE:dd/MM/yyyy],[MESSAGE]" will look like this "[D] [11/05/2012] I'm a message"
	 * <p><p> "[MESSAGE],[SD],[DATE:dd/MM/yyyy]" will look like this " I'm a message [D] [11/05/2012]"
	 * <p><p> "[LD],[DATE:dd/MM/yyyy],[MESSAGE]" will look like this "[DEBUG] [11/05/2012] I'm a message"
	 * <p><p> "[MESSAGE],[DATE:dd/MM/yyyy],[LD]" will look like this "I'm a message [11/05/2012] [DEBUG]"
	 * <p><p> And so on....
	 * <p><p> For the date format expression see {@link SimpleDateFormat}
	 */
	public static Logger getLogger(Class clazz, String format) {
		return new Logger(clazz, format);
	}

	/**
	 * <p> Return a Logger instance.
	 * <p><p> The format of the messages is the default 
	 * "[LD],[DATE:dd/MM/yyyy HH:mm:ss:SS],[MESSAGE]". 
	 * @param clazz Class to log.
	 */
	public static Logger getLogger(Class clazz) {
		return new Logger(clazz);
	}

	/**
	 * <p> Constructor. 
	 * <p><p> The format of the messages is default. 
	 * <p><p> [LD],[DATE:[dd/MM/yyyy HH:mm:ss:SS]],[MESSAGE]
	 * @param clazz Class to log.
	 */
	private Logger(Class clazz){
		this.loggerName = clazz.getName();
		this.format = DEFAULT_FORMAT;
	}


	/**
	 * Constructor.
	 * @param clazz Class to log.
	 * @param format Format of the log messages.
	 * <p><p> The format got 3 section: Date section, log level section and message section.
	 * <p><p> To define a format use {@link Logger#MESSAGE_TOKEN}, {@link Logger#DATE_TOKEN} and {@link Logger#LOG_LEV_TOKEN}.
	 * <p><p> The order of the parameters define the format of the log message.
	 * <p><p> For example:
	 * <p><p> "[SD],[DATE:dd/MM/yyyy],[MESSAGE]" will look like this "[D] [11/05/2012] I'm a message"
	 * <p><p> "[MESSAGE],[SD],[DATE:dd/MM/yyyy]" will look like this " I'm a message [D] [11/05/2012]"
	 * <p><p> "[LD],[DATE:dd/MM/yyyy],[MESSAGE]" will look like this "[DEBUG] [11/05/2012] I'm a message"
	 * <p><p> "[MESSAGE],[DATE:dd/MM/yyyy],[LD]" will look like this "I'm a message [11/05/2012] [DEBUG]"
	 * <p><p> And so on....
	 * <p><p> For the date format expression see {@link SimpleDateFormat}
	 */
	private Logger(Class clazz, String format){
		this.loggerName = clazz.getName();
		this.format = format;
	}

	/**
	 * Log the message with DEBUG level.
	 * @param eventData Message to log.
	 */
	public void debug(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_DEBUG, format, eventData, null);
	}

	/**
	 * Log the message and the stack trace of the Throwable with DEBUG level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void debug(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_DEBUG, format, eventData, t);
	}

	/**
	 * Log the message with INFO level.
	 * @param eventData Message to log.
	 */
	public void info(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_INFO, format, eventData, null);
	}

	/**
	 * Log the message and the stack trace of the Throwable with INFO level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void info(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_INFO, format, eventData, t);
	}

	/**
	 * Log the message with WARNING level.
	 * @param eventData Message to log.
	 */
	public void warn(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_WARNING, format, eventData, null);
	}

	/**
	 * Log the message and the stack trace of the Throwable with WARNING level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void warn(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_WARNING, format, eventData, t);
	}

	/**
	 * Log the message with ERROR level.
	 * @param eventData Message to log.
	 */
	public void error(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_ERROR, format, eventData, null);
	}

	/**
	 * Log the message and the stack trace of the Throwable with ERROR level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void error(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_ERROR, format, eventData, t);
	}

	/**
	 * Log the message with SEVERE level.
	 * @param eventData Message to log.
	 */
	public void severe(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_SEVERE_ERROR, format, eventData, null);
	}

	/**
	 * Log the message and the stack trace of the Throwable with SEVERE level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void severe(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_SEVERE_ERROR, format, eventData, t);
	}

	/**
	 * Log an event and its Throwable, with the given level.
	 * @param loggerName Logger instance name.
	 * @param level Log level.
	 * @param format See format in {@link Logger#Logger(Class, String)}
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	private static void logEvent(String loggerName, int level, String format, String eventData, Throwable t) {
		String formatedMessage = applyFormat(format, level, eventData);

		// Call all the appenders to log the event.
		for (int i = 0; i < appenders.size(); i++) {
			Appender appender = (Appender) appenders.elementAt(i);
			appender.logEvent(loggerName, level, formatedMessage, t);
		}
	}

	/**
	 * Return the String representation of the log level.
	 * @param level Log level.
	 * @param verbose True if the desired description is the long one, otherwise false.
	 * @return the String representation of the log level.
	 */
	private static String getLogLevel(int level, boolean verbose) {
		String logLevel = null;
		switch (level) {
			case LEVEL_DEBUG:
				logLevel = verbose ? LOG_DESC_DEBUG : LOG_PREFIX_DEBUG;
				break;
			case LEVEL_INFO:
				logLevel = verbose ? LOG_DESC_INFO : LOG_PREFIX_INFO;
				break;
			case LEVEL_WARNING:
				logLevel = verbose ? LOG_DESC_WARN : LOG_PREFIX_WARN;
				break;
			case LEVEL_ERROR:
				logLevel = verbose ? LOG_DESC_ERROR : LOG_PREFIX_ERROR;
				break;
			case LEVEL_SEVERE_ERROR:
				logLevel = verbose ? LOG_DESC_SEVERE : LOG_PREFIX_SEVERE;
				break;
		}
		return logLevel;
	}

	/**
	 * Apply the format to the given message.
	 * @param format See format in {@link Logger#Logger(Class, String)}
	 * @param level Log level.
	 * @param message Message to format.
	 * @return Formatted message.
	 */
	private static String applyFormat(String format, int level, String message) {
		StringUtils su = new StringUtils();
		Vector formatArgs = su.split(format, ',');

		String formatedMessage = "";
		for(int i = 0; i < formatArgs.size(); i++) {
			String arg = (String) formatArgs.elementAt(i);
			if(arg.equals(Logger.LOG_LEV_TOKEN)) {
				formatedMessage += getLogLevel(level, false) + " ";

			} else if(arg.equals(Logger.LOG_DESC_TOKEN)) {
				formatedMessage += getLogLevel(level, true) + " ";

			} else if(arg.equals(Logger.MESSAGE_TOKEN)) {
				formatedMessage += message + " ";

			} else if(arg.startsWith(Logger.DATE_TOKEN)) {
				String dateFormat = arg.substring(Logger.DATE_TOKEN.length(), arg.length() - 1);
				Date date = new Date();
				if (!dateFormat.equals("")) {
					SimpleDateFormat dateFormater = new SimpleDateFormat(dateFormat);
					formatedMessage += dateFormater.format(date) + " ";
				} else {
					// If the pattern is empty apply the default date style.
					SimpleDateFormat dateFormater = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss:SS]");
					formatedMessage += dateFormater.format(date) + " ";
				}
			} else {
				formatedMessage += arg + " ";
			}
		}

		return formatedMessage;
	}

	/**
	 * Add a new appender.
	 * @param newAppender New appender.
	 */
	public static void addAppender(Appender newAppender) {
		appenders.addElement(newAppender);
	}

	/**
	 * Return the logger name.
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * @return The logger format.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Set the format of the logger.
	 * @param format See format in {@link Logger#Logger(Class, String)}
	 */
	public void setFormat(String format) {
		this.format = format;
	}
}