package com.monits.blackberry.commons.uielements;

import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;

import com.monits.blackberry.commons.uielements.listener.StateChangeListener;
import com.thirdparty.connectivity.HttpConnectionFactory;

/**
 * Class that get an image from a web and display it.
 */
public class WebBitmapField extends BitmapField {
	private static final int BUFFER_SIZE = 10000;

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
		
		new Thread(
			new Runnable() {
				public void run() {
					HttpConnectionFactory factory = new HttpConnectionFactory(url);
					try {
						HttpConnection connection = factory.getNextConnection();
						InputStream inputStream = connection.openInputStream();
						
						if (connection.getResponseCode() == HttpConnection.HTTP_OK) {
							byte[] responseData = new byte[BUFFER_SIZE];
							int length;
							StringBuffer rawResponse = new StringBuffer();
							try {
								while (-1 != (length = inputStream.read(responseData))) {
									rawResponse.append(new String(responseData, 0, length));
								}
							} catch (Exception e) {
								// Ignore it, older devices throw connection closed exceptions instead of returning -1 when EOF reached..
							}
						
							byte[] dataArray = rawResponse.toString().getBytes();
							bitmap = EncodedImage.createEncodedImage(dataArray, 0,
									dataArray.length);
							
							synchronized (cache) {
								cache.put(url, bitmap);
							}
							
							synchronized (Application.getEventLock()) {
								setImage(bitmap);
								invalidate();
							}
							setState(STATE_LOADED);
						} else {
							setState(STATE_FAILED);
						}
					} catch (Exception e) {
						setState(STATE_FAILED);
					}
				}
			}
		).start();
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