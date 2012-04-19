package com.monits.blackberry.commons.connection;

/**
 * HTTPRequestRunnable
 * Create a Runnable that will 
 * process the URL supplied and supply the data that the server returns
 * 
 * See Constructor for more details
 */

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

import com.monits.blackberry.commons.Logger;
import com.thirdparty.connectivity.HttpConnectionFactory;
import com.thirdparty.connectivity.NoMoreTransportsException;

public class HTTPRequestRunnable extends Object implements Runnable {
	public static final int POST = 1;
	public static final int GET = 2;

	private static final String CONTENT_TYPE_HEADER = "content-type";

	private static final String CHARSET = "charset=";
	
	private boolean _gotResponse; // Have we got a response from the Server?
	// Only used in case a caller wishes to query it

	private String _connectionURL = null; // Actual connection

	private URLEncodedPostData _parameters;

	private String _status = null;
	private String _errorMessage = null;
	private byte [] _response = null;
	private String _contentType = null;
	private int responseCode = -1;

	private String _encoding = null;

	private int request;

	/**
	 * This utility class handles GETs and POSTs, via HTTP and HTTPS,
	 * with various connection methods and options
	 * and will even cope with Basic Authentication, adn running in the background.
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
		_gotResponse = false;
		_response = null;
	}
	
	/**
	 * external check on whether a response has arrived
	 * @return true if response has arrived.
	 */
	public boolean gotResponse() {
		return _gotResponse;
	}

	/**
	 * return response
	 * @return data bytes
	 */
	public byte [] getResponse() {
		return _response;
	}
	
	/**
	 * Retrieves the response as string in the proper encoding
	 * @return The response as string using the proper encoding.
	 */
	public String getResponseAsString() {
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
	 * @return full connnection URL used
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
		processStatus("Start of Request processing");
		boolean tryAgain = false;
		OutputStream os = null;
		int rc = -1;
		
		try {
			String connectionMethod;
			
			do {
				tryAgain = false;
				Logger.logEventInfo("Contacting: " + _connectionURL + ".");
				
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
							Logger.logEventInfo(new String(postBytes));  // Note assumption post data is UTF-8, it might not be...
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
			Logger.logEventError(exMsg);
			Logger.logEventError(ioe.toString() + "\n" + _connectionURL);
			return;
		} catch (NoMoreTransportsException e) {
			Logger.logEventError(e.toString() + "\n" + _connectionURL);
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

			Logger.logEventInfo(getResponseToLog(rc,c).toString());

			if (rc != HttpConnection.HTTP_OK) {
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
				
				Logger.logEventError(errorMessage);
				Logger.logEventError(_connectionURL);
				return;
			}

			processStatus("Response Received");
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

			String encoding = c.getEncoding();

			if (encoding == null) {
				StringMatch matcher = new StringMatch(CHARSET, false);
				String contentType = c.getHeaderField(CONTENT_TYPE_HEADER);
				
				int pos = matcher.indexOf(contentType);
				if (pos >= 0) {
					encoding = contentType.substring(pos + CHARSET.length());
				}
			}

			// now have, in response byte array, the data that we have received
			Logger.logEventInfo("Response (" + Integer.toString(response.length) + ")");
			Logger.logEventInfo(byteToString(response)); // Note assumption response is UTF8 - it might not be ....
			returnResponse(response, encoding);

		} catch (Exception e) {
			String exMsg = "Unexpected Exception Receiving Response - try later.";
			String exceptionMessage = e.toString();
			Logger.logEventError(exMsg);
			Logger.logEventError(exceptionMessage + "\n" + _connectionURL);
			processError(exMsg);
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
	 * got a reply!!!!
	 * @param reply - byte array with response in it
	 * @param encoding The encoding of the reply
	 */
	private void returnResponse(byte [] reply, String encoding) {
		_gotResponse = true;
		_response = reply;
		_encoding  = encoding;
	}

	/**
	 * Update Status
	 * @param statusString <description>
	 */
	private void processStatus(final String statusString) {
		Logger.logEventInfo("Request status update: " + statusString);
		_status = statusString;
	}

	/**
	 * Advise Error
	 * @param errorMessage - text that may be displayed
	 */
	private void processError(final String errorMessage) {
		Logger.logEventInfo("Request error: " + errorMessage);
		_status = "Error";
		_errorMessage = errorMessage;
	}

	
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

	private StringBuffer getResponseToLog(int rc, HttpConnection c) throws IOException {
		
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
		 
		 return sb;
	}

	public void setRequest(int request) {
		this.request = request;
	}
}
