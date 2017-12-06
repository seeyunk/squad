package com.squad.context;

import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.entity.Message;
import com.squad.entity.SystemState;
import com.squad.keeper.INodeEventHandler;
import com.squad.looper.AsyncLooper;
import com.squad.manager.SquadNodeManager;
import com.squad.manager.SquadServiceManager;
import com.squad.protobuf.packet.Squad.ControlServicePacket;
import com.squad.protobuf.packet.Squad.JoinSquadPacket;
import com.squad.protobuf.packet.Squad.NodePacket;
import com.squad.protobuf.packet.Squad.NodePacketList;
import com.squad.protobuf.packet.Squad.Packet;
import com.squad.protobuf.packet.Squad.Packet.Type;
import com.squad.protobuf.packet.Squad.ScoutState;
import com.squad.protobuf.packet.Squad.ServiceState;
import com.squad.rpc.client.ScoutClient;
import com.squad.rpc.server.Acceptor;
import com.squad.rpc.server.LeaderChannelHandler;
import com.squad.rpc.server.LeaderPipelineFactory;
import com.squad.rpc.server.cli.SquadCliContext;

public class SquadContext extends AsyncLooper implements ISquadContext{

	protected static final Logger logger = LoggerFactory.getLogger( SquadContext.class );
	private final Acceptor server = new Acceptor();
	private final ScoutClient scoutClient;
	private final SquadServiceManager serviceManager; 
	private final SquadNodeManager nodeManager;
	private final SquadCliContext cliContext;
	private Config config;
	
	@Override
	public Config getConfig() {
		return this.config;
	}
	
	@Override
	public SquadServiceManager getServiceManager() {
		// TODO Auto-generated method stub
		return this.serviceManager;
	}

	@Override
	public ScoutClient getClient() {
		// TODO Auto-generated method stub
		return this.scoutClient;
	}
	
	@Override
	public SquadNodeManager getNodeManager() {
		// TODO Auto-generated method stub
		return this.nodeManager;
	}
	
	public SquadContext(Config config) {
		//super.addHandler( this );
		this.config = config;
		this.scoutClient = new ScoutClient( this );
		this.nodeManager = new SquadNodeManager( this );
		this.serviceManager = new SquadServiceManager( this );
		this.cliContext = new SquadCliContext( this );
	}
	
	public void start() throws Exception {
		
		InetSocketAddress myScoutAddress = this.getConfig().getMyScoutAddress();
		this.server.createBootstrap( -1 );
		this.server.setPipelineFactory( new LeaderPipelineFactory( new LeaderChannelHandler() ) );
		this.server.bind( myScoutAddress );
		logger.info( "Server binded at {}", myScoutAddress );
		
		InetSocketAddress leaderAddress = this.getConfig().getDefaultLeaderAddress();
		this.startScoutClient( leaderAddress );
		
		this.cliContext.start( this.getConfig().getCliPort() );
	}
	
	public ScoutClient startScoutClient( InetSocketAddress leaderAddress ) {
		this.scoutClient.prepare();
		return this.scoutClient.connect( leaderAddress );
	}
	
	public void stopScoutClient() {
		this.scoutClient.stopPing();
		this.scoutClient.release();
	}
	
	public void stop() throws Exception {
		this.sendMessage( new Message( MSG.SHUTDOWN ) );
		this.serviceManager.clear();
		this.stopScoutClient();
		this.cliContext.stop();
		this.server.release();
		
	}
	
	private void handleElectNewLeaderEvent() {
		this.stopScoutClient();
		String oldLeaderId = this.getConfig().getLeaderId();
		this.nodeManager.changeScoutState( oldLeaderId, ScoutState.INACTIVATED );
		this.nodeManager.changeServiceState( oldLeaderId, ServiceState.STOPPED );
		
		Map<String, ScoutState> result = this.nodeManager.getScoutStates( ScoutState.ACTIVATED );
		result.remove( oldLeaderId );
		String nextLeaderId = null;
		if ( result.size() == 0 ) {
			nextLeaderId = this.getConfig().getScoutId();
		}
		else {
			int max = 0;
			Iterator<String> i = result.keySet().iterator();
			for ( ; i.hasNext(); ) {
				String scoutId = i.next();
				String hcNode = this.nodeManager.getHealthCheckNode( scoutId );
				int healthCheck = Integer.parseInt( this.nodeManager.getData( hcNode, String.class ) );
				if ( healthCheck >= max ) {
					nextLeaderId = scoutId;
					max = healthCheck;
				}
			}
		}
		
		this.getConfig().setLeaderId( nextLeaderId );
		InetSocketAddress newLeaderAddress = this.getConfig().getScoutAddress( nextLeaderId );
		this.startScoutClient( newLeaderAddress );
	
		logger.debug( "New leader detected::[{}]", nextLeaderId );
	}
	
