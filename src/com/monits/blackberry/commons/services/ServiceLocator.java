package com.monits.blackberry.commons.services;

import java.util.Hashtable;

import com.monits.blackberry.commons.services.implementations.ImageResizeServiceImpl;

public class ServiceLocator {
	private static Hashtable services;

	static {
		services = new Hashtable();
		services.put(ImageResizeService.class, new ImageResizeServiceImpl());
	}

	public static ImageResizeService getImageResizeService() {
		return (ImageResizeService) services.get(ImageResizeService.class);
	}
}
