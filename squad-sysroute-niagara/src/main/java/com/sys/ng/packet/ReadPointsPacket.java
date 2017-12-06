package com.sys.ng.packet;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class ReadPointsPacket extends AbstractPacket implements Serializable {
	private static final long serialVersionUID = -420284515538091446L;
	private String unitName;
	private Set<String> points;
	
	public ReadPointsPacket() {
		super( IPacket.READ_POINTS );
		// TODO Auto-generated constructor stub
	}
	
	public ReadPointsPacket( Set<String> points ) {
		this();
		this.points = points;
	}
	
	public ReadPointsPacket( String unitName ) {
		this();
		this.unitName = unitName;
	}
	
	public static ReadPointsPacket decode( ChannelBuffer buffer ) throws Exception{
		int len = buffer.readableBytes();
		if ( len == 0 ) {
			throw new Exception( "Device Id must not be null..." );
		}
		
		String packet = buffer.toString( Charset.forName( "utf-8" ) );
		return new ReadPointsPacket( packet.substring( 0, packet.indexOf( ";" ) ) );
	}
	
	public String getUnitName() {
		return this.unitName;
	}

	@Override
	public ChannelBuffer encode() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append( unitName );
		sb.append( IPacket.DELIMITER );
		Iterator<String> i = this.points.iterator();
		for ( ; i.hasNext(); ) {
			String point = i.next();
			sb.append( point );
			sb.append( IPacket.DELIMITER );
		}
		
		byte[] result = sb.toString().getBytes( Charset.forName( "utf-8" ) );
		return ChannelBuffers.wrappedBuffer( result );
	}
}