	private void handleObserveSystemState(SystemState systemState) {
		// TODO Auto-generated method stub
		String scoutId = this.getConfig().getScoutId();
		this.scoutClient.writeSystemState( scoutId, systemState );
	}
	
	private void handleTryReconnect() {
		// TODO Auto-generated method stub
		this.scoutClient.reconnect();
	}
	
	public void handleScoutStateChanged( final String scoutId, final ScoutState scoutState ) {
		final String leaderId = this.getConfig().getLeaderId();
		final String selfId = this.getConfig().getScoutId();
		this.nodeManager.changeScoutState( scoutId, scoutState, new INodeEventHandler() {
			@Override
			public void handleEvent(String node, String value) {
				// TODO Auto-generated method stub
				if ( leaderId.equals( selfId ) && 
					!leaderId.equals( scoutId ) &&
					!scoutState.equals( ScoutState.ACTIVATED ) ) {
					SquadContext.this.serviceManager.relayServices( scoutId, leaderId );
				};
			}} );
	}
	
	
	
	public void handleAckJoinSquad( String scoutId ) {
		logger.info( "[RECV]::[ACK_JOIN_SQUAD]::From::[{}]", scoutId );
		final String selfId = this.getConfig().getScoutId();
		String leaderId = this.getConfig().getLeaderId();
		if ( selfId.equals( leaderId ) ){
			Collection<NodePacket> nodePacketList = this.nodeManager.readAll();
			NodePacketList npl = NodePacketList.newBuilder()
								.addAllNodePacket( nodePacketList )
								.build();
			Packet packet = Packet.newBuilder()
							.setSourceId( leaderId )
							.addTargetId( scoutId )
							.setType( Type.SYNC_NODES )
							.setNodePacketList( npl )
							.build();
			this.scoutClient.write( packet );
			logger.info( "[SEND]::[SYNC_NODES]::[{}]::To::[{}]", selfId, scoutId );
		}
		
		this.nodeManager.setLeader( leaderId );
		SquadContext.this.nodeManager.changeScoutState( selfId, ScoutState.SYNC );
	}
	
	public void handleSyncNodes( String scoutId, NodePacketList npl ) {
		String selfId = this.getConfig().getScoutId();
		String leaderId = this.getConfig().getLeaderId();
		if ( !selfId.equals( leaderId ) ) {
			Collection<NodePacket> col = Collections.synchronizedCollection( npl.getNodePacketList() );
			this.nodeManager.copyAll( col );
		}
		
		//@@this.serviceManager.loadAllServices();
		logger.info( "[RECV]::SYNC_NODES::[{}]::FINISHED", selfId );
		
		Packet packet = Packet.newBuilder()
						.setType( Type.ACK_SYNC_NODES )
						.setSourceId( selfId )
						.build();
		
		this.scoutClient.write( packet );
	}
	
	public void handleAckSyncNodes( String scoutId ) {
		//finished new scout initialized
		this.scoutClient.startPing();
		String leaderId = this.config.getLeaderId();
		final String selfId = this.config.getScoutId();
		this.nodeManager.changeScoutState( selfId, ScoutState.ACTIVATED );
		if ( selfId.equals( scoutId ) ) {
			this.serviceManager.clear();
			this.serviceManager.loadAllServices();
			if ( selfId.equals( leaderId ) ) {
				Map<String, ScoutState> results = this.nodeManager.getScoutStates( ScoutState.INACTIVATED );
				Iterator<String> i = results.keySet().iterator();
				for ( ; i.hasNext(); ) {
					String deadScoutId = i.next();
					this.serviceManager.relayServices( deadScoutId, selfId );
				}
			}
			else {
				Iterator<String> serviceIds = this.serviceManager.getAllServiceIdsByScoutId( selfId );
				for ( ; serviceIds.hasNext(); ) {
					final String serviceId = serviceIds.next();
					final ServiceState lastState = this.nodeManager.getServiceState( selfId, serviceId );
					if ( !lastState.equals( ServiceState.NOT_REGISTERED ) 
						&& !lastState.equals( ServiceState.STARTED) ) {
						final String assignedId = this.nodeManager.getServiceTarget( serviceId );
						ControlServicePacket csp = ControlServicePacket.newBuilder()
													.setServiceId( serviceId )
													.setServiceState( ServiceState.SHUTDOWNED )
													.build();
						Packet packet = Packet.newBuilder()
										.setType( Type.CONTROL_SERVICE )
										.setSourceId( selfId )
										.addTargetId( assignedId )
										.setControlServicePacket( csp )
										.build();
						this.scoutClient.write( packet ).addListener( new ChannelFutureListener() {
							@Override
							public void operationComplete(ChannelFuture future)
									throws Exception {
								// TODO Auto-generated method stub
								final SquadNodeManager nodeManager = SquadContext.this.nodeManager;
								final SquadServiceManager serviceManager = SquadContext.this.serviceManager;
								final String targetNode = nodeManager.getServiceStateNode( assignedId, serviceId );
								nodeManager.exists( targetNode, new INodeEventHandler() {
									@Override
									public void handleEvent(String node,
											String value) {
										// TODO Auto-generated method stub
										ServiceState resultState = ServiceState.valueOf( value );
										if ( targetNode.equals( node ) &&
											( resultState.equals( ServiceState.STOPPED ) ||
											resultState.equals( ServiceState.SHUTDOWNED ) )) {
											serviceManager.startService( serviceId, true );
											logger.debug( "[RESUME]::[{}]::[{}]", selfId, serviceId );
										}
									}} );
							}
							
						});
					}
				}
			}
			this.serviceManager.startAllServices( false );
		}
		else {
			this.serviceManager.sendExamineServices( scoutId );
		}
		
		logger.debug( "[RECV]::[ACK_SYNC_NODES]::[{}]::Initialization finished", scoutId );
	}
	
