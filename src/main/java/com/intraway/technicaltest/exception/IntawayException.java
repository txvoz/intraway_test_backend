package com.intraway.technicaltest.exception;

import java.util.LinkedHashMap;

/**
 *
 * @author Gustavo Rodriguez
 */
public class IntawayException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1081697385262968531L;
	
	LinkedHashMap<String, String> exceptions;

    public IntawayException(String message) {
        super(message);
    }

    public IntawayException(LinkedHashMap<String, String> exceptions) {
        this.exceptions = exceptions;
    }

    public LinkedHashMap<String, String> getExceptions() {
        return exceptions;
    }

    public void setExceptions(LinkedHashMap<String, String> exceptions) {
        this.exceptions = exceptions;
    }

}
