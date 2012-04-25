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

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.system.EventLogger;

/**
 * FileAppender log events to a file in the SD card.
 * @author Rodrigo Pereyra
 *
 */
public class FileAppender implements Appender {
	private static final String SD_CARD = "file:///SDCard/";

	private String filename;
	private int minimumLogLevel = EventLogger.DEBUG_INFO;

	/**
	 * Constructor.
	 * @param filename Name of the log file. If it doesn't exist FileAppender create it with the given name.
	 */
	public FileAppender(String filename) {
		this.filename = filename;
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.logger.Appender#logEvent(int, java.lang.String, java.lang.Throwable)
	 */
	public void logEvent(String logPrefix, int logLevel, String message, Throwable t) {
		try {
			FileConnection fc = (FileConnection) Connector.open(SD_CARD + filename, Connector.READ_WRITE);

			// The file may or may not exist.
			if (!fc.exists()) {
				fc.create(); // create the file if it doesn't exist
			}

			// Open stream moving cursor to the end of file (AKA append)
			OutputStream os = fc.openOutputStream(fc.openDataInputStream().available());

			if (t != null) {
				os.write((logPrefix + message + "\n" + t.toString()).getBytes());
			} else {
				os.write((logPrefix + message).getBytes());
			}

			os.close();
			fc.close();
		} catch (IOException ioe) {
			// Ignore it, we can't log it!
		}
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.logger.Appender#getMinimumLogLevel()
	 */
	public int getMinimumLogLevel() {
		return minimumLogLevel;
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.logger.Appender#setMinimumLogLevel(int)
	 */
	public void setMinimumLogLevel(int minimumLogLevel) {
		this.minimumLogLevel = minimumLogLevel;
	}
}