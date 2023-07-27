package com.commerce.dscatalog.services.exceptions;

public class OrderAuthorizationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public OrderAuthorizationException(String msg) {
		super(msg);
	}
}
