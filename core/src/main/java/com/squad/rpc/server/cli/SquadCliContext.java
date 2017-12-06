package com.squad.rpc.server.cli;

import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.squad.context.ISquadContext;
import com.squad.entity.Message;
import com.squad.keeper.EventType;
import com.squad.keeper.IWatcher;
import com.squad.keeper.WatchedEvent;
import com.squad.looper.AsyncLooper;
import com.squad.manager.SquadNodeManager;
import com.squad.protobuf.cli.packet.SquadCli.NodeEventPacket;
import com.squad.protobuf.cli.packet.SquadCli.NodeEventType;
import com.squad.protobuf.cli.packet.SquadCli.SquadCliPacket;
import com.squad.protobuf.cli.packet.SquadCli.SquadCliPacket.Command;
import com.squad.rpc.server.Acceptor;


public class SquadCliContext extends AsyncLooper implements IWatcher {
	private static final Logger logger = LoggerFactory.getLogger( SquadCliContext.class );
	private Acceptor server = new Acceptor();;
	private final WeakReference<ISquadContext> refCtx;
	private final SquadCliChannelHandler handler;
	private final SquadNodeManager nodeManager;
	
	public SquadCliContext( ISquadContext ctx ) {
		this.refCtx = new WeakReference<ISquadContext>( ctx );
		this.handler = new SquadCliChannelHandler( refCtx.get() );
		this.nodeManager = refCtx.get().getNodeManager();
		this.nodeManager.register( this );
	}
	
	public void start( int port ) {
		this.server.createBootstrap( 1 );
		this.server.setPipelineFactory( new SquadCliPipelineFactory( this.handler ) );
		this.server.bind( new InetSocketAddress( port ) );
		logger.info( "Cli server binded at port {}", port );
	}
	
	public void stop() {
		this.server.release();
	}

	@Override
	public boolean handleMessage(Message message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void process( WatchedEvent event ) {
		// TODO Auto-generated method stub
		NodeEventPacket.Builder builder = NodeEventPacket.newBuilder();
		EventType eventType  = event.getEventType();
		if ( eventType.equals( EventType.NODE_CREATED ) ) {
			builder.setNodeEventType( NodeEventType.NODE_CREATED );
		}
		else if ( eventType.equals( EventType.NODE_DATA_CHANGED ) ) {
			builder.setNodeEventType( NodeEventType.NODE_DATA_CHANGED );
		}
		else if ( eventType.equals( EventType.NODE_DELETED ) ) {
			builder.setNodeEventType( NodeEventType.NODE_DELETED );
		}
		
		builder.setNode( event.getNode() );
		builder.setValue( ByteString.copyFrom( event.getValue() ) );
		SquadCliPacket packet = SquadCliPacket.newBuilder()
						.setCommand( Command.NODE_EVENT )
						.setNodeEventPacket( builder.build() )
						.build();
		this.handler.broadcast( packet );
	}
	
}
