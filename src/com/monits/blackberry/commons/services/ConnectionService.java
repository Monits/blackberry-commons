package com.monits.blackberry.commons.services;

import com.monits.blackberry.commons.exception.UserAuthenticationException;
import com.monits.blackberry.commons.services.request.RequestHandler;

/**
 * Provide methods for execute requests.
 * @author Rodrigo Pereyra
 *
 */
public interface ConnectionService {

	/**
	 * Execute a POST request.
	 * @param url
	 * @return The server response, or null in case of no response or error.
	 * @throws UserAuthenticationException When the authentication process fails.
	 */
	public String executePost(String url, RequestHandler handler) throws UserAuthenticationException;

	/**
	 * Execute a GET request
	 * @param url 
	 * @return
	 * @throws UserAuthenticationException
	 */
	public String executeGet(String url, RequestHandler handler) throws UserAuthenticationException;

	/**
	 * @param url
	 * @param handler
	 * @return
	 * @throws UserAuthenticationException
	 */
	public String executeAsincronicGet(final String url, RequestHandler handler) throws UserAuthenticationException;

	/**
	 * @param url
	 * @param handler
	 * @return
	 * @throws UserAuthenticationException
	 */
	public String executeAsincronicPost(final String url, RequestHandler handler) throws UserAuthenticationException;

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

	/**
	 * Add a parameter to the request.
	 * @param Parameter value.
	 */
	public void addParameter(Object value);

	/**
	 * 
	 * @param addAuthentication
	 */
	public void useAuthentication(boolean addAuthentication);

}
