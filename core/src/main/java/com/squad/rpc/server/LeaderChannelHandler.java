package com.squad.rpc.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.ChannelGroupFutureListener;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroupFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.protobuf.packet.Squad.JoinSquadPacket;
import com.squad.protobuf.packet.Squad.NodePacket;
import com.squad.protobuf.packet.Squad.Packet;
import com.squad.protobuf.packet.Squad.Packet.Type;
import com.squad.protobuf.packet.Squad.ScoutDetectedPacket;
import com.squad.protobuf.packet.Squad.ScoutPacket;
import com.squad.protobuf.packet.Squad.ScoutState;

public class LeaderChannelHandler extends SimpleChannelHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final ChannelGroup channelGroup = new DefaultChannelGroup( "squad-server-channels" );
	private final ConcurrentMap<Long, AtomicInteger> nodeOpMap = new ConcurrentHashMap<Long, AtomicInteger>();
	private BlockingQueue<Long> nodeQueue = new LinkedBlockingQueue<Long>();
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		final Channel channel = e.getChannel();
		if ( channel == null ) {
			throw new NullPointerException( "Acceptor socket must not be null" );
		}
		
		Packet packet = (Packet)e.getMessage();
		String scoutId = (String)channel.getAttachment();
		if ( packet.getType() == Type.PING ) {
			this.handlePingPacket( channel );
		}
		else if ( packet.getType() == Type.JOIN_SQUAD ) {
			this.handleJoinSquadPacket( channel, packet.getJoinSquadPacket() );
		}
		else if ( packet.getType() == Type.SYNC_NODES ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.ACK_SYNC_NODES ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.NODE_OPERATION ) {
			this.handleNodeOperation( packet.getNodePacket() );
		}
		else if ( packet.getType() == Type.ACK_NODE_OPERATION ) {
			this.handleAckNodeOperation( packet.getNodePacket() );
		}
		else if ( packet.getType() == Type.SCOUT_DETECTED ) {
			ScoutDetectedPacket sdp = packet.getScoutDetectedPacket();
			this.handleScoutDetectedPacket( packet.getTargetIdList(), sdp );
		}
		else if ( packet.getType() == Type.SCOUT_DETECTED_WELCOME ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.EXAMINE_SERVICES ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.REQUEST_SERVICES ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.SCOUT_STATE_CHANGED ) {
			ScoutPacket sp = packet.getScoutPacket();
			this.handleScoutStateChanged( sp.getScoutId(), sp.getScoutState() );
		}
		else if ( packet.getType() == Type.STREAM_OPEN_REQ ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.STREAM_OPEN_RES ) {
			logger.debug( "[{}] Scout received STREAM_OPEN_RES", scoutId );
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.STREAM_BEGIN ) {
			logger.debug( "[{}] Scout received STREAM_BEGIN", scoutId );
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.STREAM_RUN ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.STREAM_END ) {
			logger.debug( "[{}] Scout received STREAM_END", scoutId );
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.STREAM_CLOSE ) {
			logger.debug( "[{}] Scout received STREAM_CLOSE", scoutId );
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.CONTROL_SERVICE ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.ACK_CONTROL_SERVICE ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.SERVICE_STATE_CHANGED ) {
			this.multicast( packet );
		}
		else if ( packet.getType() == Type.SYSTEM_STATE ) {
			this.broadcast( packet );
		}
		
		super.messageReceived(ctx, e);
	}
	
	public void handleNodeOperation( NodePacket nodePacket ) {
		final Long timestamp = System.nanoTime();
		final NodePacket.Builder builder = nodePacket.toBuilder();
		builder.setTimestamp( timestamp );
		
		final NodePacket resNodePacket = builder.build();
		final Packet packet = Packet.newBuilder()
						.setType( Type.NODE_OPERATION )
						.setNodePacket( resNodePacket )
						.build();
		
		this.broadcast( packet ).addListener( new ChannelGroupFutureListener() {
			@Override
			public void operationComplete(ChannelGroupFuture future)
					throws Exception {
				// TODO Auto-generated method stub
				if ( !future.isCompleteSuccess() ) {
					LeaderChannelHandler.this.broadcast( packet ).addListener( this );
				}
				else {
					final int size = future.getGroup().size();
					LeaderChannelHandler.this.nodeQueue.add( packet.getNodePacket().getTimestamp() );
					LeaderChannelHandler.this.nodeOpMap.put( timestamp, new AtomicInteger( size ) );
				}
			}
			
		});
		//@@logger.debug( "[LEADER]::[OPERATIONS]::[MAPSIZE]::[{}]", this.nodeOpMap.size() );
	}
	
	public void broadcastNodeOperationRollback( final NodePacket nodePacket ) {
		final long timestamp = nodePacket.getTimestamp();
		final Packet rollbackPacket = Packet.newBuilder()
				.setType( Type.NODE_OPERATION_ROLLBACK )
				.setNodePacket( nodePacket )
				.build();
		this.broadcast( rollbackPacket )
			.addListener( new ChannelGroupFutureListener() {
				@Override
				public void operationComplete(ChannelGroupFuture future)
					throws Exception {
					// TODO Auto-generated method stub
					if ( !future.isCompleteSuccess() ) {
						LeaderChannelHandler.this.broadcast( rollbackPacket ).addListener( this );
						logger.debug( "Failed to send all..." );
					}
					else {
						LeaderChannelHandler.this.nodeQueue.remove( timestamp );
						LeaderChannelHandler.this.nodeOpMap.remove( timestamp );
					}
				}
			});
		logger.debug( "[LEADER]::[ROLLBACK]::[MAPSIZE]::[{}]", this.nodeOpMap.size() );
	}
	
	public void handleAckNodeOperation( NodePacket nodePacket ) {
		Iterator<Long> i = this.nodeQueue.iterator();
		for ( ; i.hasNext(); ) {
			synchronized( this ) {
				final Long newTimestamp = i.next();
				NodePacket resNodePacket = nodePacket.toBuilder()
										.setTimestamp( newTimestamp )
										.build();
				final Packet packet = Packet.newBuilder()
						.setType( Type.NODE_OPERATION_COMMIT )
						.setNodePacket( resNodePacket )
						.build();
				
				AtomicInteger remains = this.nodeOpMap.get( newTimestamp );
				if ( remains != null && remains.decrementAndGet() == 0 ) {
					this.broadcast( packet ).addListener( new ChannelGroupFutureListener() {
						@Override
						public void operationComplete(ChannelGroupFuture future)
								throws Exception {
							// TODO Auto-generated method stub
							if ( !future.isCompleteSuccess()  ) {
								LeaderChannelHandler.this.broadcast( packet ).addListener( this );
								//@@logger.debug( "Failed to send all..." );
							}
							else {
								LeaderChannelHandler.this.nodeQueue.remove( newTimestamp );
								LeaderChannelHandler.this.nodeOpMap.remove( newTimestamp );
								//@@logger.debug( "[NODE_QUEUE]::[{}]", nodeQueue.size() );
							}
						}
					} );
				}
			}
			
		}
		//@@logger.debug( "[LEADER]::[COMMIT]::[MAPSIZE]::[{}]::[QUEUE]::[{}]", this.nodeOpMap.size(), this.nodeQueue.size() );
	}
	
	private void handlePingPacket( final Channel channel ) {
		Packet pongPacket = Packet.newBuilder()
						.setType( Type.PONG )
						.build();
		channel.write( pongPacket );
	}
	
	private void handleJoinSquadPacket( final Channel channel, final JoinSquadPacket jsp ) {
		String scoutId = jsp.getScoutId();
		channel.setAttachment( scoutId );
		this.channelGroup.add( channel );
		
		Packet packet = Packet.newBuilder()
						.setType( Type.ACK_JOIN_SQUAD )
						.setJoinSquadPacket( jsp )
						.build();
		this.broadcast( packet );
	}
	
	private void handleScoutDetectedPacket( List<String> targetIds, ScoutDetectedPacket sdp ) {
		Packet packet = Packet.newBuilder()
						.setType( Type.SCOUT_DETECTED )
						.setScoutDetectedPacket( sdp )
						.build();
		this.multicast( packet );
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		final Channel channel = e.getChannel();
		e.getCause().printStackTrace();
		channel.close();
	}
	
	public void handleScoutStateChanged( final String scoutId, ScoutState scoutState ) {
		ScoutPacket scoutPacket = ScoutPacket.newBuilder()
								.setScoutId( scoutId )
								.setScoutState( scoutState ).build();
		final Packet packet = Packet.newBuilder()
						.setType( Type.SCOUT_STATE_CHANGED )
						.setScoutPacket( scoutPacket ).build();
		
		this.broadcast( packet ).addListener( new ChannelGroupFutureListener() {
			@Override
			public void operationComplete(ChannelGroupFuture future)
					throws Exception {
				// TODO Auto-generated method stub
				if ( !future.isCompleteSuccess() ) {
					broadcast( packet ).addListener( this );
				}
			}
			
		});
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		// TODO Auto-generated method stub
		super.channelConnected(ctx, e);
	}
	
	public ChannelGroupFuture broadcast( Object message ) {
		return this.channelGroup.write( message );
	}
	
	public ChannelGroupFuture multicast( Packet packet ) {
		List<String> targetIds = packet.getTargetIdList();
		ChannelGroupFuture cgf = null;
		if ( targetIds.isEmpty() ) {
			cgf =  this.broadcast( packet );
		}
		else {
			ChannelGroup channelGroup = new DefaultChannelGroup();
			List<ChannelFuture> futures = new ArrayList<ChannelFuture>();
			Iterator<Channel> i = this.channelGroup.iterator();
			for ( ; i.hasNext(); ) {
				Channel channel = (Channel)i.next();
				String id = (String)channel.getAttachment();
				if ( targetIds.contains( id ) ) {
					channelGroup.add( channel );
					ChannelFuture channelFuture = this.write( channel, packet );
					futures.add( channelFuture );
				}
			}
			cgf = new DefaultChannelGroupFuture(channelGroup, futures );
		}
		
		return cgf;
	}
	
	private ChannelFuture write( Channel channel, Object object ) {
		ChannelFuture future = null;
		if ( channel != null && channel.isConnected() ) {
			future = channel.write( object );
		}
		
		return future;
	}
	
	public void shutdown() {
		this.channelGroup.close().addListener( new ChannelGroupFutureListener() {

			@Override
			public void operationComplete(ChannelGroupFuture future)
					throws Exception {
				// TODO Auto-generated method stub
				final ChannelGroup channelGroup = LeaderChannelHandler.this.channelGroup;
				if ( !future.isCompleteSuccess() ) {
					channelGroup.close().addListener( this );
				}
				else {
					channelGroup.clear();
				}
			}} );
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		// TODO Auto-generated method stub
		final Channel channel = e.getChannel();
		final String scoutId = (String)channel.getAttachment();
		this.channelGroup.remove( channel );
		this.handleScoutStateChanged( scoutId, ScoutState.INACTIVATED );
		logger.debug( "[SERVER]::Disconnected from [{}]", scoutId  );
	}

	
}
