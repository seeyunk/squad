package com.sys.ng.routes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.camel.squadtag.SquadTagConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.annotation.SquadRoute;
import com.sys.ng.packet.IPacket;
import com.sys.ng.packet.PingPacket;
import com.sys.ng.packet.PongPacket;
import com.sys.ng.packet.ReadPointsPacket;
import com.sys.ng.packet.ReadPointsValuePacket;

@SquadRoute( config="squad-sysroute-ng.cfg" )
public class NgRoute extends RouteBuilder {
	private static final Logger logger = LoggerFactory.getLogger(NgRoute.class);
	private static final ConcurrentMap<String, Object> tagMap = new ConcurrentHashMap<String, Object>();
	
	private void copyMap( Map<String, Object> body ) {
		Iterator<Entry<String,Object>> i = body.entrySet().iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, Object> entry = i.next();
			tagMap.put( entry.getKey(), entry.getValue() );
		}
	}
	
	private void deleteEntryFromMap( Map<String, Object> body ) {
		Iterator<Entry<String,Object>> i = body.entrySet().iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, Object> entry = i.next();
			tagMap.remove( entry.getKey() );
		}
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		final NgRoute instance = NgRoute.this;
		from( "squadtag://#{redisHost}:#{redisPort}?defaultDb=#{defaultDb}&passwd=#{passwd}&loadOnStartup=true" )
		.process( new Processor() {
			@SuppressWarnings("unchecked")
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				String header = exchange.getIn().getHeader( SquadTagConstants.ACTION, String.class );
				Map<String, Object> body = (Map<String, Object>)exchange.getIn().getBody();
				if ( header.equals( SquadTagConstants.ACTION_REGISTER ) ) {
					instance.copyMap( body );
				}
				else if ( header.equals( SquadTagConstants.ACTION_DELETE ) ) {
					instance.deleteEntryFromMap( body );
				}
				else if ( header.equals( ( SquadTagConstants.ACTION_FLUSHDB ) ) ) {
					tagMap.clear();
				}
			}} )
		.to( "mock:end" );
		
		from( "netty:#{protocol}://#{host}:#{port}?serverPipelineFactory=#ng-spf&sync=true" )		
		.process( new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				IPacket body = exchange.getIn().getBody( IPacket.class );
				if ( body instanceof PingPacket ) {
					exchange.getOut().setBody( new PongPacket() );
				}
				else if ( body instanceof ReadPointsPacket ) {
					logger.debug( "[RESPONSE]::ReadPoints");
					exchange.getOut().setBody( new ReadPointsPacket( tagMap.keySet() ));
				}
				else if ( body instanceof ReadPointsValuePacket ) {
					ReadPointsValuePacket packet = (ReadPointsValuePacket)body;
					Map<String, String> resultMap = new HashMap<String, String>();
					String unitName = packet.getUnitName();
					Iterator<String> i = packet.getPointIds();
					for ( ; i.hasNext(); ) {
						String pointId = i.next();
						String pointValue = (String)tagMap.get( pointId );
						if ( pointValue != null ) {
							resultMap.put( pointId, pointValue );
							logger.debug( "[RESPONSE]::{}::{}", pointId, pointValue );
						}
					}
					
					exchange.getOut().setBody( new ReadPointsValuePacket( unitName, resultMap ) );
				}
			}} )
		.to( "mock:end" );
	}
}
