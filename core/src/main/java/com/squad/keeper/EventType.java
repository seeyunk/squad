package com.squad.keeper;

public enum EventType {
	NODE_CREATED( (byte)0x01 ),
	NODE_DELETED( (byte)0x02 ),
	NODE_DATA_CHANGED( (byte)0x03 );
	
	private byte id;
	private EventType( byte id ) {
		this.id = id;
	}
	
	private byte getId() {
		return this.id;
	}
}
