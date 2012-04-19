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

import com.monits.blackberry.commons.model.Marker;
import com.monits.blackberry.commons.uielements.listener.TouchListener;

/**
 * Map field that support Marker object in it.
 * @author Rodrigo Pereyra
 */
public class CustomMap extends MapField {

	private Marker focusLocation;
	private Marker markers[];
	
	private Bitmap crosshair;
	private TouchListener listener;

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
	
	public void setTouchListener(TouchListener listener) {
		this.listener = listener;
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

	public void setBitmap(Coordinates coordinates, Bitmap bitmap) {
		for (int i = 0; i < markers.length; i++) {
			if (markers[i].getCoor().equals(coordinates)) {
				markers[i].setBitmap(bitmap);
			}
		}
	}

	public Marker getFocusLocation() {
		return focusLocation;
	}

	public void move(int dx, int dy) {
		super.move(dx, dy);
	}

	public void moveTo(Coordinates coordinates) {
		super.moveTo(coordinates);
	}

	public void setZoom(int zoom) {
		super.setZoom(zoom);
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
						rect = new XYRect(fieldOut.x - imgW / 2, fieldOut.y
								- imgH, imgW, imgH);
						
						
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
		
		// Display the crosshair
		if (crosshair != null) {
			graphics.drawBitmap(this.getWidth() / 2 - crosshair.getWidth() / 2, this.getHeight() / 2 - crosshair.getHeight() / 2, crosshair.getWidth(), crosshair.getHeight(), crosshair, 0, 0);
		}
	}
	
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
							&& fieldOut.y < getHeight()
									+ loc.getBitmap().getHeight()) {
						int imgW = loc.getBitmap().getWidth();
						int imgH = loc.getBitmap().getHeight();
						rect = new XYRect(fieldOut.x - imgW / 2, fieldOut.y
								- imgH, imgW, imgH);
						
						
						if (rect.contains(fieldPoint)) {
							 return markers[i];
						}
					}
				}
			}
		}
		
		return null;
	}
	
	protected boolean isLocationOpen() {
		return false;
	}

	public void setOpenLocation(Marker loc) {
	}
	
	
	//#ifdef TouchEnabled
	protected boolean touchEvent(TouchEvent message) {
		boolean ret =  super.touchEvent(message);

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
					if(gesture.getTapCount() >= 2) {
						// set a minimum quantity of zoom
						setZoom(Math.max(getZoom() - 1, getMinZoom()));
						return true;
					}
					Marker poi = getMarkerAtPoint(new XYPoint(_touchX, _touchY));
					
					// if a poi, display the popup
					if (isLocationOpen()) {
						setOpenLocation(null);
					} else {
						// Go the point
						move(_touchX - getWidth() / 2, _touchY - getHeight() / 2);
						
						if (poi != null) {
							setOpenLocation(poi);
						}
					}
				}
				return true;
		}
		
		//we handled the click
		return false;
	}
	//#endif
}