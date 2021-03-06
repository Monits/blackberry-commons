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
package com.monits.blackberry.commons.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.microedition.io.HttpConnection;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.util.StringMatch;

import com.monits.blackberry.commons.logger.Logger;
import com.thirdparty.connectivity.HttpConnectionFactory;
import com.thirdparty.connectivity.NoMoreTransportsException;

/**
 * HTTPRequestRunnable
 * Create a Runnable that will 
 * process the URL supplied and supply the data that the server returns
 * 
 * See Constructor for more details
 */
public class HTTPRequestRunnable extends Object implements Runnable {

	public static final Logger logger = Logger.getLogger(HTTPRequestRunnable.class);

	public static final int POST = 1;
	public static final int GET = 2;

	private static final String CONTENT_TYPE_HEADER = "content-type";

	private static final String CHARSET = "charset=";
	
	private String _connectionURL = null; // Actual connection

	private URLEncodedPostData _parameters;

	private String _errorMessage = null;
	private byte [] _response = null;
	private String _contentType = null;
	private int responseCode = -1;

	private String _encoding = null;

	private int request;

	/**
	 * This utility class handles GETs and POSTs, via HTTP and HTTPS,
	 * with various connection methods and options
	 * and will even cope with Basic Authentication, and running in the background.
	 * This means it is actually a bit complicated, sorry about that...
	 * 
	 * An HTTP Request is created with these parameters
	 * @param targetURL - the actual URL to be used
	 * @param parameters - data to be added as POST / GET data.
	 */
	public HTTPRequestRunnable(String targetURL, URLEncodedPostData parameters) {
		super();
		_connectionURL = targetURL;
		_parameters = parameters;
		_response = null;
	}

	/**
	 * return response
	 * @return data bytes
	 */
	public byte[] getResponse() {
		return _response;
	}
	
	/**
	 * Retrieves the response as string in the proper encoding
	 * @return The response as string using the proper encoding.
	 */
	public String getResponseAsString() {
		if (_response == null) {
			return null;
		}
		
		try {
			return new String(_response, _encoding);
		} catch (UnsupportedEncodingException e) {
			return new String(_response);
		}
	}

	/**
	 * get Error Message
	 * @return error Message
	 */
	public String getErrorMessage() {
		return _errorMessage;
	}

	/**
	 * get Connection URL used
	 * @return full connection URL used
	 */
	public String getConnectionURL() {
		return _connectionURL;
	}

	/**
	 * get Content Type returned
	 * @return Content Type
	 */
	public String getResponseContentType() {
		return _contentType;
	}

	
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * Actual process which gets the response by trying the HTTP request
	 */
	public void run () {
		HttpConnection c = null;
		InputStream is = null;
		boolean tryAgain = false;
		OutputStream os = null;
		int rc = -1;
		
		try {
			String connectionMethod;
			
			do {
				tryAgain = false;
				logger.info("Contacting: " + _connectionURL + ".");
				
				String url = _connectionURL;
				
				if (request == GET) {
					url += "?" + _parameters.toString();
				}
				
				HttpConnectionFactory factory = new HttpConnectionFactory(url);
				do {
					c = factory.getNextConnection();
					switch (request) {
					case POST:
						if (_parameters != null && _parameters.size() > 0) {
							connectionMethod = HttpConnection.POST;
							c.setRequestMethod(connectionMethod);
							c.setRequestProperty(HttpProtocolConstants.HEADER_CONTENT_TYPE, _parameters.getContentType());
							// Now create our 'posting' stuff
							byte [] postBytes = _parameters.getBytes();
							logger.info(new String(postBytes));  // Note assumption post data is UTF-8, it might not be...
							c.setRequestProperty(HttpProtocolConstants.HEADER_CONTENT_LENGTH, Integer.toString(postBytes.length));
							os = c.openOutputStream();
							os.write(postBytes);
							os.flush();
							os.close();
							os = null;
						} 
						break;

					case GET:
						connectionMethod = HttpConnection.GET;
						c.setRequestMethod(connectionMethod);
						
						break;
					default:
						// ???
						logger.error("Unsupported http method " + request);
						break;
					}
					
					rc = c.getResponseCode();
					responseCode = rc;
				} while (rc == HttpConnection.HTTP_FORBIDDEN); // Sometimes the BIS gets in the way when accessing non standard ports
			} while (tryAgain);
		} catch (IOException ioe) {
			String exMsg = "Unexpected Exception sending request - correct URL or Connection used.";
			// See if message just timed out
			if ( ioe instanceof InterruptedIOException ) {
				exMsg = "Message timed out - check connectivity.";
			}
			logger.error(exMsg);
			logger.error(ioe.toString() + "\n" + _connectionURL);
			return;
		} catch (NoMoreTransportsException e) {
			logger.error(e.toString() + "\n" + _connectionURL);
			return;
		} finally {
			if ( os != null ) {
				try {
					os.close();
				} catch (Exception e) {
				}
				os = null;
			}
		}

		try {

			logger.info(getResponseToLog(rc,c));

			if (rc != HttpConnection.HTTP_OK) {
				logErrorMessage(rc);
				return;
			}

			is = processResponse(c);

		} catch (Exception e) {
			logger.error("Unexpected Exception Receiving Response - try later.");
			logger.error(e.toString() + "\n" + _connectionURL);
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch ( IOException ioe ) {
			}
			try {
				if (c != null) {
					c.close();
					c = null;
				}
			} catch ( IOException ioe ) {
			}
		}
	}

