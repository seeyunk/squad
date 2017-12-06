package com.squad.rpc.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import com.squad.protobuf.packet.Squad;

public class LeaderPipelineFactory implements ChannelPipelineFactory {
	private LeaderChannelHandler channelHandler;
	
	public LeaderPipelineFactory( LeaderChannelHandler channelHandler ) {
		this.channelHandler = channelHandler;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast( "frameDecoder", new ProtobufVarint32FrameDecoder() );
		pipeline.addLast( "decoder", new ProtobufDecoder( Squad.Packet.getDefaultInstance() ) );
		pipeline.addLast( "lengthPrefender", new ProtobufVarint32LengthFieldPrepender() );
		pipeline.addLast( "encoder", new ProtobufEncoder() );
		pipeline.addLast( "handler", this.channelHandler );
		
		return pipeline;
	}
	
}
