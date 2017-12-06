package com.sys.ng.packet;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class ReadPointsValuePacket extends AbstractPacket implements Serializable {
	private static final long serialVersionUID = -1768367750561515144L;
	private String unitName;
	private Map<String, String> points;
	
	public ReadPointsValuePacket() {
		super( IPacket.READ_POINTS_VALUE );
	}
	
	public ReadPointsValuePacket( String unitName, Map<String, String> points ) {
		this();
		this.unitName = unitName;
		this.points = points;
	}
	
	public static ReadPointsValuePacket decode( ChannelBuffer buffer ) {
		String packet = buffer.toString( Charset.forName( "utf-8" ) );
		
		String[] tokens = packet.split( ";" );
		String unitName = tokens[0];
		Map<String, String> points = new HashMap<String, String>();
		for ( int n = 1 ; n < tokens.length; n++ ) {
			points.put( tokens[n], "" );
		}
		return new ReadPointsValuePacket( unitName, points );
	}

	@Override
	public ChannelBuffer encode() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append( this.unitName );
		sb.append( IPacket.DELIMITER );
		Iterator<Entry<String, String>> i = this.points.entrySet().iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, String> entry = i.next();
			sb.append( entry.getKey() );
			sb.append( IPacket.SUB_DELIMITER );
			sb.append( entry.getValue() );
			sb.append( IPacket.DELIMITER );
		}
		
		byte[] result = sb.toString().getBytes( Charset.forName( "utf-8" ) );
		return ChannelBuffers.wrappedBuffer( result );
	}
	
	public String getUnitName() {
		return this.unitName;
	}
	
	public Iterator<String> getPointIds() {
		return this.points.keySet().iterator();
	}
	
}
