package com.squad.rpc.stream;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.context.SquadContext;
import com.squad.entity.Message;
import com.squad.looper.AsyncLooper.MSG;
import com.squad.looper.IMessageNotifier;
import com.squad.protobuf.packet.Squad.Packet;
import com.squad.protobuf.packet.Squad.Packet.Type;
import com.squad.protobuf.packet.Squad.StreamBeginPacket;
import com.squad.protobuf.packet.Squad.StreamClosePacket;
import com.squad.protobuf.packet.Squad.StreamEndPacket;
import com.squad.protobuf.packet.Squad.StreamOpenReqPacket;
import com.squad.protobuf.packet.Squad.StreamOpenResPacket;
import com.squad.protobuf.packet.Squad.StreamRunPacket;
import com.squad.protobuf.packet.Squad.StreamType;

public class StreamManager {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final AtomicInteger streamIdPool = new AtomicInteger( 1 );
	private ConcurrentMap<String, IStream> streamPool = new ConcurrentHashMap<String, IStream>();
	private CountDownLatch latch;
	private SquadContext ctx;
	
	public StreamManager( SquadContext ctx ) {
		this.ctx = ctx;
	}
	
	public String newStreamId() {
		return String.format( "%s-%d", this.ctx.getConfig().getScoutId(), streamIdPool.getAndIncrement() );
	}
	
	public IStream openFileStream( List<String> targetIds, final Channel channel, String fileName, long fileSize ) {
		this.latch = new CountDownLatch( 1 );
		final String streamId = newStreamId();
		StreamOpenReqPacket streamOpenReqPacket = StreamOpenReqPacket.newBuilder()
												.setScoutId( this.ctx.getConfig().getScoutId() )
												.setStreamId( streamId )
												.setStreamType( StreamType.FILE )
												.setFileName( fileName )
												.setFileSize( fileSize )
												.build();
		return this.openStream( targetIds, channel, streamId, streamOpenReqPacket );
	}
	
