package com.monits.blackberry.commons.services.implementations;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import com.monits.blackberry.commons.connection.HTTPRequestRunnable;
import com.monits.blackberry.commons.exception.UserAuthenticationException;
import com.monits.blackberry.commons.services.ConnectionService;
import com.monits.blackberry.commons.services.request.RequestHandler;

/**
 * @see ConnectionService
 *
 * @author Rodrigo Pereyra
 *
 */
public class ConnectionServiceImpl implements ConnectionService {

	private static URLEncodedPostData postData;
	private static String response = null;

	/**
	 * Constructor.
	 */
	public ConnectionServiceImpl() {
		postData = new URLEncodedPostData("UTF-8", false);
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#executePost(java.lang.String, com.monits.blackberry.commons.services.request.RequestHandler)
	 */
	public String executePost(String url, RequestHandler handler) throws UserAuthenticationException {
		String response = post(url);
		
		if(response != null) {
			handler.onSuccess(response);
		} else {
			handler.onFailure("There was an error while trying to get server response.");
		}
		
		return response;
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#executeGet(java.lang.String, com.monits.blackberry.commons.services.request.RequestHandler)
	 */
	public String executeGet(String url, RequestHandler handler) throws UserAuthenticationException {
		String response = get(url);
		
		if(response != null) {
			handler.onSuccess(response);
		} else {
			handler.onFailure("There was an error while trying to get server response.");
		}
		
		return response;		
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#executeAsincronicGet(java.lang.String, com.monits.blackberry.commons.services.request.RequestHandler)
	 */
	public String executeAsincronicGet(String url, RequestHandler handler) throws UserAuthenticationException {
		String response = asincronicGet(url);
		
		if(response != null) {
			handler.onSuccess(response);
		} else {
			handler.onFailure("There was an error while trying to get server response.");
		}
		return response;
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#executeAsincronicPost(java.lang.String, com.monits.blackberry.commons.services.request.RequestHandler)
	 */
	public String executeAsincronicPost(String url, RequestHandler handler) throws UserAuthenticationException {
		String response = asincronicPost(url);

		if(response != null) {
			handler.onSuccess(response);
		} else {
			handler.onFailure("There was an error while trying to get server response.");
		}
		return response;
	}

	private String post(String url) throws UserAuthenticationException {
		
		HTTPRequestRunnable getData = new HTTPRequestRunnable(url, postData);
		getData.setRequest(HTTPRequestRunnable.POST);
		getResponse(getData);
		
		String errorMessage = getData.getErrorMessage();
		clearData();
		if (errorMessage == null) {
			if (getData.gotResponse()) {
				byte[] response = getData.getResponse();
				return new String(response);
			} 
			
			//No response
			return null;
		} else {
			//Error
			return null;
		}
	}

	private String get(String url) throws UserAuthenticationException {

		HTTPRequestRunnable getData = new HTTPRequestRunnable(url, null);
		getData.setRequest(HTTPRequestRunnable.GET);
		getResponse(getData);

		String errorMessage = getData.getErrorMessage();
		clearData();
		if (errorMessage == null) {
			if (getData.gotResponse()) {
				return getData.getResponseAsString();
			} 

			//No response
			return null;
		} else {
			//Error
			return null;
		}
	}

	private String asincronicPost(final String url) throws UserAuthenticationException {
		Thread threadToRun =  new Thread(){
			public void run() {
				try {
					response = post(url);
				} catch (UserAuthenticationException e) {
					// Authentication fails
				}
			}
		};
		threadToRun.start();
		return response;
	}

	private String asincronicGet(final String url) throws UserAuthenticationException {
		Thread threadToRun =  new Thread(){
			public void run() {
				try {
					response = get(url);
				} catch (UserAuthenticationException e) {
					// Authentication fails
				}
			}
		};
		threadToRun.start();
		return response;
	}

	private void clearData() {
		postData = new URLEncodedPostData("UTF-8", false);
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#addParameter(java.lang.String, java.lang.Object)
	 */
	public void addParameter(String name, Object value) {
		postData.append(name, (String)value);
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#addHeader(java.lang.String, java.lang.String)
	 */
	public void addHeader(String name, String value) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#addParameter(java.lang.Object)
	 */
	public void addParameter(Object value) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.monits.blackberry.commons.services.ConnectionService#useAuthentication(boolean)
	 */
	public void useAuthentication(boolean addAuthentication) {
		// TODO Auto-generated method stub
	}

	/**
	 * Run processing that will connect to the identified URL and retrieve the response from the
	 * specified HttpRequest
	 * @param getData - Request to process
	 */
	private void getResponse(final HTTPRequestRunnable getData) {

		// Run the code that must be executed in the Background
		try {
			getData.run();
		} catch (Throwable t) {
			throw new RuntimeException("Exception detected while waiting: " + t.toString());
		}
	}

}