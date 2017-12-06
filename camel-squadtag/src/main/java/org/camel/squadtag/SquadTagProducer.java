package org.camel.squadtag;

import java.util.Map;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultAsyncProducer;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.AsyncProcessorHelper;
import org.apache.camel.util.ExchangeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;

import com.squadtag.jedis.JedisConnectionPool;
import com.squadtag.jedis.JedisService;

/**
 * The SquadTag producer.
 */
public class SquadTagProducer extends DefaultProducer {
    private static final Logger logger = LoggerFactory.getLogger(SquadTagProducer.class);
    private final JedisService jedisService;
    private SquadTagConfig config;
    
    public SquadTagProducer(SquadTagEndpoint endpoint) {
        super(endpoint);
        this.config = endpoint.getConfig();
        JedisPool jedisPool = JedisConnectionPool.getConnectionPool( this.config.getHost(), this.config.getPort() );
        this.jedisService = new JedisService( jedisPool.getResource(), this.config.getPasswd(), this.config.getDefaultDb() );
    }

    @SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {
    	Object body = exchange.getIn().getBody();
    	if ( !( body instanceof Map ) ) {
    		throw new Exception( "Tag body must be Map..." );
    	}
    	
    	if ( ExchangeHelper.isOutCapable( exchange ) ) {
    		exchange.getOut().setBody( jedisService.getTags( (Map<String, Object>)body ) );
    	}
    	else {
    		synchronized( this ) {
    			jedisService.setTags( (Map<String, Object>)body );
    		}
	    	exchange.getIn().setBody( body );
    	}
    	
    	boolean disconnectWhenComplete = exchange.getIn().getHeader( SquadTagConstants.DISCONNECT_WHEN_COMPLETE, Boolean.class ) == null ? false
    									: exchange.getIn().getHeader( SquadTagConstants.DISCONNECT_WHEN_COMPLETE, Boolean.class );
    	if ( disconnectWhenComplete ) {
    		JedisConnectionPool.destroy( this.config.getHost(), this.config.getPort() );
    	}
    }

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		this.jedisService.close();
		JedisConnectionPool.destroy( this.config.getHost(), this.config.getPort() );
		super.doStop();
	}
}
