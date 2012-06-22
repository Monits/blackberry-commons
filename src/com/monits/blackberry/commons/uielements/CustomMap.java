//#preprocess

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

import javax.microedition.location.Coordinates;

import net.rim.device.api.lbs.MapField;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
//#ifdef TouchEnabled
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.TouchGesture;
//#endif
import net.rim.device.api.ui.XYPoint;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.util.Arrays;
import net.rim.device.api.util.MathUtilities;

import com.monits.blackberry.commons.service.ServiceLocator;
import com.monits.blackberry.commons.uielements.map.Marker;

/**
 * Map field that support Marker object in it.
 * @author Rodrigo Pereyra
 */
public class CustomMap extends MapField {

	private Marker openLocation;
	private Marker focusLocation;
	private Marker markers[];

	private Bitmap crosshair;

	private int _touchX;
	private int _touchY;

	/**
	 * Constructor.
	 * @param crosshair Image that will be displayed in the center of the field.
	 */
	public CustomMap(Bitmap crosshair) {
		super();
		markers = new Marker[0];
		this.crosshair = crosshair;
	}

	/**
	 * Adds a new marker on the map and retrieves it.
	 * 
	 * @param marker The marker being placed.
	 * @return The created marker.
	 */
	public Marker addMarker(Marker marker) {
		Arrays.add(markers, marker);
		this.invalidate();

		return marker;
	}
	
	/**
	 * Removes all markers from the map.
	 */
	public void removeAllMarkers() {
		markers = new Marker[0];
		this.invalidate();
	}

	/**
	 * Retrieve the focus location.
	 * @return the focus location or null if there no focus location.
	 */
	public Marker getFocusLocation() {
		return focusLocation;
	}

	protected void paint(Graphics graphics) {
		super.paint(graphics);

		XYPoint actualFieldOut = new XYPoint();
		Coordinates actualCord = new Coordinates(((double) getLatitude()) / 100000.0, ((double) getLongitude()) / 100000.0, 0);
		convertWorldToField(actualCord, actualFieldOut);
		focusLocation = null;

		for (int i = 0; i < markers.length; i++) {
			XYRect rect = null;
			Marker loc = markers[i];
			if (null != loc) {
				XYPoint fieldOut = new XYPoint();
				convertWorldToField(loc.getCoor(), fieldOut);
				if (fieldOut.x > (-loc.getBitmap().getWidth())
						&& fieldOut.x < getWidth() + loc.getBitmap().getWidth()) {
					if (fieldOut.y > (-loc.getBitmap().getHeight())
							&& fieldOut.y < getHeight()
									+ loc.getBitmap().getHeight()) {
						int imgW = loc.getBitmap().getWidth();
						int imgH = loc.getBitmap().getHeight();
						rect = new XYRect(fieldOut.x - imgW / 2,
								fieldOut.y - imgH, imgW, imgH);

						if (rect.contains(actualFieldOut) && focusLocation == null) {
							graphics.drawBitmap(rect, loc.getFocusBitmap(), 0, 0);
							focusLocation = loc; 
						} else {
							graphics.drawBitmap(rect, loc.getBitmap(), 0, 0);
						}
					}
				}
			}
		}

		if (openLocation != null) {
			drawOpenMarker(graphics, openLocation);
		}

		// Display the crosshair
		if (crosshair != null) {
			graphics.drawBitmap(this.getWidth() / 2 - crosshair.getWidth() / 2, this.getHeight() / 2 - crosshair.getHeight() / 2, crosshair.getWidth(), crosshair.getHeight(), crosshair, 0, 0);
		}
	}

	protected void drawOpenMarker(Graphics graphics, Marker location) {
		// Implement this if you want to draw a description or whatever.
		// By default it doesn't do anything.
	}

