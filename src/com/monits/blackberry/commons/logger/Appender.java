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

/**
 * This interface must be implemented by classes that want to handle outputting events.
 * @author Rodrigo Pereyra.
 *
 */
public interface Appender {

	/**
	 * Log the event message and the info provided by the {@link Throwable} object.
	 * @param logPrefix logPrefix String representation of log level.
	 * @param logLevel Log level.
	 * @param message Proper event info
	 * @param t Exception to log, including it stack trace.
	 */
	public void logEvent(String logPrefix, int logLevel, String message, Throwable t);

	/**
	 * @return the minimumLogLevel
	 */
	public int getMinimumLogLevel();

	/**
	 * Set the minimum log level for this appender.
	 * @param minimumLogLevel the minimumLogLevel.
	 */
	public void setMinimumLogLevel(int logLevel);
}
