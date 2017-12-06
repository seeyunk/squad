package com.hsnc.ax.packet;

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
	public static byte SOH = 0x01;
	public static byte EOT = 0x04;
	
	public static int MAX_BUFFER = 64 * 1024;
	public static String DELIMITER = ";";
	public static String SUB_DELIMITER = ":";
	public static byte PING = 0x01;
	public static byte PONG = 0x02;
	public static byte READ_POINTS = 0x03;
	public static byte WRITE_POINTS = 0x04;
	public static byte READ_POINTS_VALUE = 0x05;
	public static byte WRITE_POINTS_VALYE = 0x06;
	
	public byte[] encode() throws Exception;
}
