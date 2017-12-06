package com.sys.tag.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.camel.squadsnmp.SquadSnmpConstants;
import org.camel.squadtag.SquadTagConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.annotation.SquadRoute;

@SquadRoute( config="squad-sysroute-tag.cfg" )
public class TagRoute extends RouteBuilder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from( "squadtag://#{redisHost}:#{redisPort}?defaultDb=#{defaultDb}&passwd=#{passwd}&loadOnStartup=true" )
		.process( new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				String header = exchange.getIn().getHeader( SquadTagConstants.ACTION, String.class );
				if ( header.equals( SquadTagConstants.ACTION_REGISTER ) ) {
					exchange.getIn().setHeader( SquadSnmpConstants.ACTION,  SquadSnmpConstants.ACTION_REGISTER );
				}
				else if ( header.equals( SquadTagConstants.ACTION_DELETE ) ) {
					exchange.getIn().setHeader( SquadSnmpConstants.ACTION,  SquadSnmpConstants.ACTION_DELETE );
				}
				else if ( header.equals( ( SquadTagConstants.ACTION_FLUSHDB ) ) ) {
					exchange.getIn().setHeader( SquadSnmpConstants.ACTION, SquadSnmpConstants.ACTION_DELETE_ALL );
				}
			}} )
		.to( "squadsnmp://#{snmpHost}:#{snmpPort}?version=#{version}&poolSize=#{poolSize}&trapHost=#{trapHost}&trapPort=#{trapPort}" );
		
		from( "vm:sys-tag-queue?size=#{queueSize}&purgeWhenStopping=true" )
		.to( "squadtag://#{redisHost}:#{redisPort}?defaultDb=#{defaultDb}&passwd=#{passwd}&loadOnStartup=true" );
	}
}
