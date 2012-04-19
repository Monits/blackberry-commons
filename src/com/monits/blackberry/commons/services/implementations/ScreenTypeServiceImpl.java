package com.monits.blackberry.commons.services.implementations;

import net.rim.device.api.system.Display;

import com.monits.blackberry.commons.services.ScreenTypeService;

/**
 * @author Pc
 *
 */
public class ScreenTypeServiceImpl implements ScreenTypeService {

	/**
	 * Get the multiplier for the actual type of screen
	 */
	private double getMultiplier() {
		double multiplier;
		if (Display.getWidth() == 480) {
			multiplier = 1;
		} else {
			multiplier = (double) Display.getWidth() / 480;
		}
		return multiplier;
	}

	/**
	 * Calculate a new value using the multiplier 
	 */
	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ScreenTypeService#getNewSize(int)
	 */
	public int getNewSize(int size) {
		double newSize = size * getMultiplier();
		/*The plus one is to complement the loss caused by the multiplication 
		when the multiplier is not 1*/
		if ((int)newSize <= 0) {
			return 1;
		}
		return (int)newSize;
	}
}
