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

import net.rim.device.api.system.Display;

import com.monits.blackberry.commons.service.ScreenTypeService;

/**
 * Implementation of {@link ScreenTypeService}
 * @author Rodrigo Pereyra
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
			return (int) (newSize + 1);
		}
		return (int)newSize;
	}
}
