package dvc.guardtec.door.codec;

import java.net.InetSocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import dvc.guardtec.door.entity.EventRequest;
import dvc.guardtec.door.entity.Header;
import dvc.guardtec.door.entity.IPacket;

public class GuardtecDoorEncoder extends OneToOneEncoder  {

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		// TODO Auto-generated method stub
		InetSocketAddress myAddress = (InetSocketAddress)ctx.getChannel().getLocalAddress();
		InetSocketAddress desAddress = (InetSocketAddress)ctx.getChannel().getRemoteAddress();
		
		if ( msg instanceof IPacket ) {
			if ( msg instanceof Header ) {
				Header header = (Header)msg;
				header.setPort( (short)myAddress.getPort() );
				header.setSrcAddress( myAddress.getAddress() );
				msg = (Object)header;
			}
			else if ( msg instanceof EventRequest ) {
				EventRequest eq = (EventRequest)msg;
				Header header = eq.getHeader();
				header.setPort( (short)myAddress.getPort() );
				header.setSrcAddress( myAddress.getAddress() );
				header.setDesAddress( desAddress.getAddress() );
				eq.setHeader( header );
				msg = (Object)eq;
			}
			
			IPacket packet = (IPacket)msg;
			return packet.encode();
		}
		
		return null;
	}

}
