package com.squad.rpc.stream;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.google.protobuf.ByteString;
import com.squad.protobuf.packet.Squad.Packet;
import com.squad.protobuf.packet.Squad.Packet.Type;
import com.squad.protobuf.packet.Squad.StreamBeginPacket;
import com.squad.protobuf.packet.Squad.StreamEndPacket;
import com.squad.protobuf.packet.Squad.StreamRunPacket;

public class Stream implements IStream {
	
	private final String streamId;
	private List<String> targetIds;
	
	private final Channel channel;
	private final StreamManager streamManager;
	private final IStreamHandler handler;
	private final int MAX_CHUNK_SIZE = 64 * 1024;
	
	public Stream( List<String> targetIds, String streamId, Channel channel, StreamManager streamManager ) {
		this( targetIds, streamId, channel, null, streamManager );
	}
	
	public Stream( List<String> targetIds, String streamId, Channel channel, IStreamHandler handler, StreamManager streamManager ) {
		this.targetIds = targetIds;
		this.streamId = streamId;
		this.channel = channel;
		this.streamManager = streamManager;
		this.handler = handler;
	}
	
	@Override
	public IStreamHandler getHandler() {
		return this.handler;
	}
	
	public String getStreamId() {
		return this.streamId;
	}
	
	@Override
	public Channel getChannel() {
		return this.channel;
	}
	
	@Override
	public void write( final InputStream is, final long length ) throws Exception {
		// TODO Auto-generated method stub
		//final String streamId = this.getStreamId();
		final String streamId = this.getStreamId();
		final Channel channel = this.getChannel();
		final CountDownLatch latch = new CountDownLatch( 1 );
		StreamBeginPacket beginPacket = StreamBeginPacket.newBuilder()
													.setStreamId( streamId )
													.build();
		Packet packet = Packet.newBuilder()
						.addAllTargetId( this.targetIds )
						.setType( Type.STREAM_BEGIN )
						.setStreamBeginPacket( beginPacket )
						.build();
		ChannelFuture future = channel.write( packet );
		future.addListener( new ChannelFutureListener() {
			private final ChannelBuffer buffer = ChannelBuffers.buffer( MAX_CHUNK_SIZE );
			private long offset = 0;
			
			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				// TODO Auto-generated method stub
				if ( !future.isSuccess() ) {
					future.getChannel().close();
					is.close();
					latch.countDown();
					return;
				}
				
				buffer.clear();
				int lenToWrite = (int)Math.min( length - offset, buffer.writableBytes() );
				buffer.writeBytes( is, lenToWrite );
				offset += buffer.writerIndex();
				
				ByteString bs = ByteString.copyFrom( buffer.toByteBuffer() );
				StreamRunPacket bufferPacket = StreamRunPacket.newBuilder()
											.setStreamId( streamId )
											.setBuffer( bs )
											.build();
				Packet packet = Packet.newBuilder()
								.setType( Type.STREAM_RUN )
								.addAllTargetId( Stream.this.targetIds )
								.setStreamRunPacket( bufferPacket )
								.build();
				ChannelFuture chunkFuture = future.getChannel().write( packet );
				if ( offset < length ) {
					chunkFuture.addListener( this );
				}
				else {
					StreamEndPacket streamEndPacket = StreamEndPacket.newBuilder()
														.setStreamId( streamId )
														.build();
					Packet endPacket = Packet.newBuilder()
										.setType( Type.STREAM_END )
										.addAllTargetId( Stream.this.targetIds )
										.setStreamEndPacket( streamEndPacket )
										.build();
					
					future.getChannel().write( endPacket );
					is.close();
					latch.countDown();
				}	
			} } );
		
		latch.await();
		return;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		this.streamManager.closeStream( this.streamId );
	}

	@Override
	public List<String> getTargetIds() {
		// TODO Auto-generated method stub
		return this.targetIds;
	}
}
