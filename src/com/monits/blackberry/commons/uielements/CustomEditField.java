package com.monits.blackberry.commons.uielements;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.monits.blackberry.commons.services.ServiceLocator;

public class CustomEditField extends BasicEditField {
	
	private static int DEFAULT_FONT_SIZE = 35;
	private static final int STATE_NORMAL = 0;
	private static final int STATE_FOCUS = 1;
	
	
	public CustomEditField(){
		
		super(EditField.NO_NEWLINE);
		
		this.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		int fontSize = ServiceLocator.getScreenTypeService().getNewSize(DEFAULT_FONT_SIZE); 
		
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
