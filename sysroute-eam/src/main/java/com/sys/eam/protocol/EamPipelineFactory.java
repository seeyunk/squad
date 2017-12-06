package com.sys.eam.protocol;

import java.nio.charset.Charset;

import org.apache.camel.component.netty.ClientPipelineFactory;
import org.apache.camel.component.netty.NettyProducer;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.annotation.SquadComponent;

@SquadComponent( id="sys-eam-cpf" )
public class EamPipelineFactory extends ClientPipelineFactory {
	private final Logger logger = LoggerFactory.getLogger( EamPipelineFactory.class );

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast( "encoder", new StringEncoder( Charset.forName( "euc-kr" ) ) );
		pipeline.addLast( "decoder", new StringDecoder( Charset.forName( "euc-kr" ) ) );
		return pipeline;
	}

	
	@Override
	public ClientPipelineFactory createPipelineFactory(NettyProducer producer) {
		// TODO Auto-generated method stub
		return this;
	}
	
}
