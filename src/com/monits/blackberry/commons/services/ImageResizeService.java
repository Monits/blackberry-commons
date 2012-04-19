package com.monits.blackberry.commons.services;

import net.rim.device.api.system.EncodedImage;


/**
 * Provides methods to resize images.
 * @author Rodrigo Pereyra
 *
 */
public interface ImageResizeService {

	/**
	 * Resize the image to fit the current display
	 * @param image The image to be resized
	 * @return The resized image
	 */
	public EncodedImage sizeToFit(EncodedImage image);

	/**
	 * Resize the image to the requested dimensions.
	 * @param image The image to be resized
	 * @return The resized image
	 */
	public EncodedImage sizeImage(EncodedImage image, int width, int height);
}
