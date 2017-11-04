package com.schedule.generator.exception;

public class ExcelException extends Exception {

	private static final long serialVersionUID = 1L;

	public ExcelException(String message) {
		super(message);
	}
	public ExcelException(String message, Throwable t) {
		super(message, t);
	}
	
}
