package org.camel.squadsnmp;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.agent.AgentConfigManager;

import com.hsnc.squad.snmp.SnmpAgent;

/**
 * The SquadSnmp producer.
 */
public class SquadSnmpProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(SquadSnmpProducer.class);
    private SquadSnmpEndpoint endpoint;
    private SnmpAgent snmpAgent;
    
    public SquadSnmpProducer(SquadSnmpEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @SuppressWarnings("unchecked")
	public void process(Exchange exchange) throws Exception {
    	String header = exchange.getIn().getHeader( SquadSnmpConstants.ACTION, String.class );
    	
    	if ( header.equals( SquadSnmpConstants.ACTION_DELETE_ALL ) ) {
    		this.snmpAgent.unregisterMOAll();
    		return;
    	}
    	
    	Map<String,String> body = exchange.getIn().getBody( HashMap.class ) ;
    	Iterator<Entry<String, String>> i = body.entrySet().iterator();
    	for ( ; i.hasNext(); ) {
    		Entry<String, String> entry = i.next();
    		String oid = entry.getKey();
    		String value = entry.getValue();
    		if ( header.equals( SquadSnmpConstants.ACTION_REGISTER ) ) {
        		if ( !this.snmpAgent.exists( oid ) ) {
        			this.snmpAgent.registerMO( oid, value );
        		}
        		else {
        			this.snmpAgent.updateMO( oid, value );
        		}
        	}
        	else if ( header.equals( SquadSnmpConstants.ACTION_DELETE ) ) {
        		this.snmpAgent.unregisterMO( oid );
        	}
    	}
    }
    
    @Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
		if ( this.snmpAgent == null ) {
			this.snmpAgent = new SnmpAgent( endpoint.getConfig() );
		}
		
		if ( this.snmpAgent.getAgentState() != AgentConfigManager.STATE_RUNNING ) {
			this.snmpAgent.run();
		}
		super.doStart();
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		if ( this.snmpAgent.getAgentState() == AgentConfigManager.STATE_RUNNING ) {
			this.snmpAgent.stop();
		}
		super.doStop();
	}
}
