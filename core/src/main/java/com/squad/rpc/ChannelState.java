package com.squad.rpc;


public enum ChannelState {
	NOT_CONNECTED( (byte)0x00 ),
	BEING_CONNECTED( (byte)0x01 ),
    CONNECTED( (byte) 0x02),
    AVAILABLE( (byte)0x03 ),
    CONNECT_FAILED( (byte)0x04 ),
    RECONNECT_FAILED( (byte)0x05 ),
	BEING_CLOSED_BY_CLIENT( (byte)0x10 ),
	CLOSED_BY_CLIENT( (byte)0x11 ),
	UNEXPECTED_CLOSED_BY_CLIENT( (byte)0x12 ),
	BEING_CLOSED_BY_SERVER( (byte)0x20 ),
	CLOSED_BY_SERVER( (byte)0x21 ),
	UNEXPECTED_CLOSED_BY_SERVER( (byte)0x22 ),
	ERROR_UNKNOWN( (byte)0xFF );
	
	private final byte id;
	
	private ChannelState( byte id ) {
		this.id = id;
	}
	
	public byte getChannelStateId() {
		return this.id;
	}
}