	public void handleChannelDisconnected() {
		logger.debug( "CHANNEL_DISCONNECTED...");
		this.scoutClient.stopPing();
	}
	
	public void handleJoinSquad() {
		final String selfId = this.getConfig().getScoutId();
		JoinSquadPacket joinSquadPacket = JoinSquadPacket.newBuilder()
										.setScoutId( selfId )
										.build();
		
		Packet packet = Packet.newBuilder()
						.setType( Type.JOIN_SQUAD )
						.setJoinSquadPacket( joinSquadPacket )
						.build();
		
		this.scoutClient.write( packet );
	}
	
	public void handlePong() {
		String scoutId = this.getConfig().getScoutId();
		this.nodeManager.handleHealthCheck( scoutId );
	}
	
	@Override
	public boolean handleMessage(Message message) {
		// TODO Auto-generated method stub
		MSG msg = message.getMessage();
		if ( msg.equals( MSG.PONG ) ) {
			this.handlePong();
			return true;
		}
		else if ( msg.equals( MSG.JOIN_SQUAD ) ) {
			this.handleJoinSquad();
			return true;
		}
		else if ( msg.equals( MSG.ACK_JOIN_SQUAD ) ) {
			String scoutId = (String)message.getParams()[0];
			this.handleAckJoinSquad( scoutId );
			return true;
		}
		else if ( msg.equals( MSG.SYNC_NODES ) ) {
			String scoutId = (String)message.getParams()[0];
			NodePacketList npl = (NodePacketList)message.getParams()[1];
			this.handleSyncNodes( scoutId, npl );
			return true;
		}
		else if ( msg.equals( MSG.ACK_SYNC_NODES ) ) {
			String scoutId = (String)message.getParams()[0];
			this.handleAckSyncNodes( scoutId );
			return true;
		}
		else if ( msg.equals( MSG.CHANNEL_DISCONNECTED ) ) {
			this.handleChannelDisconnected();
			return true;
		}
		else if ( message.getMessage() == MSG.TRY_RECONNECT ) {
			this.handleTryReconnect();
			return true;
		}
		else if ( message.getMessage() == MSG.ELECT_NEW_LEADER ) {
			this.handleElectNewLeaderEvent();
			return true;
		}
		else if ( message.getMessage() == MSG.SCOUT_STATE_CHANGED ) {
			Object[] params = message.getParams();
			String scoutId = (String)params[0];
			ScoutState scoutState = (ScoutState)params[1];
			this.handleScoutStateChanged( scoutId, scoutState );
			return true;
		}
		else if ( message.getMessage() == MSG.OBSERVE_SYSTEM_STATE ) {
			SystemState state = (SystemState)message.getParams()[0];
			this.handleObserveSystemState( state );
			return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Config config = null;
			if ( args.length == 0 ) {
				config = Config.load();
			}
			config = Config.load( args[0] );
			
			final SquadContext squadCtx = new SquadContext( config );
			squadCtx.start();
			
			Runtime.getRuntime().addShutdownHook( new Thread() {
	            public void run() {
	                try {
	                	squadCtx.stop();
	                } catch (Exception e) {
	                    throw new RuntimeException(e);
	                }
	            }
	        } );
	        
			Thread.currentThread().join();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

