package com.monits.blackberry.commons.services.implementations;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;

import com.monits.blackberry.commons.services.ImageResizeService;

/**
 * @see ImageResizeService
 * @author Rodrigo Pereyra
 *
 */
public class ImageResizeServiceImpl implements ImageResizeService {

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ImageResizeService#sizeToFit(net.rim.device.api.system.EncodedImage)
	 */
	public EncodedImage sizeToFit(EncodedImage image) {

		int maxWidth = Display.getWidth();
		int maxHeight = Display.getHeight();

		int currentWidth = image.getWidth();
		int currentHeight = image.getHeight();

		double ratioOrig = currentWidth / (double) currentHeight;
		double ratioDisplay = maxWidth / (double) maxHeight;

		int targetWidth, targetHeight;

		// If original ratio is higher than the display's the width is the limiting factor
		if (ratioOrig > ratioDisplay) {
			targetWidth = maxWidth;
			targetHeight = (int) Math.floor(currentHeight * maxWidth / (double) currentWidth);
		} else {
			targetWidth = (int) Math.floor(currentWidth * maxHeight / (double) currentHeight);
			targetHeight = maxHeight;
		}

		return sizeImage(image, targetWidth, targetHeight);
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ImageResizeService#sizeImage(net.rim.device.api.system.EncodedImage, int, int)
	 */
	public EncodedImage sizeImage(EncodedImage image, int width, int height) {
		EncodedImage result = null;

		int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
		int currentHeightFixed32 = Fixed32.toFP(image.getHeight());

		int requiredWidthFixed32 = Fixed32.toFP(width);
		int requiredHeightFixed32 = Fixed32.toFP(height);

		int scaleXFixed32 = Fixed32.div(currentWidthFixed32, requiredWidthFixed32);
		int scaleYFixed32 = Fixed32.div(currentHeightFixed32, requiredHeightFixed32);

		result = image.scaleImage32(scaleXFixed32, scaleYFixed32);
		return result;
	}
}