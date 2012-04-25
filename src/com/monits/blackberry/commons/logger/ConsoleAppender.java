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

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EventLogger;

/**
 * ConsoleAppender appends log events to System.out.
 * @author Rodrigo Pereyra.
 *
 */
public class ConsoleAppender implements Appender {
	private int minimumLogLevel = EventLogger.DEBUG_INFO;

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.logger.Appender#logEvent(int, java.lang.String, java.lang.Throwable)
	 */
	public void logEvent(String logPrefix, int logLevel, String message, Throwable t) {
		if (DeviceInfo.isSimulator() && (minimumLogLevel >= logLevel)) {
			if (t == null) {
				System.out.println(logPrefix + message);
				return;
			}
			System.out.println(logPrefix + message + ": \n" + t.toString());
		}
	}

	public int getMinimumLogLevel() {
		return minimumLogLevel;
	}

	public void setMinimumLogLevel(int minimumLogLevel) {
		this.minimumLogLevel = minimumLogLevel;
	}
}