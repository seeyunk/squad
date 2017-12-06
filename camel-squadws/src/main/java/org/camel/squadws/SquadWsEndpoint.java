package org.camel.squadws;

import java.net.InetSocketAddress;

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import com.hsnc.squad.rpc.Acceptor;
import com.hsnc.squad.rpc.SquadWsServerChannelHandler;

/**
 * Represents a SquadWs endpoint.
 */
@UriEndpoint(scheme = "squadws", title = "SquadWs", syntax="squadws:name", producerOnly = true, label = "SquadWs")
public class SquadWsEndpoint extends DefaultEndpoint {
	public static enum MODE { ws, http };
	public static enum TYPE { text, xml, json }
	
	@UriParam( label="producer", defaultValue="localhost" ) 
	@Metadata(required = "true")
    private String host;
	
	@UriParam( label="producer", defaultValue="8081" )
    private int port;

	@UriParam( label="producer", defaultValue="squad" )
	private String context;
	
	@UriParam( label="producer", defaultValue="json" )
	private TYPE type;
	
    private final Acceptor server = new Acceptor();
    
    public SquadWsEndpoint() {
    }

    public SquadWsEndpoint(String uri, SquadWsComponent component) {
        super(uri, component);
    }

    public SquadWsEndpoint(String endpointUri) {
        super(endpointUri);
    }
    
    public void broadcast( Object message ) throws Exception {
    	SquadWsServerChannelHandler handler = (SquadWsServerChannelHandler)this.server.getChannelHandler();
    	handler.broadcast( message );
    }
    
    @Override
    public Producer createProducer() throws Exception {
        return new SquadWsProducer( this );
    }

    public boolean isSingleton() {
        return true;
    }

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("SquadWs consumer is not supported.");
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}
	
	private class SquadWsPipelineFactory implements ChannelPipelineFactory {
			
		private final int DEFAULT_CHUNK_SIZE = 1024 * 64;
		private final SquadWsServerChannelHandler channelHandler;
		public SquadWsPipelineFactory( SquadWsServerChannelHandler channelHandler ) {
			this.channelHandler = channelHandler;
		}
		
		@Override
		public ChannelPipeline getPipeline() throws Exception {
			// TODO Auto-generated method stub
			ChannelPipeline pipeline = Channels.pipeline();
			pipeline.addLast( "decoder", new HttpRequestDecoder() );
			pipeline.addLast( "aggregator", new HttpChunkAggregator( DEFAULT_CHUNK_SIZE ) );
			pipeline.addLast( "encoder", new HttpResponseEncoder() );
			pipeline.addLast( "handler", this.channelHandler );
			return pipeline;
		}
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Override
	protected void doStart() throws Exception {
		// TODO Auto-generated method stub
		this.server.createBootstrap( -1 );
		this.server.setPipelineFactory( new SquadWsPipelineFactory( new SquadWsServerChannelHandler( this.context ) ) );
		this.server.bind( new InetSocketAddress( host, port ) );
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		this.server.release();
	}
}
