package com.intraway.technicaltest.DTO;

import java.io.Serializable;

import lombok.Data;


/**
 * 
 * @author Gustavo Rodriguez
 *
 */
@Data
public class SuccessResponse implements IResponse, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2947114153320728378L;
	
	private Long timestamp;
	private String code;
	private String description;
	private Object data;
}
