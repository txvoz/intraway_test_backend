package com.intraway.technicaltest.exception;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author Gustavo Rodriguez
 *
 */
public class ClientException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6344553059336155056L;
	
	private final HttpStatus statusCode;
	
	public ClientException(String message) {
		super(message);
		this.statusCode = null;
	}
	
	public ClientException(String message, HttpStatus statusCode) {
		super( message);
		this.statusCode = statusCode;

	}
	
	public ClientException(String message, HttpStatus statusCode, Throwable cause) {
		super( message, cause );
		this.statusCode = statusCode;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}
	
}
