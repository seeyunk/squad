package com.squad.rpc.client;

import java.util.List;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.entity.Message;
import com.squad.looper.AsyncLooper.MSG;
import com.squad.looper.IMessageNotifier;
import com.squad.protobuf.packet.Squad.ControlServicePacket;
import com.squad.protobuf.packet.Squad.JoinSquadPacket;
import com.squad.protobuf.packet.Squad.Packet;
import com.squad.protobuf.packet.Squad.Packet.Type;
import com.squad.protobuf.packet.Squad.PongPacket;
import com.squad.protobuf.packet.Squad.ScoutDetectedPacket;
import com.squad.protobuf.packet.Squad.ScoutPacket;
import com.squad.protobuf.packet.Squad.StreamType;
import com.squad.protobuf.packet.Squad.SystemStatePacket;
import com.squad.rpc.ChannelState;
import com.squad.rpc.stream.IStream;
import com.squad.rpc.stream.StreamManager;

public class ScoutChannelHandler extends SimpleChannelHandler {
	private static final Logger logger = LoggerFactory.getLogger( ScoutChannelHandler.class );
	protected final IMessageNotifier notifier;
	protected final StreamManager streamManager;
	private volatile ChannelState channelState = ChannelState.NOT_CONNECTED;
	private volatile Channel channel;
	
	public ScoutChannelHandler( IMessageNotifier notifier, StreamManager streamManager ) {
		this.notifier = notifier;
		this.streamManager = streamManager;
	}
	
	public IStream openFileStream( List<String> targetIds, StreamType streamType, String fileName, long fileSize ) {
		return this.streamManager.openFileStream(targetIds, this.channel, fileName, fileSize );
	}
	
