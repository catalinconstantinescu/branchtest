package com.branchtest.error;

import lombok.Getter;

@Getter
public class ResourceSerializationException extends RuntimeException{
	private final String id;
	private final String type;
	
	public ResourceSerializationException(String id, String type, String message) {
        super(message);
        this.id=id;
        this.type = type;
    }
	
	public ResourceSerializationException(String id, String type, String message, Throwable cause) {
        super(message, cause);
        this.id=id;
        this.type = type;
    }
}
