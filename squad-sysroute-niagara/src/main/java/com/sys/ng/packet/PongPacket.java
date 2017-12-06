package com.sys.ng.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class PongPacket extends AbstractPacket {
	public PongPacket() {
		super( IPacket.PONG );
	}

	@Override
	public ChannelBuffer encode() {
		// TODO Auto-generated method stub
		return ChannelBuffers.EMPTY_BUFFER;
	}
}
