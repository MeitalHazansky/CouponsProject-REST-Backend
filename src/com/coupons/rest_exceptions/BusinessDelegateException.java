package com.coupons.rest_exceptions;

public class BusinessDelegateException extends Exception {
	private static final long serialVersionUID = 1L;

	public BusinessDelegateException() {
		super("There is a problem with connecting to the Spring income micro service.");
	}
	
	public BusinessDelegateException(String message) {
		super(message);
	}
}
