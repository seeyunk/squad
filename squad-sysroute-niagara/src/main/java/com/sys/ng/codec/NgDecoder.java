package com.sys.ng.codec;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sys.ng.packet.IPacket;
import com.sys.ng.packet.PingPacket;
import com.sys.ng.packet.PongPacket;
import com.sys.ng.packet.ReadPointsValuePacket;
import com.sys.ng.packet.ReadPointsPacket;

public class NgDecoder extends FrameDecoder {
	private static final Logger logger = LoggerFactory.getLogger( NgDecoder.class );
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		// TODO Auto-generated method stub
		if ( buffer.readableBytes() < 5 ) {
			return null;
		}
		
		/*@@
		String s = "[RECV]::";
		for ( int n = 0; n < buffer.capacity(); n++ ) {
			s += (" 0x" + Integer.toHexString( buffer.getByte( n ) ) );
		}
		logger.debug( s );
		*/
		int packetLen = buffer.readInt();
		byte command = buffer.readByte();	
		Object body = null;
		if ( command == IPacket.PING ) {
			body = PingPacket.decode();
		}
		else if ( command == IPacket.READ_POINTS ) {
			body = ReadPointsPacket.decode( buffer );
		}
		else if ( command == IPacket.READ_POINTS_VALUE ) {
			body = ReadPointsValuePacket.decode( buffer );
		}
		
		return body;
	}
}
