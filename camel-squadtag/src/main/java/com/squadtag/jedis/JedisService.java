package com.squadtag.jedis;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class JedisService extends Jedis{
	private final Logger logger = LoggerFactory.getLogger( JedisService.class );
	
	public JedisService( Jedis jedis, String passwd, int defaultDb ) {
		this.auth( passwd );
		this.select( defaultDb );
	}
	
	public void setHmapTag( String key, Map<String, Object> hmap ) throws Exception {
		Iterator<Entry<String, Object>> i = hmap.entrySet().iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, Object> entry = i.next();
			this.hset(key, entry.getKey(), this.toString( entry.getValue() ) );
		}
	}
	
	public void setTags( Map<String, Object> keyValue ) throws Exception {
		Iterator<Entry<String, Object>> i = keyValue.entrySet().iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, Object> entry = i.next();
			this.set( entry.getKey(), this.toString( entry.getValue() ) );
		}
	}
	
	public Map<String, Object> getHmapTags( String key, Map<String, Object> hmap ) {
		Iterator<Entry<String, Object>> i = hmap.entrySet().iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, Object> entry = i.next();
			String value = this.hget( key, entry.getKey() );
			hmap.put( key,  value );
		}
		
		return hmap;
	}
	
	public Map<String, Object> getTags( Map<String, Object> keyValue ) {
		Iterator<Entry<String, Object>> i = keyValue.entrySet().iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, Object> entry = i.next();
			String key = entry.getKey();
			String value = this.get( key );
			keyValue.put( key, value );
		}
		
		return keyValue;
	}
	
	public String getHmapTag( String key, String tag ) {
		return this.hget( key, tag );
	}
	
	public String getTag( String key ) {
		return this.get( key );
	}
	
	public Set<String> getKeys( String pattern ) {
		return this.keys( pattern );
	}
	
	public Set<String> getAllKeys() {
		return this.getKeys( "*" );
	}
	
	public int getKeyCount() {
		return this.getAllKeys().size();
	}
	
	public void delKey( String key ) throws Exception {
		this.del( key );
	}
	
	public void delHmapTag( String key, String tag ) {
		this.hdel( key, tag );
	}
	
	public void close() {
		this.close();
	}
	
	public void listen( JedisPubSub pubsubListener ) {
		this.psubscribe( pubsubListener, "__key*__:*" );
	}
	
	private String toString( Object val ) throws Exception {
		String result = "";
		if ( val instanceof Byte ) {
			result = Byte.toString( (Byte)val );
		}
		else if ( val instanceof Short ) {
			result = Short.toString( (Short)val );
		}
		if ( val instanceof Integer ) {
			result = Integer.toString( (Integer)val ); 
		}
		else if ( val instanceof Float ) {
			result = Float.toString( (Float)val );
		}
		else if ( val instanceof Long ) {
			result = Long.toString( (Long)val );
		}
		else if ( val instanceof Double ) {
			result = Double.toString( (Double)val );
		}
		else if ( val instanceof Boolean ) {
			result = Boolean.toString( (Boolean)val );
		}
		else if ( val instanceof String ) {
			result = (String)val;
		}
		else {
			throw new Exception();
		}
		
		return result;
	}
}
