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
package com.monits.blackberry.commons.service.impl;

import java.util.Vector;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationProvider;

import com.monits.blackberry.commons.Logger;
import com.monits.blackberry.commons.location.LocationListener;
import com.monits.blackberry.commons.service.LocationService;
/**
 * Implementation for {@link LocationService}
 * @author Rodrigo Pereyra
 */
public class LocationServiceImpl implements LocationService {
	/**
	 * Will retrieve a default implementation.
	 */
	public static final int GPS_MODE_ANY = 0;
	/**
	 * Computes a fix using only satellites
	 * <p> - Requires a clear view of the sky.
	 * <p><p> - Relatively higher Average Time To First Fix (TTFF).
	 * <p><p> - No data connectivity required.
	 * <p><p> - Recommended for applications that require frequent fixes.
	 */
	public static final int GPS_MODE_STANDALONE = 1;
	/**
	 * Use a Position Determining Entity (PDE) server hosted by the carrier,
	 * and the device periodically get a fix.
	 * <p> - Requires data connectivity.
	 * <p><p> - Work fine with a partial view of the sky,outdoors and indoors.
	 * <p><p> - Requires data connectivity.
	 * <p><p> - Average TTFF is faster than Standalone.
	 */
	public static final int GPS_MODE_ASSISTED = 2;
	/**
	 * Retrieves a fix based on the location of the nearest cell towers
	 * <p> - Extremely fast
	 * <p><p> - Accuracy is very low
	 * <p><p> - Recommended for applications where accuracy is not a concern
	 */
	public static final int GPS_MODE_CELLSITE = 3;
	/**
	 * Device is assisted by the PDE server in every fix
	 * <p> - Requires data connectivity
	 * <p><p> - Can operate indoors and outdoors without any difficulty
	 * <p><p> - Very fast Average Time To First Fix
	 * <p><p> - Operates anywhere with a network connection
	 * <p><p> - Not recommended for frequent fixes
	 * <p><p> - Should not be used more than once every 10-15 minutes
	 */
	public static final int GPS_MODE_MS_ASSISTED = 4;
	/**
	 * Use PDE server, hosted by the carrier, and periodically get a fix.
	 * <p> - Requires data connectivity
	 * <p><p> - Operates outdoors and indoors with a partial view of the sky
	 * <p><p> - Average Time To First Fix is faster than Standalone
	 * <p><p> - Recommended for applications that require frequent fixes
	 */
	public static final int GPS_MODE_MS_BASED = 5;
	/**
	 * Preferred mode: MS-Based
	 * <p> - Initial mode attempted: MS-Based or MS-Assisted
	 * <p><p> - Fallback mode: If MB-Based is attempted first, fallback is to MS-Assisted; 
	 * if MS-Assisted is attempted first, fallback is to MS-Based.
	 */
	public static final int GPS_MODE_SPEED_OPTIMAL = 6;
	/**
	 * Preferred mode: MS-Assisted
	 * <p> - Initial mode attempted: MS-Assisted
	 * <p><p> - Fallback mode: MS-Based, if MS-Assisted fails
	 */
	public static final int GPS_MODE_ACCURACY_OPTIMAL = 7;
	/**
	 * Preferred mode: MS-Based
	 * <p> - Initial mode attempted: MS-Based
	 * <p><p> - Fallback mode: MS-Assisted, if MS-Based fails
	 * <p><p> - Minimum PDE/network access is allowe
	 */
	public static final int GPS_MODE_DATA_OPTIMAL = 8;

	private static int GPS_MODE;

	private Vector listeners;
	private Location lastLocation;
	private boolean started;
	private LocationProvider locationProvider;

	/**
	 * Constructor. Use a default location provider.
	 */
	public LocationServiceImpl() {
		this(GPS_MODE_ANY);
	}

	/**
	 * Constructor.
	 * 
	 * @param gpsMode The desired GPS mode
	 */
	public LocationServiceImpl(int gpsMode) {
		this(new int[] { gpsMode });
	}

