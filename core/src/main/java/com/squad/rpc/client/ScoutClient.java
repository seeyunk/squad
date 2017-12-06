package com.squad.rpc.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.context.SquadContext;
import com.squad.entity.Message;
import com.squad.entity.SystemState;
import com.squad.looper.AsyncLooper.MSG;
import com.squad.protobuf.packet.Squad.Packet;
import com.squad.protobuf.packet.Squad.Packet.Type;
import com.squad.protobuf.packet.Squad.StreamType;
import com.squad.rpc.ChannelState;
import com.squad.rpc.ScoutSocketState;
import com.squad.rpc.stream.IStream;
import com.squad.rpc.stream.StreamManager;
import com.squad.util.SquadThreadFactory;

public class ScoutClient extends ScoutSocketState {
	
	private static final Logger logger = LoggerFactory.getLogger( ScoutClient.class ); 
	private Connector connector = new Connector();
	private final int MAX_CONNECT_RETRY = 2;
	
	private static InetSocketAddress leaderAddress;
	private Timer delayTimer = new HashedWheelTimer();
	private Timer timeoutTimer = new HashedWheelTimer();
	private final StreamManager streamManager;
	private final ScoutChannelHandler channelHandler;
	private final ReconnectFutureListener listener = new ReconnectFutureListener();
	private SquadContext ctx;
	private static final long RETRY_DELAY_MILLIS = 1500L;
	private long PING_DELAY = 3000L;
	private ScheduledExecutorService pingScheduler = null;
	
	public ScoutClient( SquadContext ctx ) {
		this.ctx = ctx;
		this.streamManager = new StreamManager( this.ctx );
		this.channelHandler = new ScoutChannelHandler( this.ctx, this.streamManager );
	}
	
	public void prepare() {
		this.connector.createBootstrap();
		this.connector.setPipelineFactory( new ScoutPipelineFactory( channelHandler, this.timeoutTimer ) );
		
	}
	
	public ScoutClient connect( final InetSocketAddress address ) {
		leaderAddress = address;
		this.channelHandler.setChannelState( ChannelState.BEING_CONNECTED );
		this.delayTimer.newTimeout( new TimerTask() {
			@Override
			public void run(Timeout timeout) throws Exception {
				// TODO Auto-generated method stub
				logger.info( "Scout is trying to connect to leader({})", leaderAddress );
				ChannelFuture future = connector.connect( leaderAddress );
				future.addListener( listener );
			}} , RETRY_DELAY_MILLIS, TimeUnit.MILLISECONDS );
		return this;
	}
	
	public boolean isConnected() {
		return this.channelHandler.isConnected();
	}
	
	public ScoutClient reconnect() {
		return this.connect( leaderAddress );
	}
	
	public ScoutClient connect( String host, int port ) {
		return this.connect( new InetSocketAddress( host, port ) );
	}
	
	public void release() {
		this.channelHandler.setChannelState( ChannelState.BEING_CLOSED_BY_CLIENT );
		this.connector.closeChannel();
		this.connector.release();
		this.channelHandler.setChannelState( ChannelState.CLOSED_BY_CLIENT );
	}
	
	public ChannelFuture write( Object object ) {
		ScoutChannelHandler handler = (ScoutChannelHandler)this.channelHandler;
		return handler.write0( object );
	}
	
	public void writeSystemState( String scoutId, SystemState systemState ) {
		Packet packet = Packet.newBuilder()
							.setType( Type.SYSTEM_STATE )
							.setSystemStatePacket( SystemState.toPacket( scoutId, systemState ) )
							.build();
		this.write( packet );
	}
	
	public void syncServiceFile( List<String> targetIds, String fileName ) {
		File file = new File( this.ctx.getConfig().getSquadLibPath() + File.separator + fileName );
		IStream stream = this.channelHandler.openFileStream( targetIds, StreamType.FILE, file.getName(), file.length() );
		try {			
			InputStream is = new FileInputStream( file );
			stream.write( is, file.length() );
			is.close();
			logger.debug( "[SyncServiceFile]::stream job finished :: file::[{}]", file.getName() );
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		finally {
			stream.close();
		} 
	}
	
	public synchronized void startPing() {
		if ( this.pingScheduler == null ) {
			this.pingScheduler = Executors.newSingleThreadScheduledExecutor( SquadThreadFactory.newThreadFactory( "ping-scheduler" ) );
			this.pingScheduler.scheduleWithFixedDelay( new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Packet packet = Packet.newBuilder()
										.setType( Type.PING )
										.build();
					ScoutClient.this.write( packet );
	
				}}, 0, PING_DELAY, TimeUnit.MILLISECONDS );
		}
	}
	
	public synchronized void stopPing() {
		if ( this.pingScheduler != null ) {
			this.pingScheduler.shutdownNow();
			this.pingScheduler = null;
		}
	}
	
	public class ReconnectFutureListener implements ChannelFutureListener {
		private AtomicInteger retry = new AtomicInteger( 0 );
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			// TODO Auto-generated method stub
			if ( future.isSuccess() ) {
				channelHandler.setChannelState( ChannelState.CONNECTED );
				this.retry.set( 0 );
			}
			else {
				if ( this.retry.incrementAndGet() > MAX_CONNECT_RETRY ) {
					channelHandler.setChannelState( ChannelState.RECONNECT_FAILED );
					this.retry.set( 0 );
					ScoutClient.this.ctx.sendMessage( new Message( MSG.ELECT_NEW_LEADER ) );
					return;
				}
			}	
		}
	}
}
