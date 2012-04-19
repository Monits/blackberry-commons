package com.monits.blackberry.commons.uielements;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYPoint;


/**
 * Button field that can be customized
 *
 */
public class CustomButtonField extends Field {
	
	private String label;
	private int fontColorFocused;
	private int fontColorUnfocused;
	private int backgroundColorFocused;
	private int backgroundColorUnfocused;
	private XYPoint labelTopLeftPoint;	


	/**
	 * Constructor.
	 * @param text Button text.
	 */
	public CustomButtonField(String text){
		super();
		this.label = text;
		labelTopLeftPoint = new XYPoint();
	}

	/**
	 * Constructor.
	 * @param text Button text.
	 * @param style Button style.
	 */
	public CustomButtonField(String text, long style){
		super(style);
		this.label = text;
		labelTopLeftPoint = new XYPoint();
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

		graphics.setColor(isFocus() ? backgroundColorFocused : backgroundColorUnfocused);
		graphics.fillRoundRect(0, 0, getWidth()+25, getHeight(), 6, 6);

		graphics.setColor(backgroundColorUnfocused);
		graphics.drawRoundRect(1, 1, getWidth()+25, getHeight(), 6, 6);

		graphics.setColor(fontColorUnfocused);
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
