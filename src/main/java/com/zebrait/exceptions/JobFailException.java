package com.zebrait.exceptions;

public class JobFailException extends Exception {
	private static final long serialVersionUID = 1L;

	public JobFailException() {
		super();
	}

	public JobFailException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
