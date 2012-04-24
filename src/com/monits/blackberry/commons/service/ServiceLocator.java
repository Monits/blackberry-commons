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
package com.monits.blackberry.commons.service;

import java.util.Hashtable;

import com.monits.blackberry.commons.service.impl.ConnectionServiceImpl;
import com.monits.blackberry.commons.service.impl.ImageResizeServiceImpl;
import com.monits.blackberry.commons.service.impl.LocationServiceImpl;
import com.monits.blackberry.commons.service.impl.ResourceServiceImpl;
import com.monits.blackberry.commons.service.impl.ScreenTypeServiceImpl;

/**
 * Utility class that locates services and returns them
 * @author Rodrigo Pereyra.
 *
 */
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

	/**
	 * Return an instance of ImageResizeService
	 * @return ImageResizeService instance.
	 */
	public static ImageResizeService getImageResizeService() {
		return (ImageResizeService) services.get(ImageResizeService.class);
	}

	/**
	 * Return an instance of ResourceService
	 * @return ResourceService instance.
	 */
	public static ResourceService getResourceService() {
		return (ResourceService) services.get(ResourceService.class);
	}

	/**
	 * Return an instance of ScreenTypeService
	 * @return ScreenTypeService instance.
	 */
	public static ScreenTypeService getScreenTypeService() {
		return (ScreenTypeService) services.get(ScreenTypeService.class);
	}

	/**
	 * Return an instance of LocationService.
	 * @return LocationService instance.
	 */
	public static LocationService getLocationService() {
		return (LocationService) services.get(LocationService.class);
	}

	/**
	 * Return an instance of ConnectionService.
	 * @return ConnectionService instance.
	 */
	public static ConnectionService getConnectionService() {
		return (ConnectionService) services.get(ConnectionService.class);
	}
}