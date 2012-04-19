package com.monits.blackberry.commons.uielements.listener;

/**
 * Listener for state changes.
 *
 */
public interface StateChangeListener {

	/**
	 * This method is called when occurs state changes.
	 * @param state New state.
	 */
	void onStateChanged(int state);
}
