package org.camel.squadtag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import com.squadtag.jedis.JedisConnectionPool;
import com.squadtag.jedis.JedisKeyEventSubscriber;
import com.squadtag.jedis.JedisService;

/**
 * The SquadTag consumer.
 */
public class SquadTagConsumer extends DefaultConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SquadTagConsumer.class);
    private final SquadTagEndpoint endpoint;
    private final JedisService jedisService;
    private final JedisKeyEventSubscriber subscriber;
    private final SquadTagConfig config;
    private volatile boolean sentFlushDb = false;
    
    public SquadTagConsumer(SquadTagEndpoint endpoint, Processor processor) throws Exception {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.config = endpoint.getConfig();
        
        JedisPool jedisPool = JedisConnectionPool.getConnectionPool( this.config.getHost(), this.config.getPort() );
        this.jedisService = new JedisService( jedisPool.getResource(), config.getPasswd(), config.getDefaultDb() );
        this.subscriber = new JedisKeyEventSubscriber( new JedisEventListener(), this.config.getHost(), this.config.getPort(), 
        								this.config.getPasswd(), this.config.getDefaultDb() );
    }
    
    /*
    @Override
	protected int poll() throws Exception {
		// TODO Auto-generated method stub
    	if ( !this.sentFlushDb && this.jedisService.getKeyCount() == 0 ) {
    		final Exchange exchange = getEndpoint().createExchange();
    		exchange.getIn().setHeader( SquadTagConstants.ACTION, SquadTagConstants.ACTION_FLUSHDB );
    		
    		try {
				getProcessor().process( exchange );
				this.sentFlushDb = true;
			}
			catch( Exception e ) {
				e.printStackTrace();
			}
			finally {
	            // log exception if an exception occurred and was not handled
	            if ( exchange.getException() != null ) {
	                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
	            }
			}
    	}
    	else if ( this.jedisService.getKeyCount() > 0 ){
    		Exchange exchange = queue.take();
    		getProcessor().process( exchange );
    		this.sentFlushDb = false;
    	}
		return 0;
	}
    */
    @Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
    	if ( this.config.getLoadOnStartup() ) {
    		Iterator<String> i = this.jedisService.getAllKeys().iterator();
    		for ( ; i.hasNext(); ) {
    			String key = i.next();
    			String value = this.jedisService.getTag( key );
    			
    			Map<String, String> body = new HashMap<String, String>();
    			body.put( key, value );
    			
    			final Exchange exchange = getEndpoint().createExchange();
    			exchange.getIn().setHeader( SquadTagConstants.ACTION, SquadTagConstants.ACTION_REGISTER );
    			exchange.getIn().setBody( body );
				
				try {
					getProcessor().process( exchange );
					this.sentFlushDb = false;
				}
				catch( Exception e ) {
					e.printStackTrace();
				}
				finally {
		            // log exception if an exception occurred and was not handled
		            if ( exchange.getException() != null ) {
		                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
		            }
				}
    		}
    		
    	}
		super.doStart();
	}
    
	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		this.subscriber.terminate();
		this.jedisService.close();
		this.jedisService.disconnect();
		this.stop();
		super.doStop();
	}
	
    private class JedisEventListener extends JedisPubSub {
    	
		@Override
		public void onPMessage(String pattern, String channel, String message) {
			// TODO Auto-generated method stub
			Pattern p = Pattern.compile( "__keyevent@([0-9]+)__:(.+)" );
			Matcher m = p.matcher( channel );
			
			final Exchange exchange = getEndpoint().createExchange();
			if ( m.matches() ) {
				String command = m.group( 2 );
				String key = message;
				String value = jedisService.getTag( key );
				
				if ( command.equals( "set" ) ) {
					exchange.getIn().setHeader( SquadTagConstants.ACTION, SquadTagConstants.ACTION_REGISTER );
				}
				else if ( command.equals( "del" ) ) {
					exchange.getIn().setHeader( SquadTagConstants.ACTION, SquadTagConstants.ACTION_DELETE );
				}
				
				Map<String, String> body = new HashMap<String, String>();
				
				body.put( key, value );
				exchange.getIn().setBody( body );
				
				try {
					getProcessor().process( exchange );
				}
				catch( Exception e ) {
					e.printStackTrace();
				}
				finally {
		            // log exception if an exception occurred and was not handled
		            if ( exchange.getException() != null ) {
		                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
		            }
				}
			}
			
			super.onPMessage(pattern, channel, message);
		}
		
    }

}
