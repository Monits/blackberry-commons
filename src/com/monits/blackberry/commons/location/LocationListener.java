package com.monits.blackberry.commons.location;

import javax.microedition.location.Location;

/**
 * Listener for location changes.
 * @author Rodrigo Pereyra
 *
 */
public interface LocationListener {

	/**
	 * Update location.
	 * @param location New location value.
	 */
	void update(Location location);
}