	public ChannelFuture write0( Object message ) {
		final Channel channel = this.channel;
		return this.write( channel, message );
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		Channel channel = e.getChannel();
		Packet packet = (Packet)e.getMessage();
		
		if ( packet.getType() == Type.PONG ) {
			this.notifier.sendMessage( new Message( MSG.PONG ) );
		}
		else if ( packet.getType() == Type.ACK_JOIN_SQUAD ) {
			JoinSquadPacket jsp = packet.getJoinSquadPacket();
			this.notifier.sendMessage( new Message( MSG.ACK_JOIN_SQUAD, jsp.getScoutId() ) );
		}
		else if ( packet.getType() == Type.SYNC_NODES ) {
			this.notifier.sendMessage( new Message( MSG.SYNC_NODES, packet.getSourceId(), packet.getNodePacketList() ) );
		}
		else if ( packet.getType() == Type.ACK_SYNC_NODES ) {
			this.notifier.sendMessage( new Message( MSG.ACK_SYNC_NODES, packet.getSourceId() ) );
		}
		else if ( packet.getType() == Type.NODE_OPERATION ) {
			this.notifier.sendMessage( new Message( MSG.NODE_OPERATION, packet.getNodePacket() ) );
		}
		else if ( packet.getType() == Type.NODE_OPERATION_COMMIT ) {
			this.notifier.sendMessage( new Message( MSG.NODE_OPERATION_COMMIT, packet.getNodePacket() ) );
		}
		else if ( packet.getType() == Type.NODE_OPERATION_ROLLBACK ) {
			this.notifier.sendMessage( new Message( MSG.NODE_OPERATION_ROLLBACK, packet.getNodePacket() ) );
		}
		else if ( packet.getType() == Type.CONTROL_SERVICE ) {
			ControlServicePacket csp = packet.getControlServicePacket();
			this.notifier.sendMessage( new Message( MSG.CONTROL_SERVICE, packet.getSourceId(), csp.getServiceId(), csp.getServiceState() ) );
		}
		else if ( packet.getType() == Type.ACK_CONTROL_SERVICE ) {
			ControlServicePacket csp = packet.getControlServicePacket();
			this.notifier.sendMessage( new Message( MSG.ACK_CONTROL_SERVICE, csp.getServiceId(), csp.getServiceState() ) );
		}
		else if ( packet.getType() == Type.SCOUT_DETECTED ) {
			ScoutDetectedPacket sdp = packet.getScoutDetectedPacket();
			this.notifier.sendMessage( new Message( MSG.SCOUT_DETECTED, sdp ) );
		}
		else if ( packet.getType() == Type.SCOUT_STATE_CHANGED ) {
			ScoutPacket scoutPacket = packet.getScoutPacket();
			this.notifier.sendMessage( new Message( MSG.SCOUT_STATE_CHANGED, scoutPacket.getScoutId(), scoutPacket.getScoutState() ) );
		}
		else if ( packet.getType() == Type.EXAMINE_SERVICES ) {
			this.notifier.sendMessage( new Message( MSG.HANDLE_EXAMINE_SERVICES, packet.getNotifyServicesPacket() ) );
		}
		else if ( packet.getType() == Type.REQUEST_SERVICES ) {
			this.notifier.sendMessage( new Message( MSG.REQUEST_SERVICES_RECV, packet.getRequestServicesPacket() ) );
		}
		else if ( packet.getType() == Type.STREAM_OPEN_REQ ) {
			logger.debug( "[RECV]::STREAM_OPEN_REQ");
			this.streamManager.handleStreamOpenReqPacket( channel, packet.getStreamOpenReqPacket() );
		}
		else if ( packet.getType() == Type.STREAM_OPEN_RES ) {
			logger.debug( "[RECV]::STREAM_OPEN_RES");
			this.streamManager.handleStreamOpenResPacket( packet.getStreamOpenResPacket() );
		}
		else if ( packet.getType() == Type.STREAM_BEGIN ) {
			logger.debug( "[RECV]::STREAM_BEGIN");
			this.streamManager.handleStreamBeginPacket( packet.getStreamBeginPacket() );
		}
		else if ( packet.getType() == Type.STREAM_RUN ) {
			this.streamManager.handleStreamRunPacket( packet.getStreamRunPacket() );
		}
		else if ( packet.getType() == Type.STREAM_END ) {
			logger.debug( "[RECV]::STREAM_END");
			this.streamManager.handleStreamEndPacket( packet.getStreamEndPacket() );
		}
		else if ( packet.getType() == Type.STREAM_CLOSE ) {
			logger.debug( "[RECV]::STREAM_CLOSE");
			this.streamManager.handleStreamClosePacket( packet.getStreamClosePacket() );
		}
		else if ( packet.getType() == Type.SERVICE_STATE_CHANGED ) {
			this.notifier.sendMessage( new Message( MSG.SERVICE_STATE_CHANGED, packet.getServiceStatePacket() ) );
		}
		else if ( packet.getType() == Type.SYSTEM_STATE ) {
			SystemStatePacket systemStatePacket = (SystemStatePacket)packet.getSystemStatePacket();
			//scoutManager.updateSystemState( systemStatePacket.getScoutId(), SystemState.readPacket( systemStatePacket ) );
		}
		
		super.messageReceived(ctx, e);
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		final Channel channel = ctx.getChannel();
		this.channel = channel;
		this.channelState = ChannelState.CONNECTED;
		this.notifier.sendMessage( new Message( MSG.JOIN_SQUAD ) );
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		if ( this.channelState != ChannelState.RECONNECT_FAILED &&
			this.channelState != ChannelState.BEING_CLOSED_BY_CLIENT &&
			this.channelState != ChannelState.CLOSED_BY_CLIENT ) {
			this.notifier.sendMessage( new Message( MSG.TRY_RECONNECT ) );
		}
		else {
			this.channelState = ChannelState.CLOSED_BY_SERVER;
		}
		
		logger.debug( "[CLOSED] Last channel state:: {}", this.channelState );
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		Throwable cause = e.getCause();
		if ( cause != null ) {
			logger.debug( cause.getMessage() );
		}
		ctx.getChannel().close();
		
		
	}
	
	private ChannelFuture write( Channel channel, Object object ) {
		ChannelFuture future = null;
		if ( channel != null && channel.isConnected() ) {
			future = channel.write( object );
		}
		
		return future;
	}
	
	public void close() {
		if ( this.channel != null ) {
			this.channel.close().addListener( new ChannelFutureListener() {
	
				@Override
				public void operationComplete(ChannelFuture future)
						throws Exception {
					// TODO Auto-generated method stub
					if ( future.isSuccess() ) {
						future.awaitUninterruptibly();
					}
					else {
						ScoutChannelHandler.this.channel.close().addListener( this );
					}
				}} );
		}
		
		channelState = ChannelState.CLOSED_BY_CLIENT;
		
	}
	
	public void setChannelState( ChannelState channelState ) {
		this.channelState = channelState;
	}
	
	public boolean isConnected() {
		return this.channelState == ChannelState.CONNECTED ? true : false;
	}
}
