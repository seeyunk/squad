package com.squad.manager;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.squad.context.SquadContext;
import com.squad.entity.Message;
import com.squad.keeper.INodeEventHandler;
import com.squad.keeper.SquadKeeperContext;
import com.squad.protobuf.packet.Squad.ScoutState;
import com.squad.protobuf.packet.Squad.ServiceState;

public class SquadNodeManager extends SquadKeeperContext {
	private static final Logger logger = LoggerFactory.getLogger( SquadNodeManager.class );
	public static final String NODE_DELIMITER ="/";
	public static final String NODE_SCOUT = "scout";
	public static final String NODE_SCOUT_STATE = "state"; 
	public static final String NODE_HEALTH_CHECK = "healthCheck";
	public static final String NODE_SERVICE = "service";
	public static final String NODE_SERVICE_STATE = "state";
	public static final String NODE_SERVICE_TARGET = "target";
	public static final String NODE_SERVICE_TARGET_ALL_VALUE = "all";
	public static final String NODE_SCOUT_LEADER = "leader";
	
	public SquadNodeManager( SquadContext ctx ) {
		super( ctx.getClient() );
	}
	
	public String getScoutStateNode( String scoutId ) {
		StringBuffer sb = new StringBuffer();
		sb.append( scoutId );
		sb.append( NODE_DELIMITER );
		sb.append( NODE_SCOUT );
		sb.append( NODE_DELIMITER );
		sb.append( NODE_SCOUT_STATE );
		return sb.toString();
	}
	
	public String getHealthCheckNode( String scoutId ) {
		StringBuffer sb = new StringBuffer();
		sb.append( scoutId );
		sb.append( NODE_DELIMITER );
		sb.append( NODE_HEALTH_CHECK );
		return sb.toString();
	}
	
	public String getServiceStateNode( String scoutId, String serviceId ) {
		StringBuffer sb = new StringBuffer();
		sb.append( scoutId );
		sb.append( NODE_DELIMITER );
		sb.append( NODE_SERVICE );
		sb.append( NODE_DELIMITER );
		sb.append( NODE_SERVICE_STATE );
		sb.append( NODE_DELIMITER );
		sb.append( serviceId );
		
		return sb.toString();
	}
	
	public String getServiceTargetNode( String serviceId ) {
		StringBuffer sb = new StringBuffer();
		sb.append( serviceId );
		sb.append( NODE_DELIMITER );
		sb.append( NODE_SERVICE );
		sb.append( NODE_DELIMITER );
		sb.append( NODE_SERVICE_TARGET );
		
		return sb.toString();
	}
	
	public String getLeaderNode() {
		StringBuffer sb = new StringBuffer();
		sb.append( NODE_SCOUT );
		sb.append( NODE_DELIMITER );
		sb.append( NODE_SCOUT_LEADER );
		
		return sb.toString();
	}
	
	public void setLeader( final String leaderId ) {
		this.create( this.getLeaderNode(), leaderId.getBytes( Charset.forName( "utf-8" ) ) );
	}
	
	public void setServiceTarget( final String serviceId, String targetId ) {
		this.create( this.getServiceTargetNode( serviceId ), targetId.getBytes( Charset.forName( "utf-8") ) );
	}
	
	public void setServiceTarget( final String serviceId, String targetId, INodeEventHandler handler ) {
		this.create( this.getServiceTargetNode( serviceId), 
				targetId.getBytes( Charset.forName( "utf-8") ), 
				handler );
	}
	
	public String getServiceTarget( final String serviceId ) {
		String stn = this.getServiceTargetNode( serviceId );
		return this.getData( stn, String.class );
	}
	
	public Map<String, ScoutState> getScoutStates( ScoutState scoutState ) {
		Map<String, ScoutState> result = new HashMap<String, ScoutState>();
		Set<Entry<String, ByteString>> entries = this.readAllEntries();
		Iterator<Entry<String, ByteString>> i = entries.iterator();
		String postfix = NODE_SCOUT + NODE_DELIMITER  + NODE_SCOUT_STATE;
		for ( ; i.hasNext(); ) {
			Entry<String, ByteString> entry = i.next();
			if ( entry.getKey().contains( postfix ) ) {
				String node = entry.getKey(); 
				ScoutState ss = ScoutState.valueOf( entry.getValue().toStringUtf8() );
				if ( scoutState != null && !scoutState.equals( ss ) ) {
					continue;
				}
				result.put( node.substring( 0, node.indexOf( NODE_DELIMITER ) ), ss );
			}
		}
		
		return result;
	}
	
	public ScoutState getScoutState( String scoutId ) {
		String node = this.getScoutStateNode( scoutId );
		return ScoutState.valueOf( this.getData( node, String.class ) );
	}
	
	public void changeScoutState( String scoutId, ScoutState scoutState ) {
		this.changeScoutState( scoutId, scoutState, null );
	}
	
	public ServiceState getServiceState( String scoutId, String serviceId ) {
		String node = this.getServiceStateNode( scoutId, serviceId );
		String value = this.getData( node, String.class );
		return value == null ? ServiceState.NOT_REGISTERED : ServiceState.valueOf( this.getData( node, String.class ) );
	}
	
	public void changeScoutState( String scoutId, ScoutState scoutState, INodeEventHandler handler ) {
		this.create( this.getScoutStateNode( scoutId ), scoutState.toString().getBytes( Charset.forName( "utf-8" ) ), handler );
	}
	
	public void handleHealthCheck( String scoutId )  {
		String node = this.getHealthCheckNode( scoutId );
		String value = this.getData( node, String.class );
		int healthCheck = value == null ? 0 : Integer.parseInt( value ) + 1;
		this.create( node, Integer.toString( healthCheck ).getBytes( Charset.forName( "utf-8" ) ) );
	}
	
	public void changeServiceState( String scoutId, String serviceId, ServiceState serviceState ) {
		String node = this.getServiceStateNode( scoutId, serviceId );
		this.create( node, serviceState.toString().getBytes( Charset.forName( "utf-8" ) ) );
	}
	
	public void changeServiceState( String scoutId, ServiceState serviceState ) {
		String node = this.getServiceStateNode( scoutId, "" );
		Iterator<Entry<String, ByteString>> i = this.readAll( node ).iterator();
		for ( ; i.hasNext(); ) {
			Entry<String, ByteString> entry = i.next();
			this.create( entry.getKey(), serviceState.toString().getBytes( Charset.forName( "utf-8" ) ) );
		}
	}
	
	
}
