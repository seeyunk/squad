package com.squad.rpc.stream;

public enum StreamState {
	NOT_OPENED( (byte)0x00 ),
	OPENED( (byte)0x01 ),
    RUN( (byte) 0x02),
    CLOSED( (byte)0x03 ),
	ERROR_UNKNOWN( (byte)0xFF );
	
	private final byte id;
	private StreamState( byte id ) {
		this.id = id;
	}
	
	public byte StreamStateId() {
		return this.id;
	}
}
