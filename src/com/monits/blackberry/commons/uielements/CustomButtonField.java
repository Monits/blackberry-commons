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

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;


/**
 * Button field that can be customized
 *
 */
public class CustomButtonField extends Field {
	
	private String label;
	private int fontColorFocused;	// TODO > Use this!
	private int fontColorUnfocused;
	private int backgroundColorFocused;
	private int backgroundColorUnfocused;


	/**
	 * Constructor.
	 * @param text Button text.
	 */
	public CustomButtonField(String text){
		super();
		this.label = text;
	}

	/**
	 * Constructor.
	 * @param text Button text.
	 * @param style Button style.
	 */
	public CustomButtonField(String text, long style){
		super(style);
		this.label = text;
	}

	/**
	 * Set the diferents colors.
	 * @param fontColorFocused
	 * @param fontColorUnfocused
	 * @param backgroundColorFocused
	 * @param backgroundColorUnfocused
	 */
	public void setColors(int fontColorFocused, int fontColorUnfocused, 
					int backgroundColorFocused, int backgroundColorUnfocused) {

		this.fontColorFocused = fontColorFocused;
		this.fontColorUnfocused = fontColorUnfocused;
		this.backgroundColorFocused = backgroundColorFocused;
		this.backgroundColorUnfocused = backgroundColorUnfocused;

	}

	protected void layout(int maxWidth, int maxHeight) {
		
		Font font = getFont();

		int width = font.getAdvance(label) + (2 * 4);
		int height = font.getHeight() + (2 * 4);
		// Respect the maximum width and height available from our manager
		setExtent(Math.min(width, maxWidth), Math.min(height, maxHeight));
	}

	protected void paint(Graphics graphics) {

		graphics.setBackgroundColor(isFocus() ? backgroundColorFocused : backgroundColorUnfocused);
		graphics.fillRoundRect(0, 0, getWidth()+25, getHeight(), 6, 6);

		graphics.setBackgroundColor(backgroundColorUnfocused);
		graphics.drawRoundRect(1, 1, getWidth()+25, getHeight(), 6, 6);

		graphics.setColor(isFocus() ? fontColorFocused : fontColorUnfocused);
		int fontWidth = getFont().getAdvance(label);
		graphics.drawText(label, (getWidth()-fontWidth)/2, 2);   
	}
	
	public boolean isFocusable() {
		return true;
	}

	protected void drawFocus(Graphics graphics, boolean on) {
		 // Don't draw the default focus
	}

	protected void onFocus(int direction) {
		super.onFocus(direction);
		invalidate();
	}

	protected void onUnfocus() {
		super.onUnfocus();
		invalidate();
	}

	public boolean keyChar(char key, int status, int time) {
		if (key == Characters.ENTER) {
			fieldChangeNotify(0);
			return true;
		}
		
		return false;
	}

	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(0);
		return true;
	}
}
