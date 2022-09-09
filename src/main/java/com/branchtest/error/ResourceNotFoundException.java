package com.branchtest.error;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
	
	private final String id;
	private final String type;
	
    public ResourceNotFoundException(String id, String type, String message) {
        super(message);
        this.id=id;
        this.type = type;
    }
    
    public ResourceNotFoundException(String id, String type, String message, Throwable cause) {
        super(message, cause);
        this.id=id;
        this.type = type;
    }
}
