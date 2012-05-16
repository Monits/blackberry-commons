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

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;

import com.monits.blackberry.commons.service.ResourceService;
import com.monits.blackberry.commons.service.ServiceLocator;

/**
 * Implementation of {@link ResourceService}
 * @see ResourceService
 * @author Rodrigo Pereyra
 *
 */
public class ResourceServiceImpl implements ResourceService{

	private static final String BITMAP_PATH = "img/";

	private static final Vector _4x3_ALTERNATIVES = new Vector();
	private static final Vector _3x4_ALTERNATIVES = new Vector();
	private static final Vector _3x2_ALTERNATIVES = new Vector();
	private static final Vector _2x3_ALTERNATIVES = new Vector();
	private static final Vector _12x13_ALTERNATIVES = new Vector();
	private static final Vector _1x1_ALTERNATIVES = new Vector();

	private static final double DELTA = 0.0001;

	static {
		// For reference,see http://supportforums.blackberry.com/t5/Java-Development/List-of-Blackberry-Devices-with-resolution/td-p/556066
		_4x3_ALTERNATIVES.addElement(new Resolution(480, 360));		// 8900
		
		_3x4_ALTERNATIVES.addElement(new Resolution(360, 480));		// Storm Vertical
		
		_3x2_ALTERNATIVES.addElement(new Resolution(480, 320));		// 9000, Torch horizontal
		_3x2_ALTERNATIVES.addElement(new Resolution(320, 240));		// 8300, 8530, 8700
		_3x2_ALTERNATIVES.addElement(new Resolution(240, 160));		// 7210, 7510
		
		_2x3_ALTERNATIVES.addElement(new Resolution(240, 320));		// 8220
		_2x3_ALTERNATIVES.addElement(new Resolution(320, 480));		// Torch vertical
		
		_12x13_ALTERNATIVES.addElement(new Resolution(240, 260));	// 7100, 8100
		
		_1x1_ALTERNATIVES.addElement(new Resolution(240, 240));		// 7730, 857
		
		//BlackBerry 7100t -> 324 x 352 FUUUUUUUUUUUUUUUUUCK!!
		// 81x88
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ResourceService#getBitmap(java.lang.String)
	 */
	public Bitmap getBitmap(String path) {
		double ratio = Display.getWidth() / (double) Display.getHeight();
		Vector alternatives = new Vector();
		
		// Make sure we first look for the exact resolution
		alternatives.addElement(new Resolution(Display.getWidth(), Display.getHeight()));
		
		// Add all alternatives for the same aspect ratio...
		if(ratio - 4 / (double)3 <= DELTA) {
			for(int i = 0; i < _4x3_ALTERNATIVES.size(); i++) {
				alternatives.addElement(_4x3_ALTERNATIVES.elementAt(i));
			}
		} else if(ratio - 3 / (double)2 <= DELTA) {
			for(int i = 0; i < _3x2_ALTERNATIVES.size(); i++) {
				alternatives.addElement(_3x2_ALTERNATIVES.elementAt(i));
			}
		} else if(ratio - 3 / (double)4 <= DELTA) {
			for(int i = 0; i < _3x4_ALTERNATIVES.size(); i++) {
				alternatives.addElement(_3x4_ALTERNATIVES.elementAt(i));
			}
		} else if(ratio - 2 / (double)3 <= DELTA) {
			for(int i = 0; i < _2x3_ALTERNATIVES.size(); i++) {
				alternatives.addElement(_2x3_ALTERNATIVES.elementAt(i));
			}
		} else if(ratio - 1 / (double)1 <= DELTA) {
			for(int i = 0; i < _1x1_ALTERNATIVES.size(); i++) {
				alternatives.addElement(_1x1_ALTERNATIVES.elementAt(i));
			}
		} else if(ratio - 1 / (double)1 <= DELTA) {
			for(int i = 0; i < _12x13_ALTERNATIVES.size(); i++) {
				alternatives.addElement(_12x13_ALTERNATIVES.elementAt(i));
			}
		}

		// Use the default as fallback
		alternatives.addElement(new Resolution());

		// Get the specific image for this resolution... Or the closest one...
		EncodedImage encodedImage = null;
		Resolution alternative = null;
		for (int i = 0; i < alternatives.size(); i++) {
			alternative = (Resolution) alternatives.elementAt(i);
			encodedImage = EncodedImage.getEncodedImageResource(BITMAP_PATH + alternative.toString() + path);
			if(encodedImage != null) {
				break;
			}
		}
		Bitmap b = null;
		if(alternative.width!=0 && alternative.height!= 0){
			b = ServiceLocator.getImageResizeService().sizeImage(encodedImage, (int)(encodedImage.getWidth()* alternative.getMultiplier()) , (int)(encodedImage.getHeight() * alternative.getMultiplier())).getBitmap();
		} else {
			b = ServiceLocator.getImageResizeService().sizeImage(encodedImage, ServiceLocator.getScreenTypeService().getNewSize(encodedImage.getWidth()), ServiceLocator.getScreenTypeService().getNewSize(encodedImage.getHeight())).getBitmap();
		}
		return b;
	}

	/**
	 * Model a screen's resolution
	 * @author Rodrigo Pereyra
	 *
	 */
	private static class Resolution{
		private int width;
		private int height;

		/**
		 * Defualt constructor.
		 */
		public Resolution() {
		}

		/**
		 * Constructor.
		 * @param Screen's width 
		 * @param Screen's height
		 */
		public Resolution(int width, int height) {
			this.width = width;
			this.height = height;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			if(width != 0 && height != 0) {
				return width + "x" + height + "/";
			}
			return "";
		}

		/**
		 * Retrieves the multiplication factor 
		 * @return The multiplication factor for the current Display
		 */
		public double getMultiplier() {
			int currentWidth = Display.getWidth();
			return currentWidth / (double) width;
		}
	}
}