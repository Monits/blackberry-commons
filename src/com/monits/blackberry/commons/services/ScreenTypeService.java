package com.monits.blackberry.commons.services;

/**
 * Management of screen issues
 * @author Rodrigo Pereyra
 *
 */
public interface ScreenTypeService {

	/**
	 * Calculate new size value for the current display size.
	 * @param value The old size
	 * @return New size values
	 */
	public int getNewSize(int value);
}
