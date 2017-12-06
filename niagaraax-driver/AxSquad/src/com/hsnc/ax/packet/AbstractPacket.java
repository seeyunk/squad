package com.hsnc.ax.packet;

import javax.baja.io.ByteBuffer;


public abstract class AbstractPacket implements IPacket {
	
	private final byte command;
	
	public AbstractPacket( byte command ) {
		this.command = command;
	}
	
	public byte getCommand() {
		return this.command;
	}

	protected byte[] encode( ByteBuffer payload ) {
		int bodyLen = payload == null ? 0 : payload.getLength();
		int packetLen = 4 + 1 + bodyLen;
		
		ByteBuffer buffer = new ByteBuffer();
		buffer.writeInt( packetLen );
		buffer.write( this.command );
		if ( bodyLen > 0 ) {
			buffer.write( payload.toByteArray() );
		}
		
		return buffer.toByteArray();
	}
	
	public static void debug( String tag, byte[] buffer ) {
		System.out.println( tag + Integer.toString( buffer.length ) );
		
		String s = "[RECV::BAxSquadDriverReadResponse]::";
		for ( int i = 0; i < buffer.length; i++ ) {
			String v = " 0x" + Integer.toHexString( buffer[i] );
			s += v;
			}
  
		System.out.println( s);
	}
}
