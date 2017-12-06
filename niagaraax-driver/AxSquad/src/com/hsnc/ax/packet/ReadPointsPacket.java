package com.hsnc.ax.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.baja.io.ByteBuffer;


public class ReadPointsPacket extends AbstractPacket {
	private final String unitName;
 
	public ReadPointsPacket( String unitName ) {
		super( IPacket.READ_POINTS );
		this.unitName = unitName;
	}
 
	public byte[] encode() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append( this.unitName );
		sb.append( IPacket.DELIMITER );
		
		return super.encode( new ByteBuffer( sb.toString().getBytes( "utf-8" ) ) );
	}
	
	public static Map decode( byte[] packet ) throws Exception {
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
				resultMap.put( token, "" );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
}
