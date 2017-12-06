package org.camel.squadws;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.jboss.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * The SquadWs producer.
 */
public class SquadWsProducer extends DefaultProducer {
    private static final Logger logger = LoggerFactory.getLogger(SquadWsProducer.class);
    private final Gson gson = new GsonBuilder().create();
    private SquadWsEndpoint endpoint;

    public SquadWsProducer(SquadWsEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {
    	Map<String, Object> body = exchange.getIn().getBody( HashMap.class );
    	
    	String message = this.gson.toJson( body );
        this.endpoint.broadcast( new TextWebSocketFrame( message ) );    
    }

}
