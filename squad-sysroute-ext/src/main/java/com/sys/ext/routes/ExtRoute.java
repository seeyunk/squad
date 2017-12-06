package com.sys.ext.routes;

import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squad.annotation.SquadRoute;
import com.sys.ext.processor.ExtProtocolProcessor;

@SquadRoute( config="squad-sys-ext.cfg" )
public class ExtRoute extends RouteBuilder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ExtProtocolProcessor processor = new ExtProtocolProcessor();
	
	@Override
	public void configure() throws Exception {
		// TODO Auto-generated method stub
		from( "netty:#{protocol}://#{ip}:#{port}?serverPipelineFactory=#sys-ext-spf&sync=true&reuseAddress=true" )
		.process( processor )
		.to( "mock:end" );
		//.to( "squad:tag" );
		
	}
}
