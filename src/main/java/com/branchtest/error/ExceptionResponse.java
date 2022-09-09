package com.branchtest.error;

import lombok.Data;

@Data
public class ExceptionResponse {
	
	private final String resourceId;
	
	private final String resourceType;
	
	private final String url;

	private final String errorMessage;
}
