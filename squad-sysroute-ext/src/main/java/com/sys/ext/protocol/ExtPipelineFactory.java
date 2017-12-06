package com.sys.ext.protocol;

import java.nio.charset.Charset;

import org.apache.camel.component.netty.NettyConsumer;
import org.apache.camel.component.netty.ServerPipelineFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.annotation.SquadComponent;

@SquadComponent( id="sys-ext-spf" )
public class ExtPipelineFactory extends ServerPipelineFactory {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast( "encoder", new StringEncoder( Charset.forName( "utf-8" ) ) );
		pipeline.addLast( "decoder-delim", new DelimiterBasedFrameDecoder( Integer.MAX_VALUE, Delimiters.lineDelimiter()[0] ) );
		pipeline.addLast( "decoder", new StringDecoder( Charset.forName( "utf-8" ) ) );
		return null;
	}

	@Override
	public ServerPipelineFactory createPipelineFactory(NettyConsumer consumer) {
		// TODO Auto-generated method stub
		return this;
	}
}
