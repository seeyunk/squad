package com.squad.rpc.stream;

import org.jboss.netty.buffer.ChannelBuffer;

import com.squad.protobuf.packet.Squad.StreamBeginPacket;
import com.squad.protobuf.packet.Squad.StreamEndPacket;
import com.squad.protobuf.packet.Squad.StreamRunPacket;

public interface IStreamHandler {
	public void handleStreamBegin(StreamBeginPacket packet);
	public void handleStreamRun(StreamRunPacket packet);
	public void handleStreamRun( ChannelBuffer buffer );
	public void handleStreamEnd(StreamEndPacket packet);
}
