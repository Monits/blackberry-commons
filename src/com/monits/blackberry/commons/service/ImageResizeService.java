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
