package com.monits.blackberry.commons.services;

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
