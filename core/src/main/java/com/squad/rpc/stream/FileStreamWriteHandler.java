package com.squad.rpc.stream;

import org.jboss.netty.buffer.ChannelBuffer;

import com.google.protobuf.ByteString;
import com.squad.protobuf.packet.Squad.StreamBeginPacket;
import com.squad.protobuf.packet.Squad.StreamEndPacket;
import com.squad.protobuf.packet.Squad.StreamRunPacket;
import com.squad.util.AsyncFileWriter;

public class FileStreamWriteHandler implements IStreamHandler {
	private String defaultDir;
	private String defaultFileName;
	private AsyncFileWriter writer;

	public FileStreamWriteHandler( String dir, String fileName ) {
		this.defaultDir = dir;
		this.defaultFileName = fileName;
	}
	
	public FileStreamWriteHandler( String fileNamePrefix ) {
		StringBuilder sb = new StringBuilder();
		sb.append( fileNamePrefix );
		sb.append( "-" );
		sb.append( Long.toString( System.currentTimeMillis() ) );
		this.defaultFileName = sb.toString();
	}
	
	@Override
	public void handleStreamBegin(StreamBeginPacket packet) {
		// TODO Auto-generated method stub
		this.writer = new AsyncFileWriter( this.defaultDir, this.defaultFileName );
	}
	
	@Override
	public void handleStreamRun(StreamRunPacket packet) {
		// TODO Auto-generated method stub		
		ByteString bs = packet.getBuffer();
		this.writer.write( bs.asReadOnlyByteBuffer() );
	}
	

	@Override
	public void handleStreamRun(ChannelBuffer buffer) {
		// TODO Auto-generated method stub
		this.writer.write( buffer.toByteBuffer() );
	}

	@Override
	public void handleStreamEnd(StreamEndPacket packet) {
		// TODO Auto-generated method stub
		this.writer.close();
	}
	
	public String getFullPath() {
		return AsyncFileWriter.getFullPath( this.defaultDir, this.defaultFileName );
	}
	
	public String getDir() {
		return this.defaultDir;
	}
	
	public String getFileName() {
		return this.defaultFileName;
	}
}
