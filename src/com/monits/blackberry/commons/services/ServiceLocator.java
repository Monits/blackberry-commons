package com.monits.blackberry.commons.services;

import java.util.Hashtable;

import com.monits.blackberry.commons.services.implementations.ConnectionServiceImpl;
import com.monits.blackberry.commons.services.implementations.ImageResizeServiceImpl;
import com.monits.blackberry.commons.services.implementations.LocationServiceImpl;
import com.monits.blackberry.commons.services.implementations.ResourceServiceImpl;
import com.monits.blackberry.commons.services.implementations.ScreenTypeServiceImpl;

public class ServiceLocator {
	private static Hashtable services;

	static {
		services = new Hashtable();
		services.put(ImageResizeService.class, new ImageResizeServiceImpl());
		services.put(ResourceService.class, new ResourceServiceImpl());
		services.put(ScreenTypeService.class, new ScreenTypeServiceImpl());
		services.put(LocationService.class, new LocationServiceImpl());
		services.put(ConnectionService.class, new ConnectionServiceImpl());
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

	public static LocationService getLocationService() {
		return (LocationService) services.get(LocationService.class);
	}

	public static ConnectionService getConnectionService() {
		return (ConnectionService) services.get(ConnectionService.class);
	}
}
