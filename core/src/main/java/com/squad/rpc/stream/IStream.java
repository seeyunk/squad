package com.squad.rpc.stream;

import java.io.InputStream;
import java.util.List;

import org.jboss.netty.channel.Channel;


public interface IStream {
	public String getStreamId();
	public List<String> getTargetIds();
	public void write( InputStream is, long length ) throws Exception;
	public void close();
	public IStreamHandler getHandler();
	public Channel getChannel();
}
