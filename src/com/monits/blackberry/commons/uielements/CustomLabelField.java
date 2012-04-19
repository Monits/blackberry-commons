package com.monits.blackberry.commons.uielements;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

import com.monits.blackberry.commons.services.ServiceLocator;

public class CustomLabelField extends LabelField {

	private static int STYLE = DrawStyle.HCENTER;

	private int color = Color.WHITE;

	public CustomLabelField(String text, long style) {
		super(text, style);
	}
	
	public CustomLabelField(String text) {
		this(text, Field.FIELD_BOTTOM | STYLE);
	}

	public CustomLabelField setColor(int color) {
		this.color = color;
		return this;
	}

	public CustomLabelField setBold() {
		this.setFont(this.getFont().derive(this.getFont().getStyle() | Font.BOLD));
		return this;
	}

	public CustomLabelField setItalic() {
		this.setFont(this.getFont().derive(this.getFont().getStyle() | Font.ITALIC));
		return this;
	}

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
