package org.camel.squadsnmp;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;
import org.camel.squadsnmp.SquadSnmpConfig.VERSION;
import org.snmp4j.agent.AgentConfigManager;

import com.hsnc.squad.snmp.SnmpAgent;

/**
 * Represents the component that manages {@link SquadSnmpEndpoint}.
 */
public class SquadSnmpComponent extends UriEndpointComponent {
    private SquadSnmpConfig config;
    
    public SquadSnmpComponent() {
        super(SquadSnmpEndpoint.class);
    }

    public SquadSnmpComponent(CamelContext context) {
        super(context, SquadSnmpEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
    	SquadSnmpConfig config;
    	if ( this.config != null ) {
    		config = (SquadSnmpConfig)this.config.clone();
    	}
    	else {
    		config = new SquadSnmpConfig();
    	}
    	
    	config = this.parseConfig( config, remaining, parameters );
        Endpoint endpoint = new SquadSnmpEndpoint( uri, this, config );
        setProperties(endpoint, parameters);
        return endpoint;
    }
    
    private SquadSnmpConfig parseConfig( SquadSnmpConfig config, String remaining, Map<String, Object> parameters ) throws Exception {
    	String host = remaining.substring( 0, remaining.indexOf( ":" ) );
    	Integer port = Integer.parseInt( remaining.substring( remaining.indexOf( ":" ) + 1 ) );
    	
    	config.setHost( host );
    	config.setPort( port );
    	config.setTrapHost( this.getAndRemoveOrResolveReferenceParameter( parameters, "trapHost", String.class, null ) );
    	config.setTrapPort( this.getAndRemoveOrResolveReferenceParameter( parameters, "trapPort", Integer.class, 0 ) );
    	config.setVersion( this.getAndRemoveOrResolveReferenceParameter( parameters, "version", VERSION.class, VERSION.v2c ) );
    	config.setPoolSize( this.getAndRemoveOrResolveReferenceParameter( parameters, "poolSize", Integer.class, 4 ) );
    	
    	return config;
    }
}
