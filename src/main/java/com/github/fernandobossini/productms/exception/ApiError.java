package com.github.fernandobossini.productms.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class ApiError {
	
	private Error error;

	public ApiError(BindingResult bindingResult, HttpStatus status) {
		List<ObjectError> listObjectError = bindingResult.getAllErrors();
		
		String message = "";
		if (!listObjectError.isEmpty()) {
			message = listObjectError.stream().map(l -> l.getDefaultMessage()).collect(Collectors.joining(", "));			 
		}
		this.error = new Error(status.value(), message);
	}	

	public Error getError() {
		return error;
	}

	@JsonPropertyOrder({ "statusCode", "message" })
	public static class Error {

		@JsonProperty("status_code")
		private Integer statusCode;
		
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
