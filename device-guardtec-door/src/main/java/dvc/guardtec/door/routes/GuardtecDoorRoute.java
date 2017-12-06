package dvc.guardtec.door.routes;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import com.squad.annotation.SquadRoute;

import dvc.guardtec.door.entity.AccuDetail;
import dvc.guardtec.door.entity.EventResponse;

@SquadRoute( config="device-guardtec-door.cfg" )
public class GuardtecDoorRoute extends RouteBuilder {
	
	public static final ConcurrentMap<String, AccuDetail> accuDetails = new ConcurrentHashMap<String, AccuDetail>();
	public static final String oidRoot = "#{oidRoot}";
	public static final String csvPath = "#{csvPath}";
	static {
		try {
			List<String> lines = Files.readAllLines( Paths.get( csvPath ), Charset.forName( "utf-8" ) );
			int i = 1;
			for ( String line : lines ) {
				String oid = String.format( "%s.%d.0", oidRoot, i );
				String[] token = line.split( "," );
				AccuDetail accuDetail = new AccuDetail();
				accuDetail.setOid( oid );
				accuDetail.setAccuNo( token[0].trim() );
				
				String kdNo = String.valueOf( Integer.parseInt( token[1].trim() ) );
				accuDetail.setKdNo( kdNo );
				accuDetail.setKdName( token[2].trim() );
				accuDetail.setDoorContactNo( token[3].trim() );
				accuDetail.setDoorStrikeNo( token[5].trim() );
				
				String readerKey = String.format( "%s:%s:0:0", accuDetail.getAccuNo(), accuDetail.getKdNo() );
				accuDetails.put( readerKey,  accuDetail );
				
				String relayKey = String.format( "%s:0:%s:0", accuDetail.getAccuNo(), accuDetail.getDoorStrikeNo() );
				accuDetails.put( relayKey, accuDetail );
				
				String sensorKey = String.format( "%s:0:0:%s", accuDetail.getAccuNo(), accuDetail.getDoorContactNo() );
				accuDetails.put( sensorKey, accuDetail );
				++i;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		
		from( "direct:send" )
		.to( "netty:#{protocol}://#{ip}:#{port}?clientPipelineFactory=#device-guradtec-door-cpf&sync=false&allowDefaultCodec=false&lazyChannelCreation=false" );
		
		from( "direct:recv" )
		.process( new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				// TODO Auto-generated method stub
				Object message = exchange.getIn().getBody();
				if ( message instanceof EventResponse ) {
					EventResponse er = (EventResponse)message;
					int eventNo = (int)er.getEventNo();
					if ( eventNo < 0 ) {
						eventNo += 256;
					}
					
					String metaKey = String.format( "%s:%s:0:0", Integer.toString( er.getAccuBankNo() ),
															Byte.toString( er.getReaderNo() ) );
					if ( er.getReaderNo() == 0 )  {
						metaKey = String.format( "%s:0:0:%s", Integer.toString( er.getAccuBankNo() ),
															Byte.toString( er.getSensorNo() ) );
					}
					
					AccuDetail detail = accuDetails.get( metaKey );
					if ( detail != null ) {
						String key = detail.getOid();
						String value = Integer.toString( eventNo );
						
						//if ( !value.equals( "23" )  ) {		//opening door permanentrly code						
						Map<String, Object> body = new HashMap<String, Object>();
						body.put( key, value );
						exchange.getIn().setBody( body );
							/*
						}
						else {
							exchange.getIn().setBody( null );
						}
						*/
					}
					else {
						exchange.getIn().setBody( null );
					}
					
				}
				else {
					exchange.getIn().setBody( null );
				}
			}
		} )
		.filter( body().isNotNull() )
		.to( "vm:sys-tag-queue?size=#{queueSize}&purgeWhenStopping=true" )
		.to( "vm:sys-ws-queue?size=#{queueSize}&purgeWhenStopping=true" );
		
	}
}
