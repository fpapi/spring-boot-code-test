package com.gloot.springbootcodetest.errors;

public class EntityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 8162617223907653508L;

	public EntityNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityNotFoundException(String message) {
		super(message);
	}

}
