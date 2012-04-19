package com.monits.blackberry.commons.model;

import javax.microedition.location.Coordinates;

import net.rim.device.api.system.Bitmap;

/**
 * Model for map marker.
 * @author Rodrigo Pereyra.
 *
 */
public class Marker {

	private long id;	
	private String name;
	private String adress;
	private Coordinates coor;
	private Bitmap bitmap;
	private Bitmap focusbitmap;
	private String description;

	/**
	 * Constructor.
	 * @param coor Marker coordinates
	 * @param bitmap Normal state bitmap.
	 * @param focusbitmap Focus state bitmap.
	 */
	public Marker(Coordinates coor, Bitmap bitmap, Bitmap focusbitmap) {
		super();
		this.coor = coor;
		this.bitmap = bitmap;
		this.focusbitmap = focusbitmap;
	}

	/**
	 * Constructor.
	 * @param id Marker id.
	 * @param name of the marker.
	 * @param address of the marker.
	 * @param coordinates of the marker.
	 * @param bitmap Normal state bitmap
	 * @param focusbitmap Focus state bitmap.
	 * @param description of the marker 
	 */
	public Marker(long id, String name, String adress, Coordinates coor,
			Bitmap bitmap, Bitmap focusbitmap, String description) {
		super();
		this.id = id;
		this.name = name;
		this.adress = adress;
		this.coor = coor;
		this.bitmap = bitmap;
		this.focusbitmap = focusbitmap;
		this.description = description;
	}

	/**
	 * @return Marker's id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return Marker's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Marker's address
	 */
	public String getAdress() {
		return adress;
	}

	/**
	 * @return Marker's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the normal state bitmap.
	 * @param bitmap new normal state bitmap.
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * @return Marker's coordinates
	 */
	public Coordinates getCoor() {
		return coor;
	}

	/**
	 * @return Normal state bitmap marker 
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @return Focus state bitmap marker
	 */
	public Bitmap getFocusBitmap() {
		return focusbitmap;
	}

	/**
	 * Set the focus state bitmap
	 * @param focusbitmap New focus state bitmap
	 */
	public void setFocusbitmap(Bitmap focusbitmap) {
		this.focusbitmap = focusbitmap;
	}
}