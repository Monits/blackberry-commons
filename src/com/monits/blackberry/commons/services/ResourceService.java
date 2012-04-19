package com.monits.blackberry.commons.services;

import net.rim.device.api.system.Bitmap;


/**
 * Service that retrieves resources
 * @author Rodrigo Pereyra
 *
 */
public interface ResourceService {

	/**
	 * Retrieve a wanted bitmap.
	 * @param Path to the bitmap
	 * @return The bitmap, or null if it wasn't found
	 */
	public Bitmap getBitmap(String path);
}
