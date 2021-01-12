package com.intraway.technicaltest.enums;

import lombok.Getter;

/**
 * 
 * @author Gustavo Rodriguez
 * Enum para el manejo de estados de las respuestas
 */
@Getter
public enum EStatusRequest {

	E001("001"),
	E002("002"),
	E005("005");
	
	private String code;
	
	EStatusRequest(String code) {
		this.code = code;
	}
	
	
}
