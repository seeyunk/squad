package org.camel.squadtag;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

import com.squadtag.jedis.JedisConnectionPool;

/**
 * Represents the component that manages {@link SquadTagEndpoint}.
 */
public class SquadTagComponent extends UriEndpointComponent {
    private SquadTagConfig config;
    
    public SquadTagComponent() {
        super(SquadTagEndpoint.class);
    }

    public SquadTagComponent(CamelContext context) {
        super(context, SquadTagEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
    	SquadTagConfig config;
    	if ( this.config != null ) {
    		config = (SquadTagConfig)this.config.clone();
    	}
    	else {
    		config = new SquadTagConfig();
    	}
    	
    	config = this.parseConfig( config, remaining, parameters );
        Endpoint endpoint = new SquadTagEndpoint( uri, this, config );
        setProperties(endpoint, parameters);
        return endpoint;
    }
    
    private SquadTagConfig parseConfig( SquadTagConfig config, String remaining, Map<String, Object> parameters ) throws Exception {
    	String host = remaining.substring( 0, remaining.indexOf( ":" ) );
    	Integer port = Integer.parseInt( remaining.substring( remaining.indexOf( ":" ) + 1 ) );
    	
    	config.setHost( host );
    	config.setPort( port );
    	config.setDefaultDb( this.getAndRemoveOrResolveReferenceParameter( parameters, "defaultDb", Integer.class, 0 ) );
    	config.setPasswd( this.getAndRemoveOrResolveReferenceParameter( parameters, "passwd", String.class, null ) );
    	config.setLoadOnStartup( this.getAndRemoveOrResolveReferenceParameter( parameters, "loadOnStartup", Boolean.class, false ) );
    	return config;
    }
    
    public SquadTagConfig getConfig() {
    	return this.config;
    }
    
    public void setConfig( SquadTagConfig config ) {
    	this.config = config;
    }

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
		if ( this.config == null ) {
			this.config = new SquadTagConfig();
		}
		super.doStart();
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		JedisConnectionPool.destroyAll();
		super.doStop();
	}
}
