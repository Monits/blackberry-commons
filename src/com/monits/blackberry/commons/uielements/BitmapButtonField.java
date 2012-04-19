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

	
	public BitmapButtonField(Bitmap normal, Bitmap focused, Bitmap active) {
		super(CONSUME_CLICK | DrawStyle.HCENTER);
		mNormal = normal;
		mFocused = focused;
		mActive = active;
		
		mWidth = mNormal.getWidth();
		mHeight = mNormal.getHeight();
	}
	public BitmapButtonField(Bitmap normal, Bitmap focused) {
		this(normal, focused, focused);
	}
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
