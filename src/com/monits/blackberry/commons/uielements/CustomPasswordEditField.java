package com.monits.blackberry.commons.uielements;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.monits.blackberry.commons.services.ServiceLocator;

public class CustomPasswordEditField extends PasswordEditField {
private static int DEFAULT_FONT_SIZE = 35;

	public CustomPasswordEditField(){

		super();
		this.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));

		int fontSize = ServiceLocator.getScreenTypeService().getNewSize(DEFAULT_FONT_SIZE );
		this.setFont(this.getFont().derive(this.getFont().getStyle(), fontSize));
	}
	
	protected void layout(int width, int height) {
		super.layout(width, height);
		setExtent(getPreferredWidth(), getPreferredHeight());
	}
	
	public int getPreferredWidth() {
		return Display.getWidth() - ServiceLocator.getScreenTypeService().getNewSize(60);
	}

	public void paint(Graphics g){  
		g.setColor(Color.LIGHTGREY);
		g.drawLine(0, 0, getWidth(), 0);
		g.drawLine(0, 1, getWidth(), 1);
		
		g.setColor(Color.BLACK);
		g.setDrawingStyle(DrawStyle.HCENTER, true);
		super.paint(g);
	}
}