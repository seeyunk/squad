package com.squad.rpc.server.cli;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.protobuf.cli.packet.SquadCli.SquadCliPacket;
import com.squad.protobuf.packet.Squad;

public class SquadCliPipelineFactory implements ChannelPipelineFactory {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final SquadCliChannelHandler channelHandler;
	
	public SquadCliPipelineFactory( SquadCliChannelHandler channelHandler ) {
		this.channelHandler = channelHandler;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast( "frameDecoder", new ProtobufVarint32FrameDecoder() );
		pipeline.addLast( "decoder", new ProtobufDecoder( SquadCliPacket.getDefaultInstance() ) );
		pipeline.addLast( "lengthPrefender", new ProtobufVarint32LengthFieldPrepender() );
		pipeline.addLast( "encoder", new ProtobufEncoder() );
		pipeline.addLast( "handler", this.channelHandler );
		
		return pipeline;
	}
}