	/**
	 * Processes the response.
	 * @param c Connection to the server.
	 * @return is Response input
	 * @throws IOException If an I/O error occurs.
	 */
	private InputStream processResponse(HttpConnection c) throws IOException {
		InputStream is;
		is = c.openInputStream();

		// Get the ContentType in case the User wants to know
		_contentType = c.getType();

		// Get the length and process the data
		int len = (int)c.getLength();
		byte [] response;
		int bytesRead;
		if (len > 0) {
			// Length supplied - just read that amount
			int actual = 0;
			bytesRead = 0 ;
			response = new byte[len];
			// We have found reading it in one go doesn't work as well as the following
			while ((bytesRead != len) && (actual != -1)) {
				actual = is.read(response, bytesRead, len - bytesRead);
				bytesRead += actual;
			}
		} else {
			// No length supplied - read until EOF
			response = IOUtilities.streamToBytes(is);
			bytesRead = response.length;
		}

		// Look for an encoding
		String encoding = c.getEncoding();
		if (encoding == null) {
			// Not autodetected, look for a content-type HTTP header
			StringMatch matcher = new StringMatch(CHARSET, false);
			String contentType = c.getHeaderField(CONTENT_TYPE_HEADER);
			
			int pos = matcher.indexOf(contentType);
			if (pos >= 0) {
				encoding = contentType.substring(pos + CHARSET.length());
			}
		}

		// now have, in response byte array, the data that we have received
		logger.info("Response (" + Integer.toString(response.length) + ")");
		logger.info(byteToString(response)); // Note assumption response is UTF8 - it might not be ....
		returnResponse(response, encoding);
		return is;
	}

	/**
	 * According to an error code, this method log a proper message.
	 * @param rc Response code.
	 */
	private void logErrorMessage(int rc) {
		// We stop here - but try to give the user (and log) something useful.  
		String errorMessage;
		if ( rc < 300 ) {
			// Processed without error, but why not OK?
			errorMessage = "Request not completed - try later. Code: " + rc + ".";
		} else
		if ( rc < 400 ) {
			// Redirection, which should be handled....
			// Should update the requested address with a new one?
			errorMessage = "Requested address has changed - contact support. Code: " + rc + ".";
		} else
		if ( rc < 500 ) {
			// We did something wrong? Most commonly Server not up....
			errorMessage = "Request not understood - try later. Code: " + rc + ".";
		} else
		if ( rc < 600 ) {
			// Server error...
			errorMessage = "Processing Error - try later. Code: " + rc + ".";
		} else {
			errorMessage = "Unexpected HTTP response - try later. Code: " + rc + ".";
		}
		
		logger.error(errorMessage);
		logger.error(_connectionURL);
	}

	/**
	 * got a reply!!!!
	 * @param reply - byte array with response in it
	 * @param encoding The encoding of the reply
	 */
	private void returnResponse(byte [] reply, String encoding) {
		_response = reply;
		_encoding  = encoding;
	}

	/**
	 * Retrieve a String representation of the byte array.
	 * @param item Array to parse
	 * @return Byte array to string.
	 */
	private String byteToString (byte[] item) {
		
		StringBuffer hexString = new StringBuffer();
	
		for (int i = 0; i < item.length; i++) {
			String token = Integer.toHexString(0xFF & item[i]);
	
			// Make sure each is exactly 2 chars long
			if (token.length() < 2) {
				hexString.append("0");
			}
	
			hexString.append(token);
		}
	
		return hexString.toString();
		
	}

	/**
	 * Log response with the corresponding response code.
	 * @param rc Response code.
	 * @param c Connection from where to get the response to log
	 * @return A message containing the response info.
	 * @throws IOException When an I/O error occurs.
	 */
	private String getResponseToLog(int rc, HttpConnection c) throws IOException {
		
		 StringBuffer sb = new StringBuffer();
		 sb.append("Response: " + Integer.toString(rc) + ", " + c.getResponseMessage() + "\n");
		 for ( int j = 0; j < 20; j++ ) {
			 // Limit to 20 headers, just to reduce data sent to log.
			 String key = c.getHeaderFieldKey(j);
			 String field = c.getHeaderField(j);
			 if ( key == null && j > 0 ) {
				 // Stop when there are no more!
				 break;
			 }
			 sb.append("Header: " + Integer.toString(j) + ", " +
								  key + " : " + field + "\n"); 
			 if ( key.equalsIgnoreCase(HttpProtocolConstants.HEADER_LOCATION) ) {
				 // Save redirect location in case we are redirected
				 // Note potential bug, if we have more than 20 headers this will not be found!
			 }
		 }
		 
		 return sb.toString();
	}

	/**
	 * Set the request method. 
	 * @param request The request method.
	 */
	public void setRequest(int request) {
		this.request = request;
	}
}
