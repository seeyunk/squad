package com.sys.eam.protocol;

import org.apache.camel.component.netty.NettyConsumer;
import org.apache.camel.component.netty.ServerPipelineFactory;
import org.apache.camel.component.netty.handlers.ServerChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.annotation.SquadComponent;
import com.squad.protobuf.cli.packet.SquadCli.SquadCliPacket;

@SquadComponent( id="sys-squad-cli-cpf" ) 
public class SquadCliPipelineFactory extends ServerPipelineFactory {
	private static final Logger logger = LoggerFactory.getLogger(SquadCliPipelineFactory.class);
	private NettyConsumer consumer;
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast( "frameDecoder", new ProtobufVarint32FrameDecoder() );
		pipeline.addLast( "decoder", new ProtobufDecoder( SquadCliPacket.getDefaultInstance() ) );
		pipeline.addLast( "lengthPrefender", new ProtobufVarint32LengthFieldPrepender() );
		pipeline.addLast( "encoder", new ProtobufEncoder() );
		pipeline.addLast( "handler", new ServerChannelHandler( this.consumer ) );
		return pipeline;
	}

	@Override
	public ServerPipelineFactory createPipelineFactory(NettyConsumer consumer) {
		// TODO Auto-generated method stub
		this.consumer = consumer;
		return this;
	}

}
