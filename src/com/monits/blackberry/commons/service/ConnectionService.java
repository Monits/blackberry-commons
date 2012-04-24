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

import net.rim.device.api.userauthenticator.UserAuthenticationException;

import com.monits.blackberry.commons.service.request.RequestHandler;

/**
 * Provide methods for execute requests.
 * @author Rodrigo Pereyra
 *
 */
public interface ConnectionService {

	/**
	 * Execute a POST request.
	 * @param url The URL to POST
	 * @param handler Object that will handle the response
	 * @return The server response, or null in case of no response or error.
	 * @throws UserAuthenticationException When the authentication process fails.
	 */
	public String executePost(String url, RequestHandler handler);

	/**
	 * Execute a GET request
	 * @param url The URL to GET
	 * @param handler Object that will handle the response
	 * @return The server response, or null in case of no response or error.
	 * @throws UserAuthenticationException
	 */
	public String executeGet(String url, RequestHandler handler);

	/**
	 * Execute a GET method on a separate thread.
	 * @param url The URL to GET
	 * @param handler Object that will handle the response
	 */
	public void executeAsyncGet(final String url, RequestHandler handler);

	/**
	 * Execute a POST method separate thread.
	 * @param url The URL to POST
	 * @param handler Object that will handle the response
	 */
	public void executeAsyncPost(final String url, RequestHandler handler);

	/**
	 * Add a parameter to the request
	 * @param The parameter name
	 * @param Parameter value.
	 */
	public void addParameter(String name, Object value);

	/**
	 * Add's a Header to the request.
	 * @param Header name
	 * @param Header value
	 */
	public void addHeader(String name, String value);

}