	private IStream openStream( List<String> targetIds, final Channel channel, final String streamId, StreamOpenReqPacket streamOpenReqPacket ) {
		// TODO Auto-generated method stub
		final IStream stream = new Stream( targetIds, streamId, channel, this );
		Packet packet = Packet.newBuilder()
					.setType( Type.STREAM_OPEN_REQ )
					.addAllTargetId( targetIds )					
					.setStreamOpenReqPacket( streamOpenReqPacket )
					.build();
		channel.write( packet )
			.addListener( new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				// TODO Auto-generated method stub
				streamPool.putIfAbsent( streamId, stream );
				logger.debug( "Stream client requested to open" );
			}} );
		
		try {
			latch.await( 5000L, TimeUnit.MILLISECONDS );
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		return stream;
	}
	
	public void handleStreamOpenReqPacket( Channel channel, StreamOpenReqPacket streamOpenReqPacket ) {
		String streamId = streamOpenReqPacket.getStreamId();
		IStream stream = this.getStreamById( streamId );
		if ( stream == null ) { //server
			logger.debug( "Stream server opened" );
			StreamType streamType = streamOpenReqPacket.getStreamType();
			if ( streamType.equals( StreamType.FILE ) ) {
				this.openFileServerStream( channel, streamId, streamOpenReqPacket );
			}
		}
	}
	
	private void openFileServerStream( final Channel channel, final String streamId, StreamOpenReqPacket streamOpenReqPacket ) {
		String fileName = streamOpenReqPacket.getFileName();
		fileName = fileName.replace( ".managed", "" );
		IStreamHandler handler = new FileStreamWriteHandler( this.ctx.getConfig().getSquadLibPath(), fileName );
		
		this.openServerStream( streamId, channel, handler, streamOpenReqPacket );
	}

	private void openServerStream( final String streamId, 
									final Channel channel, 
									IStreamHandler handler, 
									StreamOpenReqPacket streamOpenReqPacket ) {
		// TODO Auto-generated method stub
		final IStream stream = new Stream( null, streamId, channel, handler, this );
		StreamOpenResPacket streamOpenResPacket = StreamOpenResPacket.newBuilder()
				.setStreamId( streamId )
				.build();
		Packet packet = Packet.newBuilder()
						.setType( Type.STREAM_OPEN_RES )
						.addTargetId( streamOpenReqPacket.getScoutId() )
						.setStreamOpenResPacket( streamOpenResPacket )
						.build();
		channel.write( packet )
			.addListener( new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				// TODO Auto-generated method stub
				streamPool.putIfAbsent( streamId, stream );
			}} );
	}
	
	public void handleStreamOpenResPacket( StreamOpenResPacket packet ) {
		// TODO Auto-generated method stub
		String streamId = packet.getStreamId();
		IStream stream = this.getStreamById( streamId );
		try {
			if ( stream == null ) {
				throw new NullPointerException( "streamId must not be null" );
			}
		}
		finally {
			latch.countDown();
		}
	}
	
	public void handleStreamBeginPacket( StreamBeginPacket packet ) {
		String streamId = packet.getStreamId();
		IStream stream = this.getStreamById( streamId );
		if ( stream == null ) {
			throw new NullPointerException( "Stream must not be null" );
		}	
		
		IStreamHandler handler = stream.getHandler();
		handler.handleStreamBegin( packet );
	}
	
	public void handleStreamRunPacket( StreamRunPacket packet ) {
		String streamId = packet.getStreamId();
		IStream stream = this.getStreamById( streamId );
		if ( stream == null ) {
			throw new NullPointerException( "Stream must not be null" );
		}	
		
		IStreamHandler handler = stream.getHandler();
		handler.handleStreamRun( packet );
	}
	
	public void handleStreamEndPacket( StreamEndPacket packet ) {
		String streamId = packet.getStreamId();
		IStream stream = this.getStreamById( streamId );
		if ( stream == null ) {
			throw new NullPointerException( "Stream must not be null" );
		}	
		
		IStreamHandler handler = stream.getHandler();
		handler.handleStreamEnd( packet );
	}
	
	public void closeStream( String streamId ) {
		// TODO Auto-generated method stub
		IStream stream = this.getStreamById( streamId );
		final Channel channel = stream.getChannel();
		
		StreamClosePacket streamClosePacket = StreamClosePacket.newBuilder()
													.setStreamId( stream.getStreamId() )
													.build();
		
		Packet packet = Packet.newBuilder().setType( Type.STREAM_CLOSE )
										.addAllTargetId( stream.getTargetIds() )
										.setStreamClosePacket( streamClosePacket )
										.build();
		channel.write( packet ).addListener( new StreamCloseFutureHandler( streamId ) );
		return;
	}
	
	private class StreamCloseFutureHandler implements ChannelFutureListener {
		private String streamId;
		public StreamCloseFutureHandler( String streamId ) {
			this.streamId = streamId;
		}
		
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			// TODO Auto-generated method stub
			streamPool.remove( this.streamId );
			logger.debug( "Stream {} closed [StreamPoolSize::{}]", this.streamId, streamPool.size() );
		}
	}
	
	public IStream getStreamById( String streamId ) {
		return this.streamPool.get( streamId );
	}
	
	public void closeStream( IStream stream ) {
		this.closeStream( stream.getStreamId() );
	}
	
	public void handleStreamClosePacket( StreamClosePacket packet ) throws Exception {
		IStreamHandler handler = this.getStreamById( packet.getStreamId() ).getHandler();
		if ( handler instanceof FileStreamWriteHandler ) {
			this.ctx.sendMessage( new Message( MSG.NEW_SERVICE_FOUND, ((FileStreamWriteHandler) handler).getFullPath() ) );
		}
		else if ( handler instanceof ObjectStreamWriteHandler ) {
			
		}
		this.streamPool.remove( packet.getStreamId() );
		logger.debug( "Stream {} closed [StreamPoolSize::{}]", packet.getStreamId(), this.streamPool.size() );
	}
	
	
}
