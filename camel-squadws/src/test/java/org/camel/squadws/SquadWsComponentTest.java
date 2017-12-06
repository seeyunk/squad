package org.camel.squadws;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;

public class SquadWsComponentTest {

    @Test
    public void testSquadWs() throws Exception {
    	
    	
        
        CamelContext ctx = new DefaultCamelContext();
        ctx.addRoutes( new RouteBuilder() {
        	AtomicInteger counter = new AtomicInteger( 0 );
			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				
				from("quartz2://myGroup/myTimerName?trigger.repeatInterval=1&trigger.repeatCount=10000000" )
				.process( new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						String key = "1.3.6.1.4.1.46894.1.0" + Integer.toString( counter.getAndIncrement() );
						Map<String, Object> body = new HashMap<String, Object>();
						body.put( key, Integer.toString( counter.get() ) );
						System.out.println( "SENT::" + key );
						exchange.getIn().setBody( body  );
					}} )
				.to( "squadtag://localhost:6379?defaultDb=0&passwd=eagleibs1@" );
				
				from( "squadtag://localhost:6379?defaultDb=0&passwd=eagleibs1@" )
				//from( "direct:in" )
				.to( "squadws://localhost:9091/squad?type=json" );
			}
        	
        });
        
        ctx.start();
        Thread.currentThread().join();
    }

}
