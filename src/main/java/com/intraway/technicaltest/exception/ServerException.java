package com.intraway.technicaltest.exception;

/**
 * 
 * @author Gustavo Rodriguez
 *
 */
public class ServerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5466534926202090602L;
	
	public ServerException(String message) {
		super(message);
	}
	
	public ServerException(String message, Throwable cause) {
		super( message, cause );
	}

}
