package org.camel.squadtag;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a SquadTag endpoint.
 */
@UriEndpoint(scheme = "squadtag", title = "SquadTag", syntax="squadtag:name", consumerClass = SquadTagConsumer.class, label = "SquadTag")
public class SquadTagEndpoint extends DefaultEndpoint {
    @UriParam
    private SquadTagConfig config; 
    
    public SquadTagEndpoint() {
    }

    public SquadTagEndpoint(String uri, SquadTagComponent component, SquadTagConfig config ) {
        super(uri, component);
        this.config = config;
    }

    public SquadTagEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new SquadTagProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new SquadTagConsumer( this, processor );
    }

    public boolean isSingleton() {
        return true;
    }
    
    public SquadTagConfig getConfig() {
    	return this.config;
    }
}
