package com.monits.blackberry.commons.services;

import java.util.Hashtable;

import com.monits.blackberry.commons.services.implementations.ImageResizeServiceImpl;
import com.monits.blackberry.commons.services.implementations.ResourceServiceImpl;
import com.monits.blackberry.commons.services.implementations.ScreenTypeServiceImpl;

public class ServiceLocator {
	private static Hashtable services;

	static {
		services = new Hashtable();
		services.put(ImageResizeService.class, new ImageResizeServiceImpl());
		services.put(ResourceService.class, new ResourceServiceImpl());
		services.put(ScreenTypeService.class, new ScreenTypeServiceImpl());
	}
	
	public static ImageResizeService getImageResizeService() {
		return (ImageResizeService) services.get(ImageResizeService.class);
	}
	
	public static ResourceService getResourceService() {
		return (ResourceService) services.get(ResourceService.class);
	}

	public static ScreenTypeService getScreenTypeService() {
		return (ScreenTypeService) services.get(ScreenTypeService.class);
	}
}
