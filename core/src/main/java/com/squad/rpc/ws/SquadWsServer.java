package com.squad.rpc.ws;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.context.ISquadContext;
import com.squad.rpc.server.Acceptor;

@Deprecated
public class SquadWsServer extends Acceptor {
	private static final Logger logger = LoggerFactory.getLogger( SquadWsServer.class );
	private static final int DEFAULT_WS_PORT = 8080;
	private final int port;
	private final ISquadContext ctx; 
	
	public SquadWsServer( ISquadContext ctx, int port ) {
		if ( port == -1 ) {
			this.port = DEFAULT_WS_PORT;
		}
		else {
			this.port = port;
		}
		
		this.ctx = ctx;
	}
	
	public void start() {
		super.createBootstrap( 1 );
		super.setPipelineFactory( new SquadWebSocketPipelineFactory( new SquadWsServerChannelHandler( this.ctx ) ) );
		super.bind( new InetSocketAddress( this.port ) );
		logger.info( "Admin server binded at port {}", this.port );
	}
	
	public void release() {
		super.release();
		logger.info( "Websocket server stopped" );
	}
	
	public ChannelHandler getChannelHandler() throws Exception{
		return this.bootstrap.getPipelineFactory().getPipeline().get( "handler" );
	}
	
	public void broadcast( Object object ) throws Exception {
		SquadWsServerChannelHandler channelHandler = (SquadWsServerChannelHandler)this.getChannelHandler();
		channelHandler.broadcast( object );
	}
	
	private class SquadWebSocketPipelineFactory implements ChannelPipelineFactory {
		private final SquadWsServerChannelHandler channelHandler;
		public SquadWebSocketPipelineFactory( SquadWsServerChannelHandler channelHandler ) {
			this.channelHandler = channelHandler;
		}
		
		@Override
		public ChannelPipeline getPipeline() throws Exception {
			// TODO Auto-generated method stub
			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast( "decoder", new HttpRequestDecoder() );
			pipeline.addLast( "aggregator", new HttpChunkAggregator( DEFAULT_CHUNK_SIZE ) );
			pipeline.addLast( "encoder", new HttpResponseEncoder() );
			pipeline.addLast( "handler", this.channelHandler );
			return pipeline;
		}
	}
}
