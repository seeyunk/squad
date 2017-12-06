package com.squad.util;

@SuppressWarnings("serial")
public class SquadRuntimeException extends RuntimeException {
	public SquadRuntimeException() {}
	
	public SquadRuntimeException( String message ) {
		super( message );
	}
	
	public SquadRuntimeException( String message, Throwable cause ) {
		super( message, cause );
	}
	
	public SquadRuntimeException( Throwable cause ) {
		super( cause );
	}
}
