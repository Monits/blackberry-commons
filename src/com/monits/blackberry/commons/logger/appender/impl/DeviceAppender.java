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
package com.monits.blackberry.commons.logger.appender.impl;

import net.rim.device.api.synchronization.UIDGenerator;
import net.rim.device.api.system.EventLogger;

import com.monits.blackberry.commons.logger.appender.Appender;

/**
 * This appender log events in the Device logger.
 * @author Rodrigo Pereyra.
 *
 */
public class DeviceAppender implements Appender{
	private String clazzToLog;

	private int minimumLogLevel = EventLogger.getMinimumLevel();
	private long guid;

	/**
	 * Constructor.
	 * @param clazzToLog Name of the class / package to log.
	 */
	public DeviceAppender(String clazzToLog) {
		this.clazzToLog = clazzToLog;

		int scopingValue = UIDGenerator.getUniqueScopingValue();
		guid = UIDGenerator.makeLUID(scopingValue, UIDGenerator.getUID(scopingValue));
		EventLogger.register(guid, clazzToLog,EventLogger.VIEWER_STRING);
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.logger.appender.Appender#logEvent(java.lang.String, int, java.lang.String, java.lang.Throwable)
	 */
	public void logEvent(String loggerName, int logLevel, String formatedMessage, Throwable t) {
		if (minimumLogLevel >= logLevel && loggerName.startsWith(clazzToLog)) {
			if (t == null) {
				EventLogger.logEvent(guid, formatedMessage.getBytes(), logLevel);
				return;
			}
			EventLogger.logEvent(guid, formatedMessage.getBytes(), logLevel);
			EventLogger.logEvent(guid, t.toString().getBytes(), logLevel);
		}
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.logger.appender.Appender#getMinimumLogLevel()
	 */
	public int getMinimumLogLevel() {
		return minimumLogLevel;
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.logger.appender.Appender#setMinimumLogLevel(int)
	 */
	public void setMinimumLogLevel(int logLevel) {
		this.minimumLogLevel = logLevel;
		EventLogger.setMinimumLevel(logLevel);
	}
}