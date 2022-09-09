package com.branchtest.error;

import lombok.Getter;

@Getter
public class ResourceLookupThrottleException extends RuntimeException{
	private final String id;
	private final String type;
	
    public ResourceLookupThrottleException(String id, String type, String message) {
        super(message);
        this.id=id;
        this.type = type;
    }
    
    public ResourceLookupThrottleException(String id, String type, String message, Throwable cause) {
        super(message, cause);
        this.id=id;
        this.type = type;
    }
}
