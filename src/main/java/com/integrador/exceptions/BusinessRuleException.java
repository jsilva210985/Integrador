package com.integrador.exceptions;

public class BusinessRuleException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private int code;

	public BusinessRuleException(int code, String message) {
		super(message);
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
