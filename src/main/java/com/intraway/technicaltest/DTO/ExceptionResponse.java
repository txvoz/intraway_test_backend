package com.intraway.technicaltest.DTO;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @author Gustavo Rodriguez
 *
 */
@Data
public class ExceptionResponse implements IResponse, Serializable {

	private static final long serialVersionUID = -3111527912582132707L;
	
	private Long timestamp;
	private int status;
	private String error;
	private String exception;
	private String message;
	private String path;

}
