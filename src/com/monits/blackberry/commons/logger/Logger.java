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

import java.util.Vector;

import com.monits.blackberry.commons.logger.appender.Appender;

import net.rim.device.api.system.EventLogger;

/*
 * Logger.java
 *
 */
public class Logger {

	public static final int LEVEL_DEBUG = EventLogger.DEBUG_INFO;
	public static final int LEVEL_INFO = EventLogger.INFORMATION;
	public static final int LEVEL_WARNING = EventLogger.WARNING;
	public static final int LEVEL_ERROR = EventLogger.ERROR;
	public static final int LEVEL_SEVERE_ERROR = EventLogger.SEVERE_ERROR;

	public static final String LOG_PREFIX_DEBUG = "D: ";
	public static final String LOG_PREFIX_INFO = "I: ";
	public static final String LOG_PREFIX_WARN = "W: ";
	public static final String LOG_PREFIX_ERROR = "E: ";
	public static final String LOG_PREFIX_SEVERE = "S: ";

	private static Vector appenders = new Vector();

	private String loggerName;

	/**
	 * Return a Logger instance.
	 * @param clazz Class to log.
	 * @return New Logger instance.
	 */
	public static Logger getLogger(Class clazz){
		return new Logger(clazz);
	}

	/**
	 * Constructor.
	 * @param clazz Class to log.
	 */
	private Logger(Class clazz){
		this.loggerName = clazz.getName();
	}

	/**
	 * Log the message with DEBUG level.
	 * @param eventData Message to log.
	 */
	public void debug(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_DEBUG, Logger.LOG_PREFIX_DEBUG + eventData, null);
	}

	/**
	 * Log the message and the stack trace of the Throwable with DEBUG level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void debug(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_DEBUG, Logger.LOG_PREFIX_DEBUG + eventData, t);
	}

	/**
	 * Log the message with INFO level.
	 * @param eventData Message to log.
	 */
	public void info(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_INFO, Logger.LOG_PREFIX_INFO + eventData, null);
	}

	/**
	 * Log the message and the stack trace of the Throwable with INFO level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void info(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_INFO, Logger.LOG_PREFIX_INFO + eventData, t);
	}

	/**
	 * Log the message with WARNING level.
	 * @param eventData Message to log.
	 */
	public void warn(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_WARNING, Logger.LOG_PREFIX_WARN + eventData, null);
	}


	/**
	 * Log the message and the stack trace of the Throwable with WARNING level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void warn(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_WARNING, Logger.LOG_PREFIX_WARN + eventData, t);
	}

	/**
	 * Log the message with ERROR level.
	 * @param eventData Message to log.
	 */
	public void error(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_ERROR, Logger.LOG_PREFIX_ERROR + eventData, null);
	}

	/**
	 * Log the message and the stack trace of the Throwable with ERROR level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void error(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_ERROR, Logger.LOG_PREFIX_ERROR + eventData, t);
	}

	/**
	 * Log the message with SEVERE level.
	 * @param eventData Message to log.
	 */
	public void severe(String eventData) {
		Logger.logEvent(loggerName, Logger.LEVEL_SEVERE_ERROR, Logger.LOG_PREFIX_SEVERE + eventData, null);
	}

	/**
	 * Log the message and the stack trace of the Throwable with SEVERE level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	public void severe(String eventData, Throwable t) {
		Logger.logEvent(loggerName, Logger.LEVEL_SEVERE_ERROR, Logger.LOG_PREFIX_SEVERE + eventData, t);
	}

	/**
	 * Log an event and its Throwable, with the given level.
	 * @param loggerName Logger instance name.
	 * @param level Log level.
	 * @param eventData Message to log.
	 * @param t The exception to log, including its stack trace.
	 */
	private static void logEvent(String loggerName, int level, String eventData, Throwable t) {
		String prefix = null;
		switch (level) {
			case LEVEL_DEBUG:
				prefix = LOG_PREFIX_DEBUG;
				break;
			case LEVEL_INFO:
				prefix = LOG_PREFIX_INFO;
				break;
			case LEVEL_WARNING:
				prefix = LOG_PREFIX_WARN;
				break;
			case LEVEL_ERROR:
				prefix = LOG_PREFIX_ERROR;
				break;
			case LEVEL_SEVERE_ERROR:
				prefix = LOG_PREFIX_SEVERE;
				break;
		}

		// Call all the appender to log the event.
		for (int i = 0; i < appenders.size(); i++) {
			Appender appender = (Appender) appenders.elementAt(i);
			appender.logEvent(loggerName, prefix, level, eventData, t);
		}
	}

	/**
	 * Add a new appender.
	 * @param newAppender New appender.
	 */
	public static void addAppender(Appender newAppender) {
		appenders.addElement(newAppender);
	}

	/**
	 * @return the loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}
}