	/**
	 * Retrieve the marker at the given point.
	 * @param fieldPoint Position of the marker.
	 * @return A marker object that correspond with the point, 
	 * 			or null if there no marker at the given point.
	 */
	public Marker getMarkerAtPoint(XYPoint fieldPoint) {
		for (int i = 0; i < markers.length; i++) {
			XYRect rect = null;
			Marker loc = markers[i];
			if (null != loc) {
				XYPoint fieldOut = new XYPoint();
				convertWorldToField(loc.getCoor(), fieldOut);
				if (fieldOut.x > (-loc.getBitmap().getWidth())
						&& fieldOut.x < getWidth() + loc.getBitmap().getWidth()) {
					if (fieldOut.y > (-loc.getBitmap().getHeight())
							&& fieldOut.y < getHeight() + loc.getBitmap().getHeight()) {
						int imgW = loc.getBitmap().getWidth();
						int imgH = loc.getBitmap().getHeight();
						rect = new XYRect(fieldOut.x - imgW / 2,
								fieldOut.y - imgH, imgW, imgH);

						if (rect.contains(fieldPoint)) {
							return markers[i];
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * @return True if a marker is open, otherwise false 
	 */
	protected boolean isMarkerOpen() {
		return openLocation != null;
	}

	/**
	 * Open the given marker.
	 * @param loc Marker to be open.
	 */
	public void openMarker(Marker loc) {
		openLocation = loc;
		if (null != loc) {
			XYPoint fieldOut = new XYPoint();
			Coordinates worldOut = new Coordinates((float) 0.0, (float) 0.0, (float) 0.0);
			convertWorldToField(new Coordinates(loc.getCoor().getLatitude(), loc.getCoor().getLongitude(), (float) 0.0), fieldOut);
			int newY = fieldOut.y - openLocation.getBitmap().getHeight() - ServiceLocator.getScreenTypeService().getNewSize(15);
			convertFieldToWorld(new XYPoint(fieldOut.x, newY), worldOut);
			moveTo(worldOut);
		} else {
			this.invalidate();
		}
	}

	public Marker getOpenLocation() {
		return openLocation;
	}

	//#ifdef TouchEnabled
	protected boolean touchEvent(TouchEvent message) {
		switch (message.getEvent()) {
			//mark that we're starting to interact
			case TouchEvent.DOWN:
				_touchX = message.getX(1);
				_touchY = message.getY(1);
				return true;

			//user is wanting to move the map
			case TouchEvent.MOVE:
				int dx = _touchX - message.getX(1);
				int dy = _touchY - message.getY(1);
				_touchX = message.getX(1);
				_touchY = message.getY(1);

				//perform checks to make sure we don't move outside of the map's range
				int lat = getLatitude() - dy*(int)MathUtilities.pow(2, (double)getZoom());
				if(lat < -9000000) {
					lat = -9000000;
				}
				else if (lat > 9000000) {
					lat = 9000000;
				}
				int lon = getLongitude() + dx*(int)MathUtilities.pow(2, (double)getZoom());
				if(lon < -18000000) {
					lon = -18000000;
				}
				else if (lon > 18000000) {
					lon = 18000000;
				}

				moveTo(lat, lon);
				return true;

			case TouchEvent.GESTURE:
				TouchGesture gesture = message.getGesture();
				if (gesture.getEvent() == TouchGesture.TAP) {
					// Handle the double tapping
					if (gesture.getTapCount() >= 2) {
						// set a minimum quantity of zoom
						setZoom(Math.max(getZoom() - 1, getMinZoom()));
						return true;
					}

					Marker m = getMarkerAtPoint(new XYPoint(_touchX, _touchY));
					
					// if a poi, display the popup
					if (isMarkerOpen()) {
						openMarker(null);
					} else {
						// Go the point
						move(_touchX - getWidth() / 2, _touchY - getHeight() / 2);
						
						if (m != null) {
							openMarker(m);
						}
					}
				}
				return true;
		}

		return super.touchEvent(message);
	}
	//#endif
}