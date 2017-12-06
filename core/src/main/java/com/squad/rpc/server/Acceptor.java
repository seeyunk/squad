package com.squad.rpc.server;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioServerBossPool;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioWorkerPool;
import org.jboss.netty.util.ThreadNameDeterminer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.util.SquadThreadFactory;

public class Acceptor {
	private static final Logger logger = LoggerFactory.getLogger( Acceptor.class );
	
	protected static final long DEFAULT_TIMEOUT_MILLIS = 5000L;
	protected static final int DEFAULT_WORKER_COUNT = 4;
	protected static final int DEFAULT_CHUNK_SIZE = 1024 * 64;
	
	protected ServerBootstrap bootstrap;
	protected Channel channel;
	
	public Acceptor() {
	}
	
	public void setPipelineFactory( ChannelPipelineFactory pipelineFactory ) {
		this.bootstrap.setPipelineFactory( pipelineFactory );
	}
	
	public void createBootstrap( int workerCount ) {
		if ( workerCount == -1 ) {
			workerCount = DEFAULT_WORKER_COUNT;
		}
		this.bootstrap = this.createBootstrap( 1, workerCount );
		this.bootstrap.setOption( "child.tcpNoDelay", true );
		this.bootstrap.setOption( "child.keepAlive", true );
		this.bootstrap.setOption( "child.reuseAddress", true );
		this.bootstrap.setOption( "child.sendBufferSize", DEFAULT_CHUNK_SIZE);
		this.bootstrap.setOption( "child.receiveBufferSize", DEFAULT_CHUNK_SIZE );
	}
	
	private ServerBootstrap createBootstrap( int bossCount, int workerCount ) {
		
		ExecutorService boss = Executors.newCachedThreadPool( SquadThreadFactory.newThreadFactory( "squad-server-boss") );
		NioServerBossPool bossPool = new NioServerBossPool( boss, bossCount, ThreadNameDeterminer.CURRENT );
		
		ExecutorService worker = Executors.newCachedThreadPool( SquadThreadFactory.newThreadFactory( "squad-server-worker" ) );
		NioWorkerPool workerPool = new NioWorkerPool( worker, workerCount, ThreadNameDeterminer.CURRENT );
		
		return new ServerBootstrap( new NioServerSocketChannelFactory( bossPool, workerPool ) );
	}
	
	public Channel bind( InetSocketAddress address ) {
		this.channel = this.bootstrap.bind( address );
		return this.channel;
	}
	
	public void release() {
		if ( this.channel != null ) {
			ChannelFuture close = this.channel.close();
			close.awaitUninterruptibly( DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS );
			this.channel = null;
		}
		
		if ( this.bootstrap != null ) {
			this.bootstrap.releaseExternalResources();
			this.bootstrap = null;
		}
		
		logger.info( "Server stopped" );
	}
	
	public ChannelHandler getChannelHandler() throws Exception{
		return this.bootstrap.getPipelineFactory().getPipeline().get( "handler" );
	}
}
