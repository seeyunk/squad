package com.squad.rpc.server.cli;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.ChannelGroupFutureListener;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.context.ISquadContext;

public class SquadCliChannelHandler extends SimpleChannelHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ChannelGroup channelGroup = new DefaultChannelGroup( "squad-server-channels" );
	private final ISquadContext ctx; 
			
	public SquadCliChannelHandler( ISquadContext ctx ) {
		this.ctx = ctx;
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		final Channel channel = e.getChannel();
		this.channelGroup.add( channel );
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		final Channel channel = e.getChannel();
		this.channelGroup.remove( channel );
	}
	
	public void shutdown() {
		this.channelGroup.close().addListener( new ChannelGroupFutureListener() {

			@Override
			public void operationComplete(ChannelGroupFuture future)
					throws Exception {
				// TODO Auto-generated method stub
				final ChannelGroup channelGroup = SquadCliChannelHandler.this.channelGroup;
				if ( !future.isCompleteSuccess() ) {
					channelGroup.close().addListener( this );
				}
				else {
					channelGroup.clear();
				}
			}} );
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		super.messageReceived(ctx, e);
	}
	
	public ChannelGroupFuture broadcast( Object message ) {
		return this.channelGroup.write( message );
	}
}
