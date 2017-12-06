package org.camel.squadsnmp;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a SquadSnmp endpoint.
 */
@UriEndpoint(scheme = "squadsnmp", title = "SquadSnmp", syntax="squadsnmp:name", consumerClass = SquadSnmpConsumer.class, label = "SquadSnmp")
public class SquadSnmpEndpoint extends DefaultEndpoint {
	
	@UriParam
	private SquadSnmpConfig config;

    public SquadSnmpEndpoint() {
    }

    public SquadSnmpEndpoint(String uri, SquadSnmpComponent component, SquadSnmpConfig config ) {
        super(uri, component);
        this.config = config;
    }

    public SquadSnmpEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new SquadSnmpProducer( this );
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new SquadSnmpConsumer( this, processor );
    }

    public boolean isSingleton() {
        return true;
    }
    
    public SquadSnmpConfig getConfig() {
    	return this.config;
    }
}
