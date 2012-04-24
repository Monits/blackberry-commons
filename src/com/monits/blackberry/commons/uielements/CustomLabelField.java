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

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

import com.monits.blackberry.commons.service.ServiceLocator;

/**
 * Customizable label field.
 */
public class CustomLabelField extends LabelField {

	private static int STYLE = DrawStyle.HCENTER;

	private int color = Color.WHITE;

	/**
	 * Construct an instance with the given style.
	 * @param text Text for the label.
	 * @param style Field style.
	 */
	public CustomLabelField(String text, long style) {
		super(text, style);
	}
	
	/**
	 * Construct an instance with a predefined style.
	 * @param text Text for the label.
	 */
	public CustomLabelField(String text) {
		this(text, Field.FIELD_BOTTOM | STYLE);
	}

	/**
	 * Set the text color.
	 * @param color Text color.
	 * @return this (for chained calls.)
	 */
	public CustomLabelField setColor(int color) {
		this.color = color;
		return this;
	}

	/**
	 * Set the field text bold. 
	 * @return this (for chained calls.)
	 */
	public CustomLabelField setBold() {
		this.setFont(this.getFont().derive(this.getFont().getStyle() | Font.BOLD));
		return this;
	}

	/**
	 * Set the field text italic.
	 * @return this (for chained calls.)
	 */
	public CustomLabelField setItalic() {
		this.setFont(this.getFont().derive(this.getFont().getStyle() | Font.ITALIC));
		return this;
	}

	/**
	 * Set the text size.
	 * @param size New size.
	 * @return this (for chained calls.)
	 */
	public CustomLabelField setSize(int size) {
		this.setFont(this.getFont().derive(this.getFont().getStyle(), ServiceLocator.getScreenTypeService().getNewSize(size)));
		return this;
	}

	public void paint(Graphics g) {
		g.setColor(color);
		g.setDrawingStyle(STYLE, true);
		super.paint(g);
	}
}
