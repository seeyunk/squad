package com.squad.rpc.client;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientBossPool;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioWorkerPool;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.ThreadNameDeterminer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.util.SquadThreadFactory;

public class Connector {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final long DEFAULT_TIMEOUT_MILLIS = 1000L;
	private static final int DEFAULT_WORKER_COUNT = 4;
	private static final int DEFAULT_CHUNK_SIZE = 1024 * 64;
	
	private ClientBootstrap bootstrap;
	private Timer timer;
	
	public void setPipelineFactory( ChannelPipelineFactory pipelineFactory ) {
		this.bootstrap.setPipelineFactory( pipelineFactory );
	}
	
	private Timer createTimer() {
		HashedWheelTimer timer = new HashedWheelTimer(SquadThreadFactory.newThreadFactory( "client-connection-timeout-handler"), 
				ThreadNameDeterminer.CURRENT, 100, 
				TimeUnit.MILLISECONDS, 
				512 );
		timer.start();
		return timer;
	}
	
	public void createBootstrap() {
		this.timer = createTimer();
		this.bootstrap = this.createBootstrap( 1, DEFAULT_WORKER_COUNT, timer );
		this.bootstrap.setOption( "tcpNoDelay", true );
		this.bootstrap.setOption( "sendBufferSize", DEFAULT_CHUNK_SIZE );
		this.bootstrap.setOption( "receiveBufferSize", DEFAULT_CHUNK_SIZE );
		this.bootstrap.setOption( "keepAlive", true );
		this.bootstrap.setOption( "connectTimeoutMillis", DEFAULT_TIMEOUT_MILLIS );
	}
	
	private ClientBootstrap createBootstrap( int bossCount, int workerCount, Timer timer ) {
		ExecutorService boss = Executors.newCachedThreadPool( SquadThreadFactory.newThreadFactory( "squad-client-boss") );
		NioClientBossPool bossPool = new NioClientBossPool( boss, bossCount, timer, ThreadNameDeterminer.CURRENT );

	    ExecutorService worker = Executors.newCachedThreadPool( SquadThreadFactory.newThreadFactory( "squad-client-worker" ) );
	    NioWorkerPool workerPool = new NioWorkerPool( worker, workerCount, ThreadNameDeterminer.CURRENT );
		return new ClientBootstrap( new NioClientSocketChannelFactory( bossPool, workerPool ) );
	}
	
	public ChannelFuture connect( final InetSocketAddress address ) {
		if ( this.bootstrap == null ) {
			return null;
		}
		
		this.bootstrap.setOption( "remoteAddress", address );
		ChannelFuture future =  this.bootstrap.connect( address );
		return future;
	}
	
	public void release() {
        if ( this.bootstrap != null ) {
        	this.bootstrap.releaseExternalResources();
        }
        Set<Timeout> stop = this.timer.stop();
        if ( !stop.isEmpty() ) {
            logger.info("stop Timeout:{}", stop.size());
        }
        
        this.bootstrap = null;
	}

	public ChannelHandler getChannelHandler() throws Exception {
		return this.bootstrap.getPipelineFactory().getPipeline().get( "handler" );
	}
	
	public void closeChannel() {
		try{
			ScoutChannelHandler channelHandler = (ScoutChannelHandler)this.getChannelHandler();
			channelHandler.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			
		}
		
	}
	
	public <T extends ChannelHandler> void removeChannelHandler( Class<T> clazz ) throws Exception {
		this.bootstrap.getPipelineFactory().getPipeline().remove( clazz );
	}
}
