package com.hsnc.squad.snmp;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.camel.squadsnmp.SquadSnmpConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;

public class SnmpAgent extends AbstractSnmpAgent {
	private static final Logger logger = LoggerFactory.getLogger( SnmpAgent.class );
	private static Object lock = new Object();
	private static final String CONTEXT_PUBLIC = "public";
	
	public SnmpAgent( SquadSnmpConfig config ) throws Exception {
		super( config.getHost(), config.getPort(), config.getTrapHost(), 
				config.getTrapPort(), config.getVersion(), config.getPoolSize() );
	}
	
	public void registerMO( String oid, String value ) {
		try {
			OID managedOid = new OID( oid );
			super.getServer().register( new MOScalar( managedOid, MOAccessImpl.ACCESS_READ_WRITE, new OctetString( value ) ), 
					new OctetString( CONTEXT_PUBLIC ) );
			this.registeredOids.add( managedOid );
		} catch (DuplicateRegistrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean exists( String oid ) {
		OID managedOid = new OID( oid );
		MOScalar mo = (MOScalar)this.getServer().getManagedObject( managedOid, new OctetString( CONTEXT_PUBLIC ) );
		return mo == null ? false : true;
	}
	
	public void updateMO( String oid, String value ) {
		OID managedOid = new OID( oid );
		
		MOScalar mo = (MOScalar)this.getServer().getManagedObject( managedOid, new OctetString( CONTEXT_PUBLIC ) );
		this.getServer().lock( lock, mo );
		mo.setValue( new OctetString( value ) );
		this.getServer().unlock( lock, mo );
	}
		
	public void unregisterMO( String oid ) {
		OID managedOid = new OID( oid );		
		ManagedObject mo = this.server.getManagedObject( managedOid, new OctetString( CONTEXT_PUBLIC ) );
		if ( mo != null ) {
			this.getServer().lock( lock, mo );
			this.server.unregister( mo, new OctetString( CONTEXT_PUBLIC ) );
			this.registeredOids.remove( managedOid );
			this.getServer().unlock( lock, mo );
		}
	}
	
	public void unregisterMOAll() {
		Set<OID> bulkSet = new HashSet<OID>();
		bulkSet.addAll( this.registeredOids );
		Iterator<OID> i = bulkSet.iterator();
		for ( ; i.hasNext(); ) {
			OID oid = i.next();
			this.unregisterMO( oid.toString() );
		}
		
		bulkSet.clear();
	}
	
}
