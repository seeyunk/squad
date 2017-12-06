package com.sys.ng.handler;

import org.apache.camel.component.netty.NettyConsumer;
import org.apache.camel.component.netty.ServerPipelineFactory;
import org.apache.camel.component.netty.handlers.ServerChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.annotation.SquadComponent;
import com.sys.ng.codec.NgDecoder;
import com.sys.ng.codec.NgEncoder;

@SquadComponent( id="ng-spf" )
public class NgPipelineFactory extends ServerPipelineFactory {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private NettyConsumer consumer;
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast( "encoder", new NgEncoder() );
		pipeline.addLast( "decoder", new NgDecoder() );
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
