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

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ButtonField;

/**
 * A bitmap field with the behavior of a button.
 *
 */
public class BitmapButtonField extends ButtonField {
	private static final int STATE_NORMAL = 0;
	private static final int STATE_FOCUS = 1;
	private static final int STATE_ACTIVE = 2;
	
	private Bitmap mNormal;
	private Bitmap mFocused;
	private Bitmap mActive;

	private int mWidth;
	private int mHeight;
	private int mState = STATE_NORMAL;

	
	/**
	 * Constructor.
	 * @param normal Bitmap for normal state.
	 * @param focused Bitmap for focused state.
	 * @param active Bitmap for active state.
	 */
	public BitmapButtonField(Bitmap normal, Bitmap focused, Bitmap active) {
		super(CONSUME_CLICK | DrawStyle.HCENTER);
		mNormal = normal;
		mFocused = focused;
		mActive = active;
		
		mWidth = mNormal.getWidth();
		mHeight = mNormal.getHeight();
	}
	/**
	 * Constructor.
	 * @param normal Bitmap for normal state.
	 * @param focused Bitmap for focused state.
	 */
	public BitmapButtonField(Bitmap normal, Bitmap focused) {
		this(normal, focused, focused);
	}
	/**
	 * Constructor.
	 * @param normal Bitmap for normal state.
	 */
	public BitmapButtonField(Bitmap normal) {
		this(normal, normal, normal);
	}
	protected void paint(Graphics graphics) {
		Bitmap bitmap = null;
		switch (getState()) {
		case STATE_NORMAL:
			bitmap = mNormal;
			break;
		case STATE_FOCUS:
			bitmap = mFocused;
			break;
		case STATE_ACTIVE:
			bitmap = mActive;
			break;
		default:
			bitmap = mNormal;
		}
		graphics.drawBitmap(0, 0, bitmap.getWidth(), bitmap.getHeight(),
						bitmap, 0, 0);
	}

	public int getState() {
		return mState;
	}

	protected void applyTheme()
	{}
	protected void onFocus(int direction) {
		mState = STATE_FOCUS;
		super.onFocus(direction);
		getManager().getManager().invalidate();
	}

	protected void onUnfocus() {
		mState = STATE_NORMAL;
		super.onUnfocus();
		getManager().getManager().invalidate();
	}

	protected boolean navigationClick(int status, int time) {
		mState = STATE_ACTIVE;
		return super.navigationClick(status, time);
	}

	protected boolean navigationUnclick(int status, int time) {
		mState = STATE_NORMAL;
		return super.navigationUnclick(status, time);
	}

	public int getPreferredWidth() {
		return mWidth;
	}

	public int getPreferredHeight() {
		return mHeight;
	}

	protected void layout(int width, int height) {
		super.layout(width, height);
		setExtent(getPreferredWidth(), getPreferredHeight());
	}
}
