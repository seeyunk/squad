package dvc.guardtec.door.codec;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import dvc.guardtec.door.entity.AcuRelay;
import dvc.guardtec.door.entity.AcuState;
import dvc.guardtec.door.entity.EventResponse;
import dvc.guardtec.door.entity.Header;

public class GuardtecDoorDecoder extends FrameDecoder {
	public GuardtecDoorDecoder() {
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		// TODO Auto-generated method stub
		if ( buffer.readableBytes() < Header.HEADER_LEN ) {
			return null;
		}
		
		buffer.markReaderIndex();
		Header header = Header.decode( buffer );
		if ( buffer.readableBytes() < header.getDataLength() ) {
			buffer.resetReaderIndex();
			return null;
		}
		
		if ( header.getMessageId() == Header.MESSAGE_ID_ACU_STATE ) {
			if ( header.getDataLength() < AcuState.ACU_STATE_LEN ) {
				return null;
			}
			
			List<AcuState> acuStates= new ArrayList<AcuState>();
			for ( ; buffer.readableBytes() > 0; ) {
				AcuState acuState = AcuState.decode( buffer );
				acuStates.add( acuState );
			}
			
			return (AcuState[])acuStates.toArray( new AcuState[acuStates.size()]);
		}
		else if ( header.getMessageId() == Header.MESSAGE_ID_ACU_RELAY ) {
			if ( buffer.readableBytes() < header.getDataLength() ) {
				return null;
			}
			
			return AcuRelay.decode( buffer );
		}
		else if ( header.getMessageId() == Header.MESSAGE_ID_RECV_EVENT ) {
			return EventResponse.decode( buffer );
		}
		else {
			buffer.skipBytes( header.getDataLength() );
		}
		
		
		return null;
	}
	
	public String getByteString( byte[] buffer ) {
		StringBuffer sb = new StringBuffer();
		for ( int n =  0; n < buffer.length; n++ ) {
			sb.append( buffer[n] );
		}
		return sb.toString();
	}
}
