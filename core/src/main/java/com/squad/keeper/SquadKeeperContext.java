package com.squad.keeper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.squad.entity.Message;
import com.squad.looper.AsyncLooper;
import com.squad.protobuf.packet.Squad.NodeOperation;
import com.squad.protobuf.packet.Squad.NodePacket;
import com.squad.protobuf.packet.Squad.Packet;
import com.squad.protobuf.packet.Squad.Packet.Type;
import com.squad.rpc.client.ScoutClient;

public class SquadKeeperContext extends AsyncLooper implements IKeeperOperationHandler{
	private static final Logger logger = LoggerFactory.getLogger( SquadKeeperContext.class );
	
	@SuppressWarnings("serial")
	private final ConcurrentMap<String, ByteString> nodeMap = new ConcurrentHashMap<String, ByteString>() {
		@Override
		public ByteString remove(Object key) {
			// TODO Auto-generated method stub
			SquadKeeperContext.this.nodeEventHandler.remove( key );
			return super.remove(key);
		}
	};
	
	private final ConcurrentMap<Long, NodePacket> candidates = new ConcurrentHashMap<Long, NodePacket>();
	private final ConcurrentMap<String, INodeEventHandler> nodeEventHandler= new ConcurrentHashMap<String, INodeEventHandler>();
	private final ScoutClient scoutClient; 
	//private final BlockingQueue<IWatcher> watchers = new LinkedBlockingQueue<IWatcher>();
	private IWatcher watcher;
	
	public SquadKeeperContext( ScoutClient scoutClient ) {
		this.scoutClient = scoutClient;
	}
	
	public void DEBUG() {
		logger.debug( "-------------------------" );
		Iterator<String> i = nodeMap.keySet().iterator();
		for ( ; i.hasNext(); ) {
			String key = i.next();
			String value = nodeMap.get( key ).toStringUtf8();
			logger.debug( "[{}]::[{}]", key, value );
			/*
			if ( !key.contains( "healthCheck" ) ) {
				logger.debug( "[{}]::[{}]", key, value );
			}
			*/
		}
		logger.debug( "-------------------------" );
	}
	
	public void DEBUG_CANDIDATES() {
		if ( this.candidates.size() > 0 ) {
			Iterator<NodePacket> cands = this.candidates.values().iterator();
			for ( ; cands.hasNext(); ) {
				NodePacket np = cands.next();
				logger.debug( "REMAIN_CANDIDATES::\r\n[{}]", np.toString() );
			}
		}
		logger.debug( "CANDIDATES::[{}]", this.candidates.size() );
	}
	

