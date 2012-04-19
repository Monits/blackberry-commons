package com.monits.blackberry.commons;

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EventLogger;

/*
 * Logger.java
 *
 */

public class Logger {

	public static int LEVEL_INFO = EventLogger.INFORMATION;
	public static int LEVEL_WARNING = EventLogger.WARNING;
	public static int LEVEL_ERROR = EventLogger.ERROR;
	public static int LEVEL_SEVERE_ERROR = EventLogger.SEVERE_ERROR;

	public static String LOG_PREFIX_INFO = "I: ";
	public static String LOG_PREFIX_WARN = "W: ";
	public static String LOG_PREFIX_ERROR = "E: ";
	public static String LOG_PREFIX_SEVERE = "S: ";

	public static boolean DEBUG = true;

	// ----------------------------------------------
	// Shared code
	// ----------------------------------------------
	/**
	 * Debug.
	 * Not actually used in this project, here in case useful for someone else.
	 */
	public static void debug(String s) {
		// output debug for JDE debugging
		if ( DEBUG ) {
			System.out.println(s);
		}
	}


	// -----------------------------------------------------------------------------
	// Quick Access to Logging.

	public static void logEventInfo(String eventData) {
		Logger.logEvent(true, Logger.LEVEL_INFO, Logger.LOG_PREFIX_INFO + eventData);
	}

	public static void logEventWarn(String eventData) {
		Logger.logEvent(true, Logger.LEVEL_WARNING, Logger.LOG_PREFIX_WARN + eventData);
	}

	public static void logEventError(String eventData) {
		Logger.logEvent(true, Logger.LEVEL_ERROR, Logger.LOG_PREFIX_ERROR + eventData);
	}

	public static void logEventSevere(String eventData) {
		Logger.logEvent(true, Logger.LEVEL_SEVERE_ERROR, Logger.LOG_PREFIX_SEVERE + eventData);
	}

	/**
	 * Log Event.
	 */
	public static void logEvent(boolean logging, int level, String eventData) {
		if ( DeviceInfo.isSimulator() ) {
			System.out.println(eventData);
		}
	}


} 
