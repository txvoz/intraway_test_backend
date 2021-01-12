package com.intraway.technicaltest.exception;

/**
 * 
 * @author Gustavo Rodriguez
 *
 */
public class ResourceNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8924743012701274556L;
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	public ResourceNotFoundException(String message, Throwable cause) {
		super( message, cause );
	}

}
