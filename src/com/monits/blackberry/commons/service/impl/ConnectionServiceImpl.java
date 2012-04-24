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
package com.monits.blackberry.commons.service.impl;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import com.monits.blackberry.commons.connection.HTTPRequestRunnable;
import com.monits.blackberry.commons.service.ConnectionService;
import com.monits.blackberry.commons.service.request.RequestHandler;

/**
 * This class provides method to execute HTTP Request.
 *
 * @author Rodrigo Pereyra
 */
public class ConnectionServiceImpl implements ConnectionService {

	private URLEncodedPostData parameters;

	/**
	 * Constructor.
	 */
	public ConnectionServiceImpl() {
		clearData();
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#executePost(java.lang.String, com.monits.blackberry.commons.services.request.RequestHandler)
	 */
	public String executePost(String url, RequestHandler handler) {
		return execute(url, HTTPRequestRunnable.POST, handler);
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#executeGet(java.lang.String, com.monits.blackberry.commons.services.request.RequestHandler)
	 */
	public String executeGet(String url, RequestHandler handler) {
		return execute(url, HTTPRequestRunnable.GET, handler);
	}

	/*
	 * (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#executeAsyncGet(java.lang.String, com.monits.blackberry.commons.services.request.RequestHandler)
	 */
	public void executeAsyncGet(String url, RequestHandler handler) {
		executeAsync(url, HTTPRequestRunnable.GET, handler);
	}

	/*
	 * (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#executeAsyncPost(java.lang.String, com.monits.blackberry.commons.services.request.RequestHandler)
	 */
	public void executeAsyncPost(String url, RequestHandler handler) {
		executeAsync(url, HTTPRequestRunnable.POST, handler);
	}

	/**
	 * Clear the request parameters.
	 */
	private void clearData() {
		parameters = new URLEncodedPostData("UTF-8", false);
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#addParameter(java.lang.String, java.lang.Object)
	 */
	public void addParameter(String name, Object value) {
		parameters.append(name, value.toString());
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String name, String value) {
		// TODO Auto-generated method stub
	}

	/**
	 * Performs a request.
	 * 
	 * @param url The URL in which will run the selected method.
	 * @param httpMethod Request method.
	 * @param hadler Object that will handle the response.
	 * 
	 * @return If the request was successful returns the response, otherwise returns null.
	 */
	private String execute(String url, int httpMethod, RequestHandler handler) {

		HTTPRequestRunnable request = new HTTPRequestRunnable(url, parameters);
		request.setRequest(httpMethod);

		// Run the code that must be executed in the Background
		try {
			request.run();
			clearData();

			String errorMessage = request.getErrorMessage();

			if (errorMessage == null) {
				String ret = request.getResponseAsString();
				handler.onSuccess(ret, request.getResponseCode());
				return ret;
			} else {
				handler.onFailure(errorMessage);
			}
		} catch (Throwable t) {
			handler.onError(t);
		}

		return null;
	}

	/**
	 * Performs a request on a separate thread
	 * 
	 * @param url The URL in which will run the selected method.
	 * @param httpMethod Request method.
	 * @param hadler Object that will handle the response.
	 */
	private void executeAsync(final String url, final int httpMethod, final RequestHandler handler) {
		new Thread(new Runnable() {

			public void run() {
				execute(url, httpMethod, handler);
			}
		}).start();
	}
}