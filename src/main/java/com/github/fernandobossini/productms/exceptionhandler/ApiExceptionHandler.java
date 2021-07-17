package com.github.fernandobossini.productms.exceptionhandler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.annotation.JsonProperty;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,	                                                               
			HttpHeaders headers, HttpStatus status, WebRequest request) {	
		String message = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		Error error = new Error(HttpStatus.BAD_REQUEST.value(), message);
		return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, 
			HttpHeaders headers, HttpStatus status, WebRequest request) {		
		Error error = new Error(HttpStatus.BAD_REQUEST.value(), createStringError(ex.getBindingResult()));
		return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
	}
		
	private String createStringError(BindingResult bindingResult) {
		List<ObjectError> listObjectError = bindingResult.getAllErrors();
		
		String message = "";
		if (!listObjectError.isEmpty()) {
			message = listObjectError.stream().map(l -> l.getDefaultMessage()).collect(Collectors.joining(", "));			 
		}
		
		return message;
	}
	
	public static class Error {

		@JsonProperty("status_code")
		private Integer statusCode;
		
		@JsonProperty("message")
		private String message;

		public Error(Integer statusCode, String message) {
			this.statusCode = statusCode;
			this.message = message;
		}

		public Integer getStatusCode() {
			return statusCode;
		}

		public String getMessage() {
			return message;
		}
	}	
}
