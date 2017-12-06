package com.squad.rpc.stream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.protobuf.packet.Squad.StreamBeginPacket;
import com.squad.protobuf.packet.Squad.StreamEndPacket;
import com.squad.protobuf.packet.Squad.StreamRunPacket;

public class ObjectStreamWriteHandler implements IStreamHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handleStreamBegin(StreamBeginPacket packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleStreamRun(StreamRunPacket packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleStreamRun(ChannelBuffer buffer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleStreamEnd(StreamEndPacket packet) {
		// TODO Auto-generated method stub
		
	}
}
