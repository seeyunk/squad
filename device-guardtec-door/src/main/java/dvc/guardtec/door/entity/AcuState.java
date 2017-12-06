package dvc.guardtec.door.entity;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class AcuState implements Serializable {
	private static final long serialVersionUID = -2098362156648103892L;
	public static final int ACU_STATE_LEN = 61;
	
	private InetAddress accuBankIp;
	private short port;
	private int accuBankNo;
	private String accuName;
	
	public static AcuState decode( ChannelBuffer buffer ) throws UnknownHostException {
		AcuState acuState = new AcuState();
		acuState.setAccuBankIp( InetAddress.getByAddress( buffer.readBytes( 4 ).array() ) );
		acuState.setPort( buffer.readShort() );
		
		ChannelBuffer leAccuBankNo = ChannelBuffers.copiedBuffer( ByteOrder.LITTLE_ENDIAN, buffer.readBytes( 4  ).array() );
		acuState.setAccuBankNo( leAccuBankNo.readInt() );
		
		acuState.setAccuName( buffer.readBytes( 51 ).toString( Charset.forName( "utf-8" ) ) );
		return acuState;
	}
	
	public InetAddress getAccuBankIp() {
		return accuBankIp;
	}
	public void setAccuBankIp(InetAddress accuBankIp) {
		this.accuBankIp = accuBankIp;
	}
	public short getPort() {
		return port;
	}
	public void setPort(short port) {
		this.port = port;
	}
	public int getAccuBankNo() {
		return accuBankNo;
	}
	public void setAccuBankNo(int accuBankNo) {
		this.accuBankNo = accuBankNo;
	}
	public String getAccuName() {
		return accuName;
	}
	public void setAccuName(String accuName) {
		this.accuName = accuName;
	}
}
