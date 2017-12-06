package org.camel.squadtag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;

public class SquadTagComponentTest {

	@Test
	public void test() throws Exception {
		
		CamelContext context = new DefaultCamelContext();
		context.addRoutes( new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				// TODO Auto-generated method stub
				
				/*
				from( "stream:in?promptMessage=>>")
				.process( new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						Map<String, Object> body = new HashMap<String, Object>();
						body.put( "1.3.6.1.4.1.5050.0.3.0", null );
						exchange.getIn().setBody( body  );
					}} )
				.to( "squadtag://localhost:6379?defaultDb=0&passwd=password1!" )
				.process( new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						String s = exchange.getOut().getBody( String.class );
						exchange.getIn().setBody( s );
					}} )
				.to( "stream:out" );
				*/
				final AtomicInteger counter = new AtomicInteger();
				
				from("quartz2://myGroup/myTimerName?trigger.repeatInterval=1&trigger.repeatCount=10000000" )
				.process( new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						String key = "1.3.6.1.4.1.5050.0.3.0." + Integer.toString( counter.getAndIncrement() );
						Map<String, Object> body = new HashMap<String, Object>();
						body.put( key, Integer.toString( counter.get() ) );
						//System.out.println( "SENT::" + key );
						exchange.getIn().setBody( body  );
					}} )
				.to( "squadtag://localhost:6379?defaultDb=0&passwd=eagleibs1@" );
				
				from( "squadtag://localhost:6379?defaultDb=0&passwd=eagleibs1@" )
            	.process( new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						// TODO Auto-generated method stub
						String header = exchange.getIn().getHeader( SquadTagConstants.ACTION, String.class );
						Map<String, Object> body = exchange.getIn().getBody( HashMap.class );
						Iterator<String> i  = body.keySet().iterator();
						for ( ; i.hasNext(); ) {
							System.out.println( "RECV::" + i.next() );
						}
					}} )
            	.to( "mock:end" );
				
				
				
				
				
            	
			}} );
		context.start();
		Thread.currentThread().join();
		System.out.println( "asdfasdf" );
	}
}
