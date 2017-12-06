package com.squadtag.jedis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

public class JedisKeyEventSubscriber implements Runnable {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final int DEFAULT_POOL_SIZE = 1;
	private JedisService jedisService;
	private JedisPubSub pubsub;
	
	private ExecutorService exeService = Executors.newFixedThreadPool( DEFAULT_POOL_SIZE, new ThreadFactory() {
			public Thread newThread( Runnable r ) {
				Thread t = Executors.defaultThreadFactory().newThread( r );
				t.setDaemon( false );
				return t;
			}
	} );
	
	public JedisKeyEventSubscriber( JedisPubSub pubsub, String host, int port, String passwd, int defaultDb ) {
		JedisPool jedisPool = JedisConnectionPool.getConnectionPool( host, port );
		this.jedisService = new JedisService( jedisPool.getResource(), passwd, defaultDb );
		this.pubsub = pubsub;
		this.exeService.execute( this );
	}
	
	public void terminate() throws InterruptedException {
		if ( this.pubsub.isSubscribed() ) {
			this.pubsub.unsubscribe();
		}
		
		this.exeService.shutdown();
		this.exeService.awaitTermination( 5000L, TimeUnit.MILLISECONDS );
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.jedisService.listen( this.pubsub );
	}
}
