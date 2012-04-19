package com.monits.blackberry.commons.uielements.listener;

import com.monits.blackberry.commons.model.Marker;

/**
 * Listener for touch events.
 *
 */
public interface TouchListener {

	/**
	 * This method is called when a marker is touch.
	 * @param marker Touched marker.
	 */
	void markerTouched(Marker marker);

}
