package com.hsnc.ax.packet;

public class PingPacket extends AbstractPacket {	
	public PingPacket() {
		super( IPacket.PING );
	}
	
	public byte[] encode() {
		// TODO Auto-generated method stub
		return super.encode( null );
	}
}