	@Override
	public void register(IWatcher watcher) {
		// TODO Auto-generated method stub
		/*
		try {
			this.watchers.put( watcher );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		this.watcher = watcher;
	}
	
	@Override
	public void create(String node, byte[] value ) {
		// TODO Auto-generated method stub
		NodePacket nodePacket = NodePacket.newBuilder()
						.setNodeOperation( NodeOperation.CREATE )
						.setNode( node )
						.setValue( ByteString.copyFrom( value ) )
						.build();
						
		Packet packet = Packet.newBuilder()
						.setType( Type.NODE_OPERATION )
						.setNodePacket( nodePacket )
						.build();
		
		if ( this.scoutClient.isConnected() ) {
			this.scoutClient.write( packet );
		}
		else {
			this.handleNodePacket( nodePacket );
		}
	}
	
	@Override
	public void delete(String node) {
		// TODO Auto-generated method stub
		NodePacket nodePacket = NodePacket.newBuilder()
								.setNode( node )
								.setNodeOperation( NodeOperation.DELETE )
								.build();
		
		Packet packet = Packet.newBuilder()
						.setType( Type.NODE_OPERATION )
						.setNodePacket( nodePacket )
						.build();
		
		if ( this.scoutClient.isConnected() ) {
			this.scoutClient.write( packet );
		}
		else {
			this.handleNodePacket( nodePacket );
		}
	}
	
	public void exists( String node, INodeEventHandler handler ) {
		this.nodeEventHandler.put( node, handler );
	}
	
	@Override
	public void create(String node, byte[] value, INodeEventHandler handler) {
		// TODO Auto-generated method stub
		if ( handler != null ) {
			this.nodeEventHandler.put( node, handler );
		}
		this.create( node, value );
	}

	@Override
	public void delete(String node, INodeEventHandler handler) {
		// TODO Auto-generated method stub
		if ( handler != null ) {
			this.nodeEventHandler.put( node,handler );
		}
		this.delete( node );
	}
	
	protected void handleNodeOperation( NodePacket nodePacket ) {
		this.candidates.put( nodePacket.getTimestamp(), nodePacket );
		
		NodePacket resNodePacket = NodePacket.newBuilder()
								.setNode( nodePacket.getNode() )
								.setTimestamp( nodePacket.getTimestamp() )
								.build();
		final Packet packet = Packet.newBuilder()
						.setType( Type.ACK_NODE_OPERATION )
						.setNodePacket( resNodePacket )
						.build();
		final ChannelFuture future = this.scoutClient.write( packet );
			if ( future != null ) {
			future.addListener( new ChannelFutureListener() {
	
				@Override
				public void operationComplete(ChannelFuture future)
						throws Exception {
					// TODO Auto-generated method stub
					if ( !future.isSuccess() ) {
						SquadKeeperContext.this.scoutClient.write( packet ).addListener( this );
					}
				}
			} );
		}
	}
	
	private ByteString handleNodePacket( NodePacket nodePacket ) {
		ByteString result = null;
		if ( nodePacket.getNodeOperation() == NodeOperation.CREATE ) {
			result = this.nodeMap.put( nodePacket.getNode(), nodePacket.getValue() );
		}
		else {
			result = this.nodeMap.remove( nodePacket.getNode() );
		}
		
		EventType eventType = EventType.NODE_DELETED;
		if ( nodePacket.getNodeOperation() == NodeOperation.CREATE ) {
			eventType = EventType.NODE_DATA_CHANGED;
			if ( result == null ) { 
				eventType = EventType.NODE_CREATED;
			}
			
			//@@__
			if ( this.watcher != null ) {
				this.watcher.process( new WatchedEvent( eventType, nodePacket.getNode(), nodePacket.getValue().toByteArray() ) );
			}
		}
		
		
		return result;
	}
	
	protected void handleNodeOperationCommit( NodePacket nodePacket ) {
		nodePacket = this.candidates.remove( nodePacket.getTimestamp() );
		if ( nodePacket != null ) {
			handleNodePacket( nodePacket );
			INodeEventHandler handler = this.nodeEventHandler.get( nodePacket.getNode() );
			if ( handler != null ) {
				handler.handleEvent( nodePacket.getNode(), nodePacket.getValue().toStringUtf8() );
				this.nodeEventHandler.remove( nodePacket.getNode() );
			}
			//DEBUG_CANDIDATES();
		}
		DEBUG();
	}
	
	protected void handleNodeOperationRollback( NodePacket nodePacket ) {
		if ( nodePacket != null ) {
			NodePacket np = this.candidates.remove( nodePacket.getTimestamp() );
			if ( np != null ) {
				String node = np.getNode();
				this.nodeEventHandler.remove( node );
			}
		}
		DEBUG();
	}
	
	public Collection<NodePacket> readAll() {
		List<NodePacket> packets = new ArrayList<NodePacket>();
		Set<Entry<String, ByteString>> entries = this.nodeMap.entrySet();
		Iterator<Entry<String, ByteString>> i = entries.iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, ByteString> entry = i.next();
			NodePacket np = NodePacket.newBuilder()
							.setNode( entry.getKey() )
							.setValue( entry.getValue() )
							.build();
			packets.add( np );
		}
		
		return Collections.synchronizedList( packets );
	}
	
	public Set<Entry<String, ByteString>> readAll( String nodeRegex ) {
		Set<Entry<String, ByteString>> results = new HashSet<Entry<String, ByteString>>();
		Collections.synchronizedSet( results );
		
		Iterator<Entry<String, ByteString>> i = this.nodeMap.entrySet().iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, ByteString> entry = i.next();
			if ( entry.getKey().contains( nodeRegex ) ) {
				results.add( entry );
			}
		}
		return results;
	}
	
	public Set<Entry<String,ByteString>> readAllEntries() {
		return this.nodeMap.entrySet();
	}
	
	public void clear() {
		this.candidates.clear();
		this.nodeMap.clear();
	}
	
	public void copyAll( Collection<NodePacket> collection ) {
		Iterator<NodePacket> i = collection.iterator();
		for ( ; i.hasNext(); ) {
			NodePacket np = i.next();
			this.nodeMap.put( np.getNode(), np.getValue() );
		}
		
		logger.debug( "COPY_ALL" );
		//DEBUG();
	}
	
	public void copyAll( Set<Entry<String,ByteString>> collection ) {
		Iterator<Entry<String,ByteString>> i = collection.iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, ByteString> entity = i.next();
			this.nodeMap.put( entity.getKey(), entity.getValue() );
		}
		
		logger.debug( "COPY_ALL" );
		//DEBUG();
	}
	
	@Override
	public byte[] getData(String node) {
		// TODO Auto-generated method stub
		return this.nodeMap.get( node ).toByteArray();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getData( String node, Class<T> clazz ) {
		ByteString value = this.nodeMap.get( node );
		if ( value != null ) {
			if (  clazz == String.class ) {
				return (T)value.toStringUtf8(); 
			}
		}
		return null;
	}
	
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean handleMessage( Message message ) {
		// TODO Auto-generated method stub
		MSG msg = message.getMessage();
		if ( msg.equals( MSG.NODE_OPERATION ) ) {
			NodePacket nodePacket = (NodePacket)message.getParams()[0];
			this.handleNodeOperation( nodePacket );
			return true;
		}
		else if ( msg.equals( MSG.NODE_OPERATION_COMMIT ) ) {
			NodePacket nodePacket = (NodePacket)message.getParams()[0];
			this.handleNodeOperationCommit( nodePacket );
			return true;
		}
		else if ( msg.equals( MSG.NODE_OPERATION_ROLLBACK ) ) {
			NodePacket nodePacket = (NodePacket)message.getParams()[0];
			this.handleNodeOperationRollback( nodePacket );
			return true;
		}
		
		return false;
	}
	
	public NodePacket getCandidatesNodePacket( Long timestamp ) {
		return this.candidates.get( timestamp );
	}

}
