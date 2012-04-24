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
package com.monits.blackberry.commons.service;

import javax.microedition.location.Location;

import com.monits.blackberry.commons.location.LocationListener;

/**
 * Provides support for geolocation.
 * @author Rodrigo Pereyra
 */
public interface LocationService {

	/**
	 * Start searching for the device's location
	 */
	void start();

	/**
	 * Stop fixing the location.
	 */
	void stop();

	/**
	 * Check if the Location Service already start the search.
	 * @return true if the service already start working, otherwise false.
	 */
	boolean isStarted();

	/**
	 * Retrieves the last known location.
	 * @return
	 */
	Location getCurrentLocation();

	/**
	 * Add a Location Listener
	 * @param listener New location listener
	 */
	void addLocationListener(LocationListener listener);

	/**
	 * Remove the given location listener
	 * @param listener Listener to remove
	 */
	void removeLocationListener(LocationListener listener);
}
