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
package com.monits.blackberry.commons.service.request;

/**
 * This interface must be implemented by the classes that want to handle HTTP Request.
 * @author Rodrigo Pereyra.
 *
 */
public interface RequestHandler {

	/**
	 * The request was completed but not successful.
	 * @param message Description failure text.
	 */
	public void onFailure(String message);

	/**
	 * The request was completed successfully.
	 * @param response A string representation of the error.
	 * @param responseCode The response code.
	 */
	public void onSuccess(String response, int responseCode);

	/**
	 * An error occurs while the request was processing the request.
	 * @param t Stack trace error.
	 */
	public void onError(Throwable t);
}