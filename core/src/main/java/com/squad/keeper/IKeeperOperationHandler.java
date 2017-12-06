package com.squad.keeper;


public interface IKeeperOperationHandler {
	public void connect();
	public void create( String node, byte[] data );
	public void create( String node, byte[] data, INodeEventHandler handler );
	public void delete( String node );
	public void delete( String node, INodeEventHandler handler );
	public void register( IWatcher watcher );
	public byte[] getData( String node );
	
	public void close();
}
