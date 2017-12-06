package dvc.guardtec.door.entity;

import org.jboss.netty.buffer.ChannelBuffer;

public interface IPacket {
	public ChannelBuffer encode();
}
