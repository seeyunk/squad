package com.squad.rpc.client;

import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.Timer;

import com.squad.protobuf.packet.Squad;

public class ScoutPipelineFactory implements ChannelPipelineFactory {
	private SimpleChannelHandler channelHandler;
	private final Timer timeoutTimer;
	public ScoutPipelineFactory( SimpleChannelHandler socketHandler, Timer timer ) {
		this.channelHandler = socketHandler;
		this.timeoutTimer = timer;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast( "frameDecoder", new ProtobufVarint32FrameDecoder() );
		pipeline.addLast( "decoder", new ProtobufDecoder( Squad.Packet.getDefaultInstance() ) );
		pipeline.addLast( "lengthPrefender", new ProtobufVarint32LengthFieldPrepender() );
		pipeline.addLast( "encoder", new ProtobufEncoder() );
		pipeline.addLast( "readTimeoutHandler", new ReadTimeoutHandler( timeoutTimer, 3000L, TimeUnit.MILLISECONDS ));
		pipeline.addLast( "handler", this.channelHandler );
		
		return pipeline;
	}
}
