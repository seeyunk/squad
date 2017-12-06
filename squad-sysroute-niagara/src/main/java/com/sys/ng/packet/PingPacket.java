package com.sys.ng.packet;

import java.io.Serializable;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class PingPacket extends AbstractPacket implements Serializable {
	private static final long serialVersionUID = 2495277840023848931L;
	
	public PingPacket() {
		super( IPacket.PING );
	}
	
	public static PingPacket decode() {
		return new PingPacket();
	}

	@Override
	public ChannelBuffer encode() {
		// TODO Auto-generated method stub
		return ChannelBuffers.EMPTY_BUFFER;
	}
	
}
