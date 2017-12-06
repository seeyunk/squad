package com.sys.ext.processor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtProtocolProcessor implements Processor {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String HEADER_GET = "@GET";
	public static final String HEADER_SET = "@SET";
	public static final String HEADER_GCK = "@GCK";
	public static final String HEADER_SCK = "@SCK";
	public static final int HEADER_LEN = 4;
	public static final String DELIMITER = ";";
	public static final String INDICATOR = ">";
	public static final String GCK_NOT_EXIST_TAG = "NoTag";
	public static final String GCK_NOT_EXIST_VALUE = "null";
	
	private Map<String, String> parseGetProtocol( String in ) throws Exception {
		Map<String, String> tagMap = new HashMap<String, String>();
		int first = in.indexOf( DELIMITER ) + 1;
		int last = in.lastIndexOf( DELIMITER );
		if ( first > 0 && last > 0 && last > first ) {
			String[] tags = in.substring( first, last ).split( DELIMITER );
			for ( int n = 0; n < tags.length; n++ ) {
				tagMap.put( tags[n], GCK_NOT_EXIST_TAG );
			}
		}
		
		return tagMap;
	}
	
	private Map<String, String> parseSetProtocol( String in ) throws Exception {
		Map<String, String> tagMap = new HashMap<String, String>();
		int first = in.indexOf( DELIMITER ) + 1;
		int last = in.lastIndexOf( DELIMITER );
		if ( first > 0 && last > 0 && last > first ) {
			String[] tagSet = in.substring( first, last ).split( DELIMITER );
			for ( int n = 0; n < tagSet.length; n++ ) {
				String[] elems = tagSet[n].split( INDICATOR );
				if ( elems.length != 2 ) {
					throw new Exception();
				}
				tagMap.put( elems[0], elems[1] );
			}
		}
		return tagMap;
	}
	
	private String encode( String header, Map<String, String> payload ) {
		StringBuffer sb = new StringBuffer();
		sb.append( header );
		sb.append( DELIMITER );
		
		ChannelBuffer delim = Delimiters.lineDelimiter()[0];
		if ( header.equals( HEADER_GCK ) ) {
			Set<Entry<String, String>> entries = payload.entrySet();
			for ( Iterator<Entry<String, String>> i = entries.iterator(); i.hasNext(); ) {
				Entry<String, String> entry = i.next();
				sb.append( entry.getKey() );
				sb.append( INDICATOR );
				sb.append( entry.getValue() );
				sb.append( DELIMITER );
			}
		}
		else if ( header.equals( HEADER_SCK ) ) {
			Set<Entry<String, String>> entries = payload.entrySet();
			for ( Iterator<Entry<String, String>> i = entries.iterator(); i.hasNext(); ) {
				Entry<String, String> entry = i.next();
				sb.append( entry.getKey() );
				sb.append( DELIMITER );
			}
		}
		
		sb.append( "\r\n" );
		
		return sb.toString();
	}
	
	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		String packet = exchange.getIn().getBody( String.class );
		if ( packet.length() < HEADER_LEN ) {
			exchange.getIn().setFault( true );
			logger.info( "invalid ext header#1" );
			return;
		}
		
		String header = packet.substring( 0, HEADER_LEN );
		if ( !header.equals( HEADER_GET ) &&
				!header.equals( HEADER_SET ) ) {
			exchange.getIn().setFault( true );
			logger.info( "invalid ext header#2" );
			return;
		}
		
		exchange.getIn().setHeader( "ext-header", header );
		if ( header.equals( HEADER_GET ) ) {
			Map<String, String> params = this.parseGetProtocol( packet );
			//get tags from payload
			Map<String, String> payload = null;
			String result = this.encode( HEADER_GCK, payload );
			exchange.getOut().setBody( result );
		}
		else if ( header.equals( HEADER_SET ) ) {
			Map<String, String> payload = this.parseSetProtocol( packet );
			String result = this.encode( HEADER_SCK, payload );
			exchange.getOut().setBody( result );
		}
		
	}
}
