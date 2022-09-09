package com.branchtest.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import feign.FeignException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ExceptionResponse handleResourceNotFound(final ResourceNotFoundException e,
			final HttpServletRequest request) {

		ExceptionResponse error = new ExceptionResponse(e.getId(), e.getType(), request.getRequestURL().toString(), "Resource not found");

		return error;
	}
	
	@ExceptionHandler(ResourceLookupThrottleException.class)	
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public @ResponseBody ExceptionResponse handleResourceNotFound(final ResourceLookupThrottleException e,
			final HttpServletRequest request) {

		ExceptionResponse error = new ExceptionResponse(e.getId(), e.getType(), request.getRequestURL().toString(), "Temporary lookup throttle limit reached.");


		return error;
	}
	
	@ExceptionHandler(ResourceSerializationException.class)	
	@ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public @ResponseBody ExceptionResponse handleResourceNotFound(final ResourceSerializationException e,
			final HttpServletRequest request) {

		ExceptionResponse error = new ExceptionResponse(e.getId(), e.getType(), request.getRequestURL().toString(), "Failed serialization.");


		return error;
	}
}