	/**
	 * Constructor. This method iterate the alternatives, 
	 * and use the first one that is available.
	 *
	 * @param gpsModeAlternatives Alternatives for GPS mode, in order of importance.
	 */
	public LocationServiceImpl(int[] gpsModeAlternatives){
		for(int i = 0; i < gpsModeAlternatives.length; i++) {
			LocationProvider lp;
			try {
				lp = LocationProvider.getInstance(setGpsMode((gpsModeAlternatives[i])));
				if(lp != null){
					GPS_MODE = gpsModeAlternatives[i];
					locationProvider = lp;
				}
			} catch (LocationException e) {
				Logger.error("Error trying to inititate the location service. Most likely unavailable.");
			}
		}

		listeners = new Vector();
		lastLocation = null;
		started = false;

		start();
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.LocationService#getCurrentLocation()
	 */
	public Location getCurrentLocation() {
		return lastLocation;
	}
	
	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.LocationService#isStarted()
	 */
	public boolean isStarted() {
		return started;
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.LocationService#addLocationListener(com.monits.blackberry.commons.location.LocationListener)
	 */
	public synchronized void addLocationListener(LocationListener listener) {
		if (!listeners.contains(listener)) {
			listeners.addElement(listener);
			
			// Immediately notify the listener if possible!
			if (lastLocation != null) {
				listener.update(lastLocation);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.LocationService#removeLocationListener(com.monits.blackberry.commons.location.LocationListener)
	 */
	public synchronized void removeLocationListener(LocationListener listener) {
		listeners.removeElement(listener);
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.LocationService#start()
	 */
	public synchronized void start() {
		if (started) {
			return;
		}
		
		try {

			locationProvider.setLocationListener(new javax.microedition.location.LocationListener() {

				public void providerStateChanged(LocationProvider provider,
						int newState) {
					// Pass
				}

				public void locationUpdated(LocationProvider provider,
						Location location) {

					if (!location.isValid()) {
						return;
					}
					
					LocationServiceImpl.this.lastLocation = location;

					// Notify all listeners
					for (int i = 0; i < LocationServiceImpl.this.listeners.size(); i++) {
						LocationListener listener = (LocationListener) LocationServiceImpl.this.listeners.elementAt(i);
						listener.update(location);
					}
				}
			}, -1, -1, -1);
			
			try {
				// The API is pretty crappy and takes 30 secs to give the first fix, but this is almost instantaneous...
				lastLocation = locationProvider.getLocation(5);
			} catch (InterruptedException e) {
				Logger.error("Failed to get a first location due to interruption. " + e.getMessage());
			}
			
			started = true;
		} catch (LocationException e) {
			Logger.error("Failed to get a location provider. " + e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.LocationService#stop()
	 */
	public synchronized void stop() {
		if (!started) {
			return;
		}
		
		locationProvider.setLocationListener(null, -1, -1, -1);
		locationProvider.reset();
		started = false;
	}

	/**
	 * Set the GPS mode.
	 * @param gpsMode
	 * @return If is available, the Criteria object for the gps mode. 
	 */
	public Criteria setGpsMode(int gpsMode){
		Criteria criteria = new Criteria();
		switch(GPS_MODE) {

			case GPS_MODE_ANY:
				criteria = null;
				break;

			case GPS_MODE_STANDALONE:
				criteria.setCostAllowed(false);
				break;

			case GPS_MODE_ASSISTED:
				criteria.setCostAllowed(true);
				criteria.setHorizontalAccuracy(200);
				criteria.setVerticalAccuracy(200);
				criteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_MEDIUM);
				break;

			case GPS_MODE_CELLSITE:
				criteria.setCostAllowed(true);
				criteria.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);
				criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
				criteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_LOW);
				break;

			case GPS_MODE_MS_ASSISTED:
				criteria.setCostAllowed(true); 
				criteria.setHorizontalAccuracy(200); 
				criteria.setVerticalAccuracy(200); 
				criteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_MEDIUM);
				break;

			case GPS_MODE_MS_BASED:
				criteria.setCostAllowed(true); 
				criteria.setHorizontalAccuracy(200); 
				criteria.setVerticalAccuracy(200); 
				criteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_MEDIUM); 
				break;

			case GPS_MODE_SPEED_OPTIMAL:
				criteria.setCostAllowed(true); 
				criteria.setHorizontalAccuracy(200); 
				criteria.setVerticalAccuracy(200); 
				criteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_HIGH); 
				break;

			case GPS_MODE_ACCURACY_OPTIMAL:
				criteria.setCostAllowed(true); 
				criteria.setHorizontalAccuracy(200); 
				criteria.setVerticalAccuracy(200); 
				criteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_HIGH); 
				break;

			case GPS_MODE_DATA_OPTIMAL:
				criteria.setCostAllowed(true); 
				criteria.setHorizontalAccuracy(Criteria.NO_REQUIREMENT); 
				criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT); 
				break;
		}
		return criteria;
	}
}
