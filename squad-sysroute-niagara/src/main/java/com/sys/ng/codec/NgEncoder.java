package com.sys.ng.codec;

import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sys.ng.packet.IPacket;

public class NgEncoder extends OneToOneEncoder {
	private static final Logger logger = LoggerFactory.getLogger( NgEncoder.class );

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		// TODO Auto-generated method stub
		ChannelBuffer header = ChannelBuffers.buffer( 4 + 1 );
		if ( msg instanceof IPacket ) {
			IPacket packet = (IPacket)msg;
			ChannelBuffer payload = packet.encode();
			int payloadLen = 5 + payload.readableBytes();
			byte command = packet.getCommand();
			
			header.writeInt( payloadLen );
			header.writeByte( command );
			
			ChannelBuffer soh = ChannelBuffers.buffer( 1 );
			soh.writeByte( IPacket.SOH );
			
			ChannelBuffer eot = ChannelBuffers.buffer( 1 );
			eot.writeByte( IPacket.EOT );
			
			ChannelBuffer result = ChannelBuffers.wrappedBuffer( soh, header, payload, eot );
			/*@@
			String s = "[SEND]::";
			for ( int n = 0; n < result.capacity(); n++ ) {
				s += (" 0x" + Integer.toHexString( result.getByte( n ) ) );
			}
			logger.debug( s );
			*/
			return result;
		}
		
		return null;
	}
}
