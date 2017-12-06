package com.hsnc.squad.snmp;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.camel.squadsnmp.SquadSnmpConfig.VERSION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.io.ImportModes;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.TransportDomains;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MessageProcessingModel;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.Variable;
import org.snmp4j.transport.TransportMappings;
import org.snmp4j.util.ThreadPool;

public class AbstractSnmpAgent extends BaseAgent {
	private static final Logger logger = LoggerFactory.getLogger( AbstractSnmpAgent.class );
	protected String host;
	protected int port;
	protected String trapHost;
	protected int trapPort;
	protected Set<OID> registeredOids = new HashSet<OID>();
	
	public AbstractSnmpAgent( String host, int port, 
							String trapHost, int trapPort, 
							VERSION version, int poolSize ) throws Exception {
		super( new File( "config.bc" ), new File( "config.c" ), null );
	    if ( !version.equals( VERSION.v2c ) ) {
	    	throw new Exception( "Only supports snmp v2c" );
	    }
	    
	    this.host = host;
	    this.port = port;
	    this.trapHost = trapHost;
	    this.trapPort = trapPort;
	    
	    CommandProcessor commandProcessor = new CommandProcessor( OctetString.fromHexString( "00:00:00:00:00:00:02", ':' ) );
	    this.setAgent( commandProcessor );
	    this.agent.setWorkerPool( ThreadPool.create( "squad-snmp-tp", poolSize ) );
	    this.init();
	    this.loadConfig( ImportModes.REPLACE_CREATE );
	    this.server.addContext( new OctetString( "public" ) );
	   
	    this.finishInit();
	}

	@Override
	protected void initTransportMappings() throws IOException {
		// TODO Auto-generated method stub
		String address = String.format( "%s/%d", this.host, this.port );
		this.transportMappings = new TransportMapping[1];
		Address addr = GenericAddress.parse( address );
		TransportMapping tm = TransportMappings.getInstance().createTransportMapping( addr );
		transportMappings[0] = tm;
	}
	
	@Override
	protected void addCommunities(SnmpCommunityMIB communityMIB) {
		// TODO Auto-generated method stub
		Variable[] com2sec = new Variable[] 
	    {
	        new OctetString( "public" ),              // community name
	        new OctetString( "cpublic" ),              // security name
	        getAgent().getContextEngineID(),        // local engine ID
	        new OctetString( "public" ),              // default context name
	        new OctetString(),                      // transport tag
	        new Integer32( StorageType.nonVolatile ), // storage type
	        new Integer32( RowStatus.active )         // row status
	    };
	    
	    MOTableRow row =
	        communityMIB.getSnmpCommunityEntry().createRow(
	          new OctetString( "public2public" ).toSubIndex( true ), com2sec );
	    
	    communityMIB.getSnmpCommunityEntry().addRow( row );
	}


	@Override
	protected void addNotificationTargets(SnmpTargetMIB targetMIB,
			SnmpNotificationMIB notificationMIB ) {
		// TODO Auto-generated method stub
		targetMIB.addDefaultTDomains();

		String address = String.format( "%s/%d", this.trapHost, this.trapPort );
	    targetMIB.addTargetAddress(new OctetString("notificationV2c"),
	                               TransportDomains.transportDomainUdpIpv4,
	                               new OctetString(new UdpAddress( address ).getValue()),
	                               200, 1,
	                               new OctetString( "notify" ),
	                               new OctetString( "v2c" ),
	                               StorageType.permanent);
	   
	    targetMIB.addTargetParams(new OctetString( "v2c" ),
	                              MessageProcessingModel.MPv2c,
	                              SecurityModel.SECURITY_MODEL_SNMPv2c,
	                              new OctetString("cpublic"),
	                              SecurityLevel.AUTH_PRIV,
	                              StorageType.permanent);
	   
	    notificationMIB.addNotifyEntry(new OctetString("default"),
	                                   new OctetString("notify"),
	                                   SnmpNotificationMIB.SnmpNotifyTypeEnum.inform,
	                                   StorageType.permanent);
	}

	
	@Override
	protected void addUsmUser(USM arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void addViews(VacmMIB vacm) {
		// TODO Auto-generated method stub
		vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv1,
  				new OctetString("cpublic"),
  				new OctetString("v1v2group"),
  				StorageType.nonVolatile);
  
		vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c,
		            new OctetString("cpublic"),
		            new OctetString("v1v2group"),
		            StorageType.nonVolatile);
		
		vacm.addAccess(new OctetString("v1v2group"), new OctetString("public"),
					SecurityModel.SECURITY_MODEL_ANY,
					SecurityLevel.NOAUTH_NOPRIV,
					MutableVACM.VACM_MATCH_EXACT,
					new OctetString("fullReadView"),
					new OctetString("fullWriteView"),
					new OctetString("fullNotifyView"),
					StorageType.nonVolatile);
		
		vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"),
	            	new OctetString(), VacmMIB.vacmViewIncluded,
	            StorageType.nonVolatile);
		vacm.addViewTreeFamily(new OctetString("fullWriteView"), new OID("1.3"),
	            	new OctetString(), VacmMIB.vacmViewIncluded,
	            	StorageType.nonVolatile);
		vacm.addViewTreeFamily(new OctetString("fullNotifyView"), new OID("1.3"),
	            	new OctetString(), VacmMIB.vacmViewIncluded,
	            	StorageType.nonVolatile);
	}

	@Override
	protected void registerManagedObjects() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void unregisterManagedObjects() {
		// TODO Auto-generated method stub
		for ( OID oid : this.registeredOids ) {
            ManagedObject mo = this.server.getManagedObject( oid, null );
            if ( mo != null ) {
                server.unregister( mo, null );
            }
        }
		
		this.registeredOids.clear();
	}
}
