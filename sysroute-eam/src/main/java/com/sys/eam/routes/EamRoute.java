package com.sys.eam.routes;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.squad.annotation.SquadRoute;
import com.squad.protobuf.cli.packet.SquadCli.NodeEventPacket;
import com.squad.protobuf.cli.packet.SquadCli.SquadCliPacket;
import com.squad.protobuf.cli.packet.SquadCli.SquadCliPacket.Command;

@SquadRoute( config="squad-sysroute-eam.cfg" )
public class EamRoute extends RouteBuilder {
	public static final String ALM_ROUTE_ID = "sys-alm";
	public static final String ALM_IO_ADDR = "alm_addr";
	public static final String ALM_STATE = "alm_state";
	public static final String ALM_DESC = "alm_desc";
	public static final String ALM_NORMAL = "N";
	public static final String ALM_ABNORMAL = "F";
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from( "netty:#{cliProtocol}://#{cliHost}:#{cliPort}?serverPipelineFactory=#sys-squad-cli-cpf&sync=false&clientMode=true&lazyChannelCreation=false" )
		.process( new Processor() {

			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				//String protoPacket = exchange.getIn().getBody( String.class );
				//SquadCliPacket packet = SquadCliPacket.parseFrom( protoPacket.getBytes( Charset.forName( "utf-8" ) ) );
				SquadCliPacket packet = exchange.getIn().getBody( SquadCliPacket.class );
				if ( packet.getCommand().equals( Command.NODE_EVENT ) ) {
					NodeEventPacket nodeEventPacket = packet.getNodeEventPacket();
					String node = nodeEventPacket.getNode();
					String state = ALM_ABNORMAL;
					String value = nodeEventPacket.getValue().toStringUtf8();
					
					if ( node.contains( "healthCheck" ) ) {
						exchange.getIn().setBody(  null );
					}
					else {
						if ( value.equals( "STARTED" ) || 
								value.equals( "ACTIVATED" ) ) {
							state = ALM_NORMAL;
						}
						Map<String, String> body = new HashMap<String, String>();
						body.put( ALM_IO_ADDR, node );
						body.put( ALM_STATE, state  );
						body.put( ALM_DESC, value );
						exchange.getIn().setBody( body );
					}
				}

			}} )
		.filter( body().isNotNull() )
		.to( "vm:sys-eam-queue?size=#{queueSize}&purgeWhenStopping=true" );
		
		from( "vm:sys-eam-queue?size=#{queueSize}&purgeWhenStopping=true" )
		.process( new Processor() {
			@SuppressWarnings("unchecked")
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				Object in = exchange.getIn().getBody();
				if ( in == null || !( in instanceof Map ) ) {
					throw new Exception( "[SYSTEM-ROUTE] Invalid message from sys-eam" );
				}
				
				Map<String, String> message = (Map<String, String>)in;
				StringBuffer sb = new StringBuffer();
				sb.append( message.get( ALM_IO_ADDR ).trim() );
				sb.append( ";" );
				sb.append( message.get( ALM_STATE ).trim() );
				sb.append( ";" );
				sb.append( message.get( ALM_DESC ).trim() );
				sb.append( ";" );
				
				exchange.getIn().setBody( sb.toString() );
			}
		} )
		.to( "netty:#{protocol}://#{ip}:#{port}?clientPipelineFactory=#sys-eam-cpf&sync=false" );
	}
	
}
