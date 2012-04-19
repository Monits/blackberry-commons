package com.monits.blackberry.commons.services.request;

public interface RequestHandler {

	public void onFailure(String messageError);

	public void onSuccess(Object o);

}
