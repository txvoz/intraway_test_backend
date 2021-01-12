package com.intraway.technicaltest.exception;

import java.util.Date;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.UnexpectedTypeException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.intraway.technicaltest.DTO.ExceptionResponse;

/**
 * 
 * @author Gustavo Rodriguez
 *Controlador de excepciones 
 */
@ControllerAdvice
public class ExceptionController {

	final static Logger logger = LogManager.getLogger(ExceptionController.class);

	/**
	 * Server error
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ServerException.class)
	public ResponseEntity<ExceptionResponse> handleHttpServerErrorException(ServerException e) {
		Date date = new Date();
		ExceptionResponse response = new ExceptionResponse();
		response.setTimestamp(date.getTime());
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		response.setError(e.getMessage());
		response.setException(e.getMessage());
		response.setMessage(e.getMessage());
		response.setPath("/");
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * Not found in database
	 * @param e
	 * @return
	 */
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ExceptionResponse> handleNoSuchElementException(NoSuchElementException e) {
		Date date = new Date();
		ExceptionResponse response = new ExceptionResponse();
		response.setTimestamp(date.getTime());
		response.setStatus(HttpStatus.NOT_FOUND.value());
		response.setError(HttpStatus.NOT_FOUND.toString());
		response.setException(e.getMessage());
		response.setMessage("El registro no fue encontrado en la busqueda");
		response.setPath("/");
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	/**
	 * Error en ingreso de formatos tipo date no puede ser nulo o vacio
	 * @param e
	 * @return
	 */
	@ExceptionHandler(UnexpectedTypeException.class)
	public ResponseEntity<ExceptionResponse> handleUnexpectedTypeException(UnexpectedTypeException e) {
		Date date = new Date();
		ExceptionResponse response = new ExceptionResponse();
		response.setTimestamp(date.getTime());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError(HttpStatus.BAD_REQUEST.toString());
		response.setException(e.getMessage());
		response.setMessage("Error de input, datos tipo fecha no pueden estar vacios");
		response.setPath("/");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Error cuando desde el cliente no se envia adecuadamente el formato json /error token json
	 * @param e
	 * @return
	 */
	@ExceptionHandler(JsonEOFException.class)
	public ResponseEntity<ExceptionResponse> handleJsonEOFException(JsonEOFException e) {
		Date date = new Date();
		ExceptionResponse response = new ExceptionResponse();
		response.setTimestamp(date.getTime());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError(HttpStatus.BAD_REQUEST.toString());
		response.setException(e.getMessage());
		response.setMessage("Error en el envio de datos desde el cliente al servidor tipo json");
		response.setPath("/");		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Error en ingreso de formatos numericos
	 * @param e
	 * @return
	 */
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<ExceptionResponse> handleNumberFormatException(NumberFormatException e) {
		Date date = new Date();
		ExceptionResponse response = new ExceptionResponse();
		response.setTimestamp(date.getTime());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError(HttpStatus.BAD_REQUEST.toString());
		response.setException(e.getMessage());
		response.setMessage("Error de input, no se puede convertir un alfanumerico a un tipo int");
		response.setPath("/");	
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Empty inputs
	 * @param e
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException e) {
		StringBuilder strBuild = new StringBuilder();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			if(!strBuild.toString().trim().equals("")) {
				strBuild.append(",");
			}
			strBuild.append(fieldName);
			strBuild.append(" ");
			strBuild.append(errorMessage);
		});
		ExceptionResponse response = new ExceptionResponse();
		Date date = new Date();
		response.setTimestamp(date.getTime());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError(HttpStatus.BAD_REQUEST.toString());
		response.setException(e.getMessage());
		response.setMessage("Verificar los siguientes inputs: "+strBuild.toString());
		response.setPath("/");	
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Error cuando no se puede deserializar el json
	 * @param e
	 * @return
	 */
	@ExceptionHandler(JsonParseException.class)
	public ResponseEntity<ExceptionResponse> handleJsonParseException(JsonParseException e) {
		ExceptionResponse response = new ExceptionResponse();
		Date date = new Date();
		response.setTimestamp(date.getTime());
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setError(HttpStatus.BAD_REQUEST.toString());
		response.setException(e.getMessage());
		response.setMessage("No se puede deserializar el request desde el cliente");
		response.setPath("/");	
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Server error
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ClientException.class)
	public ResponseEntity<ExceptionResponse> handleHttpServerClientException(HttpServletRequest req,ClientException e) {
		ExceptionResponse response = new ExceptionResponse();
		Date date = new Date();
		response.setTimestamp(date.getTime());
		response.setStatus(e.getStatusCode().value());
		response.setError(e.getStatusCode().toString());
		response.setException(e.toString());
		response.setMessage(e.getMessage());
		response.setPath(req.getPathInfo());	
		return new ResponseEntity<>(response, e.getStatusCode());
	}
	
	
}
