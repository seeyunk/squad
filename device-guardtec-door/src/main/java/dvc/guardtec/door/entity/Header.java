package dvc.guardtec.door.entity;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class Header implements Serializable, IPacket {
	private static final long serialVersionUID = -1003913278067058075L;
	public static final int MESSAGE_ID_ACU_STATE = 252010102;
	public static final int MESSAGE_ID_ACU_RELAY = 251010102;
	public static final int MESSAGE_ID_EVENT = 252100101;
	public static final int MESSAGE_ID_RECV_EVENT = 251030101;
	public static final int MESSAGE_ID_END_EVENT = 252100102;
	public static final int HEADER_LEN = 32;
	
	private byte soh = 0x01;
	private byte protocolType = 0x02;
	private short port;
	private int idNumber;
	private byte[] version = { 0x02, 0x05, 0x00, 0x00 };
	private int messageId = -1;
	private InetAddress srcAddress;
	private InetAddress desAddress;
	private int dataLength;
	private int result;
	
	public Header() throws UnknownHostException {
		this( -1 );
	}
	
	public Header( int messageId )  throws UnknownHostException {
		this.messageId = messageId;
		this.srcAddress = InetAddress.getByAddress( new byte[] { 0x00, 0x00, 0x00, 0x00 } );
		this.desAddress = InetAddress.getByAddress( new byte[] { 0x00, 0x00, 0x00, 0x00 } );
	}
	
	public ChannelBuffer encode() {
		ChannelBuffer buffer = ChannelBuffers.buffer( HEADER_LEN );
		buffer.writeByte( this.soh );
		buffer.writeByte( this.protocolType );
		
		ChannelBuffer lePort = ChannelBuffers.buffer( ByteOrder.LITTLE_ENDIAN, 2 );
		lePort.writeShort( this.port );
		buffer.writeBytes( lePort );
		
		ChannelBuffer leIdNumber = ChannelBuffers.buffer( ByteOrder.LITTLE_ENDIAN, 4 );
		leIdNumber.writeInt( this.idNumber );
		buffer.writeBytes( leIdNumber );

		buffer.writeBytes( this.version );
		
		ChannelBuffer leMessageId = ChannelBuffers.buffer( ByteOrder.LITTLE_ENDIAN, 4 );
		leMessageId.writeInt( this.messageId );
		buffer.writeBytes( leMessageId );
		
		buffer.writeBytes( this.srcAddress.getAddress() );
		buffer.writeBytes( this.desAddress.getAddress() );
		
		ChannelBuffer leDataLength = ChannelBuffers.buffer( ByteOrder.LITTLE_ENDIAN, 4 );
		leDataLength.writeInt( this.dataLength );
		buffer.writeBytes( leDataLength );
		
		buffer.writeInt( this.result );
		
		return buffer;
	}
	
	public static Header decode( ChannelBuffer buffer ) throws UnknownHostException {
		
		Header header = new Header();
		header.setSoh( buffer.readByte() );
		header.setProtocolType( buffer.readByte() );
		
		ChannelBuffer lePort = ChannelBuffers.copiedBuffer( ByteOrder.LITTLE_ENDIAN, buffer.readBytes( 2 ).array() );
		header.setPort( lePort.readShort() );
		
		ChannelBuffer leIdNumber = ChannelBuffers.copiedBuffer( ByteOrder.LITTLE_ENDIAN, buffer.readBytes( 4 ).array() );
		header.setIdNumber( leIdNumber.readInt()  );
		header.setVersion( buffer.readBytes( 4 ).array() );
		
		ChannelBuffer leMessageId = ChannelBuffers.copiedBuffer( ByteOrder.LITTLE_ENDIAN, buffer.readBytes( 4 ).array() );
		header.setMessageId( leMessageId.readInt() );
		
		header.setSrcAddress( InetAddress.getByAddress( buffer.readBytes( 4 ).array() ) );
		header.setDesAddress( InetAddress.getByAddress( buffer.readBytes( 4 ).array() ) );
		
		ChannelBuffer leDataLength = ChannelBuffers.copiedBuffer( ByteOrder.LITTLE_ENDIAN, buffer.readBytes( 4 ).array() );
		header.setDataLength( leDataLength.readInt() );
		
		ChannelBuffer leResult = ChannelBuffers.copiedBuffer( ByteOrder.LITTLE_ENDIAN, buffer.readBytes( 4 ).array() );
		header.setResult( leResult.readInt() );
		
		return header;
	}
	
	public byte getSoh() {
		return soh;
	}

	public void setSoh(byte soh) {
		this.soh = soh;
	}

	public byte getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(byte protocolType) {
		this.protocolType = protocolType;
	}

	public short getPort() {
		return port;
	}

	public void setPort(short port) {
		this.port = port;
	}

	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	public byte[] getVersion() {
		return version;
	}

	public void setVersion(byte[] version) {
		this.version = version;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public InetAddress getSrcAddress() {
		return srcAddress;
	}

	public void setSrcAddress(InetAddress srcAddress) {
		this.srcAddress = srcAddress;
	}

	public InetAddress getDesAddress() {
		return desAddress;
	}

	public void setDesAddress(InetAddress desAddress) {
		this.desAddress = desAddress;
	}

	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}
}
