package org.camel.squadws;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.StartupListener;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link SquadWsEndpoint}.
 */
public class SquadWsComponent extends UriEndpointComponent {
    
    public SquadWsComponent() {
        super(SquadWsEndpoint.class);
    }

    public SquadWsComponent(CamelContext context) {
        super(context, SquadWsEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
    	
    	Pattern pattern = Pattern.compile( "^(.+):(.+)/(.+)" );
		Matcher matcher = pattern.matcher( remaining );
		if ( !matcher.matches() ) {
			throw new Exception( "Illegal uri format..." );
		}
		String host = matcher.group( 1 );
		Integer port = Integer.parseInt( matcher.group( 2 ) );
		String context = matcher.group( 3 );
		
    	SquadWsEndpoint.TYPE type = this.getAndRemoveOrResolveReferenceParameter( parameters, "type", 
    																		SquadWsEndpoint.TYPE.class, 
    																		SquadWsEndpoint.TYPE.json );
    	SquadWsEndpoint endpoint = new SquadWsEndpoint(uri, this);
        endpoint.setHost( host );
        endpoint.setPort( port );
        endpoint.setContext( context );
        endpoint.setType( type );
        
        return endpoint;
    }
}
