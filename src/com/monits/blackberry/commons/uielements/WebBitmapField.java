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
package com.monits.blackberry.commons.uielements;

import java.util.Hashtable;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;

import com.monits.blackberry.commons.logger.Logger;
import com.monits.blackberry.commons.service.ConnectionService;
import com.monits.blackberry.commons.service.ServiceLocator;
import com.monits.blackberry.commons.service.request.RequestHandler;
import com.monits.blackberry.commons.uielements.listener.StateChangeListener;

/**
 * Class that get an image from a web and display it.
 */
public class WebBitmapField extends BitmapField {

	public static final Logger logger = Logger.getLogger(WebBitmapField.class);

	// TODO : Replace cache with a local persistent storage?
	private static final Hashtable cache = new Hashtable();

	public static final int STATE_LOADING = 0;
	public static final int STATE_LOADED = 1;
	public static final int STATE_FAILED = -1;

	private EncodedImage bitmap = null;

	private int state = STATE_LOADING;

	private StateChangeListener listener;

	/**
	 * Constructor.
	 * @param url The URL of the image.
	 */
	public WebBitmapField(final String url) {
		synchronized (cache) {
			if (cache.containsKey(url)) {
				bitmap = (EncodedImage) cache.get(url);
				setImage(bitmap);
				setState(STATE_LOADED);
				return;
			}
		}

		ConnectionService cs = ServiceLocator.getConnectionService();
		cs.executeAsyncGet(url, new RequestHandler() {

			public void onSuccess(String response, int responseCode) {

				logger.debug("Reading the server response.");
				byte[] dataArray = response.toString().getBytes();
				bitmap = EncodedImage.createEncodedImage(dataArray, 0, dataArray.length);

				synchronized (cache) {
					cache.put(url, bitmap);
				}

				synchronized (Application.getEventLock()) {
					logger.debug("Displaying the image.");
					setImage(bitmap);
					invalidate();
				}
				setState(STATE_LOADED);
			}

			public void onFailure(String message) {
				logger.warn("The image request has failed. Make sure the URL is correct.");
				setState(STATE_FAILED);
			}

			public void onError(Throwable t) {
				logger.error("An error ocurrs while trying to get the image.\n");
				logger.error(t.getMessage());
				setState(STATE_FAILED);
			}

		});
	}

	/**
	 * @return The image
	 */
	public EncodedImage getImage() {
		return bitmap;
	}

	/* (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#getState()
	 */
	public int getState() {
		return state;
	}

	/**
	 * Set a {@link StateChangeListener}
	 * @param listener New state listener
	 */
	public void setStateChangeListener(StateChangeListener listener) {
		this.listener = listener;

		// trigger an update immediately!
		setState(state);
	}

	/**
	 * Set the state of the field.
	 * @param state New state.
	 */
	private void setState(int state) {
		this.state = state;
		
		if (listener != null) {
			listener.onStateChanged(state);
		}
	}
}