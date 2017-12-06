package com.squadtag.jedis;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisConnectionPool {
	private final Logger logger = LoggerFactory.getLogger( JedisConnectionPool.class );
	private static ConcurrentMap<String, JedisPool> pool = new ConcurrentHashMap<String, JedisPool>();
	
	private JedisConnectionPool() {
		
	}
	
	public static JedisPool getConnectionPool( String host, int port ) {
		String key = getKey( host, port );
		JedisPool jedisPool = pool.get( key );
		if ( jedisPool == null ) {
			jedisPool = new JedisPool( new JedisPoolConfig(), host, port );			
			pool.put( key, jedisPool  );
		}
	
		return jedisPool;
	}
	
	public static void destroy( String host, int port ) {
		String key = getKey( host, port );
		JedisPool jedisPool = pool.get( key );
		if ( jedisPool != null ) {
			jedisPool.destroy();
		}
		
		pool.remove( key );
	}
	
	private static String getKey( String host, int port ) {
		StringBuffer sb = new StringBuffer();
		sb.append( host );
		sb.append( ":" );
		sb.append( port );
		return sb.toString(); 
	}
	public static void destroyAll() {
		Iterator<JedisPool> i = pool.values().iterator();
		for ( ; i.hasNext(); ) {
			JedisPool jedisPool = i.next();
			if ( !jedisPool.isClosed() ) {
				jedisPool.destroy();
			}
		}
		
		pool.clear();
	}
}
