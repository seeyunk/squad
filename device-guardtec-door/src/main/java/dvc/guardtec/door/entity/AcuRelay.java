package dvc.guardtec.door.entity;

import java.io.Serializable;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class AcuRelay implements Serializable {
	private static final long serialVersionUID = 2935582324879506359L;
	public static final int ACU_RELAY_LEN = 24;
	private byte[] sensor;
	private byte[] relay;
	
	public ChannelBuffer encode() {
		ChannelBuffer buffer = ChannelBuffers.buffer( ACU_RELAY_LEN );
		buffer.writeBytes( this.sensor );
		buffer.writeBytes( this.relay );
		
		return buffer;
	}
	
	public static AcuRelay decode( ChannelBuffer buffer ) {
		AcuRelay acuRelay = new AcuRelay();
		acuRelay.setSensor( buffer.readBytes( 16 ).array() );
		acuRelay.setRelay( buffer.readBytes( 8 ).array() );
		
		return acuRelay;
	}
	
	public byte[] getSensor() {
		return sensor;
	}
	public void setSensor(byte[] sensor) {
		this.sensor = sensor;
	}
	public byte[] getRelay() {
		return relay;
	}
	public void setRelay(byte[] relay) {
		this.relay = relay;
	}
	
}
