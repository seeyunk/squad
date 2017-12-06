package dvc.guardtec.door.entity;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class EventRequest implements Serializable, IPacket {
	private static final long serialVersionUID = -3383418451176554515L;
	private static final int EVENT_REQUEST_LEN = 22;
	private Header header;
	
	private int mode = 0;
	private byte[] sDateTime = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	private byte[] eDateTime = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	private short between = 0;
	
	public EventRequest() throws UnknownHostException {
		this.header = new Header( Header.MESSAGE_ID_EVENT );
		this.header.setDataLength( 22 );
	}
	
	public void setHeader( Header header ) {
		this.header = header;
	}
	
	public Header getHeader() {
		return this.header;
	}
	
	public ChannelBuffer encode() {
		ChannelBuffer buffer = ChannelBuffers.buffer( EVENT_REQUEST_LEN );
		
		ChannelBuffer leMode = ChannelBuffers.buffer( ByteOrder.LITTLE_ENDIAN, 4 );
		leMode.writeInt( this.mode );
		buffer.writeBytes( leMode );
		
		buffer.writeBytes( sDateTime );
		buffer.writeBytes( eDateTime );
		
		ChannelBuffer leBetween = ChannelBuffers.buffer( ByteOrder.LITTLE_ENDIAN, 2 );
		leBetween.writeShort( this.between );
		buffer.writeBytes( leBetween );
		
		return ChannelBuffers.wrappedBuffer( this.header.encode(), buffer );
	}
	
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public byte[] getsDateTime() {
		return sDateTime;
	}
	public void setsDateTime(byte[] sDateTime) {
		this.sDateTime = sDateTime;
	}
	public byte[] geteDateTime() {
		return eDateTime;
	}
	public void seteDateTime(byte[] eDateTime) {
		this.eDateTime = eDateTime;
	}
	public short getBetween() {
		return between;
	}
	public void setBetween(short between) {
		this.between = between;
	}
	
}
