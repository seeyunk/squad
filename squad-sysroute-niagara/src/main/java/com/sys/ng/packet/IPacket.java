package com.sys.ng.packet;

import org.jboss.netty.buffer.ChannelBuffer;

public interface IPacket {
	/*
	 * 					request : len(4) + command(1) + payload
	 * 					response : len(4) + command(1) + payload
	 * 					
	 * 					req: READ_POINTS : deviceName;
	 * 					res: READ_POINTS : deviceName;key;key; 
	 * 					req: READ_POINTS_VALUE : deviceName;key;key;
	 * 					res: READ_POINTS_VALUE : deviceName;key:val;
	 * 					
	 * 
	 * 
	 */
	public ChannelBuffer encode();
	public byte getCommand();
	public static int MAX_BUFFER = 64 * 1024;
	public static String DELIMITER = ";";
	public static String SUB_DELIMITER = ":";
	public static byte SOH = 0x01;
	public static byte EOT = 0x04;
	public static byte PING = 0x01;
	public static byte PONG = 0x02;
	public static byte READ_POINTS = 0x03;
	public static byte WRITE_POINTS = 0x04;
	public static byte READ_POINTS_VALUE = 0x05;
	public static byte WRITE_POINTS_VALUE = 0x06;
}
