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
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.KeypadUtil;

import com.monits.blackberry.commons.service.ScreenTypeService;
import com.monits.blackberry.commons.service.ServiceLocator;

/**
 * Custom checkbox field.
 * @author Rodrigo Pereyra
 *
 */
public class CustomCheckboxField extends Field {
	private static final int STATE_NORMAL = 0;
	private static final int STATE_FOCUS = 1;

	private static int state;

	private Bitmap checked;
	private Bitmap unchecked;

	private boolean isChecked;
	private String label;
	private ScreenTypeService multiplier;

	/**
	 * Constructor.
	 * @param checked Bitmap for checked field.
	 * @param unchecked Bitmap for unchecked field.
	 * @param label Text for the field.
	 * @param isChecked Initial state of the field.
	 */
	public CustomCheckboxField(Bitmap checked, Bitmap unchecked, String label, boolean isChecked) {
		super(Field.USE_ALL_WIDTH);

		this.checked = checked;
		this.unchecked = unchecked;
		this.label = label;

		this.isChecked = isChecked; 
		this.multiplier = ServiceLocator.getScreenTypeService();
	}

	/**
	 * Set the checkbox state.
	 * @param checked State of the field.
	 */
	public void setChecked(boolean checked){
		isChecked = checked;
		invalidate();
	}

	/**
	 * Return the state of the field.
	 * @return true if the field is checked, otherwise false.
	 */
	public boolean isChecked(){
		return isChecked;
	}

	protected boolean keyDown(int keycode, int time) {
		char key = KeypadUtil.getKeyChar(keycode, KeypadUtil.MODE_UI_CURRENT_LOCALE);

		if (key == Characters.ENTER) {
			if(isChecked){
				return true;
			}

			isChecked = !isChecked;
			fieldChangeNotify(0);
			invalidate();

			return true;
		}
		return super.keyDown(keycode, time);
	}

	protected boolean navigationClick(int status, int time) {
		if(isChecked){
			return true;
		}

		this.setChecked(true);
		fieldChangeNotify(0);
		return true;
	}

	public int getState(){
		return state;
	}

	public boolean isFocusable(){
		return true;
	}

	public void onFocus(int direction){
		state = STATE_FOCUS;
		super.onFocus(direction);
	}

	public void onUnfocus(){
		state = STATE_FOCUS;
		super.onUnfocus();
	}

	protected void layout(int width, int height) {
		setExtent(this.getFont().derive(Font.BOLD, multiplier.getNewSize(18)).getAdvance(label) + checked.getWidth() + multiplier.getNewSize(40), multiplier.getNewSize(40));
	}

	protected void paint(Graphics graphics) {
		graphics.setBackgroundColor(0x00e6e6e6);
		graphics.setFont(this.getFont().derive(Font.BOLD, multiplier.getNewSize(18)));
		graphics.setColor(0x00888888);

		int height = getHeight();
		int posY = height / 2 - graphics.getFont().getHeight() / 2;
		int advance = multiplier.getNewSize(20);

		graphics.drawText(label, advance, posY);

		advance += multiplier.getNewSize(10) + graphics.getFont().getAdvance(label);

		switch(state){
			case STATE_NORMAL:
				if (isChecked) {
					graphics.drawBitmap(advance, posY, checked.getWidth(), checked.getHeight(), checked, 0, 0);
				} else {
					graphics.drawBitmap(advance, posY, unchecked.getWidth(), unchecked.getHeight(), unchecked, 0, 0);
				}
				break;
				
			case STATE_FOCUS:
				if (isChecked) {
					graphics.drawBitmap(advance, posY, checked.getWidth(), checked.getHeight(), checked, 0, 0);
				} else {
					graphics.drawBitmap(advance, posY, unchecked.getWidth(), unchecked.getHeight(), unchecked, 0, 0);
				}
				break;
		}
	}
}