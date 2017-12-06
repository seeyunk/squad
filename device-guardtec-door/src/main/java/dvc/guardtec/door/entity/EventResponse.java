package dvc.guardtec.door.entity;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class EventResponse implements Serializable, IPacket {
	private static final long serialVersionUID = -8019668239092039450L;
	private static final int EVENT_RESPONSE_LEN = 44;
	
	private int accuBankNo;
	private int seqNo;
	private byte flag;
	private byte year;
	private byte month;
	private byte day;
	private byte dow;
	private byte hour;
	private byte minute;
	private byte second;
	private byte readerNo;
	private byte sensorNo;
	private byte eventNo;
	private byte uidH;
	private int uidL;
	
	public ChannelBuffer encode() {
		return null;
	}
	
	public static EventResponse decode( ChannelBuffer buffer ) throws UnknownHostException {
		EventResponse eventResponse = new EventResponse();
		
		buffer.skipBytes( 8 + 8 + 4 );
		ChannelBuffer leAccuBankNo = ChannelBuffers.copiedBuffer( ByteOrder.LITTLE_ENDIAN, buffer.readBytes( 4 ).array() );
		eventResponse.setAccuBankNo( leAccuBankNo.readInt() );
		
		ChannelBuffer leSeqNo = ChannelBuffers.copiedBuffer( ByteOrder.LITTLE_ENDIAN, buffer.readBytes( 4 ).array() );
		eventResponse.setSeqNo( leSeqNo.readInt() );
		
		eventResponse.setFlag( buffer.readByte() );
		eventResponse.setYear( buffer.readByte() );
		eventResponse.setMonth( buffer.readByte() );
		eventResponse.setDay( buffer.readByte() );
		eventResponse.setDow( buffer.readByte() );
		eventResponse.setHour( buffer.readByte() );
		eventResponse.setMinute( buffer.readByte() );
		eventResponse.setSecond( buffer.readByte() );
		eventResponse.setReaderNo( buffer.readByte() );
		eventResponse.setSensorNo( buffer.readByte() );
		eventResponse.setEventNo( buffer.readByte() );
		eventResponse.setUidH( buffer.readByte() );
		eventResponse.setUidL( buffer.readInt() );
		
		return eventResponse;
	}

	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public int getAccuBankNo() {
		return accuBankNo;
	}
	public void setAccuBankNo(int accuBankNo) {
		this.accuBankNo = accuBankNo;
	}
	public byte getFlag() {
		return flag;
	}
	public void setFlag(byte flag) {
		this.flag = flag;
	}
	public byte getYear() {
		return year;
	}
	public void setYear(byte year) {
		this.year = year;
	}
	public byte getMonth() {
		return month;
	}
	public void setMonth(byte month) {
		this.month = month;
	}
	public byte getDay() {
		return day;
	}
	public void setDay(byte day) {
		this.day = day;
	}
	public byte getDow() {
		return dow;
	}
	public void setDow(byte dow) {
		this.dow = dow;
	}
	public byte getHour() {
		return hour;
	}
	public void setHour(byte hour) {
		this.hour = hour;
	}
	public byte getMinute() {
		return minute;
	}
	public void setMinute(byte minute) {
		this.minute = minute;
	}
	public byte getSecond() {
		return second;
	}
	public void setSecond(byte second) {
		this.second = second;
	}
	public byte getReaderNo() {
		return readerNo;
	}
	public void setReaderNo(byte readerNo) {
		this.readerNo = readerNo;
	}
	public byte getSensorNo() {
		return sensorNo;
	}
	public void setSensorNo(byte sensorNo) {
		this.sensorNo = sensorNo;
	}
	public byte getEventNo() {
		return eventNo;
	}
	public void setEventNo(byte eventNo) {
		this.eventNo = eventNo;
	}
	public byte getUidH() {
		return uidH;
	}
	public void setUidH(byte uidH) {
		this.uidH = uidH;
	}
	public int getUidL() {
		return uidL;
	}
	public void setUidL(int uidL) {
		this.uidL = uidL;
	}
	

}
