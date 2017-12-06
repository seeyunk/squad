package com.hsnc.ax.packet;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.baja.io.ByteBuffer;

public class ReadPointsValuePacket extends AbstractPacket {
	private final String unitName;
	private final String pointId;
	private String pointValue;
	
	public ReadPointsValuePacket( String unitName, String pointId ) {
		super( IPacket.READ_POINTS_VALUE );
		this.unitName = unitName;
		this.pointId = pointId;
	}
	
	public ReadPointsValuePacket( String unitName, String pointId, String pointValue ) {
		super( IPacket.READ_POINTS_VALUE );
		this.unitName = unitName;
		this.pointId = pointId;
		this.pointValue = pointValue;
	}

	public byte[] encode() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append( this.unitName );
		sb.append( IPacket.DELIMITER );
		sb.append( this.pointId );
		sb.append( IPacket.DELIMITER );
		
		return super.encode( new ByteBuffer( sb.toString().getBytes( "utf-8" ) ) );
	}
	
	public static Map decode( byte[] packet ) {
		Map resultMap = new HashMap();
		ByteBuffer buffer = new ByteBuffer( packet );
		try {
			String result = new String( buffer.toByteArray(), "utf-8" );
			StringTokenizer st = new StringTokenizer( result, IPacket.DELIMITER );
			for ( int i = 0; st.hasMoreTokens(); i++ ) {
				String token = st.nextToken();
				if ( i == 0 ) {
					continue;
				}
				
				StringTokenizer st2 = new StringTokenizer( token, IPacket.SUB_DELIMITER );
				resultMap.put( st2.nextToken(), st2.nextToken() );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
	
	public String getPointId() {
		return this.pointId;
	}
	
	public String getPointValue() {
		return this.pointValue;
	}
}
