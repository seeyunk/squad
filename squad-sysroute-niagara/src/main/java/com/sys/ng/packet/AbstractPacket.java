package com.sys.ng.packet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPacket implements IPacket {
	private static final Logger logger = LoggerFactory.getLogger( AbstractPacket.class );
	private final byte command;
	
	public AbstractPacket( byte command ) {
		this.command = command;
	}
	
	@Override
	public byte getCommand() {
		return this.command;
	}
	
}
